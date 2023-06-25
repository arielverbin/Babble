const Chat = require('../models/chat');
const User = require('../models/user');
const Message = require('../models/message')
const FilesAttach = require('../models/files')
const userService = require('./user');
// Create a new chat between user1 and user2.
const createChat = async (user1, user2) => {
    try {
        const chat = new Chat({
            users: [user1.id, user2.id] // Extract the _id values from user1 and user2
        });
        await chat.save();
        return chat._id.toString(); // Convert _id to string
    } catch (error) {
        console.log('[!] Error creating chat: ' + error);
        return null;
    }
};

const searchChatsByUser = async (username) => {
    //find user.
    const user = await User.findOne({username: username});
    if (!user) {
        return null;
    }

    const chats = await Chat.find({users: user._id}).populate('users').populate('messages');

    return chats.map(chat => {
        const otherUser = chat.users.find(user => user.username !== username);

        return {
            id: chat._id,
            user: otherUser,
            lastMessage: chat.messages[0] || null
        };
    });

};

const searchChatsByIDs = async (user1Id, user2Id) => {
    try {
        const chats = await Chat.find({
            users: {
                $all: [user1Id, user2Id]
            }
        }).populate('users').populate('messages');

        return chats.map((chat) => {
            const {_id, ...chatData} = chat.toObject();
            return {id: _id, ...chatData};
        });
    } catch (error) {
        console.log("[!] Error in finding chats: " + error);
        return null;
    }
};


const deleteChat = async (chatId) => {
    try {
        // Find the chat by its _id and populate the 'messages' field
        const chat = await Chat.findOne({_id: chatId}).populate('messages');

        if (!chat) {
            // Chat not found
            return -1;
        }

        // Delete files with attachedMsg that belong to messages in the chat
        const fileDeletionPromises = chat.messages.map(async (message) => {
            await FilesAttach.deleteMany({attachedMsg: message._id});
        });

        await Promise.all(fileDeletionPromises);

        // Delete the messages from the 'messages' array in MessageSchema
        await Message.deleteMany({_id: {$in: chat.messages}});

        // Delete the chat
        await Chat.deleteOne({_id: chatId});

        return 1;
    } catch (error) {
        console.error('[!] Error deleting chat: ', error);
        return 0;
    }
}

const findChatById = async (id) => {
    try {
        const chat = await Chat.findOne({_id: id})
            .populate('users')
            .populate({
                path: 'messages',
                populate: {
                    path: 'sender',
                    model: 'User'
                }
            })
            .lean();

        if (chat) {
            const {_id, ...chatData} = chat;
            return {id: _id, ...chatData};
        } else {
            return null;
        }
    } catch (error) {
        console.log("[!] Error in finding chat: " + error);
        return null;
    }
};


const addMsgToChat = async (chatId, msgID) => {
    try {
        const chat = await Chat.findOne({_id: chatId});
        if(!chat) {
            return 'chat-not-found';
        }
        chat.messages.unshift(msgID);
        await chat.save();
        return 'success';
    } catch (error) {
        console.log("[!] Error in adding message to chat: " + error);
        return null;
    }
}

const findReceiver = async (senderUsername, chatID) => {
    const senderID = (await userService.getUser(senderUsername)).id;
    const curChat = await Chat.findOne({_id: chatID}).populate('users');
    return curChat.users.find(user => user._id.toString() !== senderID.toString());
}

async function checkIfChatExists(user1Username, user2Username) {
    try {
        const user1 = await User.findOne({ username: user1Username });
        const user2 = await User.findOne({ username: user2Username });

        const chat = await Chat.findOne({
            users: { $all: [user1._id, user2._id] }
        });

        if(chat) {
            return chat;
        } else {
            return null;
        }

    } catch (error) {
        // Handle any errors
        console.error('[!] Error checking chat existence: ', error);
        throw error;
    }
}


module.exports = {
    createChat,
    searchChatsByIDs,
    searchChatsByUser,
    deleteChat,
    findChatById,
    addMsgToChat,
    findReceiver,
    checkIfChatExists,
}
