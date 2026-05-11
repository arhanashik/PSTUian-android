import FirebaseCore
import FirebaseMessaging
import presentation
import SwiftUI
import UserNotifications

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate {
    
    let iosBridge = IOSBridge()
    
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        
        // Pick the right config file based on build configuration
       #if DEBUG
       let configFileName = "GoogleService-Info-Debug"
       #else
       let configFileName = "GoogleService-Info-Release"
       #endif

       if let filePath = Bundle.main.path(forResource: configFileName, ofType: "plist"),
          let options = FirebaseOptions(contentsOfFile: filePath) {
           FirebaseApp.configure(options: options)
       } else {
           FirebaseApp.configure()
       }

        iosBridge.initializeKoin()

        // Set delegates
       UNUserNotificationCenter.current().delegate = self
       Messaging.messaging().delegate = self

        // Request notification permissions
       let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
       UNUserNotificationCenter.current().requestAuthorization(
         options: authOptions,
         completionHandler: { _, _ in }
       )

        // Register for remote notifications
       application.registerForRemoteNotifications()

        return true
    }

   func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
       Messaging.messaging().apnsToken = deviceToken
   }
}

extension AppDelegate: MessagingDelegate {
   func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
       print("Firebase registration token: \(String(describing: fcmToken))")
       iosBridge.updateFcmToken(token: fcmToken)
   }
}

@main
struct pstuianApp: App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
