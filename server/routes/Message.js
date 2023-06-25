const messageController = require('../controllers/message');

const express = require('express');
const router = express.Router();

router.route('/:id/Messages')
    .post(messageController.isLoggedIn, messageController.sendMessage)
    .get(messageController.isLoggedIn, messageController.getMessages);

router.route('/:id/Messages/FilesAttach')
    .get(messageController.isLoggedIn, messageController.getFileNames)

router.route('/Messages/FilesAttach/:msgId')
    .get(messageController.isLoggedIn, messageController.getFile);

module.exports = router;