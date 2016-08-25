package com.f1l.permission.util;

import android.Manifest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by songxd
 * on 16/8/25.
 */
public class PermissionTip {
    public static HashMap<String ,String> permissionMsgMap = new HashMap<>();
    static {
        permissionMsgMap.put(Manifest.permission.READ_CALENDAR,"日历");
        permissionMsgMap.put(Manifest.permission.WRITE_CALENDAR,"日历");

        permissionMsgMap.put(Manifest.permission.CAMERA,"摄像头");

        permissionMsgMap.put(Manifest.permission.READ_CONTACTS,"联系人");
        permissionMsgMap.put(Manifest.permission.WRITE_CONTACTS,"联系人");
        permissionMsgMap.put(Manifest.permission.GET_ACCOUNTS,"联系人");

        permissionMsgMap.put(Manifest.permission.ACCESS_FINE_LOCATION,"定位");
        permissionMsgMap.put(Manifest.permission.ACCESS_COARSE_LOCATION,"定位");

        permissionMsgMap.put(Manifest.permission.RECORD_AUDIO,"录音");

        permissionMsgMap.put(Manifest.permission.READ_PHONE_STATE,"拨打电话");
        permissionMsgMap.put(Manifest.permission.CALL_PHONE,"拨打电话");
        permissionMsgMap.put(Manifest.permission.READ_CALL_LOG,"拨打电话");
        permissionMsgMap.put(Manifest.permission.WRITE_CALL_LOG,"拨打电话");
        permissionMsgMap.put(Manifest.permission.ADD_VOICEMAIL,"拨打电话");
        permissionMsgMap.put(Manifest.permission.USE_SIP,"拨打电话");
        permissionMsgMap.put(Manifest.permission.PROCESS_OUTGOING_CALLS,"拨打电话");

        permissionMsgMap.put(Manifest.permission.BODY_SENSORS,"传感器");

        permissionMsgMap.put(Manifest.permission.SEND_SMS,"短信");
        permissionMsgMap.put(Manifest.permission.RECEIVE_SMS,"短信");
        permissionMsgMap.put(Manifest.permission.READ_SMS,"短信");
        permissionMsgMap.put(Manifest.permission.RECEIVE_WAP_PUSH,"短信");
        permissionMsgMap.put(Manifest.permission.RECEIVE_MMS,"短信");

        permissionMsgMap.put(Manifest.permission.READ_EXTERNAL_STORAGE,"存储");
        permissionMsgMap.put(Manifest.permission.WRITE_EXTERNAL_STORAGE,"存储");

    }
    public static String buildTip(String[] permissions) {

        if (permissions == null || permissions.length == 0) {
            return "";
        }
        HashSet<String> msgSet = new HashSet<>();
        for (String permission : permissions) {
            msgSet.add(getPermissionMessage(permission));
        }

        StringBuffer sb = new StringBuffer();
        sb.append(getPermissionMessage(permissions[0]));
        Iterator<String> iterator = msgSet.iterator();
        sb.append(iterator.next());
        while (iterator.hasNext()) {
            sb.append("、");
            sb.append(iterator.next());
        }
        return sb.toString();

    }

    private static String getPermissionMessage (String permission) {
        return permissionMsgMap.get(permission);
    }
}
