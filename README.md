# 🚀 Offline File Share App

A production-ready, lightning-fast Android offline file sharing application built with Kotlin, Jetpack Compose, and robust TCP Sockets.

This app automatically intelligently routes connections via local WiFi or Mobile Hotspots to transfer large files (1GB+) securely and rapidly without any internet connection, backend servers, or cloud storage.

## 📥 Download
[**Download the latest APK**](https://github.com/nischalsir/DShare/releases/latest) from the Releases page.

## ✨ Features

* **Smart Network Selection:** Automatically detects the optimal connection. Prioritizes local IP sockets if both devices share a WiFi network; seamlessly fallbacks to a Mobile Hotspot if no shared network exists.
* **True Background Transfers:** Utilizes Foreground Services and Coroutines. Transfers survive app minimization and screen locks.
* **Real-time Notifications:** Persistent notifications display the current file name, progress percentage, transfer speed (MB/s), and a cancellation action.
* **QR Code Pairing:** Frictionless device connection. The sender generates a QR code containing their IP and Port, which the receiver scans to establish an instant socket connection.
* **Heavy-Duty File Engine:** Capable of handling sequential transfers of multiple files and massive files (1GB+). Built with buffered streams (8KB) for optimal IO performance.

## 🛠 Tech Stack & Architecture

Built following Clean Architecture and modern Android development standards:

* **UI:** Jetpack Compose (Material 3)
* **Language:** Kotlin
* **Architecture:** MVVM (Model-View-ViewModel) + Repository Pattern
* **Concurrency:** Kotlin Coroutines & `Dispatchers.IO`
* **Networking:** Java `ServerSocket` / `Socket` (TCP)
* **File Handling:** Storage Access Framework (SAF)
* **Background Work:** Android Foreground Services & NotificationManager

## 📱 Screenshots
*(Add your UI screenshots here once your Compose layouts are finished!)*
## 🧑‍💻 How It Works Under the Hood

### The Transfer Protocol
1. **Sender** opens a `ServerSocket` on port `8888` and waits for a client.
2. **Receiver** scans the QR code to extract the IP address and connects via standard Socket.
3. Files are chunked and streamed through an 8KB buffer to prevent memory overflow, even with massive video/APK files.
4. If a connection drops, the app features logic to gracefully halt and prompt a retry without freezing the UI.

### Testing Locally
To test the socket connection, you can run the app on an emulator and a physical device (e.g., testing the connection dynamics between your PC environment and a mobile device like a Xiaomi 15T). Ensure both are on the exact same local network to test the first-priority routing, or disconnect one to test the Hotspot fallback.

## 🤝 Contributing
Contributions, issues, and feature requests are welcome!
1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License
Distributed under the MIT License. See `LICENSE` for more information.