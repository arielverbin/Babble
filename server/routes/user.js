const userController = require('../controllers/user');

const express = require('express');
const router = express.Router();

router.route('/')
    .post(userController.createUser);

router.route('/:username')
    .get(userController.isLoggedIn, userController.getUser)
    .put(userController.isLoggedIn, userController.updateUser);


module.exports = router;