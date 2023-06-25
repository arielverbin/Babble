const authService = require('../services/token');

async function login(req, res) {

    // Check login credentials
    const {username, password} = req.body;

    const user = await authService.getUserByUsername(username);

    if (!user || user.password !== password) {
        return res.status(404).json({message: 'Invalid username and/or password'});
    }

    if(user.currentSocket != null || user.firebaseToken != null) {
        return res.status(409).json({message: "Another device is currently logged in to this account. Please log out from that device and try again."})
    }


    // Generate the JWT token
    const token = authService.generateToken(username);

    // Success! Send JWT back to client.
    return res.status(200).send(token);
}

module.exports = {login};

