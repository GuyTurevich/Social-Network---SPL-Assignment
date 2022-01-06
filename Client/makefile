CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system

all: EchoClient
	@echo 'Building target: EchoClient'
	@echo 'Invoking: C++ Linker'
	g++ -o bin/echoClient bin/connectionHandler.o bin/echoClient.o $(LDFLAGS)
	@echo 'Finished building target: main'

EchoClient: bin/connectionHandler.o bin/echoClient.o

bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/echoClient.o: src/echoClient.cpp
	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp
	
.PHONY: clean
clean:
	rm -f bin/*
