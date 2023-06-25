const messageService = require('../services/message');
const userService = require('../services/user');
const chatService = require('../services/chat');
const socketService = require('../services/sockets');
const firebaseService = require('../services/firebase');

const jwt = require('jsonwebtoken');

const key = "Some super secret key shhhhhhhhhhhhhhhhh!!!!!";

let io;

const setIo = (newIo) => {
    io = newIo;
}

function getTime(timeString) {
    const dateObj = new Date(timeString);
    const hours = dateObj.getHours().toString().padStart(2, '0');
    const minutes = dateObj.getMinutes().toString().padStart(2, '0');
    return `${hours}:${minutes}`;
}

function convertTimeFormat(timeString) {
    const dateTime = new Date(timeString);

    const day = String(dateTime.getDate()).padStart(2, '0');
    const month = String(dateTime.getMonth() + 1).padStart(2, '0');
    const year = String(dateTime.getFullYear());
    const hours = String(dateTime.getHours()).padStart(2, '0');
    const minutes = String(dateTime.getMinutes()).padStart(2, '0');

    return `${day}/${month}/${year} ${hours}:${minutes}`;
}

const sendMessage = async (req, res) => {
    // Get our username ID.
    const userID = (await userService.getUser(req.username)).id;
    // Get the current chat ID.
    const chatID = req.params.id;

    if (req.body.msg === '' && !req.body.fileName) {
        return res.status(400).send("Empty messages are not allowed.");
    }

    //create message
    const message = await messageService.createMessage(userID, req.body.msg);
    if (!message) {
        return res.status(500).send("Error creating a new message.");
    }
    //add message to chat
    const result = await chatService.addMsgToChat(chatID, message.id);
    if (result === 'chat-not-found') {
        return res.status(404).send("Chat was deleted.");
    } else if(result !== 'success') {
        return res.status(500).send("Error adding message to chat.");
    }

    if (req.body.fileName) {
        if (await messageService.saveFile(req.body.fileName, req.body.fileData, message.id)) {
            return res.status(200).json(message);
        }
        return res.status(500).send("Error saving file.");
    }

    // notify receiver - either in socket.io or firebase (or both).
    const receiver = await chatService.findReceiver(req.username, chatID);
    await notifyReceiver(receiver, req.username, chatID, message, req.body.fileName);

    return res.status(200).json(message);
}

const getMessages = async (req, res) => {
    const chat = await chatService.findChatById(req.params.id);
    if (!chat) {
        return res.status(404).send("Chat not found");
    }

    //get the chat's messages
    const messages = chat.messages;
    return res.status(200).json(messages);
}

// return all file names for a specific chat.
const getFileNames = async (req, res) => {
    const chat = await chatService.findChatById(req.params.id);
    if (!chat) {
        return res.status(404).send("Chat not found");
    }
    const messageIds = chat.messages.map((message) => message);
    const names = await messageService.findFileNamesByMessages(messageIds);

    return res.status(200).json(names);
}

// get a specific file from a message.
const getFile = async (req, res) => {
    const file = await messageService.getFileByMsgId(req.params.msgId);
    if(!file) {
        return res.status(500).send('Error fetching file.');
    }
    return res.status(200).json({data : file.data});
}

const notifyReceiver = async (receiver, username, chatID, message, fileName) => {
    // socket.io
    const receiverSocketId = await socketService.getSocketID(receiver.username);
    if(receiverSocketId) { // receiver is online via socket.io
        console.log('[SOCKET IO] sending ' + message.content + ', to ' +
            receiverSocketId + ' (' + receiver.username + ')');

        const receiverSocket = io.sockets.sockets.get(receiverSocketId);
        const socketMessage = {
            chatID: chatID,
            sender: username,
            message: {
                content: message.content,
                reply: false,
                timeSent: getTime(message.created),
                extendedTime: convertTimeFormat(message.created),
                attachedFile: fileName ? fileName : null
            }
        };
        await socketService.notifySendMessage(receiverSocket, socketMessage);
    }

    // firebase
    const receiverToken = await firebaseService.getFirebaseToken(receiver._id);
    if (receiverToken) {
        console.log('[FIREBASE] sending ' + message.content + ', to ' +
            receiverToken + ' (' + receiver.username + ')');

        await firebaseService.notify(receiverToken, {
            senderUsername: username,
            chatId: chatID,
            content: message.content,
            timeSent: getTime(message.created),
            extendedTime: convertTimeFormat(message.created),
        });
    }
}

const isLoggedIn = async (req, res, next) => {
    // If the request has an authorization header
    if (req.headers.authorization) {
        // Extract the token from that header
        const token = req.headers.authorization.split(" ")[1];
        try {
            // Verify the token is valid
            const data = jwt.verify(token, key);
            req.username = data.username; // Store the data in req.user object

            // Token validation was successful. Continue to the actual function (index)
            return next()
        } catch (err) {
            return res.status(401).send("Invalid Token");
        }
    } else
        return res.status(403).send('Token required');
};


module.exports = {isLoggedIn, sendMessage, getMessages, getFileNames, getFile, setIo};
