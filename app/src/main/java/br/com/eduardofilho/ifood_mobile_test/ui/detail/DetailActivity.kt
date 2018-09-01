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
        viewModel.analyzeSentiment("It might seem crazy what I'm 'bout to say\n" +
                "Sunshine she's here, you can take a break\n" +
                "I'm a hot air balloon that could go to space\n" +
                "With the air, like I don't care, baby, by the way")
    }

}