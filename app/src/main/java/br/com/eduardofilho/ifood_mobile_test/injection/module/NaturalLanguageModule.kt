package br.com.eduardofilho.ifood_mobile_test.injection.module

import br.com.eduardofilho.ifood_mobile_test.App
import br.com.eduardofilho.ifood_mobile_test.network.GoogleAccessTokenReceiver
import br.com.eduardofilho.ifood_mobile_test.network.NaturalLanguageAnalyzer
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
@Suppress("unused")
object NaturalLanguageModule{

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideGoogleAccessTokenReceiver() : GoogleAccessTokenReceiver {
        return GoogleAccessTokenReceiver(App.applicationContext())
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideNaturalLanguageAnalyzer() : NaturalLanguageAnalyzer {
        return NaturalLanguageAnalyzer()
    }

}