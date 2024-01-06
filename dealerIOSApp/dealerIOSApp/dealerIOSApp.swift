import SwiftUI
import Firebase
import UIKit
import shared


@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
  var window: UIWindow?
  
  var rootRouterContext = RouterContextKt.defaultRouterContext()
  
  func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
    window = UIWindow(frame: UIScreen.main.bounds)
    let mainViewController = Main_iosKt.MainUIController(routerContext: rootRouterContext)
    window?.rootViewController = mainViewController
    window?.makeKeyAndVisible()
    return true
  }
  
  
  func applicationDidBecomeActive(_ application: UIApplication) {
    RouterContextKt.resume(rootRouterContext.lifecycle)
  }
  
  func applicationWillResignActive(_ application: UIApplication) {
    RouterContextKt.stop(rootRouterContext.lifecycle)
  }
  
  func applicationWillTerminate(_ application: UIApplication) {
    RouterContextKt.destroy(rootRouterContext.lifecycle)
  }
}
/*@main
struct dealerIOSApp: App {
    
    init(){
        FirebaseApp.configure()
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}*/
