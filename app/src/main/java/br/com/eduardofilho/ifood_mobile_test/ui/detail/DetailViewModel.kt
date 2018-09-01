package br.com.eduardofilho.ifood_mobile_test.ui.detail

import android.content.Context
import br.com.eduardofilho.ifood_mobile_test.App
import br.com.eduardofilho.ifood_mobile_test.base.BaseViewModel
import br.com.eduardofilho.ifood_mobile_test.network.NaturalLanguageAnalyzer
import br.com.eduardofilho.ifood_mobile_test.utils.ACCESS_TOKEN_PREF
import br.com.eduardofilho.ifood_mobile_test.utils.PREFERENCES
import com.google.api.services.language.v1.model.Sentiment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailViewModel : BaseViewModel(){

    @Inject
    lateinit var naturalLanguageAnalyzer: NaturalLanguageAnalyzer

    private lateinit var subscription: Disposable

    init {
        val token = getAccessTokenIfExisted()
        if(!token.isNullOrEmpty()){
            naturalLanguageAnalyzer.setGoogleCredential(token!!)
        }else{
            // Request token again
        }
    }

    fun analyzeSentiment(text : String){
        subscription = Observable.fromCallable {naturalLanguageAnalyzer.analyzeSentiment(text)}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveServiceStart() }
                .doOnTerminate { onRetrieveServiceFinish() }
                .subscribe(
                        { token -> onRetrieveTextSentimentSuccess(token)},
                        { e -> onRetrieveTextSentimentError(e)})
    }

    private fun onRetrieveServiceStart(){


    }

    private fun onRetrieveServiceFinish(){

    }


    private fun onRetrieveTextSentimentSuccess(sentiment : Sentiment){

    }

    private fun onRetrieveTextSentimentError(e: Throwable){
        e.printStackTrace()
    }

    private fun getAccessTokenIfExisted() : String?{
        val preferences = App.applicationContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        return preferences.getString(ACCESS_TOKEN_PREF, "")
    }
}