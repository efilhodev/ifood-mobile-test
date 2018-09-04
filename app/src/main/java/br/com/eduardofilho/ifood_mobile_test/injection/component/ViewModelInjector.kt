package br.com.eduardofilho.ifood_mobile_test.injection.component

import br.com.eduardofilho.ifood_mobile_test.injection.module.NaturalLanguageModule
import br.com.eduardofilho.ifood_mobile_test.injection.module.NetworkModule
import br.com.eduardofilho.ifood_mobile_test.ui.detail.DetailViewModel
import br.com.eduardofilho.ifood_mobile_test.ui.home.HomeViewModel
import br.com.eduardofilho.ifood_mobile_test.ui.start.StartViewModel
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [NetworkModule::class, NaturalLanguageModule::class])
interface ViewModelInjector{

    fun inject(viewModel : HomeViewModel)
    fun inject(viewModel : DetailViewModel)
    fun inject(viewModel: StartViewModel)

    interface Builder {
        fun build(): ViewModelInjector
        fun networkModule(networkModule: NetworkModule): Builder
        fun naturalLanguageModule(naturalLanguage : NaturalLanguageModule)
    }
}