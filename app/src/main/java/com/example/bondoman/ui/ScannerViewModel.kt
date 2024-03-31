import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bondoman.util.CameraClient

class ScannerViewModel : ViewModel() {
    private val _hasCameraPermission = MutableLiveData<Boolean>()
    val hasCameraPermission: LiveData<Boolean>
        get() =  _hasCameraPermission

    fun requestCameraPermission(cameraClient: CameraClient) {
        // Request camera permission here
        // For simplicity, assume permission is granted after 3 seconds
        // In your actual implementation, you should use the proper permission request code and logic
//        val hasPermission = cameraClient.requestCameraPermission()
//        _hasCameraPermission.setValue(hasPermission)
//        Log.v("di view model", hasCameraPermission.value.toString())
    }
}
