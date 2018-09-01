package br.com.eduardofilho.ifood_mobile_test.injection.component

import br.com.eduardofilho.ifood_mobile_test.injection.module.NetworkModule
import br.com.eduardofilho.ifood_mobile_test.ui.home.HomeViewModel
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [NetworkModule::class])
interface ViewModelInjector{

    fun inject(viewModel: HomeViewModel)

    interface Builder {
        fun build(): ViewModelInjector
        fun networkModule(networkModule: NetworkModule): Builder
    }
}