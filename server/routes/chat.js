const chatController = require('../controllers/chat');

const express = require('express');
const router = express.Router();

router.route('/')
    .get(chatController.isLoggedIn, chatController.getChats)
    .post(chatController.isLoggedIn, chatController.addChat);

router.route('/:id')
    .get(chatController.isLoggedIn, chatController.getChatsWithUser)
    .delete(chatController.isLoggedIn, chatController.deleteChat);

module.exports = router;