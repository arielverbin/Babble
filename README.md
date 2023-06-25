# Babble - 4 Assignment - Android Client ![favicon](https://github.com/arielverbin/Babble/assets/106393592/19955a72-00cd-442b-abc4-8f1a7da1bc11)

The Babble Android Client is an Android application that allows users to chat in real-time using the Babble system. The app provides a seamless and synchronized communication experience, enabling users to send messages, create and delete chats, update their profile picture and display name, and attach files to messages.
## About the Name 'Babble'

The name "Babble" reflects the concept of talking rapidly and continuously in a foolish, excited, or incomprehensible way. It also resonates with the playful nature of bubbles, which inspired the choice of logo for the website.

## Screens

The app consists of the following screens:

### Login Screen

The login screen allows users to authenticate and log into their Babble account. Users need to enter their username and password to access the chat system.

![login](https://github.com/arielverbin/Babble/assets/106393592/b8ceed2b-f31c-40bb-9d5b-9f4611845470)



### Registration Screen

The registration screen allows new users to create an account on the Babble system. Users need to provide their desired username, password, email, and other required information to register.

![register](https://github.com/arielverbin/Babble/assets/106393592/f9d4fcc3-6979-4a1c-9d9f-13b8c2ac7cce)


### Contacts Screen

The contacts screen displays a list of chats that the user is a part of. Each chat entry shows the name of the chat and the latest message received or sent in that chat. Users can select a chat from the list to open the single chat screen.

![chatsList](https://github.com/arielverbin/Babble/assets/106393592/77766b0a-9998-4797-8f3d-20950f8bb4be) ![night-mode-chatList](https://github.com/arielverbin/Babble/assets/106393592/210cd1b4-bba0-4d17-a2ce-dcd9e0b224cc)

### Single Chat Screen

The single chat screen displays the messages exchanged in a specific chat. Users can send messages, view received messages, and attach files to their messages. The screen also shows the profile pictures and display names of the chat participants.

![chat](https://github.com/arielverbin/Babble/assets/106393592/d99b7db7-c543-4a2a-b969-f8d8fbca1bc7)


### Add Contact Screen

The add contact screen allows users to search for and add new contacts to their contact list. Users can enter the username or email of the person they want to add as a contact.

![addChat](https://github.com/arielverbin/Babble/assets/106393592/37955b73-ce71-4274-b01f-53b30f9e90c4)

### Settings

Allows a user to change some settings of the app(like night mode...)

![settings](https://github.com/arielverbin/Babble/assets/106393592/f751d33c-9272-4707-b4b5-5e8e09adca69)


## Getting Started - Android Client

To run the Babble Android Client, follow these steps:

1. Clone the repository: `git clone https://github.com/Babble.git`

2. Open the project in Android Studio.

3. Build and run the app on an Android emulator or physical device.

4. The Babble Android Client should be running, and you can now use the app to log in, register, and chat with other users.



## Getting Started - Server & Web Client

To run the Babble website locally, follow these steps:

1. Clone the repository: `git clone https://github.com/arielverbin/Babble.git`

2. Open a new terminal window, and navigate to the repository. Then, enter the *server* folder.
3. Make sure that you have installed all required dependencies: `npm i express cors body-parser mongoose custom-env socket.io`, `npm install firebase`
4. Run the server using `npm test`, (or `export NODE_ENV=test && node app.js` for macOS/Linux, `set NODE_ENV=test && node app.js` for Windows).
5. The server should be running.
6. Open your browser and visit [http://localhost:5001](http://localhost:5001) to access the Babble website.

## WebSockets
We used WebSocket technology in our application to provide real-time updates for sending messages, or creating/deleting chats. Whenever a user performs these actions, the changes will instantly appear on the other side, ensuring seamless and synchronized communication. However, for updates like changing the profile picture or display name, we made a conscious decision not to notify users immediately. These updates are not considered urgent and do not require immediate notification. Therefore, users can conveniently view the updated information after reopening the application or retrieving their contacts, which may occur periodically. This approach balances the need for timely updates with the consideration of user experience.

## Requirements

- Android Studio
- Android SDK version 21 or higher (minSdk 21)

## Dependencies

The Babble Android Client relies on the following dependencies:

- [Retrofit](https://square.github.io/retrofit/) - For making API requests to the Babble server.
- [Glide](https://github.com/bumptech/glide) - For loading and displaying profile pictures.
- [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview) - For displaying lists of chats and messages.
- [OkHttp](https://square.github.io/okhttp/) - For handling HTTP requests and WebSocket connections.

Make sure to include these dependencies in your project's build.gradle file.

## Usage

Once the app is running, you can perform the following actions:

1. Log in with your Babble account or register a new account.
2. After logging in, you will be directed to the contacts screen, where you can see a list of your chats.
3. Select a chat from the list to open the single chat screen.
4. In the single chat screen, you can view and send messages to the chat participants. You can also attach files to your messages.
5. Use the add contact screen to search for and add new contacts to your contact list.

## Real-Time Chatting

The Babble Android Client uses WebSocket technology to provide real-time updates for sending messages, creating and deleting chats, and updating profile pictures and display names. Whenever a user performs these actions, the changes will instantly appear on the other side, ensuring seamless and synchronized communication.

Note: For real-time updates to work properly, it is recommended to have multiple users connected using different devices or emulators.


## More Notes Real-Time Chatting
In this project, we have chosen to store the user's information in the localStorage of the browser.
This decision was made to ensure that users do not get logged out when they refresh the page or reopen the browser.
By utilizing localStorage, we provide a seamless experience where users can continue their session without interruption.

However, it is important to note that localStorage is shared across all tabs within the same browser.
While this approach enables persistence, it poses a limitation for experiencing real-time chatting.
To fully utilize the real-time chatting functionality, it is required to connect to multiple users using different browsers,
or opening a single incognito tab alongside a single regular tab.

## Conclusion

The Babble Android Client provides a user-friendly interface for accessing and interacting with the Babble chat system on Android devices. With real-time messaging, file attachments, and an intuitive user experience, users can

enjoy seamless and synchronized communication with others. Happy chatting! 😁
