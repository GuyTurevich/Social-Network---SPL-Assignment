
#include "../include/ReadWrite.h"
#include <iostream>




ReadWrite::ReadWrite(std::mutex &mutex, ConnectionHandler *ch,std::condition_variable& conditionVariable):_mutex(mutex),connectionHandler(ch),cv(conditionVariable){}

bool ReadWrite::readKeyBoard(){
    while (true) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);

        std::string operation;

        //get operation from line
        size_t found = line.find(' ');
        if (found != std::string::npos) {
            for (int i = 0; line[i] != ' '; i++) {
                operation.push_back(line[i]);
            }
        } else operation = line;


        bool delivered;


        if (operation == "REGISTER") {
            delivered = connectionHandler->sendLine(line, 01);
        } else if (operation == "LOGIN") {
            delivered = connectionHandler->sendLine(line, 02);
        } else if (operation == "LOGOUT") {
            delivered = connectionHandler->sendLine(line, 03);
            std::unique_lock<std::mutex> lock(_mutex);
            cv.wait(lock);
            std::cout<< "Client closing.." << std::endl;
            return true;
        } else if (operation == "FOLLOW") {
            delivered = connectionHandler->sendLine(line, 04);
        } else if (operation == "POST") {
            delivered = connectionHandler->sendLine(line, 05);
        } else if (operation == "PM") {
            delivered = connectionHandler->sendLine(line, 06);
        } else if (operation == "LOGSTAT") {
            delivered = connectionHandler->sendLine(line, 07);
        } else if (operation == "STAT") {
            delivered = connectionHandler->sendLine(line, 8);
        } else if (operation == "BLOCK") {
            delivered = connectionHandler->sendLine(line, 12);
        }

        if (!delivered) {
            std::cout << "Disconnected. Exiting....\n" << std::endl;
            break;
        }
    }
    return false;

}

bool ReadWrite::readFromSocket() {
    while (true) {
        std::string response;
        if (!connectionHandler->getLine(response)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        response = response.substr(0, response.length() - 1);
        std::cout << response << std::endl;

        if (response == ("ACK 3")) {
            cv.notify_all();
            break;
        }
    }
    return false;
}