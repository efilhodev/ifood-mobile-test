package br.com.eduardofilho.ifood_mobile_test.network

import br.com.eduardofilho.ifood_mobile_test.model.Tweet
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface AppRestEndpoints{

    @GET("statuses/user_timeline.json")
    fun getTweetsByScreenName(@Query("screen_name") screenName : String)  : Observable<List<Tweet>>

}