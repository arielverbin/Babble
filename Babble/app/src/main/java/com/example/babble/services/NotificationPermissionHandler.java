package com.example.babble.services;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class NotificationPermissionHandler {
    private static final int PERMISSION_REQUEST_CODE = 123;
    private final Activity activity;

    public NotificationPermissionHandler(Activity activity) {
        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.POST_NOTIFICATIONS)) {
                // Show rationale dialog explaining why the permission is needed
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Permission Required")
                        .setMessage("Enable Babble to send notification?")
                        .setPositiveButton("Allow", (dialog, which) -> {
                            // Request the permission
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            // Permission denied, handle accordingly
                            // You can show a message or take any other action
                        })
                        .show();
            } else {
                // Directly ask for the permission
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
            }
        }
    }
}
