// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

/* Created by Xinlu Chen on 03/30/2018 */

// Import firebase functions modules. The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// Import admin module. The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');

// Initialize the firebase-admin app instance. It helps to trigger firebase events.
admin.initializeApp(functions.config().firebase);

/* Listens for new messages added to /messages/:pushId and sends a notification to the specific user. onCreate() triggers when new data is created in the Realtime Database. */
exports.pushNotification = functions.database.ref('/messages/{pushId}').onCreate( event => {

	console.log('Push notification event triggered');

	/* Grab the current value of what was written to the Realtime Database */
    var valueObject = event.data.val();

	// This registration token comes from the client FCM SDKs. store it in Users child db
    var token = valueObject.recipient;

	/* Create a notification and data payload. They contain the notification information, and message to be sent respectively */ 
    const payload = {
        notification: {
            title: valueObject.title,
            body: valueObject.message,
            sound: "default"
        },
        // data: {
        //     title: valueObject.title,
        //     message: valueObject.message
        // }
    };

	/* Create an options object that contains the time to live for the notification and the priority. */
    const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24 //24 hours
    };

    return admin.messaging().sendToDevice(token, payload, options);
});
