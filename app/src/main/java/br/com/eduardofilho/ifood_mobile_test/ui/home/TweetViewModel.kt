package br.com.eduardofilho.ifood_mobile_test.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.eduardofilho.ifood_mobile_test.model.Tweet
import br.com.eduardofilho.ifood_mobile_test.utils.extensions.toDatePatternString

class TweetViewModel : ViewModel(){

    val tweetContent = MutableLiveData<String>()
    val tweetScreenName = MutableLiveData<String>()
    val tweetRealName = MutableLiveData<String>()
    val tweetCreatedAt = MutableLiveData<String>()

    fun bind(tweet : Tweet){
        tweetContent.value = tweet.text
        tweetScreenName.value = tweet.user.screenName
        tweetRealName.value = tweet.user.name
        tweetCreatedAt.value = tweet.createdAt.toDatePatternString()
    }
}