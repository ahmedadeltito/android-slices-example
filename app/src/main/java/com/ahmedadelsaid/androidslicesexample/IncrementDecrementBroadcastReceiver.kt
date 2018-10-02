package com.ahmedadelsaid.androidslicesexample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri

class IncrementDecrementBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_VALUE_KEY = "extra_value"
        const val DECREMENT_COUNTER_ACTION = "com.ahmedadelsaid.androidslicesexample" +
                ".IncrementDecrementBroadcastReceiver.ACTION_DECREMENT_COUNTER"
        const val INCREMENT_COUNTER_ACTION = "com.ahmedadelsaid.androidslicesexample" +
                ".IncrementDecrementBroadcastReceiver.ACTION_INCREMENT_COUNTER"

        var currentValue = 0
        val dynamicSliceUri: Uri = Uri.parse("content://com.ahmedadelsaid.androidslicesexample" +
                "/dynamicCountExample")
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action?.equals(DECREMENT_COUNTER_ACTION) == true && intent.hasExtra(EXTRA_VALUE_KEY)) {
            currentValue = intent.getIntExtra(EXTRA_VALUE_KEY, 0)
            context.contentResolver?.notifyChange(dynamicSliceUri, null)
        } else if (intent.action?.equals(INCREMENT_COUNTER_ACTION) == true && intent.hasExtra(EXTRA_VALUE_KEY)) {
            currentValue = intent.getIntExtra(EXTRA_VALUE_KEY, 0)
            context.contentResolver?.notifyChange(dynamicSliceUri, null)
        }
    }
}
