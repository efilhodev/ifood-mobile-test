package br.com.eduardofilho.ifood_mobile_test.ui.detail

import android.view.View
import androidx.lifecycle.MutableLiveData
import br.com.eduardofilho.ifood_mobile_test.base.BaseViewModel
import br.com.eduardofilho.ifood_mobile_test.model.SentimentCategoryEnum
import br.com.eduardofilho.ifood_mobile_test.model.Tweet
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
    private lateinit var tweet : Tweet

    var onSentimentAnalyzed: ((SentimentCategoryEnum) -> Unit)? = null

    val detailAnalyzerLoadingVisibility : MutableLiveData<Int> = MutableLiveData()
    val detailAnalyzerButtonVisibility : MutableLiveData<Int> = MutableLiveData()


    fun analyzeTweetSentiment(tweet: Tweet){
        this.tweet = tweet
        getOrRefreshAccessServiceTokenIfNeeded()
    }

    private fun getTextSentimentByService(text : String, token : String){

        subscription = Observable.fromCallable {naturalLanguageAnalyzer.analyzeSentimentByService(text, token)}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { }
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
                .doOnTerminate { }
                .subscribe(
                        { token -> onRetrieveAccessTokenSuccess(token)},
                        { e -> onRetrieveAccessTokenError(e)})
    }

    private fun onRetrieveServiceStart(){
        detailAnalyzerButtonVisibility.value = View.INVISIBLE
        detailAnalyzerLoadingVisibility.value =  View.VISIBLE
    }

    private fun onRetrieveServiceFinish(){
        detailAnalyzerButtonVisibility.value = View.VISIBLE
        detailAnalyzerLoadingVisibility.value =  View.GONE
    }

    private fun onRetrieveTextSentimentSuccess(sentiment : Sentiment){
        val sentimentCategoryEnum = categorizeSentiment(sentiment)
        onSentimentAnalyzed?.invoke(sentimentCategoryEnum)
    }

    private fun onRetrieveTextSentimentError(e: Throwable){
        e.printStackTrace()
    }

    private fun onRetrieveAccessTokenSuccess(token : String?){
        getTextSentimentByService(tweet.text, token!!)
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

    private fun categorizeSentiment(sentiment: Sentiment) : SentimentCategoryEnum{
        return when{
            sentiment.score in 0.6..1.0 && sentiment.magnitude > 1.0 -> SentimentCategoryEnum.POSITIVE
            sentiment.score in -1.0..-0.1 && sentiment.magnitude > 1.0 -> SentimentCategoryEnum.NEGATIVE
            else -> SentimentCategoryEnum.NEUTRAL
        }
    }
}