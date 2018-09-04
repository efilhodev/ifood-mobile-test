package br.com.eduardofilho.ifood_mobile_test.ui.detail

import android.view.View
import androidx.lifecycle.MutableLiveData
import br.com.eduardofilho.ifood_mobile_test.base.BaseViewModel
import br.com.eduardofilho.ifood_mobile_test.model.SentimentCategoryEnum
import br.com.eduardofilho.ifood_mobile_test.model.Tweet
import br.com.eduardofilho.ifood_mobile_test.network.GoogleAccessTokenReceiver
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
    lateinit var googleAccessTokenReceiver : GoogleAccessTokenReceiver

    private lateinit var subscription: Disposable
    private lateinit var tweet : Tweet

    var onSentimentAnalyzed: ((SentimentCategoryEnum) -> Unit)? = null

    val detailAnalyzerLoadingVisibility : MutableLiveData<Int> = MutableLiveData()
    val detailAnalyzerButtonVisibility : MutableLiveData<Int> = MutableLiveData()


    fun analyzeTweetSentiment(tweet: Tweet){
        this.tweet = tweet
        getGoogleAccessTokenToCallService(::getTweetSentimentByService)
    }

    private fun getGoogleAccessTokenToCallService(service : (tweet : Tweet, token : String) -> Unit){

        subscription = Observable.fromCallable {googleAccessTokenReceiver.getOrRefreshGoogleAccessTokenIfNeeded()}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveServiceStart() }
                .doOnTerminate { }
                .subscribe(
                        { token -> onRetrieveAccessTokenSuccess(service, token)},
                        { e -> onRetrieveAccessTokenError(e)})
    }

    private fun getTweetSentimentByService(tweet: Tweet, token : String){

        subscription = Observable.fromCallable {naturalLanguageAnalyzer.analyzeSentimentByService(tweet.text, token)}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { }
                .doOnTerminate { onRetrieveServiceFinish() }
                .subscribe(
                        { sentiment -> onRetrieveTweetSentimentSuccess(sentiment)},
                        { e -> onRetrieveTweetSentimentError(e)})
    }

    private fun onRetrieveServiceStart(){
        detailAnalyzerButtonVisibility.value = View.INVISIBLE
        detailAnalyzerLoadingVisibility.value =  View.VISIBLE
    }

    private fun onRetrieveServiceFinish(){
        detailAnalyzerButtonVisibility.value = View.VISIBLE
        detailAnalyzerLoadingVisibility.value =  View.GONE
    }

    private fun onRetrieveTweetSentimentSuccess(sentiment : Sentiment){
        val sentimentCategoryEnum = categorizeSentiment(sentiment)
        onSentimentAnalyzed?.invoke(sentimentCategoryEnum)
    }

    private fun onRetrieveAccessTokenSuccess(service : (tweet : Tweet, token : String) -> Unit,
                                             token : String?){
        service(tweet, token!!)
    }

    private fun onRetrieveTweetSentimentError(e: Throwable){
        e.printStackTrace()
    }

    private fun onRetrieveAccessTokenError(e : Throwable){
        e.printStackTrace()
    }

    private fun categorizeSentiment(sentiment: Sentiment) : SentimentCategoryEnum{
        return when{
            sentiment.score in 0.6..1.0 && sentiment.magnitude > 2.0 -> SentimentCategoryEnum.POSITIVE
            sentiment.score in 0.6..1.0 && sentiment.magnitude < 2.0 -> SentimentCategoryEnum.POSSIBLY_POSITIVE
            sentiment.score in -1.0..-0.1 && sentiment.magnitude > 2.0 -> SentimentCategoryEnum.NEGATIVE
            sentiment.score in -1.0..-0.1 && sentiment.magnitude < 2.0 -> SentimentCategoryEnum.POSSIBLY_NEGATIVE
            else -> SentimentCategoryEnum.NEUTRAL
        }
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