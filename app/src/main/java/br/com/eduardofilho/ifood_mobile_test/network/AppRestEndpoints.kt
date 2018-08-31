package br.com.eduardofilho.ifood_mobile_test.network

import retrofit2.http.GET
import retrofit2.http.Query


interface AppRestEndpoints{

    @GET("statuses/user_timeline.json")
    fun getTweetsByScreenName(@Query("screen_name") screenName : String)

}