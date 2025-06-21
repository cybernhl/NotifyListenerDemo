package idv.neo.service.notificationlistener

import android.service.notification.StatusBarNotification

interface NotifyListener  {
    open fun onReceiveMessage(sbn: StatusBarNotification)
    open fun onRemovedMessage(sbn: StatusBarNotification)
}