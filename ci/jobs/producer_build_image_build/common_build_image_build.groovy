import groovy.json.*

def jobname = "common_build_image_build_${BRANCH_NAME}"

freeStyleJob(jobname) {
    description("""
        <b>Generated Job</b>
        <p>Common library build image build
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
                url("ssh://erostamas@review.gerrithub.io:29418/erostamas/common")
                refspec('$GERRIT_REFSPEC')
                credentials('d15a0909-bacf-4de1-a358-0768d2cf8b33')
            }
            branch("${BRANCH_NAME}")
            extensions{
                choosingStrategy {
                    gerritTrigger()
                }
                cleanBeforeCheckout()
                relativeTargetDirectory('common')
            }
        }
    }
    triggers {
        gerrit {
            project('erostamas/common', "${BRANCH_NAME}")
			events {
                changeMerged()
            }
            configure { gerrit ->
                gerrit / serverName('Gerrithub')
                gerrit / silentStartMode(true)
                gerrit / 'gerritProjects' /
                    'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data.GerritProject' << {
                        'filePaths' {
                            'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data.FilePath' {
                                'pattern'('env/buildenv/**')
                                'compareType'('ANT')
                            }
                            'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data.FilePath' {
                                'pattern'('ci/jobs/common_build_image_build/build.sh')
                                'compareType'('PLAIN')
                            }
                        }
                    }
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
            ${WORKSPACE}/common/ci/jobs/common_build_image_build/build.sh ${BRANCH_NAME}
        '''.stripIndent().trim())
    }
}
