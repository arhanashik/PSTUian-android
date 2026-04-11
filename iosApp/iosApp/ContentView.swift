import SwiftUI
import shared

struct ContentView: View {
    @ObservedObject private var viewModel = ObservableSplashViewModel()

    var body: some View {
        VStack {
            if let state = viewModel.state {
                Text("Splash Screen State: \(String(describing: state))")
                if let loadingText = state.displayState.loadingText {
                    Text("Loading: \(loadingText)")
                }
            } else {
                Text("Loading ViewModel...")
            }
        }
        .onAppear {
            viewModel.activate()
        }
    }
}

class ObservableSplashViewModel: ObservableObject {
    @Published var state: SplashScreenState?
    private var watcher: Closeable?

    func activate() {
        let helper = KoinHelper()
        let splashVMWrapper = helper.getSplashViewModel()
        
        // Observe the state flow using the CommonFlow wrapper
        self.watcher = splashVMWrapper.state.watch { [weak self] newState in
            self?.state = newState
        }
        
        // Trigger initial check
        splashVMWrapper.instance.checkAuth()
    }
    
    deinit {
        watcher?.close()
    }
}
