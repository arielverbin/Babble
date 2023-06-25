const jwt = require('jsonwebtoken'); // Import the 'jsonwebtoken' library
const userService = require('../services/user')
const firebaseService = require('../services/firebase')
const key = "Some super secret key shhhhhhhhhhhhhhhhh!!!!!";

const associateFirebaseToken = async (req, res) => {
    const {username} = req.username;
    const token = req.body.token;
    const result = await firebaseService.associateToken(username, token);
    if(result) {
        res.status(200).send("Updated token");
    } else {
        res.status(500).send("Error updating token");
    }
}

const removeFirebaseToken = async (req, res) => {
    const {username} = req.username;
    const result = await firebaseService.removeToken(username);
    if(result) {
        res.status(200).send("token removed");
    } else {
        res.status(500).send("Error removing token");
    }
}


const isLoggedIn = async (req, res, next) => {
    if (req.headers.authorization) {
        // Extract the token from that header
        const token = req.headers.authorization.split(" ")[1];
        try {
            // Verify the token is valid
            req.username = jwt.verify(token, key);

            //Token validation was successful. Continue to the actual function (index)
            return next();
        } catch (err) {
            return res.status(401).send("Invalid Token");
        }
    } else
        return res.status(403).send('Token required');
};

module.exports = {isLoggedIn, associateFirebaseToken, removeFirebaseToken};
