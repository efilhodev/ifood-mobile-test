package br.com.eduardofilho.ifood_mobile_test.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import br.com.eduardofilho.ifood_mobile_test.R
import br.com.eduardofilho.ifood_mobile_test.databinding.ActivityDetailBinding
import br.com.eduardofilho.ifood_mobile_test.model.Tweet
import br.com.eduardofilho.ifood_mobile_test.utils.extensions.toDatePatternString

class DetailActivity : AppCompatActivity(){

    private lateinit var binding : ActivityDetailBinding
    private lateinit var viewModel : DetailViewModel
    private lateinit var tweet : Tweet

    companion object {
        fun navigate(context: Context, tweet : Tweet){
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(Tweet::class.simpleName, tweet)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tweet = intent.getParcelableExtra(Tweet::class.simpleName)

        setupBindingView()
        setupViewBehavior()
    }

    private fun setupBindingView(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        binding.tvDetailTweetContent.text = tweet.text
        binding.tvDetailTweetCreatedAt.text = tweet.createdAt.toDatePatternString()
        binding.tvDetailTweetRealName.text = tweet.user.name
        binding.tvDetailTweetScreenName.text = tweet.user.screenName


        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        binding.viewModel = viewModel
    }

    private fun setupViewBehavior(){

    }

}