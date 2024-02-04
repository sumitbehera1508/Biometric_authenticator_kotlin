package `in`.apps.sumit.contactrecognization

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor
import javax.security.auth.callback.Callback

class MyBiometricManager private constructor(){
    private var executor : Executor? = null
    private var biometricPrompt:BiometricPrompt? = null
    private var promtInfo : PromptInfo? = null
    private var context:Context? = null
    private var fragmentActivity:FragmentActivity? = null
    private var callback : Callback ?= null

    companion object{
        private var instance : MyBiometricManager?=null
        const val REQUEST_CODE = 100
        fun getInstance(context: Context) : MyBiometricManager?{
            if(instance==null){
                instance = MyBiometricManager()
            }
            instance!!.init(context)
            return instance
        }
    }

    private fun init(context: Context) {
        this.context = context
        fragmentActivity= context as FragmentActivity
        callback = context as Callback
    }

    fun checkBiometricAvailable():Boolean{
        val biometricManager = BiometricManager.from(context!!)
        when (biometricManager.canAuthenticate()){
            BiometricManager.BIOMETRIC_SUCCESS ->{
                Log.d("sk","App can authenticate")
                return true
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e("sk","Hardware not compatible")
                Toast.makeText(context,"No biometric available",Toast.LENGTH_SHORT).show()
                return false
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL)
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
                            or android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                fragmentActivity!!.startActivityForResult(enrollIntent, REQUEST_CODE)
                return false
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e("sk","Hardware not compatible")
                Toast.makeText(context,"No biometric available",Toast.LENGTH_SHORT).show()
                return false
            }

        }
        return false
    }
}