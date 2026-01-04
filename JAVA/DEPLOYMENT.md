# Code Breaker Game - Deployment Guide

## Prerequisites
- Docker installed on your machine
- Git installed

## 1. Clone the Repository
```bash
git clone https://github.com/ifatnah/ccs-interview
cd ccs-interview/JAVA
```

## 2. Build the Docker Image
```bash
docker build -t game-server .
```

## 3. Run the Docker Container
```bash
docker run -p 8080:8080 game-server
```

## 4. Connect and Play the Game
Open two separate terminals and run:
```bash
cd JAVA
java -cp target/classes client.GameClient
```

Or use telnet:
```bash
telnet localhost 8080
```

## Game Rules
- 2 players take turns guessing a 4-digit code
- Enter a number between 1000-9999
- The player who guesses correctly wins!
- Type 'exit' to leave the game