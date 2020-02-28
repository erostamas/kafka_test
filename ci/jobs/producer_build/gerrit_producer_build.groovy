import groovy.json.*

def jobname = "gerrit_producer_library_build_${BRANCH_NAME}"

freeStyleJob(jobname) {
    description("""
        <b>Generated Job</b>
        <p>Producer build - Gerrit
    """.stripIndent().trim())
    logRotator{
        numToKeep(10)
        artifactNumToKeep(10)
    }
    parameters {
        stringParam('GERRIT_REFSPEC', "refs/heads/${BRANCH_NAME}", '"Build Now" workaround')
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
                patchsetCreated()
            }
            configure { gerrit ->
                gerrit / serverName('Gerrithub')
                gerrit / 'triggerOnEvents' /
                    'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.events.PluginCommentAddedContainsEvent' /
                        'commentAddedCommentContains'('.*rebuild.*')
                gerrit / 'triggerOnEvents' /
                    'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.events.PluginPatchsetCreatedEvent' /
                        'excludeDrafts'(true)
                gerrit / 'gerritProjects' /
                    'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data.GerritProject' << {
                        'filePaths' {
                            'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data.FilePath' {
                                'pattern'('*/**')
                                'compareType'('ANT')
                            }
                        }
                        'forbiddenFilePaths' {
                            'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data.FilePath' {
                                'pattern'('README.md')
                                'compareType'('ANT')
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
            ${WORKSPACE}/producer/ci/jobs/producer_build/main.sh
        '''.stripIndent().trim())
    }
    publishers {
        archiveArtifacts {
            pattern('producer/build/producer')
        }
    }
}
