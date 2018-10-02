package com.ahmedadelsaid.androidslicesexample

import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiManager
import android.provider.Settings
import androidx.core.graphics.drawable.IconCompat
import androidx.slice.Slice
import androidx.slice.SliceProvider
import androidx.slice.builders.*
import androidx.slice.core.SliceHints
import java.util.*
import kotlin.concurrent.schedule

class ExamplesSliceProvider : SliceProvider() {

    companion object {
        var contentLoaded = false

        const val actualFare = "45 miles | 45 mins | $45.23"
        val delayContentSliceUri: Uri = Uri.parse("content://com.ahmedadelsaid.androidslicesexample/delayContentSliceExample")
    }

    /**
     * Instantiate any required objects. Return true if the provider was successfully created,
     * false otherwise.
     */
    override fun onCreateSliceProvider(): Boolean {
        return true
    }

    /**
     * ************************************************************************************************
     */

    /**
     * Converts URL to content URI (i.e. content://com.ahmedadelsaid.androidslicesexample...)
     */
    override fun onMapIntentToUri(intent: Intent?): Uri {
        // Note: implementing this is only required if you plan on catching URL requests.
        // This is an example solution.
        var uriBuilder: Uri.Builder = Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
        if (intent == null) return uriBuilder.build()
        val data = intent.data
        if (data != null && data.path != null) {
            val path = data.path?.replace("/", "")
            uriBuilder = uriBuilder.path(path)
        }
        val context = context
        if (context != null) {
            uriBuilder = uriBuilder.authority(context.packageName)
        }
        return uriBuilder.build()
    }

    /**
     * ************************************************************************************************
     */

    /**
     * Construct the Slice and bind data if available.
     */
    override fun onBindSlice(sliceUri: Uri): Slice? {
        val context = context ?: return null
        val activityAction =
                createActivityAction(
                        Intent(context, MainActivity::class.java),
                        R.drawable.ic_pizza_slice_24,
                        SliceHints.ICON_IMAGE)
        return when {

            sliceUri.path == "/basicHeaderExample" -> createBasicHeaderSlice(sliceUri)

            sliceUri.path == "/basicActionClickExample" -> createBasicInteractiveSlice(sliceUri, activityAction)

            sliceUri.path == "/wifiToggleActionExample" -> createWifiToggleActionSlice(sliceUri)

            sliceUri.path == "/dynamicCountExample" -> createDynamicSlice(sliceUri, activityAction)

            sliceUri.path == "/inputRangeExample" -> createInputRangeSlice(sliceUri, activityAction)

            sliceUri.path == "/rangeExample" -> createRangeSlice(sliceUri, activityAction)

            sliceUri.path == "/headerWithMoreActionsExample" -> createHeaderSliceWithMoreActions(sliceUri)

            sliceUri.path == "/headerWithHeaderAndRowExample" -> createSliceWithHeaderAndRow(sliceUri, activityAction)

            sliceUri.path == "/gridRowExample" -> createSliceWithGridRow(sliceUri, activityAction)

            sliceUri.path == "/delayContentExample" -> createSliceShowingLoading(sliceUri, activityAction)

            sliceUri.path == "/seeMoreRowExample" -> createSliceWithSeeMoreAction(sliceUri, activityAction)

            sliceUri.path == "/combineExample" -> createCombineSlices(sliceUri)

            sliceUri.path == "/trafficInfoExample" -> createTrafficInfoSlice(sliceUri)

            else -> createErrorSlice(sliceUri, activityAction)

        }
    }

    /**
     * ************************************************************************************************
     */

    /**
     * Basic Slices
     */

    // basicHeaderExample
    private fun createBasicHeaderSlice(sliceUri: Uri): Slice? {
        context?.let { context ->
            return ListBuilder(context, sliceUri, ListBuilder.INFINITY)
                    .setHeader {
                        it.title = "Welcome Android Slice Example"
                        it.subtitle = "Header of Slice"
                    }
                    .build()
        }
        return null
    }

    // basicActionClickExample
    private fun createBasicInteractiveSlice(sliceUri: Uri, sliceAction: SliceAction): Slice? {
        context?.let { context ->
            val listBuilder = ListBuilder(context, sliceUri, ListBuilder.INFINITY)
            val rowBuilder = ListBuilder.RowBuilder(listBuilder)
                    .setTitle("Android Slice Example")
                    .setSubtitle("Click Me", true)
                    .setPrimaryAction(sliceAction)
            listBuilder.addRow(rowBuilder)
            return listBuilder.build()
        }
        return null
    }

