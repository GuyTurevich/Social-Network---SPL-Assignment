#include <stdlib.h>
#include <connectionHandler.h>

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);
    
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

	//From here we will see the rest of the ehco client implementation:
    while (1) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
		std::string line(buf);
		int len=line.length();
        if (!connectionHandler.sendLine(line)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
		// connectionHandler.sendLine(line) appends '\n' to the message. Therefor we send len+1 bytes.
        std::cout << "Sent " << len+1 << " bytes to server" << std::endl;

 
        // We can use one of three options to read data from the server:
        // 1. Read a fixed number of characters
        // 2. Read a line (up to the newline character using the getline() buffered reader
        // 3. Read up to the null character
        std::string answer;
        // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
        // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
        if (!connectionHandler.getLine(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        
		len=answer.length();
		// A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
		// we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.
        answer.resize(len-1);
        std::cout << "Reply: " << answer << " " << len << " bytes " << std::endl << std::endl;
        if (answer == "bye") {
            std::cout << "Exiting...\n" << std::endl;
            break;
        }
    }
    return 0;
}

//read from console and sends a message to the server
bool readKeyBoard(ConnectionHandler connectionHandler){
    while(true) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        //test if this works
        std::cout<< "the next row is to check if moving the char array to string worked. we should see a string of a command from client" << std::endl;
        std::cout<< line << std::endl;
        int len=line.length();
        std::string operation;

        //get operation from line
        for(int i = 0 ; line[i] != ' ' ; i++){
            operation.push_back(line[i]);
        }

        bool delivered;


        if(operation.compare("REGISTER")){
            delivered = connectionHandler.sendLine(line,1);
        }

        else if(operation.compare("LOGIN")){
            delivered = connectionHandler.sendLine(line,2);
        }

        else if(operation.compare("LOGOUT")){
            delivered = connectionHandler.sendLine(line,3);
        }
        else if(operation.compare("FOLLOW") || operation.compare("UNFOLLOW")){
            delivered = connectionHandler.sendLine(line,4);
        }

        else if(operation.compare("POST")){
            delivered = connectionHandler.sendLine(line,5);
        }

        else if(operation.compare("PM")){
            delivered = connectionHandler.sendLine(line,6);
        }

        else if(operation.compare("LOGSTAT")){
            delivered = connectionHandler.sendLine(line,7);
        }

        else if(operation.compare("STAT")){
            delivered = connectionHandler.sendLine(line,8);
        }

//        else if(operation.compare("NOTIFICATION")){
//            delivered = connectionHandler.sendLine(line,9);
//        }

        else if(operation.compare("BLOCK")){
            delivered = connectionHandler.sendLine(line,12);
        }

        if(!delivered){
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
    }
    return false;
}

bool readFromSocket(ConnectionHandler connectionHandler){
    while(true){
        std::string response;
        if (!connectionHandler.getLine(response)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }

        std::cout <<response<<std::endl;
        if(response.compare("ACK 3")){
            break;
        }
    }
    return false;
}
