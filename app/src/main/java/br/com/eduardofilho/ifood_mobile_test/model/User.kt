package br.com.eduardofilho.ifood_mobile_test.model

import com.google.gson.annotations.SerializedName

data class User(@SerializedName("id") val id : Int,
                @SerializedName("name") val name : String,
                @SerializedName("screen_name") val screenName : String,
                @SerializedName("url") val url : String)