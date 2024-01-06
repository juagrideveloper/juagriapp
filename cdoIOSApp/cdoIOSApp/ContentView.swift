import UIKit
import SwiftUI
import shared

/*@UIApplicationMain
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
}*/


struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        Main_iosKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
                .ignoresSafeArea(.all, edges: .bottom) // Compose has own keyboard handler
    }
}



