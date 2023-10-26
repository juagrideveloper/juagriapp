import SwiftUI
import Firebase

@main
struct dealerIOSApp: App {
    
    init(){
        FirebaseApp.configure()
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
