import SwiftUI
import feature_presentation

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea(.keyboard) // Compose has its own keyboard handling
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        IOSBridge().mainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
