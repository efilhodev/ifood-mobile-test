package br.com.eduardofilho.ifood_mobile_test.ui.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import br.com.eduardofilho.ifood_mobile_test.R
import br.com.eduardofilho.ifood_mobile_test.databinding.ActivityDetailBinding
import br.com.eduardofilho.ifood_mobile_test.model.SentimentCategoryEnum
import br.com.eduardofilho.ifood_mobile_test.model.Tweet
import br.com.eduardofilho.ifood_mobile_test.utils.extensions.toDatePatternString
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import br.com.eduardofilho.ifood_mobile_test.utils.TRANSITION_KEY


class DetailActivity : AppCompatActivity(){

    private lateinit var binding : ActivityDetailBinding
    private lateinit var viewModel : DetailViewModel
    private lateinit var tweet : Tweet

    companion object {
        fun navigate(activity : Activity, tweet : Tweet, view: View){
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(Tweet::class.simpleName, tweet)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                    view, TRANSITION_KEY)

            ActivityCompat.startActivity(activity, intent, options.toBundle())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tweet = intent.getParcelableExtra(Tweet::class.simpleName)

        setupBindingView()
        setupViewBehavior()

        ViewCompat.setTransitionName(binding.root, TRANSITION_KEY)
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
        binding.btnDetailTweetSentimentAnalyzer.setOnClickListener { viewModel.analyzeTweetSentiment(tweet)}
        viewModel.onSentimentAnalyzed = {sentimentCategory -> setupSentimentInfoBehavior(sentimentCategory)}
    }

    private fun setupSentimentInfoBehavior(sentimentCategoryEnum: SentimentCategoryEnum){
        when(sentimentCategoryEnum){
            SentimentCategoryEnum.POSITIVE -> {
                initSentimentInfoText("Esse é um Tweet positivo", resources.getColor(R.color.colorAccent))
            }
            SentimentCategoryEnum.NEUTRAL -> {
                initSentimentInfoText("Esse é um Tweet neutro", resources.getColor(R.color.colorAccent))
            }
            SentimentCategoryEnum.NEGATIVE -> {
                initSentimentInfoText("Esse é um Tweet negativo", resources.getColor(R.color.colorAccent))
                startSentimentInfoAnimation()
            }
        }
        startSentimentInfoAnimation()
    }

    private fun initSentimentInfoText(text : String, colorResource : Int){
        binding.tvDetailTweetSentimentInfo.text = text
        binding.tvDetailTweetSentimentInfo.setBackgroundColor(colorResource)
    }

    private fun startSentimentInfoAnimation(){
        val animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_top)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                binding.tvDetailTweetSentimentInfo.visibility = View.VISIBLE
            }
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {}
        })
        binding.tvDetailTweetSentimentInfo.startAnimation(animation)
    }
}