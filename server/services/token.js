const jwt = require('jsonwebtoken');
const User = require('../models/user');

const key = "Some super secret key shhhhhhhhhhhhhhhhh!!!!!";


async function getUserByUsername(username) {
    return User.findOne({username: username});
}

function generateToken(username) {
    const data = {username};
    return jwt.sign(data, key);
}


module.exports = {getUserByUsername, generateToken};

