import groovy.json.*

def jobname = "producer_deployment_image_build_${BRANCH_NAME}"

freeStyleJob(jobname) {
    description("""
        <b>Generated Job</b>
        <p>Producer deployment image build
    """.stripIndent().trim())
    logRotator{
        numToKeep(10)
        artifactNumToKeep(10)
    }
    parameters {
        stringParam('GERRIT_REFSPEC', "refs/heads/${BRANCH_NAME}", '"Build Now" workaround')
        stringParam('BRANCH_NAME', "${BRANCH_NAME}", 'Make BRANCH_NAME available in the job scripts')
    }
    concurrentBuild()
    scm {
        git {
            remote{
                url("ssh://erostamas@review.gerrithub.io:29418/erostamas/producer")
                refspec('$GERRIT_REFSPEC')
                credentials('d15a0909-bacf-4de1-a358-0768d2cf8b33')
            }
            branch("${BRANCH_NAME}")
            extensions{
                choosingStrategy {
                    gerritTrigger()
                }
                cleanBeforeCheckout()
                relativeTargetDirectory('producer')
            }
        }
    }
    triggers {
        gerrit {
            project('erostamas/producer', "${BRANCH_NAME}")
			events {
                changeMerged()
            }
        }
    }
    wrappers {
        timeout {
            absolute(60)
        }
        timestamps()
        colorizeOutput('xterm')
    }
    steps{
        shell('''
            ${WORKSPACE}/producer/ci/jobs/producer_deployment_image_build/build.sh ${BRANCH_NAME}
        '''.stripIndent().trim())
    }
}
