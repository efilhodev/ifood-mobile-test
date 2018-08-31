package br.com.eduardofilho.ifood_mobile_test.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Tweet(@SerializedName("id") val id : Long,
                 @SerializedName("text") val text : String,
                 @SerializedName("created_at") val createdAt : Date,
                 @SerializedName("user") val user: User)