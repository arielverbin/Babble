const mongoose = require('mongoose');

const Schema = mongoose.Schema;
const User = new Schema({
    username: {
        type: String,
        required: true
    },
    password: {
        type: String,
        required: true
    },
    displayName: {
        type: String,
        required: true
    },
    currentSocket: {
        type: String,
        default: null
    },
    firebaseToken : {
        type: String,
        default: null
    },
    profilePic: {
        type: String,
        required: true
    },
});

module.exports = mongoose.model('User', User);

