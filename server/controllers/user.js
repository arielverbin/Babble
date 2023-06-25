const userService = require('../services/user');
const jwt = require('jsonwebtoken'); // Import the 'jsonwebtoken' library

const key = "Some super secret key shhhhhhhhhhhhhhhhh!!!!!";

const createUser = async (req, res) => {
    const user = await userService.getUser(req.body.username);
    if (user) {
        return res.status(409).json({message: 'Username is already taken'});
    }

    const newUser = await userService.createUser(req.body.username, req.body.password,
        req.body.displayName, req.body.profilePic);
    if (!newUser) {
        return res.status(500).send("Error creating user.");
    }

    // Return 200 status code and the saved user object
    return res.status(200).json({
        username: req.body.username,
        displayName: req.body.displayName,
        profilePic: req.body.profilePic
    });

};

// Define a function that responds with a json response.
// Only logged-in users should be able to execute this function
const getUser = async (req, res) => {
    const user = await userService.getUser(req.params.username);
    if (!user) {
        res.status(404).send("User not found.");
    }
    res.status(200).json({username: user.username, displayName: user.displayName, profilePic: user.profilePic});
}

const updateUser = async (req, res) => {
    if (!(await userService.updateUser(req.params.username, req.body.newPic, req.body.newDisplayName))) {
        res.status(500).send('Error updating user');
    }
    res.status(200).json({});
}

const isLoggedIn = async (req, res, next) => {
    if (req.headers.authorization) {
        // Extract the token from that header
        const token = req.headers.authorization.split(" ")[1];
        try {
            // Verify the token is valid
            jwt.verify(token, key);

            //Token validation was successful. Continue to the actual function (index)
            return next();
        } catch (err) {
            return res.status(401).send("Invalid Token");
        }
    } else
        return res.status(403).send('Token required');
};

module.exports = {createUser, isLoggedIn, getUser, updateUser};
