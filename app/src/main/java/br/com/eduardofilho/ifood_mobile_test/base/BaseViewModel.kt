package br.com.eduardofilho.ifood_mobile_test.base

import androidx.lifecycle.ViewModel
import br.com.eduardofilho.ifood_mobile_test.injection.component.DaggerViewModelInjector
import br.com.eduardofilho.ifood_mobile_test.injection.component.ViewModelInjector
import br.com.eduardofilho.ifood_mobile_test.injection.module.NaturalLanguageModule
import br.com.eduardofilho.ifood_mobile_test.injection.module.NetworkModule
import br.com.eduardofilho.ifood_mobile_test.ui.detail.DetailViewModel
import br.com.eduardofilho.ifood_mobile_test.ui.home.HomeViewModel
import br.com.eduardofilho.ifood_mobile_test.ui.start.StartViewModel

abstract class BaseViewModel : ViewModel(){
    private val injector: ViewModelInjector = DaggerViewModelInjector
            .builder()
            .networkModule(NetworkModule)
            .naturalLanguageModule(NaturalLanguageModule)
            .build()

    init {
        inject()
    }

    private fun inject() {
        when (this) {
            is HomeViewModel -> injector.inject(this)
            is DetailViewModel -> injector.inject(this)
            is StartViewModel -> injector.inject(this)
        }
    }
}