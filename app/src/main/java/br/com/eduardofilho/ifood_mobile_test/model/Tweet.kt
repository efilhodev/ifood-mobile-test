package br.com.eduardofilho.ifood_mobile_test.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Tweet(@SerializedName("id") val id : Long,
                 @SerializedName("text") val text : String,
                 @SerializedName("created_at") val createdAt : Date,
                 @SerializedName("user") val user: User) : Parcelable