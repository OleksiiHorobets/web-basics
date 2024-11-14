const express = require("express");
const http = require("http");
const socketIo = require("socket.io");
const path = require("path");

const app = express();
const server = http.createServer(app);
const io = socketIo(server);

const PORT = process.env.PORT || 3000;

const rooms = {};

app.use(express.static(path.join(__dirname, "public")));

app.use((req, res, next) => {
  console.log(`${new Date().toISOString()} - ${req.method} ${req.url}`);
  next();
});

const defaultRoom = "Main";

rooms[defaultRoom] = [];
rooms["Room 1"] = [];
rooms["Room 2"] = [];

io.on("connection", (socket) => {
  console.log("New client connected");

  socket.emit("roomsList", Object.keys(rooms));

  socket.emit("joinRoom", { username: "System", room: defaultRoom });

  socket.on("joinRoom", ({ username, room }) => {
    socket.username = username;
    socket.room = room;
    socket.join(room);

    if (!rooms[room]) {
      rooms[room] = [];
    }

    socket.emit("previousMessages", rooms[room]);

    const joinMessage = {
      username: "System",
      message: `${username} joined the chat!`,
    };
    rooms[room].push(joinMessage);
    console.log("Pushed join message to room", room);
    io.to(room).emit("chatMessage", joinMessage);
    console.log(`${username} has joined the chat room: ${room}`);
  });

  socket.on("chatMessage", (msg) => {
    const messageData = { username: socket.username, message: msg };
    rooms[socket.room].push(messageData);
    console.log(`${socket.username} in room ${socket.room}: ${msg}`);
    io.to(socket.room).emit("chatMessage", messageData);
  });

  socket.on("disconnect", () => {
    if (socket.username && socket.room) {
      const leaveMessage = {
        username: "System",
        message: `${socket.username} left the chat :(`,
      };
      rooms[socket.room].push(leaveMessage);
      io.to(socket.room).emit("chatMessage", leaveMessage);
      console.log(`${socket.username} has left the chat room: ${socket.room}`);
    }
    console.log("Client disconnected");
  });
});

server.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
