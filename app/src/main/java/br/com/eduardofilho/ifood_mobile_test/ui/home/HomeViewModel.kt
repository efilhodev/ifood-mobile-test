package br.com.eduardofilho.ifood_mobile_test.ui.home

import android.view.View
import androidx.lifecycle.MutableLiveData
import br.com.eduardofilho.ifood_mobile_test.base.BaseViewModel
import br.com.eduardofilho.ifood_mobile_test.model.Tweet
import br.com.eduardofilho.ifood_mobile_test.network.AccessTokenReceiver
import br.com.eduardofilho.ifood_mobile_test.network.AppRestEndpoints
import br.com.eduardofilho.ifood_mobile_test.utils.Mock
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel : BaseViewModel(){

    @Inject
    lateinit var api : AppRestEndpoints

    @Inject
    lateinit var accessTokenReceiver : AccessTokenReceiver

    private lateinit var subscription: Disposable

    val homeLoadingVisibility : MutableLiveData<Int> = MutableLiveData()
    val homeTweetListVisibility : MutableLiveData<Int> = MutableLiveData()
    val homeTweetAdapter : TweetAdapter = TweetAdapter()

    init {
        homeTweetAdapter.updateTweetList(Mock.getMockTweets(20))
    }

    fun loadTweets(screenName : String){
        subscription = api.getTweetsByScreenName(screenName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveServiceStart() }
                .doOnTerminate { onRetrieveServiceFinish() }
                .subscribe(
                        { result -> onRetrieveTweetListSuccess(result) },
                        { e -> onRetrieveTweetListError(e) }
                )
    }

    fun refreshAccessTokenIfNeeded(){

        subscription = Observable.fromCallable {accessTokenReceiver.refreshAccessTokenIfNeeded()}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveServiceStart() }
                .doOnTerminate { onRetrieveServiceFinish() }
                .subscribe(
                        { token -> onRetrieveAccessTokenSuccess(token)},
                        { e -> onRetrieveAccessTokenError(e)})
    }


    private fun onRetrieveServiceStart(){
        homeLoadingVisibility.value = View.VISIBLE
        homeTweetListVisibility.value = View.GONE

    }

    private fun onRetrieveServiceFinish(){
        homeLoadingVisibility.value = View.GONE
        homeTweetListVisibility.value = View.VISIBLE
    }

    private fun onRetrieveTweetListSuccess(tweets : List<Tweet>){
        homeTweetAdapter.updateTweetList(tweets)
    }

    private fun onRetrieveTweetListError(e : Throwable){
        e.printStackTrace()
    }

    private fun onRetrieveAccessTokenSuccess(token : String?){

    }

    private fun onRetrieveAccessTokenError(e : Throwable){
        e.printStackTrace()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}