const chatService = require('../services/chat');
const userService = require('../services/user')
const jwt = require('jsonwebtoken'); // Import the 'jsonwebtoken' library


const key = "Some super secret key shhhhhhhhhhhhhhhhh!!!!!";

const getChats = async (req, res) => {
    // Access the username from req.user object set by isLoggedIn middleware
    const {username} = req.username;

    const chats = await chatService.searchChatsByUser(username);
    return res.status(200).json(chats);
};

const getChatsWithUser = async (req, res) => {
    // Access the username from req.user object set by isLoggedIn middleware
    const ourUserId = (await userService.getUser(req.username)).id;
    const targetUserId = req.params.id;
    const chats = await chatService.searchChatsByIDs(ourUserId, targetUserId);

    if (!chats) {
        return res.status(404).send("Chat not found");
    }
    return res.status(200).json(chats);
}

const addChat = async (req, res) => {
    // Access the username from req.user object set by isLoggedIn middleware
    const {username} = req.username;

    const myUser = await userService.getUser(username);
    const targetUser = await userService.getUser(req.body.username);

    const alreadyExists = await chatService.checkIfChatExists(myUser.username, targetUser.username);
    if (alreadyExists) {
        return res.status(200).json({
            id: alreadyExists._id,
            user: {
                username: targetUser.username,
                displayName: targetUser.displayName,
                profilePic: targetUser.profilePic
            }
        })
    }

    if (!targetUser) {
        return res.status(400).send('No such user');
    }
    if (myUser.username === targetUser.username) {
        return res.status(400).send('Thou shalt not talk with thy self');
    }
    const chatId = await chatService.createChat(myUser, targetUser);
    if (!chatId) {
        return res.status(500).send('Error creating a new chat.');
    }
    // Success! send targetUser details back to client.
    return res.status(200).json({
        id: chatId,
        user: {username: targetUser.username, displayName: targetUser.displayName, profilePic: targetUser.profilePic}
    });
};

const deleteChat = async (req, res) => {
    const deleteResult = await chatService.deleteChat(req.params.id);
    if (deleteResult === 1) {
        return res.status(200).send('');
    } else if (deleteResult === -1) {
        return res.status(404).send('');
    } else return res.status(400).send('');
}


const isLoggedIn = (req, res, next) => {
    // If the request has an authorization header
    if (req.headers.authorization) {
        // Extract the token from that header
        const token = req.headers.authorization.split(" ")[1];
        try {
            // Verify the token is valid

            req.username = jwt.verify(token, key); // Store the data in req.user object
            // Token validation was successful. Continue to the actual function (index)
            return next()
        } catch (err) {
            return res.status(401).send("Invalid Token");
        }
    } else
        return res.status(403).send('Token required');
};


module.exports = {isLoggedIn, getChatsWithUser, getChats, addChat, deleteChat}