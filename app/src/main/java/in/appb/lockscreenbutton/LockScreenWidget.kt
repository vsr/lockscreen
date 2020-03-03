package `in`.appb.lockscreenbutton

//import sun.jvm.hotspot.utilities.IntArray

import android.app.PendingIntent
import android.app.admin.DevicePolicyManager
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast


/**
 * Implementation of App Widget functionality.
 */
class LockScreenWidget : AppWidgetProvider() {
    private val _lockButtonClick = "_lockButtonClick"

    protected fun getPendingSelfIntent(
        context: Context?,
        action: String?
    ): PendingIntent? {
        Log.d("lockscreenwidget", "getPendingSelfIntent")
        val intent = Intent(context, javaClass)
        intent.action = action
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {

            val views = RemoteViews(context.packageName, R.layout.lock_screen_widget)
            views.setOnClickPendingIntent(
                R.id.appwidget_button,
                getPendingSelfIntent(context, _lockButtonClick)
            )
            appWidgetManager.updateAppWidget(appWidgetId, views)
            Log.d("lockscreenwidget", "onUpdate")
//            updateAppWidget(context, appWidgetManager, appWidgetId)

        }
    }

    override fun onEnabled(context: Context) {
        Log.d("lockscreenwidget", "onEnabled")
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        Log.d("lockscreenwidget", "onDisabled")
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent) {
        Log.d("lockscreenwidget", "onReceive")
        Log.d("lockscreenwidget", intent.action)
        super.onReceive(context, intent)
        if (_lockButtonClick.equals(intent.action)) {
            if (context != null) {
                lockScreen(context)
            }

        }
    }

    fun lockScreen(context: Context) {
        Log.d("lockscreenwidget", "lock the screen")
        val devicePolicyManager =
            context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val mDeviceAdmin = ComponentName(context, DeviceAdmin::class.java)
        if (devicePolicyManager.isAdminActive(mDeviceAdmin)) {
            devicePolicyManager.lockNow()
        } else {
            Toast.makeText(
                context,
                "You need to enable the Admin Device Features",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}

//internal fun updateAppWidget(
//    context: Context,
//    appWidgetManager: AppWidgetManager,
//    appWidgetId: Int
//) {
//    val widgetText = context.getString(R.string.appwidget_text)
//    // Construct the RemoteViews object
//    val views = RemoteViews(context.packageName, R.layout.lock_screen_widget)
//
////    views.setTextViewText(R.id.appwidget_text, widgetText)
//    // Instruct the widget manager to update the widget
//
//
//    appWidgetManager.updateAppWidget(appWidgetId, views)
//}