package br.com.eduardofilho.ifood_mobile_test.model

import com.google.gson.annotations.SerializedName

data class OAuthToken(@SerializedName("access_token") val accessToken : String,
                      @SerializedName("token_type") val tokenType : String){

    val authorization : String = accessToken + tokenType
}
