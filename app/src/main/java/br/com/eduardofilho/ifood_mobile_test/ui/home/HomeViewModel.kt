package br.com.eduardofilho.ifood_mobile_test.ui.home

import android.view.View
import androidx.lifecycle.MutableLiveData
import br.com.eduardofilho.ifood_mobile_test.App
import br.com.eduardofilho.ifood_mobile_test.R
import br.com.eduardofilho.ifood_mobile_test.base.BaseViewModel
import br.com.eduardofilho.ifood_mobile_test.model.Tweet
import br.com.eduardofilho.ifood_mobile_test.network.AppRestEndpoints
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel : BaseViewModel(){

    @Inject
    lateinit var api : AppRestEndpoints

    private lateinit var subscription: Disposable

    private val context = App.applicationContext()

    var retrieveTweetListError: ( (message : String)  -> Unit)? = null

    val homeLoadingVisibility : MutableLiveData<Int> = MutableLiveData()
    val homeTweetListVisibility : MutableLiveData<Int> = MutableLiveData()
    val homeTweetListInfoText : MutableLiveData<String> = MutableLiveData()
    val homeTweetListInfoVisibility : MutableLiveData<Int> = MutableLiveData()
    val homeTweetAdapter : TweetAdapter = TweetAdapter()

    fun loadTweets(screenName : String){
        subscription = api.getTweetsByScreenName(screenName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveTweetListStart() }
                .doOnTerminate { onRetrieveTweetListFinish() }
                .subscribe(
                        { result -> onRetrieveTweetListSuccess(result) },
                        { e -> onRetrieveTweetListError(e) }
                )
    }

    fun onRetrieveTweetListStart(){
        homeTweetListVisibility.value = View.GONE
        homeTweetListInfoVisibility.value = View.GONE
        homeLoadingVisibility.value = View.VISIBLE
    }

    fun onRetrieveTweetListFinish(){
        homeLoadingVisibility.value = View.GONE
        homeTweetListVisibility.value = View.VISIBLE
    }

    fun onRetrieveTweetListSuccess(tweets : List<Tweet>){
        if(!tweets.isEmpty()){
            homeTweetAdapter.updateTweetList(tweets)
        }else{
            showTweetListInfoMessage(context.getString(R.string.info_tweet_list_empty))
        }
    }

    fun onRetrieveTweetListError(e : Throwable){
        retrieveTweetListError?.invoke(e.message ?: context.getString(R.string.err_something_wrong))

        showTweetListInfoMessage(context.getString(R.string.err_something_wrong))

        e.printStackTrace()
    }

    fun showTweetListInfoMessage(message : String){
        homeTweetListInfoText.value = message
        homeTweetListInfoVisibility.value = View.VISIBLE
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