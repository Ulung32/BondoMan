package com.example.bondoman.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import com.example.bondoman.AddTransactionActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import pub.devrel.easypermissions.EasyPermissions
import java.util.concurrent.CompletableFuture

class LocationClient (private val context: Context, private val activity: Activity){
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    fun getLocationUpdates(): Location {
        val future = CompletableFuture<Location>()

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    future.complete(location)
                } else {
                    future.completeExceptionally(Exception("Last known location is null"))
                }
            }
            .addOnFailureListener { e ->
                future.completeExceptionally(e)
            }

        return future.get()
    }

    public fun requestLocationPermissions() {
        if(!EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )) {
            EasyPermissions.requestPermissions(
                activity,
                "Izinkan akses lokasi?",
                123,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }

    public fun haveLocationPermissions(): Boolean{
        return EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}