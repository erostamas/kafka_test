link_directories(/usr/lib/erostamas/)

add_executable(producer
    producer.cc
)

target_link_libraries(producer
    et_udp
    pthread
    boost_program_options
    boost_system
)

INSTALL(TARGETS producer
	DESTINATION /usr/bin/erostamas/
	PERMISSIONS OWNER_READ OWNER_EXECUTE GROUP_READ GROUP_EXECUTE WORLD_READ WORLD_EXECUTE
)