    // wifiToggleActionExample
    private fun createWifiToggleActionSlice(sliceUri: Uri): Slice? {
        context?.let { context ->
            val subTitle: String
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE)
            val isWifiEnabled = wifiManager is WifiManager && wifiManager.isWifiEnabled
            subTitle = if (isWifiEnabled) {
                "Enabled"
            } else {
                "Not Enabled"
            }
            return list(context, sliceUri, ListBuilder.INFINITY) {
                row {
                    title = "Wifi"
                    subtitle = subTitle
                    primaryAction = createWiFiToggleAction(isWifiEnabled)
                }
            }
        }
        return null
    }

    // dynamicCountExample
    private fun createDynamicSlice(sliceUri: Uri, sliceAction: SliceAction): Slice? {
        val incrementPendingIntent = PendingIntent.getBroadcast(context,
                0,
                Intent(context, IncrementDecrementBroadcastReceiver::class.java)
                        .setAction(IncrementDecrementBroadcastReceiver.INCREMENT_COUNTER_ACTION)
                        .putExtra(IncrementDecrementBroadcastReceiver.EXTRA_VALUE_KEY,
                            IncrementDecrementBroadcastReceiver.currentValue + 1),
                PendingIntent.FLAG_UPDATE_CURRENT)

        val decrementPendingIntent = PendingIntent.getBroadcast(context,
                0,
                Intent(context, IncrementDecrementBroadcastReceiver::class.java)
                        .setAction(IncrementDecrementBroadcastReceiver.DECREMENT_COUNTER_ACTION)
                        .putExtra(IncrementDecrementBroadcastReceiver.EXTRA_VALUE_KEY,
                            IncrementDecrementBroadcastReceiver.currentValue - 1),
                PendingIntent.FLAG_UPDATE_CURRENT)

        val incrementAction = SliceAction.create(incrementPendingIntent,
                IconCompat.createWithResource(context, R.drawable.ic_plus),
                ListBuilder.ICON_IMAGE, "Increment Counter.")
        val decrementAction = SliceAction.create(decrementPendingIntent,
                IconCompat.createWithResource(context, R.drawable.ic_minus),
                ListBuilder.ICON_IMAGE, "Decrement Counter.")

        context?.let { context ->
            return list(context, sliceUri, ListBuilder.INFINITY) {
                row {
                    primaryAction = sliceAction
                    title = "Total Count : ${IncrementDecrementBroadcastReceiver.currentValue}"
                    subtitle = "This is Dynamic Slice Android Example"
                    addEndItem(incrementAction)
                    addEndItem(decrementAction)
                }
            }
        }
        return null
    }

    // inputRangeExample
    private fun createInputRangeSlice(sliceUri: Uri, sliceAction: SliceAction): Slice? {
        context?.let { context ->
            return list(context, sliceUri, ListBuilder.INFINITY) {
                inputRange {
                    title = "Adaptive brightness"
                    subtitle = "Optimizes brightness for available light"
                    min = 0
                    max = 100
                    value = 45
                    inputAction = createSettingsPendingIntent()
                    // not working primary action.
                    primaryAction = sliceAction
                }
            }
        }
        return null
    }

    // rangeExample
    private fun createRangeSlice(sliceUri: Uri, sliceAction: SliceAction): Slice? {
        context?.let { context ->
            return list(context, sliceUri, ListBuilder.INFINITY) {
                range {
                    title = "Current brightness level"
                    subtitle = "40 %"
                    max = 100
                    value = 40
                    primaryAction = sliceAction
                }
            }
        }
        return null
    }

    // headerWithMoreActionsExample
    private fun createHeaderSliceWithMoreActions(sliceUri: Uri): Slice? {
        context?.let { context ->
            return list(context, sliceUri, ListBuilder.INFINITY) {
                header {
                    title = "Header with 2 actions"
                    subtitle = "Choose any action"
                    summary = "Choose any action from two actions"
                }
                addAction(createActivityAction(Intent(Settings.ACTION_WIFI_SETTINGS),
                        R.drawable.ic_wifi_24, SliceHints.ICON_IMAGE))
                addAction(createActivityAction(Intent(Settings.ACTION_BLUETOOTH_SETTINGS),
                        R.drawable.ic_bluetooth_24, SliceHints.ICON_IMAGE))
            }
        }
        return null
    }

    // headerWithHeaderAndRowExample
    private fun createSliceWithHeaderAndRow(sliceUri: Uri, sliceAction: SliceAction): Slice? {
        context?.let { context ->
            return list(context, sliceUri, ListBuilder.INFINITY) {
                header {
                    title = "Get a ride."
                    subtitle = "Ride in 4 min."
                    summary = "Work in 45 min | Home in 15 min."
                }
                row {
                    title = "Home"
                    subtitle = "15 miles | 15 min | $15.23"
                    primaryAction = sliceAction
                }
                row {
                    title = "Work"
                    subtitle = "45 miles | 45 min | $15.23"
                    addEndItem(sliceAction)
                }
                row {
                    title = "Slice Row"
                    subtitle = "contains start and end items"
                    primaryAction = sliceAction
                    setTitleItem(sliceAction)
                }
                setAccentColor(R.color.colorAccent)
            }
        }
        return null
    }

    // gridRowExample
    private fun createSliceWithGridRow(sliceUri: Uri, sliceAction: SliceAction): Slice? {
        context?.let { context ->
            return list(context, sliceUri, ListBuilder.INFINITY) {
                header {
                    title = "Famous restaurants"
                    primaryAction = createActivityAction(Intent(context, MainActivity::class.java), R.drawable.ic_restaurant_24, SliceHints.ICON_IMAGE)
                }
                gridRow {
                    cell {
                        addImage(IconCompat.createWithResource(context, R.drawable.android_icon_one), SliceHints.LARGE_IMAGE)
                        addTitleText("Android Restaurant One")
                        addText("0.3 mil")
                        contentIntent = createSettingsPendingIntent()
                    }
                    cell {
                        addImage(IconCompat.createWithResource(context, R.drawable.android_icon_two), SliceHints.LARGE_IMAGE)
                        addTitleText("Android Restaurant Two")
                        addText("0.5 mil")
                        contentIntent = createSettingsPendingIntent()
                    }
                    cell {
                        addImage(IconCompat.createWithResource(context, R.drawable.android_icon_three), SliceHints.LARGE_IMAGE)
                        addTitleText("Android Restaurant Three")
                        addText("0.9 mi")
                        contentIntent = createSettingsPendingIntent()
                    }
                    cell {
                        addImage(IconCompat.createWithResource(context, R.drawable.android_icon_four), SliceHints.LARGE_IMAGE)
                        addTitleText("Android Restaurant Four")
                        addText("1.2 mi")
                        contentIntent = createSettingsPendingIntent()
                    }
                    cell {
                        addImage(IconCompat.createWithResource(context, R.drawable.android_icon_five), SliceHints.LARGE_IMAGE)
                        addTitleText("Android Restaurant Five")
                        addText("1.2 mi")
                        contentIntent = createSettingsPendingIntent()
                    }
                    setSeeMoreAction(createSettingsPendingIntent())
                    primaryAction = sliceAction
                }
            }
        }
        return null
    }

    // delayContentExample
    private fun createSliceShowingLoading(sliceUri: Uri, sliceAction: SliceAction): Slice? {
        Timer("Setting Up", false).schedule(2000) {
            contentLoaded = true
            onSlicePinned(delayContentSliceUri)
        }
        context?.let { context ->
            return list(context, sliceUri, ListBuilder.INFINITY) {
                row {
                    title = "Ride to work!"
                    if (contentLoaded) {
                        setSubtitle(actualFare, false)
                    } else {
                        setSubtitle(null, true)
                    }
                    primaryAction = sliceAction
                }
            }
        }
        return null
    }

    // seeMoreRowExample
    private fun createSliceWithSeeMoreAction(sliceUri: Uri, sliceAction: SliceAction): Slice? {

        val gmmIntentUri = Uri.parse("geo:30.0444,31.2357?q=restaurants")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        context?.let { context ->
            return list(context, sliceUri, ListBuilder.INFINITY) {
                header {
                    title = "Near by restaurants"
                    primaryAction = sliceAction
                }
                row {
                    primaryAction = createActivityAction(mapIntent, R.drawable.ic_place_24, SliceHints.ICON_IMAGE)
                    title = "Mcdonalds"
                    setTitleItem(IconCompat.createWithResource(context, R.drawable.android_icon_one), SliceHints.SMALL_IMAGE)
                }
                row {
                    primaryAction = createActivityAction(mapIntent, R.drawable.ic_place_24, SliceHints.ICON_IMAGE)
                    title = "Hardees"
                    setTitleItem(IconCompat.createWithResource(context, R.drawable.android_icon_two), SliceHints.SMALL_IMAGE)
                }
                row {
                    primaryAction = createActivityAction(mapIntent, R.drawable.ic_place_24, SliceHints.ICON_IMAGE)
                    title = "Pizza Hut"
                    setTitleItem(IconCompat.createWithResource(context, R.drawable.android_icon_three), SliceHints.SMALL_IMAGE)
                }
                setSeeMoreAction(PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), 0))
            }
        }
        return null
    }

    // combineExample
    private fun createCombineSlices(sliceUri: Uri): Slice? {
        context?.let { context ->
            return list(context, sliceUri, ListBuilder.INFINITY) {
                row {
                    title = "Upcoming Trip: Heliopolis"
                    subtitle = "Aug 15-20 • 5 Guests"
                    primaryAction = createActivityAction(Intent(context, MainActivity::class.java), R.drawable.ic_email_24, SliceHints.ICON_IMAGE)
                }
                gridRow {
                    cell {
                        addImage(IconCompat.createWithResource(context, R.drawable.android_icon_one), SliceHints.LARGE_IMAGE)
                    }
                }
                gridRow {
                    cell {
                        addTitleText("Check In")
                        addText("2:00 PM, Aug 15")
                    }
                    cell {
                        addTitleText("Check In")
                        addText("11:00 AM, Aug 20")
                    }
                }
            }
        }
        return null
    }

    // trafficInfoExample
    private fun createTrafficInfoSlice(sliceUri: Uri): Slice? {

        val gmmIntentUri = Uri.parse("geo:30.0444,31.2357")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        context?.let { context ->
            return list(context, sliceUri, ListBuilder.INFINITY) {
                header {
                    title = "Heavy traffic in your area"
                    subtitle = "Typical conditions delays up to 28"
                }
                gridRow {
                    cell {
                        addImage(IconCompat.createWithResource(context, R.drawable.ic_home_24), SliceHints.ICON_IMAGE)
                        addTitleText("Home")
                        addText("30 min")
                        contentIntent = createMapsActivityPendingIntent()
                    }
                    cell {
                        addImage(IconCompat.createWithResource(context, R.drawable.ic_work_24), SliceHints.ICON_IMAGE)
                        addTitleText("Work")
                        addText("45 min")
                        contentIntent = createMapsActivityPendingIntent()
                    }
                    cell {
                        addImage(IconCompat.createWithResource(context, R.drawable.ic_restaurant_24), SliceHints.ICON_IMAGE)
                        addTitleText("Restaurant")
                        addText("5 min")
                        contentIntent = createMapsActivityPendingIntent()
                    }
                    primaryAction = createActivityAction(mapIntent, R.drawable.ic_directions_24, SliceHints.ICON_IMAGE)
                }
            }
        }
        return null
    }

    /**
     * ************************************************************************************************
     */

    /**
     * Pending Intents
     */
    private fun createSettingsPendingIntent(): PendingIntent {
        return PendingIntent.getActivity(
                context,
                0,
                Intent(Settings.ACTION_SETTINGS),
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun createMapsActivityPendingIntent(): PendingIntent {
        val gmmIntentUri = Uri.parse("geo:30.0444,31.2357")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        return PendingIntent.getActivity(
                context,
                0,
                mapIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun createWiFiToggleAction(wifiEnabled: Boolean): SliceAction {
        val intent = Intent(context, WifiBroadcastReceiver::class.java)
                .setAction(WifiBroadcastReceiver.TOGGLE_WIFI)
                .putExtra(WifiBroadcastReceiver.EXTRA_VALUE_KEY,
                        wifiEnabled)
        return SliceAction(PendingIntent.getBroadcast(context,
                0,
                intent,
                0), "Toggle Wi-Fi", wifiEnabled)
    }

    /**
     * ************************************************************************************************
     */

    /**
     * Error Slice
     */
    private fun createErrorSlice(sliceUri: Uri, sliceAction: SliceAction): Slice? {
        context?.let { context ->
            return ListBuilder(context, sliceUri, ListBuilder.INFINITY)
                    .addRow(ListBuilder.RowBuilder()
                            .setTitle("URI not found.")
                            .setPrimaryAction(sliceAction)
                    ).build()
        }
        return null
    }

    /**
     * ************************************************************************************************
     */

    /**
     * Slice has been pinned to external process. Subscribe to data source if necessary.
     */
    override fun onSlicePinned(sliceUri: Uri?) {
        sliceUri?.let {
            context?.contentResolver?.notifyChange(it, null)
        }
    }

    /**
     * ************************************************************************************************
     */

    /**
     * Unsubscribe from data source if necessary.
     */
    override fun onSliceUnpinned(sliceUri: Uri?) {
        // Remove any observers if necessary to avoid memory leaks.
    }

    private fun createActivityAction(actionIntent: Intent, drawableInt: Int, imageMode: Int): SliceAction {
        return SliceAction.create(
                PendingIntent.getActivity(context, 0, actionIntent, 0),
                IconCompat.createWithResource(context, drawableInt),
                imageMode,
                "Open MainActivity.")
    }

}
