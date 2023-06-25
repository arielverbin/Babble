const User = require("../models/user");

const getSocketID = async (username) => {
    try {
        return (await User.findOne({username})).currentSocket;
    } catch (error) {
        console.log("[!] Error getting socket.")
        return null;
    }
}


const attachSocket = async (username, socketID) => {
    try {
        const user = await User.findOne({username});

        if (user) {
            user.currentSocket = socketID;
            await user.save();
            console.log("[SOCKET IO] Successfully attached socket " + socketID + " to: " + username);
        } else {
            console.log('[SOCKET IO] User not found: ' + username + ', socketID=' + socketID);
        }
    } catch (error) {
        console.error('[SOCKET IO] Error updating current socket: ', error);
    }
}

const notifyNewChat = async (socket) => {
    if (socket) {
        socket.emit('new-chat', 'true');
    } else {
        console.log("Socket not found;")
    }
}

const notifyRemoveChat = async (socket) => {
    if (socket) {
        socket.emit('deleted-chat', 'true');
    } else {
        console.log("Socket not found;")
    }
}

const notifySendMessage = async (socket, message) => {
    if (socket) {
        socket.emit('new-message', message);
    } else {
        console.log("Socket not found;")
    }
}

const disconnect = async (username) => {
    try {
        const user = await User.findOne({username});
        if (user) {
            user.currentSocket = null;
            await user.save();
        } else {
            console.log('[SOCKET IO] User not found: ' + username);
        }
    } catch (error) {
        console.error('[SOCKET IO] Error updating current socket: ', error);
    }
}

module.exports = {getSocketID, attachSocket, notifyNewChat, notifyRemoveChat, notifySendMessage, disconnect}