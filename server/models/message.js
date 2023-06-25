const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const MessageSchema = new Schema({
    created: {
        type: String,
        default: () => new Date().toISOString()
    },
    sender: {
        type: Schema.Types.ObjectId,
        ref: 'User',
        default: null
    },
    content: String
});

module.exports = mongoose.model('Message', MessageSchema);
