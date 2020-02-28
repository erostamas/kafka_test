sectionedView("producer_${BRANCH_NAME}") {
    filterExecutors()
    sections {
        listView {
            name('Gerrit - Review')
            alignment('LEFT')
            width('HALF')
            jobs {
                name("gerrit_producer_library_build_${BRANCH_NAME}")
            }
            columns {
                status()
                weather()
                name()
                lastBuildNode()
                lastBuildConsole()
            }
        }
    }
}
