# Code Breaker Game - Deployment Guide

## Prerequisites
- Docker installed (or Java 21 + Maven for local run)
- Git installed

## 1. Clone the Repository
```bash
git clone https://github.com/ifatnah/ccs-interview
cd ccs-interview/JAVA
```

## 2. Build and Run with Docker
```bash
docker build -t game-server .
docker run -p 8080:8080 game-server
```

## 3. Connect Two Players
This is a 2-player game. Open two terminals:

**Player 1:**
```bash
java -cp target/classes client.GameClient
```

**Player 2 (separate terminal):**
```bash
java -cp target/classes client.GameClient
```

The game begins once both players connect.
```

## Game Rules
- 2 players take turns guessing a 4-digit code
- Enter a number between 1000-9999
- The player who guesses correctly wins!
- Type 'exit' to leave the game