package br.com.eduardofilho.ifood_mobile_test.ui.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
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
import br.com.eduardofilho.ifood_mobile_test.base.BaseActivity
import br.com.eduardofilho.ifood_mobile_test.utils.TRANSITION_KEY
import br.com.eduardofilho.ifood_mobile_test.utils.extensions.slideDownAnimation


class DetailActivity : BaseActivity(){

    lateinit var binding : ActivityDetailBinding
    lateinit var viewModel : DetailViewModel
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

    override fun onNetworkConnectionChangedStatus(isConnected: Boolean) {
        if (!isConnected) {
            showErrorSnackBar(binding.root, getString(R.string.err_not_connected))
            disableAnalyzerSentimentButton()
        } else {
            hideErrorSnackBar()
            enableAnalyzerSentimentButton()
        }
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
        binding.btnDetailTweetSentimentAnalyzer.setOnClickListener {
            viewModel.analyzeTweetSentiment(tweet)
        }

        viewModel.onSentimentAnalyzed = {
            sentimentCategory -> wrapSentimentInfoBehavior(sentimentCategory)
        }

        viewModel.onServiceError = {
            message -> showErrorSnackBar(binding.root, message)
        }
    }

    fun wrapSentimentInfoBehavior(sentimentCategoryEnum: SentimentCategoryEnum){
        when(sentimentCategoryEnum){
            SentimentCategoryEnum.POSITIVE -> {
                initSentimentInfoText("Esse é um Tweet positivo \ud83d\ude00", resources.getColor(R.color.colorPositiveTweet))
            }
            SentimentCategoryEnum.POSSIBLY_POSITIVE -> {
                initSentimentInfoText("Esse possivelmente é um Tweet positivo \ud83d\ude00", resources.getColor(R.color.colorPositiveTweet))
            }
            SentimentCategoryEnum.NEUTRAL -> {
                initSentimentInfoText("Esse é um Tweet neutro \ud83d\ude10", resources.getColor(R.color.colorNeutralTweet))
            }
            SentimentCategoryEnum.POSSIBLY_NEGATIVE -> {
                initSentimentInfoText("Esse possivelmente é um Tweet negativo \uD83D\uDE14", resources.getColor(R.color.colorNegativeTweet))
            }
            SentimentCategoryEnum.NEGATIVE -> {
                initSentimentInfoText("Esse é um Tweet negativo \ud83d\ude14", resources.getColor(R.color.colorNegativeTweet))
            }
        }
    }

    fun initSentimentInfoText(text : String, colorResource : Int){
        binding.tvDetailTweetSentimentInfo.text = Html.fromHtml(text)
        binding.tvDetailTweetSentimentInfo.setBackgroundColor(colorResource)
        binding.tvDetailTweetSentimentInfo.slideDownAnimation(this)

        if(colorResource == resources.getColor(R.color.colorPositiveTweet)){
            binding.tvDetailTweetSentimentInfo.setTextColor(resources.getColor(android.R.color.darker_gray))
        }else{
            binding.tvDetailTweetSentimentInfo.setTextColor(resources.getColor(android.R.color.white))
        }
    }


    fun disableAnalyzerSentimentButton(){
        binding.btnDetailTweetSentimentAnalyzer.isEnabled = false
        binding.btnDetailTweetSentimentAnalyzer.alpha = 0.5f
    }

    fun enableAnalyzerSentimentButton(){
        binding.btnDetailTweetSentimentAnalyzer.isEnabled = true
        binding.btnDetailTweetSentimentAnalyzer.alpha = 1.0f
    }

}