const Message = require('../models/message');
const FilesAttach = require('../models/files');

const createMessage = async (user, content) => {
    try {
        const message = new Message({
            sender: user,
            content: content
        });
        await message.save();

        const messageObject = message.toObject();
        messageObject.id = messageObject._id.toString();
        delete messageObject._id;

        return messageObject;
    } catch (error) {
        console.log('[!] Error creating message: ' + error);
        return null;
    }
};


const findFileNamesByMessages = async (messagesIds) => {
    return FilesAttach.find(
        {attachedMsg: {$in: messagesIds}},
        'name attachedMsg -_id' // Include name and attachedMsg fields, exclude _id field
    );
}

const saveFile = async (fileName, fileData, msgId) => {
    try {
        const file = new FilesAttach({
            name: fileName,
            attachedMsg: msgId,
            data: fileData
        });
        await file.save();
        return file._id;

    } catch (error) {
        console.log('Error saving file: ' + error);
        return null;
    }
}

const getFileByMsgId = async (msgId) => {
    return FilesAttach.findOne({attachedMsg : msgId});
}


module.exports = {createMessage, findFileNamesByMessages, saveFile, getFileByMsgId}
