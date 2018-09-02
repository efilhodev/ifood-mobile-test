package br.com.eduardofilho.ifood_mobile_test.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import br.com.eduardofilho.ifood_mobile_test.R
import br.com.eduardofilho.ifood_mobile_test.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity(){

    private lateinit var binding : ActivityDetailBinding
    private lateinit var viewModel : DetailViewModel

    companion object {
        fun navigate(context: Context){
            val intent = Intent(context, DetailActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBindingView()
        setupViewBehavior()
    }

    private fun setupBindingView(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        binding.viewModel = viewModel
    }

    private fun setupViewBehavior(){

    }

}