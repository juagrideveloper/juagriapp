import SwiftUI
import Firebase

@main
struct cdoIOSApp: App {
    
    init() {
        FirebaseApp.configure()
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
