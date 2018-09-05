package br.com.eduardofilho.ifood_mobile_test.model

import okhttp3.Response

data class HttpException(override val message : String,
                         val code : Int,
                         val response: Response) : Exception()