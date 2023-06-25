# Babble <img src="./babble-logo.ico" alt="Logo" width="30" height="auto">

Babble is a chatting platform that allows users to chat in real-time. In this repository, we introduce the Android app version for our platform, along the existing website (See https://github.com/arielverbin/Babble3).

The app provides a seamless and synchronized communication experience, enabling users to send messages, create and delete chats, update their profile picture and display name.

### About the Name 'Babble'
The name "Babble" reflects the concept of talking rapidly and continuously in a foolish, excited, or incomprehensible way. It also resonates with the playful nature of bubbles, which inspired the choice of logo for the platform.

## Sending Messages
Our app utilizes Firebase for real-time messaging and push notifications, ensuring instant communication and timely updates. With Firebase's seamless integration, users can enjoy a smooth and engaging chatting experience on their devices.

## Settings
Our app's settings screen provides users with a convenient way to personalize their chat experience. Users can modify their display name and profile picture, allowing them to customize their identity within the app. Additionally, the settings screen offers a choice between light and dark themes, enabling users to tailor the app's appearance to their preferences. Moreover, users have the flexibility to change the server address, granting them the freedom to connect to different chat servers based on their needs.

## Our Activities
These are some screenshots of our different screens.

<div style="display: flex; flex-wrap: wrap;">
  <img src="https://github.com/arielverbin/Babble/assets/106393592/b8ceed2b-f31c-40bb-9d5b-9f4611845470" alt="login" style="width: 13%; max-height: 100px; margin-right: 5px;">
  <img src="https://github.com/arielverbin/Babble/assets/101435503/602a4f44-1c46-4163-b309-dc5d553d2888" alt="register" style="width: 13%; max-height: 100px; margin-right: 5px;">
  <img src="https://github.com/arielverbin/Babble/assets/106393592/77766b0a-9998-4797-8f3d-20950f8bb4be" alt="chatsList" style="width: 13%; max-height: 100px; margin-right: 5px;">
  <img src="https://github.com/arielverbin/Babble/assets/106393592/210cd1b4-bba0-4d17-a2ce-dcd9e0b224cc" alt="night-mode-chatList" style="width: 13%; max-height: 100px; margin-right: 5px;">
  <img src="https://github.com/arielverbin/Babble/assets/106393592/d99b7db7-c543-4a2a-b969-f8d8fbca1bc7" alt="chat" style="width: 13%; max-height: 100px; margin-right: 5px;">
  <img src="https://github.com/arielverbin/Babble/assets/106393592/37955b73-ce71-4274-b01f-53b30f9e90c4" alt="addChat" style="width: 13%; max-height: 100px; margin-right: 5px;">
  <img src="https://github.com/arielverbin/Babble/assets/106393592/f751d33c-9272-4707-b4b5-5e8e09adca69" alt="settings" style="width: 13%; max-height: 100px; margin-right: 5px;">
</div>



## Getting Started - Server

To run the Babble website locally, follow these steps:

1. Clone the repository: `git clone https://github.com/arielverbin/Babble.git`
2. Open a new terminal window, and navigate to the repository. Then, enter the *server* folder.
3. Make sure that you have installed all required dependencies: `npm i express cors body-parser mongoose custom-env socket.io firebase-admin path`
5. Run the server using `npm test`, (or `export NODE_ENV=test && node app.js` for macOS/Linux, `SET NODE_ENV=test && node app.js` for Windows).
6. The server should be running.

## Getting Started - Android Client

To run the Babble Android Client, follow these steps:

1. Clone the repository: `git clone https://github.com/arielverbin/Babble`
2. Open the project in Android Studio.
3. Build and run the app on an Android emulator.
4. The Babble Android Client should be running, and you can now use the app to log in, register, and chat with other users.

## Requirements

- Android Studio
- Android SDK version 21 or higher (minSdk 21)

## Creating and Deleting Chats
When a chat is created in our app, the receiver user is not immediately notified. The notification is triggered only when the first message is sent within the chat. This ensures that the receiver is informed at the appropriate time and reduces unnecessary notifications for empty chats.

In terms of chat deletion, if a user decides to delete a chat, it will be removed from their local database and from the server. However, for the other user involved in the chat, the conversation will remain in their local database. The chat will be marked as deleted, indicating that it has been removed by the first user. The other user can choose to delete the chat themselves at a later time if they wish to remove it from their local database as well. This approach allows for individual control over chat deletion while preserving the chat's availability until both users opt to delete it.

## Conclusion

The Babble Android Client provides a user-friendly interface for accessing and interacting with the Babble chat system on Android devices. With real-time messaging, and an intuitive user experience, users can enjoy seamless and synchronized communication with others. 

### Happy chatting! 😁
