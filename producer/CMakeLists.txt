link_directories(/usr/lib/erostamas/)

add_executable(producer
    producer.cc
)

target_link_libraries(producer
    et_udp
    pthread
    boost_program_options
)