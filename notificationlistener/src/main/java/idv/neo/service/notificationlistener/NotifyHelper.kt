package idv.neo.service.notificationlistener

import android.service.notification.StatusBarNotification
import android.util.Log

class NotifyHelper private constructor() {
    private var notifyListener: NotifyListener? = null

    fun onReceive(sbn: StatusBarNotification) {
        notifyListener?.onReceiveMessage(sbn)
    }

    fun onRemoved(sbn: StatusBarNotification) {
        notifyListener?.onRemovedMessage(sbn)
    }

    fun setNotifyListener(notifyListener: NotifyListener?) {
        this.notifyListener = notifyListener
    }

    companion object {
        @Volatile
        private var INSTANCE: NotifyHelper? = null
//        @JvmStatic
//        var instance: NotifyHelper? = null
//            get() {
//                synchronized(NotifyHelper::class.java) {
//                    if (field == null) {
//                        field = NotifyHelper()
//                    }
//                }
//                return field
//            }
//            private set

//        @JvmStatic
//        val instance: NotifyHelper by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
//            NotifyHelper()
//        }
        @JvmStatic
        fun getInstance(): NotifyHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: run {
                    val newInstance = NotifyHelper()
                    //For debug android:process
//                    Log.e("NotifyHelper_Debug", " 創建新實例 : ${newInstance.hashCode()} | " +
//                            "進程ID: ${android.os.Process.myPid()} | " +
//                            "ClassLoader: ${newInstance.javaClass.classLoader}")
                    newInstance
                }.also { INSTANCE = it }
            }
        }
    }

    init {
        // 防禦性檢查：確保不會通過反射創建多個實例
        if (INSTANCE != null) {
            throw IllegalStateException("已存在實例: ${INSTANCE.hashCode()}!" +
                    " 禁止通過反射創建新實例: ${this.hashCode()}")
        }
    }
}
