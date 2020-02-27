#include <iostream>
#include <boost/program_options.hpp>
#include <ctime>
#include <chrono>
#include <thread>

#include "erostamas/UdpSender.h"

int main(int argc, char** argv) {
    namespace po = boost::program_options;

    po::variables_map vm;
    try {
        po::options_description desc("Allowed options");
        desc.add_options()
            ("help", "produce help message")
            ("ipaddress,a", po::value<std::string>()->required(), "Destination IP address")
            ("port,p", po::value<unsigned int>()->required(), "Destination UDP port")
            ("timeout,t", po::value<unsigned int>(), "Timeout between messages (sec)")
        ;

        po::store(po::parse_command_line(argc, argv, desc), vm);

        if (vm.count("help"))
        {
            std::cout << desc << std::endl;
            return 0;
        }

        po::notify(vm);
    } catch (const std::exception& e) {
        std::cerr << e.what() << std::endl;
        return 1;
    } catch (...) {
        std::cerr << "Unknown error!" << std::endl;
        return 1;
    }

    std::string destinationIpAddress = vm["ipaddress"].as<std::string>();
    unsigned int destinationPort = vm["port"].as<unsigned int>();
    unsigned int timeout = 1;

    if (vm.count("timeout")) {
        timeout = vm["timeout"].as<unsigned int>();
    }

    std::cout << "Sending messages to " << destinationIpAddress << ":" << destinationPort << " with " << timeout << " seconds timeout" << std::endl;

    UdpSender sender(destinationIpAddress, destinationPort);

    while(true) {
        std::string msg = std::to_string(std::time(0));
        sender.send(msg);
        std::cout << "Sent message: " << msg << std::endl;
        std::this_thread::sleep_for(std::chrono::seconds(timeout));
    }
    return 0;
}