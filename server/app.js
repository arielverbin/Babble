const socketService = require('./services/sockets')
const messageController = require('./controllers/message')

// importing express
const express = require('express');
const app = express();

// importing http.
const http = require("http");
// use of socket.io
const {Server} = require("socket.io");
// use body-parser to phrase the body of requests as json/URL-encoded.
const bodyParser = require('body-parser');

app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json({limit: '4mb'}));

app.use(express.json());

// The cors middleware allows cross-origin requests,
// enabling the API to be accessed from different domains or ports.
const cors = require('cors');
app.use(cors());

//load environment variables from a configuration file.
const customEnv = require('custom-env');
customEnv.env(process.env.NODE_ENV, './config');

//importing mongoose library to connect to a MongoDB database.
const mongoose = require('mongoose');
mongoose.connect(process.env.CONNECTION_STRING, {
    useNewUrlParser: true,
    useUnifiedTopology: true
});

// Any files in the public directory can be accessed directly by the client.
app.use("/", express.static('public'));
app.use("/login", express.static('public'));
app.use("/register", express.static('public'));
app.use("/babble", express.static('public'));

// Route setup.
const users = require('./routes/user');
app.use('/api/Users', users);

const messages = require('./routes/Message');
app.use('/api/Chats', messages);

const chats = require('./routes/chat');
app.use('/api/Chats', chats);

const token = require('./routes/Token');
const {env} = require("custom-env");
app.use('/api/Tokens', token);

const firebase = require('./routes/firebase');
app.use('/api/firebase', firebase);

// initialize http server.
const server = http.createServer(app)
const io = new Server(server, {
    cors: {
        origin: "http://localhost:3000",
        methods: ["GET", "POST", "DELETE", "PUT"],
    },
});
messageController.setIo(io);

const admin = require('firebase-admin');
const serviceAccount = require("./firebasePrivateKey.json");
const path = require('path');

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
});

io.on("connection", async (socket) => {
    const currentUsername = socket.handshake.query.username;

    await socketService.attachSocket(currentUsername, socket.id);

    socket.on("add-chat", async (contactUsername) => {
        const receiverSocketID = (await socketService.getSocketID(contactUsername));
        if (receiverSocketID) { // receiver is connected
            const receiverSocket = io.sockets.sockets.get(receiverSocketID);
            await socketService.notifyNewChat(receiverSocket);
        }
    });

    socket.on("remove-chat", async (contactUsername) => {
        const receiverSocketID = (await socketService.getSocketID(contactUsername));
        if (receiverSocketID) { // receiver is connected
            const receiverSocket = io.sockets.sockets.get(receiverSocketID);
            await socketService.notifyRemoveChat(receiverSocket);
        }
    });

    socket.on("disconnect", async () => {
        // Client disconnected
        console.log("Client " + socket.handshake.query.username + " disconnected:", socket.id);
        await socketService.disconnect(socket.handshake.query.username);
    });
});
// // server start.
server.listen(process.env.PORT);

console.log("Now running on port " + process.env.PORT);
