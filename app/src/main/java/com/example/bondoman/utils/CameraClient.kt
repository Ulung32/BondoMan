package com.example.bondoman.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import pub.devrel.easypermissions.EasyPermissions

class CameraClient(private val context: Context, private val activity: Activity) {
    
    fun requestCameraPermission(){
        if (!EasyPermissions.hasPermissions(context, Manifest.permission.CAMERA)) {
            EasyPermissions.requestPermissions(
                activity,
                "Izinkan akses kamera untuk mengambil gambar?",
                122,
                Manifest.permission.CAMERA
            )
        }

    }

    fun hasCameraPermission(): Boolean {
        return EasyPermissions.hasPermissions(context, Manifest.permission.CAMERA)
    }
}
