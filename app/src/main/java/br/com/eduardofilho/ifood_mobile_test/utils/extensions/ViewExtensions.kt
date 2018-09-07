package br.com.eduardofilho.ifood_mobile_test.utils.extensions

import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import br.com.eduardofilho.ifood_mobile_test.R

fun View.getParentActivity(): AppCompatActivity? {
    var context = this.context
    while (context is ContextWrapper) {
        if (context is AppCompatActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun View.slideDownAnimation(context : Context){
    val animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_top)
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
            this@slideDownAnimation.visibility = View.VISIBLE
        }
        override fun onAnimationRepeat(animation: Animation?) {}
        override fun onAnimationEnd(animation: Animation?) {}
    })
    this.startAnimation(animation)
}