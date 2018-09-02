package br.com.eduardofilho.ifood_mobile_test.base

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.eduardofilho.ifood_mobile_test.network.ConnectivityReceiver
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener{
    private var mSnackBar : Snackbar? = null

    abstract fun onNetworkConnectionChangedStatus(isConnected: Boolean)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(ConnectivityReceiver(),
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onResume() {
        super.onResume()

        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        onNetworkConnectionChangedStatus(isConnected)
    }

    fun showErrorSnackBar(view : View, error : String){
        mSnackBar = Snackbar.make(view, error, Snackbar.LENGTH_LONG)
        mSnackBar!!.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
        mSnackBar!!.show()
    }

    fun hideErrorSnackBar(){
        mSnackBar?.dismiss()
    }
}