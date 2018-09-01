package br.com.eduardofilho.ifood_mobile_test.injection.module

import br.com.eduardofilho.ifood_mobile_test.ui.home.TweetViewModel
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
@Suppress("unused")
object ViewHolderModule {

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRowHomeTweetViewModel() : TweetViewModel {
        return TweetViewModel()
    }
}