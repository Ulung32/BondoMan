package com.example.bondoman.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.concurrent.CompletableFuture

class LocationClient (private val context: Context){
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
}