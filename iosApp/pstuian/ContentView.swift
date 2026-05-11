import SwiftUI
import presentation

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        IOSBridge().mainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
