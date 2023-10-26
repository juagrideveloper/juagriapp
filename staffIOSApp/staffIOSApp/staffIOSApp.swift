import SwiftUI
import Firebase

@main
struct staffIOSApp: App {
    
    init(){
        FirebaseApp.configure()
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
