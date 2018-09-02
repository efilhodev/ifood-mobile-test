package br.com.eduardofilho.ifood_mobile_test.ui.detail

import androidx.lifecycle.MutableLiveData
import br.com.eduardofilho.ifood_mobile_test.base.BaseViewModel
import br.com.eduardofilho.ifood_mobile_test.network.AccessTokenReceiver
import br.com.eduardofilho.ifood_mobile_test.network.NaturalLanguageAnalyzer
import com.google.api.services.language.v1.model.Sentiment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailViewModel : BaseViewModel(){

    @Inject
    lateinit var naturalLanguageAnalyzer: NaturalLanguageAnalyzer

    @Inject
    lateinit var accessTokenReceiver : AccessTokenReceiver

    private lateinit var subscription: Disposable

    val detailTweetContent = MutableLiveData<String>()


    fun analyzeTextSentiment(){
        getOrRefreshAccessServiceTokenIfNeeded()
    }

    private fun getTextSentimentByService(text : String, token : String){

        subscription = Observable.fromCallable {naturalLanguageAnalyzer.analyzeSentimentByService(text, token)}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveServiceStart() }
                .doOnTerminate { onRetrieveServiceFinish() }
                .subscribe(
                        { sentiment -> onRetrieveTextSentimentSuccess(sentiment)},
                        { e -> onRetrieveTextSentimentError(e)})
    }

    private fun getOrRefreshAccessServiceTokenIfNeeded(){

        subscription = Observable.fromCallable {accessTokenReceiver.getOrRefreshServiceAccessTokenIfNeeded()}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveServiceStart() }
                .doOnTerminate { onRetrieveServiceFinish() }
                .subscribe(
                        { token -> onRetrieveAccessTokenSuccess(token)},
                        { e -> onRetrieveAccessTokenError(e)})
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

    private fun onRetrieveAccessTokenSuccess(token : String?){
        if(detailTweetContent.value.isNullOrEmpty() || token.isNullOrEmpty()) return

        getTextSentimentByService(detailTweetContent.value!!, token!!)
    }

    private fun onRetrieveAccessTokenError(e : Throwable){
        e.printStackTrace()
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