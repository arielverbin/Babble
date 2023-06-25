const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const ChatSchema = new Schema({
    users: [
        {
            type: Schema.Types.ObjectId,
            ref: 'User',
            required: true
        }
    ],
    messages: [
        {
            type: Schema.Types.ObjectId,
            ref: 'Message',
            required: true
        }
    ]
});

module.exports = mongoose.model('Chat', ChatSchema);
