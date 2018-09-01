package br.com.eduardofilho.ifood_mobile_test.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.eduardofilho.ifood_mobile_test.injection.component.DaggerViewHolderInjector
import br.com.eduardofilho.ifood_mobile_test.injection.component.ViewHolderInjector
import br.com.eduardofilho.ifood_mobile_test.injection.module.ViewHolderModule
import br.com.eduardofilho.ifood_mobile_test.ui.home.TweetAdapter

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private val injector: ViewHolderInjector = DaggerViewHolderInjector
            .builder()
            .viewHolderModule(ViewHolderModule)
            .build()

    init {
        inject()
    }

    private fun inject() {
        when (this) {
            is TweetAdapter.ViewHolder -> injector.inject(this)
        }
    }
}