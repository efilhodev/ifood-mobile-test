package br.com.eduardofilho.ifood_mobile_test.utils.extensions

import android.annotation.SuppressLint
import br.com.eduardofilho.ifood_mobile_test.utils.APP_DATE_PATTERN
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Date.toDatePatternString() : String {
    val format = SimpleDateFormat(APP_DATE_PATTERN)
    return format.format(this)
}