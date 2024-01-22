package com.example.mostri.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mostri.MainActivity;
import com.example.mostri.api.CommunicationController;
import com.example.mostri.api.OnFailureListener;

public class SessionDataRepository {
    private static String sid;
    private static String uid;

    public static void initSidAndUid(@NonNull Context context, InitSidAndUidListener initSidAndUidListener, OnFailureListener onFailureListener) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences", 0);
        sid = sharedPreferences.getString("sid", "");
        uid = sharedPreferences.getString("uid", "");
        if (TextUtils.isEmpty(sid) || TextUtils.isEmpty(uid)) {
            CommunicationController.createUser(
                    (newSid, newUid) -> {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("sid", newSid);
                        editor.putString("uid", newUid);
                        editor.apply();
                        sid = newSid;
                        uid = newUid;
                        Log.d(MainActivity.TAG, "Chiamata 1 - new sid: " + sid);
                        Log.d(MainActivity.TAG, "Chiamata 1 - new uid: " + uid);
                        initSidAndUidListener.onInitSidAndUidComplete(sid, uid);
                    },
                    t -> {
                        Log.d(MainActivity.TAG, "register failed");
                        onFailureListener.onFailure(t);
                    }
            );
        } else {
            Log.d(MainActivity.TAG, "Chiamata 1 - sid: " + sid);
            Log.d(MainActivity.TAG, "Chiamata 1 - uid: " + uid);
            initSidAndUidListener.onInitSidAndUidComplete(sid, uid);
        }
    }

    public static String getSid() {
        return sid;
    }

    public static String getUid() {
        return uid;
    }
}