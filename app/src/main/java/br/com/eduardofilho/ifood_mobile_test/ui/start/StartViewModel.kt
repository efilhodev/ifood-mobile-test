package br.com.eduardofilho.ifood_mobile_test.ui.start

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import br.com.eduardofilho.ifood_mobile_test.App
import br.com.eduardofilho.ifood_mobile_test.R
import br.com.eduardofilho.ifood_mobile_test.base.BaseViewModel
import br.com.eduardofilho.ifood_mobile_test.model.TwitterOAuthToken
import br.com.eduardofilho.ifood_mobile_test.network.AppRestEndpoints
import br.com.eduardofilho.ifood_mobile_test.utils.PREFERENCES
import br.com.eduardofilho.ifood_mobile_test.utils.TWITTER_ACCESS_TOKEN_PREF
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class StartViewModel : BaseViewModel(){

    @Inject
    lateinit var api : AppRestEndpoints

    var retrieveTwitterOAuthTokenError: ( (message : String)  -> Unit)? = null
    var retrieveTwitterOAuthTokenSuccess : (() -> Unit)? = null

    val startLoadingVisibility : MutableLiveData<Int> = MutableLiveData()
    val startButtonVisibility : MutableLiveData<Int> = MutableLiveData()

    private lateinit var subscription: Disposable

    private val context = App.applicationContext()

    fun getTwitterOAuthToken(){
        subscription = api.postTwitterCredentials("client_credentials")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveOAuthTokenStart() }
                .doOnTerminate { onRetrieveOAuthTokenFinish() }
                .subscribe(
                        { result -> onRetrieveTwitterOAuthTokenSuccess(result)},
                        { e -> onRetrieveTwitterOAuthTokenError(e)}
                )
    }

    fun validateUsernameInput(username : String) : Boolean{
        if(username.isEmpty() || username.contains(" ")) return false
        return true
    }

    fun onRetrieveOAuthTokenStart(){
        startLoadingVisibility.value = View.VISIBLE
        startButtonVisibility.value = View.INVISIBLE

        removeOldTwitterOAuthTokenAuthorization()
    }

    fun onRetrieveOAuthTokenFinish(){
        startLoadingVisibility.value = View.GONE
        startButtonVisibility.value = View.VISIBLE
    }

    fun onRetrieveTwitterOAuthTokenSuccess(twitterOAuthToken : TwitterOAuthToken){
        saveTwitterOAuthTokenAuthorization(twitterOAuthToken.getAuthorization())

        retrieveTwitterOAuthTokenSuccess?.invoke()
    }

    fun onRetrieveTwitterOAuthTokenError(e : Throwable){
        retrieveTwitterOAuthTokenError?.invoke(e.message ?: context.getString(R.string.err_something_wrong))
        e.printStackTrace()
    }

    fun saveTwitterOAuthTokenAuthorization(authorization : String){
        val preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        preferences.edit().putString(TWITTER_ACCESS_TOKEN_PREF, authorization).apply()
    }

    fun removeOldTwitterOAuthTokenAuthorization(){
        val preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        preferences.edit().remove(TWITTER_ACCESS_TOKEN_PREF).apply()
    }

    override fun onCleared() {
        super.onCleared()
        try {
            subscription.dispose()
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
}