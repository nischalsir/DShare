package com.example.core.network

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Build
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.net.InetAddress
import java.util.concurrent.CopyOnWriteArrayList

data class DiscoveredDevice(
    val name: String,
    val ipAddress: InetAddress,
    val port: Int
)

class LocalNetworkManager(private val context: Context) {
    private val nsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager
    private val serviceType = "_dshare._tcp."
    private val serviceName = "DShareDevice-${Build.MODEL}"

    private val _devices = MutableStateFlow<List<DiscoveredDevice>>(emptyList())
    val devices: StateFlow<List<DiscoveredDevice>> = _devices.asStateFlow()
    
    private val deviceList = CopyOnWriteArrayList<DiscoveredDevice>()

    private var registrationListener: NsdManager.RegistrationListener? = null
    private var discoveryListener: NsdManager.DiscoveryListener? = null

    fun registerService(port: Int) {
        try {
            val serviceInfo = NsdServiceInfo().apply {
                serviceName = this@LocalNetworkManager.serviceName
                serviceType = this@LocalNetworkManager.serviceType
                setPort(port)
            }

            registrationListener = object : NsdManager.RegistrationListener {
                override fun onServiceRegistered(NsdServiceInfo: NsdServiceInfo) {
                    Log.d("LocalNetworkManager", "Service registered: ${NsdServiceInfo.serviceName}")
                }

                override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                    Log.e("LocalNetworkManager", "Registration failed: $errorCode")
                }

                override fun onServiceUnregistered(arg0: NsdServiceInfo) {
                    Log.d("LocalNetworkManager", "Service unregistered")
                }

                override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                    Log.e("LocalNetworkManager", "Unregistration failed: $errorCode")
                }
            }

            nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("LocalNetworkManager", "Failed to register service", e)
        }
    }

    fun discoverServices() {
        try {
            discoveryListener = object : NsdManager.DiscoveryListener {
                override fun onDiscoveryStarted(regType: String) {
                    Log.d("LocalNetworkManager", "Service discovery started")
                    deviceList.clear()
                    _devices.value = emptyList()
                }

                override fun onServiceFound(service: NsdServiceInfo) {
                    Log.d("LocalNetworkManager", "Service found: $service")
                    if (service.serviceType.contains(serviceType.trim('.')) && service.serviceName != serviceName) {
                        try {
                            nsdManager.resolveService(service, object : NsdManager.ResolveListener {
                                override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                                    Log.e("LocalNetworkManager", "Resolve failed: $errorCode")
                                }

                                override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
                                    Log.d("LocalNetworkManager", "Resolve Succeeded. $serviceInfo")
                                    val host = serviceInfo.host ?: return
                                    val port = serviceInfo.port
                                    val newDevice = DiscoveredDevice(serviceInfo.serviceName, host, port)
                                    
                                    if (deviceList.none { it.name == newDevice.name }) {
                                        deviceList.add(newDevice)
                                        _devices.value = deviceList.toList()
                                    }
                                }
                            })
                        } catch (e: Exception) {
                            Log.e("LocalNetworkManager", "Resolve failed to start", e)
                        }
                    }
                }

                override fun onServiceLost(service: NsdServiceInfo) {
                    Log.e("LocalNetworkManager", "service lost: $service")
                    deviceList.removeAll { it.name == service.serviceName }
                    _devices.value = deviceList.toList()
                }

                override fun onDiscoveryStopped(serviceType: String) {
                    Log.i("LocalNetworkManager", "Discovery stopped: $serviceType")
                }

                override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                    Log.e("LocalNetworkManager", "Discovery failed: Error code:$errorCode")
                    stopDiscovery()
                }

                override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                    Log.e("LocalNetworkManager", "Stop Discovery failed: Error code:$errorCode")
                    stopDiscovery()
                }
            }

            nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("LocalNetworkManager", "Failed to discover services", e)
        }
    }

    fun stopAdvertising() {
        try {
            registrationListener?.let { nsdManager.unregisterService(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            registrationListener = null
        }
    }

    fun stopDiscovery() {
        try {
            discoveryListener?.let { nsdManager.stopServiceDiscovery(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            discoveryListener = null
        }
    }
}
