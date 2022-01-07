#include <stdlib.h>
#include <mutex>
#include "connectionHandler.h"
#include <condition_variable>

#ifndef CLIENT_READWRITE_H
#define CLIENT_READWRITE_H


class ReadWrite {
private:
    std::mutex & _mutex;
    ConnectionHandler *connectionHandler;
    std::condition_variable& cv;
public:
    ReadWrite(std::mutex & mutex,ConnectionHandler *ch,std::condition_variable& conditionVariable);
    bool readKeyBoard();
    bool readFromSocket();

};


#endif //CLIENT_READWRITE_H
