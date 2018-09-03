package br.com.eduardofilho.ifood_mobile_test.injection.module

import br.com.eduardofilho.ifood_mobile_test.network.AppRestEndpoints
import br.com.eduardofilho.ifood_mobile_test.utils.APP_DATE_PATTERN
import br.com.eduardofilho.ifood_mobile_test.utils.BASE_URL
import br.com.eduardofilho.ifood_mobile_test.utils.TWITTER_CONSUMER_KEY
import br.com.eduardofilho.ifood_mobile_test.utils.TWITTER_CONSUMER_SECRET
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
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat(APP_DATE_PATTERN).create()))
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideOkHttpClient() : OkHttpClient{
        return OkHttpClient.Builder()
                .addInterceptor { it.proceed(it.request().newBuilder()
                    .addHeader("Authorization", Credentials.basic(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET))
                    .build())
        }.build()
    }
}