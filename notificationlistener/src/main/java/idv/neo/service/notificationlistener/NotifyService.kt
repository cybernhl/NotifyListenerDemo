package idv.neo.service.notificationlistener

import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotifyService : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        NotifyHelper.getInstance().onReceive(sbn)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        NotifyHelper.getInstance().onRemoved(sbn)
    }

    override fun onListenerDisconnected() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            requestRebind(
                ComponentName(
                    this,
                    NotifyService::class.java
                )
            )
        }
    }
}
