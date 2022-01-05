#include <connectionHandler.h>
 
using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;
 
ConnectionHandler::ConnectionHandler(string host, short port): host_(host), port_(port), io_service_(), socket_(io_service_){}
    
ConnectionHandler::~ConnectionHandler() {
    close();
}
 
bool ConnectionHandler::connect() {
    std::cout << "Starting connect to " 
        << host_ << ":" << port_ << std::endl;
    try {
		tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
		boost::system::error_code error;
		socket_.connect(endpoint, error);
		if (error)
			throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
			tmp += socket_.read_some(boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);			
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
			tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getLine(std::string& line) {
    return getFrameAscii(line, ';');
}

bool ConnectionHandler::sendLine(std::string& line,short opcode) {
    //replace all spaces with "\0"
    std::replace(line.begin(),line.end(),' ', '\0');

    // add a ";" to the end of the string
    /// line.push_back(' '); not sure if space is needed
    line.push_back(';');

    return sendFrameAscii(line, ';',opcode);
}
 
bool ConnectionHandler::getFrameAscii(std::string& frame, char delimiter) {
    char ch;
    int i = 0;
    char* opcodebytes = new char[2];
    short opcode;
    std::string command;
    // Stop when we encounter the null character. 
    // Notice that the null character is not appended to the frame string.
    try {
		do{
			getBytes(&ch, 1);

            //only add to frame after opcode decoded
            if (i>1)
                frame.append(1, ch);
            else{
                if (i=0)
                    opcodebytes[0]=ch;                        ///why is this not reachable
                else {
                    opcodebytes[1]=ch;
                    opcode = bytesToShort(opcodebytes);
                    command=findCommandString(opcode);
                    frame.append(command);
                }
            }
            i++;
        }while (delimiter != ch);

        std::replace(frame.begin(),frame.end(),'\0', ' ');

    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::sendFrameAscii(const std::string& frame, char delimiter,short opcode) {
    std::string newFrame = '\0' + frame;
    char *cstr = new char [newFrame.length()+2];
    shortToBytes(opcode,cstr);
    strcpy(cstr+2,newFrame.c_str());

	bool result=sendBytes(cstr,newFrame.length()+2);
	if(!result) return false;
	return sendBytes(&delimiter,1);
}
 
// Close down the connection properly.
void ConnectionHandler::close() {
    try{
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}



//Encode short to 2 bytes
void ConnectionHandler::shortToBytes(short num, char *bytesArr) {
    bytesArr[0] = ((num >> 8) & 0xFF);

    bytesArr[1] = (num & 0xFF);
}


//Decode 2 bytes to short
short ConnectionHandler::bytesToShort(char* bytesArr) {
    short result = (short)((bytesArr[0] & 0xff) << 8);

    result += (short)(bytesArr[1] & 0xff);

    return result;
}

std::string ConnectionHandler::findCommandString(short opcode) {
    if (opcode==9)
        return "NOTIFICATION";
    else if (opcode == 10)
        return "ACK";
    else
        return "ERROR";
}
