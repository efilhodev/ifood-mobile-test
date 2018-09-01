package br.com.eduardofilho.ifood_mobile_test.ui.home

import android.view.View
import androidx.lifecycle.MutableLiveData
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

    val homeLoadingVisibility :MutableLiveData<Int> = MutableLiveData()
    val homeTweetAdapter : TweetAdapter = TweetAdapter()


    fun loadTweets(screenName : String){
        subscription = api.getTweetsByScreenName(screenName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveTweetListStart() }
                .doOnTerminate { onRetrieveTweetListFinish() }
                .subscribe(
                        { result -> onRetrieveTweetListSuccess(result) },
                        { onRetrieveTweetListError() }
                )
    }

    private fun onRetrieveTweetListStart(){
        homeLoadingVisibility.value = View.VISIBLE

    }

    private fun onRetrieveTweetListFinish(){
        homeLoadingVisibility.value = View.GONE
    }

    private fun onRetrieveTweetListSuccess(tweets :List<Tweet>){
        homeTweetAdapter.updateTweetList(tweets)
    }

    private fun onRetrieveTweetListError(){
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}