package br.com.eduardofilho.ifood_mobile_test.network

import br.com.eduardofilho.ifood_mobile_test.model.TwitterOAuthToken
import br.com.eduardofilho.ifood_mobile_test.model.Tweet
import io.reactivex.Observable
import retrofit2.http.*


interface AppRestEndpoints{

    @FormUrlEncoded
    @POST("oauth2/token")
    fun postTwitterCredentials(@Field("grant_type") grantType : String) : Observable<TwitterOAuthToken>

    @GET("1.1/statuses/user_timeline.json")
    fun getTweetsByScreenName(@Query("screen_name") screenName : String) : Observable<List<Tweet>>

}