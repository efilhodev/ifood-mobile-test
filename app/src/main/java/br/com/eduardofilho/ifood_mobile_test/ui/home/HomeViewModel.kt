package br.com.eduardofilho.ifood_mobile_test.ui.home

import android.view.View
import androidx.lifecycle.MutableLiveData
import br.com.eduardofilho.ifood_mobile_test.App
import br.com.eduardofilho.ifood_mobile_test.R
import br.com.eduardofilho.ifood_mobile_test.base.BaseViewModel
import br.com.eduardofilho.ifood_mobile_test.model.Tweet
import br.com.eduardofilho.ifood_mobile_test.network.AppRestEndpoints
import br.com.eduardofilho.ifood_mobile_test.utils.mock.Mock
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel : BaseViewModel(){

    @Inject
    lateinit var api : AppRestEndpoints

    private lateinit var subscription: Disposable

    var onServiceError: ( (message : String)  -> Unit)? = null

    val homeLoadingVisibility : MutableLiveData<Int> = MutableLiveData()
    val homeTweetListVisibility : MutableLiveData<Int> = MutableLiveData()
    val homeTweetListInfoText : MutableLiveData<String> = MutableLiveData()
    val homeTweetListInfoVisibility : MutableLiveData<Int> = MutableLiveData()
    val homeTweetAdapter : TweetAdapter = TweetAdapter()

    val context = App.applicationContext()

    init {
        homeTweetAdapter.updateTweetList(Mock.getMockTweets(20))
    }

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

    private fun onRetrieveTweetListStart(){
        homeTweetListVisibility.value = View.GONE
        homeTweetListInfoVisibility.value = View.GONE
        homeLoadingVisibility.value = View.VISIBLE
    }

    private fun onRetrieveTweetListFinish(){
        homeLoadingVisibility.value = View.GONE
        homeTweetListVisibility.value = View.VISIBLE
    }

    private fun onRetrieveTweetListSuccess(tweets : List<Tweet>){
        if(!tweets.isEmpty()){
            homeTweetAdapter.updateTweetList(tweets)
        }else{
            showTweetListInfoMessage(context.getString(R.string.info_tweet_list_empty))
        }
    }

    private fun onRetrieveTweetListError(e : Throwable){
        showTweetListInfoMessage(context.getString(R.string.err_something_wrong))
        onServiceError?.invoke(e.message ?: context.getString(R.string.err_something_wrong))

        e.printStackTrace()
    }


    private fun showTweetListInfoMessage(message : String){
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