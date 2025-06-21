package com.llw.notify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import com.llw.notify.databinding.ActivityMainBinding;

import idv.neo.service.notificationlistener.NotifyHelper;
import idv.neo.service.notificationlistener.NotifyListener;
import idv.neo.service.notificationlistener.NotifyService;

public class MainActivity extends AppCompatActivity implements NotifyListener {
    private final String TAG="MainActivity";
    private ActivityMainBinding binding;
    private CustomAdapter adapter = new CustomAdapter();
    private static final int REQUEST_CODE = 9527;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        NotifyHelper.getInstance().setNotifyListener(this);
    }

    /**
     * 请求权限
     *
     * @param view
     */
    public void requestPermission(View view) {
        if (!isNLServiceEnabled()) {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            showMsg("通知服务已开启");
            toggleNotificationListenerService();
        }
    }

    /**
     * 是否启用通知监听服务
     *
     * @return
     */
    public boolean isNLServiceEnabled() {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(this);
        if (packageNames.contains(getPackageName())) {
            return true;
        }
        return false;
    }

    /**
     * 切换通知监听器服务
     */
    public void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(getApplicationContext(), NotifyService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(new ComponentName(getApplicationContext(), NotifyService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (isNLServiceEnabled()) {
                showMsg("通知服务已开启");
                toggleNotificationListenerService();
            } else {
                showMsg("通知服务未开启");
            }
        }
    }


    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 收到通知
     *
     * @param sbn 状态栏通知
     */
    @Override
    public void onReceiveMessage(StatusBarNotification sbn) {
        final String applicationid = sbn.getPackageName();
        final long posttime = sbn.getPostTime();
        final String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE).format(new Date(posttime));
        final Bundle bundle = sbn.getNotification().extras;
        final StringBuilder sb = new StringBuilder();
        final Set<String> keys = bundle.keySet();
        if (keys != null && !keys.isEmpty()) {
            for (String key : keys) {
                try {
                    Object obj = bundle.get(key);
                    String value;
                    if (obj != null) {
                        value = obj.toString()
                                .replace("\n", "\\n")
                                .replace("\t", "\\t");
                        value = value.isEmpty() ? "[empty]" : value;
                    } else {
                        value = "null";
                    }
                    sb.append("  ")
                            .append(key)
                            .append(": ")
                            .append(value)
                            .append("\n");
                } catch (Exception e) {
                    final String errorMsg = e.getMessage() != null ?
                            e.getMessage().replace("\n", " ") : "Unknown error";
                    sb.append("  ")
                            .append(key)
                            .append(": [ERROR] ")
                            .append(errorMsg)
                            .append("\n");
                }
            }
        }
        final String msgContent = String.format(Locale.getDefault(),
                "应用包名：%s\n消息内容：%s\n消息时间：%s\n",
                sbn.getPackageName(), sb, time);
        adapter.addContent(msgContent);
    }

    /**
     * 移除通知
     *
     * @param sbn 状态栏通知
     */
    @Override
    public void onRemovedMessage(StatusBarNotification sbn) {

    }

    @Override
    public void onResume() {
        super.onResume();
        binding.list.setAdapter(adapter);
    }
}