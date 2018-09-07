package br.com.eduardofilho.ifood_mobile_test.injection.module

import android.content.Context
import br.com.eduardofilho.ifood_mobile_test.App
import br.com.eduardofilho.ifood_mobile_test.model.HttpException
import br.com.eduardofilho.ifood_mobile_test.network.AppRestEndpoints
import br.com.eduardofilho.ifood_mobile_test.utils.*
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.google.gson.GsonBuilder
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named


@Module
@Suppress("unused")
object NetworkModule {

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideApi(retrofit: Retrofit) : AppRestEndpoints{
        return retrofit.create(AppRestEndpoints::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofitInterface(okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").create()))
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideOkHttpClient(@Named("headerInterceptor") headerInterceptor : Interceptor,
                                     @Named("httpCodeInterceptor") httpInterceptor : Interceptor) : OkHttpClient{

        return OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .addInterceptor(httpInterceptor)
                .build()
    }

    @Provides
    @Reusable
    @JvmStatic
    @Named("headerInterceptor")
    internal fun provideHeaderInterceptor() : Interceptor{
        return Interceptor {

            val preferences = App.applicationContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            val authorization = preferences.getString(TWITTER_ACCESS_TOKEN_PREF,
                    Credentials.basic(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET))

            it.proceed(it.request().newBuilder()
                    .addHeader("Authorization",authorization!!)
                    .build())
        }
    }

    @Provides
    @Reusable
    @JvmStatic
    @Named("httpCodeInterceptor")
    internal fun provideHttpCodeInterceptor() : Interceptor{
        return Interceptor {

            val response = it.proceed(it.request())
            when(response.code()){
                200 -> response
                400 -> throw HttpException("400 Requisição inválida", response.code(), response)
                401 -> throw HttpException("401 Não autorizado", response.code(), response)
                403 -> throw HttpException("403 Proibido", response.code(), response)
                404 -> throw HttpException("404 Não encontrado", response.code(), response)
                else -> {
                    throw HttpException("Erro interno", response.code(), response)
                }
            }
        }
    }
}