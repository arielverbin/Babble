const firebaseController = require('../controllers/firebase');

const express = require('express');
const router = express.Router();

router.route('/')
    .post(firebaseController.isLoggedIn, firebaseController.associateFirebaseToken)
    .delete(firebaseController.isLoggedIn, firebaseController.removeFirebaseToken);

module.exports = router;