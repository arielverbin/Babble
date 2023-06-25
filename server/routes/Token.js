// routes/Token.js
const express = require('express');
const tokenController = require('../controllers/Token');

const router = express.Router();

router.post('/', tokenController.login);

module.exports = router;

