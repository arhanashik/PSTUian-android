## PSTUian
Welcome to PSTUian mobile app code base.

## DESCRIPTION
The project is fully developed with Kotlin and latest MVI architecture. 
MVI in a nutshell: 
1. when a user makes any action on user interfaces(Activity, Fragment, Dialog etc), it 
   triggers an intent. 
2. View model process that intent and makes necessary operations. If view model needs any
   data it requests that to the Repository classes. Repository classes decides how to
   provide the data and from where(api, room, datastore or any other storage).
3. After that, view model delivers the data to related view states.
4. User interfaces continue listening to the view states and makes necessary updates to the 
   ui depending on the view states.

[Here](https://medium.com/swlh/mvi-architecture-with-android-fcde123e3c4a) is a blog on MVI architecture.

Technology used:
- Kotlin Programming Language
- MVI architecture
- Material Design
- Coroutine
- Room
- Datastore
- Work Manager
- Koin (Dependency Injection)
- Firebase Cloud Messaging
- Retrofit
- Coil (Image Loader)
- Shimmer (Placeholder for items)
- AgentWeb (Web browser)
- Lottie (animation library) etc.

Project structure:
- java/com/workfort/pstuian:
  - app: 
    - data: 
      - local: Contains the model classes, databases, shared preferences and constants
      - remote: Api Helper classes for making the api calls and responses
      - repository: Repository classes for data. Repository classes are responsible to 
        provide data to the view models. Repository classes decides weather to get data 
        from and store in api or room.
    - ui: Contains all the activities, adapters, dialogs and every other views. Also 
      contains the ViewModels, Intents(intents of MVI architecture) and other necessary 
      classes.
      - base: Base classes for the activity, adapter, fragments etc to reduce boilerplate 
        codes.
  - util:
    - extension: Kotlin extension functions
    - helper: Helper classes such as helper function for formatting date, showing toast etc.
    - lib:
      - fcm: Support classes for firebase cloud messaging
      - koin: Contains the dependency injection codes
      - workmanager: Work Manger works
    - remote: Api service classes for Retrofit
    - view: Custom views
  
- res: Contains the layout files, animations, styles, resources etc

## INSTALLATION
Clone the repo and open with android studio. That's all!

## DEPLOY
Just build the release signed bundle/apk. YES, it's THAT easy.

## Version Control
Here is an overview on the branches:
- main: Contains the latest stable released code
- dev: Contains the latest under development codebase
- release_***: Contains the code for that particular release

## CHANGELOG
3.0.0 - Compose
----------------------
- Project is converted to use jetpack compose and ui state
- It makes the app easier to maintain and improves the performance
- Several ui improvement to make the UX better

2.1.0 - Account Delete option
----------------------
- User can now send request to delete account. After the action user won't be able to 
  use the account any more and if doesn't request for recovery within 3 days it might get
  permanently deleted
- Code improvement with modules and a lot more
- Minor UI improvements
- Version updates for used libraries

2.0.2 - Firebase token issue
----------------------
- Some device does not support fcm(for example Poco phone f1). Those device can now register 
  without the fcm token.

2.0.1 - Device Id Bug
----------------------
- Even if the data is cleared the device id should remain the same

2.0.0 - Blood Donation, Check In, Account Verification, Multiple Device
----------------------
- Blood donation and donation request option
- Check in and create check in location option
- Email verification is a must to see all information except the home page
- Sign in from multiple devices
- See the signed in devices and sign out from all of them

1.0.0 - Initial
---------------
- Android app with the most expected features and without any known bug
- See all the data(faculties, batches, students, teachers, courses, employees, 
  notifications etc) with/without sign in or sign up
- SignIn, SignUp, reset password if forgot, Sign out option for teacher/student
- Update profile info(everything)
- Make a donation and see other donors list
- Admission support page
- Support option if users need any help
- Data clear option if necessary
- Get notification and show if user is inside the app