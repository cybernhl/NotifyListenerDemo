package com.llw.notify;

import android.service.notification.StatusBarNotification;

/**
 * @author llw
 * @description NotifyHelper
 * @date 2021/8/12 11:27
 */
public class NotifyHelper {
    private static volatile NotifyHelper instance;
    private NotifyListener notifyListener;

    public static NotifyHelper getInstance() {
        if (instance == null) {
            synchronized (NotifyHelper.class) {
                if (instance == null) {
                    instance = new NotifyHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 设置回调方法
     *
     * @param notifyListener 通知监听
     */
    public void setNotifyListener(NotifyListener notifyListener) {
        this.notifyListener = notifyListener;
    }

    /**
     * 收到消息
     *
     * @param sbn 状态栏通知
     */
    public void onReceive(StatusBarNotification sbn) {
        if (notifyListener != null) {
            notifyListener.onReceiveMessage(sbn);
        }
    }

    /**
     * 移除消息
     *
     * @param sbn 状态栏通知
     */
    public void onRemoved(StatusBarNotification sbn) {
        if (notifyListener != null) {
            notifyListener.onRemovedMessage(sbn);
        }
    }
}
