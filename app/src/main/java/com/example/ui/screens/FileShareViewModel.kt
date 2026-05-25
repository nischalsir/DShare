package com.example.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.network.DiscoveredDevice
import com.example.core.network.LocalNetworkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

data class IncomingRequest(
    val deviceName: String,
    val fileCount: Int,
    val totalSize: String
)

class FileShareViewModel(application: Application) : AndroidViewModel(application) {
    val localNetworkManager = LocalNetworkManager(application)

    val peers: StateFlow<List<DiscoveredDevice>> = localNetworkManager.devices.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _connectedDevice = MutableStateFlow<DiscoveredDevice?>(null)
    val connectedDevice: StateFlow<DiscoveredDevice?> = _connectedDevice.asStateFlow()

    private val _isReceiverReady = MutableStateFlow(false)
    val isReceiverReady: StateFlow<Boolean> = _isReceiverReady.asStateFlow()

    private val _incomingRequest = MutableStateFlow<IncomingRequest?>(null)
    val incomingRequest: StateFlow<IncomingRequest?> = _incomingRequest.asStateFlow()

    fun startDiscovery() {
        localNetworkManager.discoverServices()
    }

    fun stopDiscovery() {
        localNetworkManager.stopDiscovery()
    }
    
    fun startAdvertising(port: Int = 8888) {
        localNetworkManager.registerService(port)
        _isReceiverReady.value = true
        
        // FOR PROTOTYPE: Simulate incoming request after 5 seconds
        /*
        viewModelScope.launch {
            delay(5000)
            _incomingRequest.value = IncomingRequest("Pixel 7 Pro", 3, "24.5 MB")
        }
        */
    }
    
    fun stopAdvertising() {
        localNetworkManager.stopAdvertising()
        _isReceiverReady.value = false
        _incomingRequest.value = null
    }

    fun approveRequest() {
        // Start receiving logic...
    }

    fun rejectRequest() {
        _incomingRequest.value = null
    }

    fun connectToPeer(device: DiscoveredDevice) {
        _connectedDevice.value = device
    }
    
    fun disconnect() {
        _connectedDevice.value = null
    }

    override fun onCleared() {
        super.onCleared()
        localNetworkManager.stopDiscovery()
        localNetworkManager.stopAdvertising()
    }
}
