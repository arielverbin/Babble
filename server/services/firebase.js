const User = require('../models/user');
const admin = require('firebase-admin');

const MAX_NOTIFICATION_BODY_LENGTH = 100; // Maximum length for the notification body

const associateToken = async (username, token) => {
    try {
        const user = await User.findOne({username});

        if (user) {
            user.firebaseToken = token;
            await user.save();
            console.log("[FIREBASE] Successfully attached token " + token + " to: " + username);
            return 'success';
        } else {
            console.log('[!] User not found: ' + username + ', token=' + token);
            return null;
        }
    } catch (error) {
        console.error('[!] Error updating current token:', error);
        return null;
    }
}
const removeToken = async (username) => {
    try {
        const user = await User.findOne({username});

        if (user) {
            user.firebaseToken = null;
            await user.save();
            console.log("[FIREBASE] Successfully removed token to: " + username);
        } else {
            console.log('[!] User not found:'  + username);
        }
    } catch (error) {
        console.error('[!] Error updating current token:', error);
    }
}

const getFirebaseToken = async (userId) => {
    try {
        return (await User.findOne({_id: userId})).firebaseToken;
    } catch (error) {
        console.log("[!] Error getting firebase token.")
        return null;
    }
}


const notify = async (firebaseToken, message) => {
    let notificationBody = message.content;

    if (notificationBody.length > MAX_NOTIFICATION_BODY_LENGTH) {
        notificationBody = notificationBody.substring(0, MAX_NOTIFICATION_BODY_LENGTH) + '...';
    }

    try {
        const notification = {
            token: firebaseToken,
            notification: {
                title: message.senderUsername,
                body: notificationBody,
            },
            data: {
                message: JSON.stringify(message),
            },
        };

        // Send the notification using the Firebase Admin SDK
        await admin.messaging().send(notification);

        console.log('[FIREBASE] Notification ' + message.content +' sent successfully.');
    } catch (error) {
        console.error('[FIREBASE] Error sending notification: ', error);
    }
}
module.exports = {associateToken, removeToken, getFirebaseToken, notify}
