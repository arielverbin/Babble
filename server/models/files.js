const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const FilesAttachSchema = new Schema({
    name: {
        type: String,
        default: "Name Unknown"
    },
    attachedMsg : {
        type: Schema.Types.ObjectId,
        ref: 'MessageSchema',
        default: null
    },
    data : {
        type: String,
        default: null
    }
})

module.exports = mongoose.model('FilesAttach', FilesAttachSchema);