package br.com.eduardofilho.ifood_mobile_test.injection.module

import android.content.Context
import br.com.eduardofilho.ifood_mobile_test.App
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
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory


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
    internal fun provideOkHttpClient() : OkHttpClient{

        return OkHttpClient.Builder()
                .addInterceptor {

                    val preferences = App.applicationContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
                    val authorization = preferences.getString(TWITTER_ACCESS_TOKEN_PREF, Credentials.basic(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET))

                    it.proceed(it.request().newBuilder()
                        .addHeader("Authorization",authorization!!)
                        .build())
        }.build()
    }
}