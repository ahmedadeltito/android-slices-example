package com.ahmedadelsaid.androidslicesexample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiManager

class WifiBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_VALUE_KEY = "extra_value"
        const val TOGGLE_WIFI = "com.ahmedadelsaid.androidslicesexample.WifiBroadcastReceiver.TOGGLE_WIFI"

        val wifiToggleUri: Uri = Uri.parse("content://com.ahmedadelsaid.androidslicesexample/wifiToggleActionExample")
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action?.equals(TOGGLE_WIFI) == true) {
            val wifiManager = context.applicationContext?.getSystemService(Context.WIFI_SERVICE)
            if (wifiManager is WifiManager) {
                val wifiState = intent.getBooleanExtra(EXTRA_VALUE_KEY, wifiManager.isWifiEnabled)
                wifiManager.isWifiEnabled = !wifiState
                context.contentResolver?.notifyChange(wifiToggleUri, null)
            }
        }
    }
}
