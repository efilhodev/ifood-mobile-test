package br.com.eduardofilho.ifood_mobile_test.injection.component

import br.com.eduardofilho.ifood_mobile_test.injection.module.ViewHolderModule
import br.com.eduardofilho.ifood_mobile_test.ui.home.TweetAdapter
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [ViewHolderModule::class])
interface ViewHolderInjector{

    fun inject(viewHolder : TweetAdapter.ViewHolder)

    interface Builder {
        fun build(): ViewHolderInjector
        fun viewHolderModule(viewHolderModule : ViewHolderModule) : Builder
    }
}