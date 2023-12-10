package com.example.githubsearch.core.network.networkConnectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class NetworkConnectivityImpl @Inject constructor(
	@ApplicationContext context: Context
): INetworkConnectivity, CoroutineScope {

	override val coroutineContext: CoroutineContext
		get() = Job() + Dispatchers.Default

	private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
	private val _sharedFlow = MutableSharedFlow<Boolean>()
	override val networkStateFlow: Flow<Boolean> = _sharedFlow

	init {
		val networkRequest = NetworkRequest.Builder()
			.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
			.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
			.build()

		connectivityManager.registerNetworkCallback(networkRequest, object: ConnectivityManager.NetworkCallback() {
			override fun onAvailable(network: Network) {
				super.onAvailable(network)
				launch { _sharedFlow.emit(true) }
			}

			override fun onUnavailable() {
				super.onUnavailable()
				launch { _sharedFlow.emit(false) }
			}

			override fun onLost(network: Network) {
				super.onLost(network)
				launch { _sharedFlow.emit(false) }
			}
		})
	}

	override val wifiConnection:Boolean
		get() {
			return networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
		}

	override fun isConnected(): Boolean {
		val actNw = networkCapabilities ?: return false
		return when {
			actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
			actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
			else -> false
		}
	}

	private val networkCapabilities: NetworkCapabilities?
		get() {
			return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
		}
}
