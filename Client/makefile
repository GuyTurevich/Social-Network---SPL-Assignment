CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -pthread

all: BGSClient
	g++ -o bin/BGSClient bin/connectionHandler.o bin/BGSClient.o bin/ReadWrite.o $(LDFLAGS)

BGSClient: bin/connectionHandler.o bin/BGSClient.o bin/ReadWrite.o

bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/BGSClient.o: src/BGSClient.cpp
	g++ $(CFLAGS) -o bin/BGSClient.o src/BGSClient.cpp

bin/ReadWrite.o: src/ReadWrite.cpp
	g++ $(CFLAGS) -o bin/ReadWrite.o src/ReadWrite.cpp

.PHONY: clean
clean:
	rm -f bin/*