package br.com.eduardofilho.ifood_mobile_test.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(@SerializedName("id") val id : Long,
                @SerializedName("name") val name : String,
                @SerializedName("screen_name") val screenName : String,
                @SerializedName("url") val url : String?) : Parcelable