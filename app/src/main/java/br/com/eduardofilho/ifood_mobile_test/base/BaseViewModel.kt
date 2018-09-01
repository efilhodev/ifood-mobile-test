package br.com.eduardofilho.ifood_mobile_test.base

import androidx.lifecycle.ViewModel
import br.com.eduardofilho.ifood_mobile_test.injection.component.DaggerViewModelInjector
import br.com.eduardofilho.ifood_mobile_test.injection.component.ViewModelInjector
import br.com.eduardofilho.ifood_mobile_test.injection.module.NetworkModule
import br.com.eduardofilho.ifood_mobile_test.ui.home.HomeViewModel

abstract class BaseViewModel : ViewModel(){
    private val injector: ViewModelInjector = DaggerViewModelInjector
            .builder()
            .networkModule(NetworkModule)
            .build()

    init {
        inject()
    }

    private fun inject() {
        when (this) {
            is HomeViewModel -> injector.inject(this)
        }
    }
}