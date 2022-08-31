// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  firebase: {
    apiKey: "AIzaSyBk18IZXI8o5rFScjz6ouh5ix4mvGvAu_8",
  authDomain: "cardgame2-b719f.firebaseapp.com",
  projectId: "cardgame2-b719f",
  storageBucket: "cardgame2-b719f.appspot.com",
  messagingSenderId: "566960767857",
  appId: "1:566960767857:web:7b365d9b39c332371cacd0"
  },
  production: false,
  apiBase: "http://localhost:8080/api",
  socketBase: "ws://localhost:8081/retrieve/"
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
