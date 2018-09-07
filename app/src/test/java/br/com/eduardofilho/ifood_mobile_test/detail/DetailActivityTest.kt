package br.com.eduardofilho.ifood_mobile_test.detail

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import br.com.eduardofilho.ifood_mobile_test.R
import br.com.eduardofilho.ifood_mobile_test.model.SentimentCategoryEnum
import br.com.eduardofilho.ifood_mobile_test.model.Tweet
import br.com.eduardofilho.ifood_mobile_test.model.User
import br.com.eduardofilho.ifood_mobile_test.ui.detail.DetailActivity
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class DetailActivityTest {

    private lateinit var activity : DetailActivity

    @Before
    fun setup(){
        val cal = Calendar.getInstance()
        cal.set(2018, Calendar.SEPTEMBER, 6, 10, 10, 10)

        val user = User(1, "Mock", "Mock_Name", "")
        val tweet = Tweet(1,"Mock text", cal.time, user)

        val intent = Intent()
        intent.putExtra(Tweet::class.simpleName, tweet)
        activity =  Robolectric.buildActivity(DetailActivity::class.java, intent).create().resume().get()
    }

    @Test
    fun testSetupBindingView(){
        Assert.assertNotNull(activity.binding)
        Assert.assertNotNull(activity.viewModel)

        Assert.assertEquals("Mock", activity.binding.tvDetailTweetRealName.text.toString())
        Assert.assertEquals("Mock_Name", activity.binding.tvDetailTweetScreenName.text.toString())
        Assert.assertEquals("Mock text", activity.binding.tvDetailTweetContent.text.toString())
        Assert.assertEquals("06-09-2018 10:10", activity.binding.tvDetailTweetCreatedAt.text.toString())
    }

    @Test
    fun testInitSentimentInfoText(){
        activity.initSentimentInfoText("Mock sentiment test", R.color.colorPrimary)
        Assert.assertEquals("Mock sentiment test", activity.binding.tvDetailTweetSentimentInfo.text.toString())

        val view = activity.binding.tvDetailTweetSentimentInfo.background as ColorDrawable
        val color = view.color
        Assert.assertEquals(R.color.colorPrimary, color)
    }

    @Test
    fun testDisableAnalyzerSentimentButton(){
        activity.disableAnalyzerSentimentButton()

        Assert.assertFalse(activity.binding.btnDetailTweetSentimentAnalyzer.isEnabled)
        Assert.assertEquals(0.5f, activity.binding.btnDetailTweetSentimentAnalyzer.alpha)
    }

    @Test
    fun testEnableAnalyzerSentimentButton(){
        activity.enableAnalyzerSentimentButton()

        Assert.assertTrue(activity.binding.btnDetailTweetSentimentAnalyzer.isEnabled)
        Assert.assertEquals(1.0f, activity.binding.btnDetailTweetSentimentAnalyzer.alpha)
    }

    @Test
    fun testWrapSentimentInfoBehaviorWhenPositive(){
        activity.wrapSentimentInfoBehavior(SentimentCategoryEnum.POSITIVE)

        Assert.assertEquals("Esse é um Tweet positivo \uD83D\uDE00", activity.binding.tvDetailTweetSentimentInfo.text.toString())

        val view = activity.binding.tvDetailTweetSentimentInfo.background as ColorDrawable
        val color = view.color
        Assert.assertEquals(activity.getColor(R.color.colorPositiveTweet), color)
    }

    @Test
    fun testWrapSentimentInfoBehaviorWhenPossiblyPositive(){
        activity.wrapSentimentInfoBehavior(SentimentCategoryEnum.POSSIBLY_POSITIVE)

        Assert.assertEquals("Esse possivelmente é um Tweet positivo \uD83D\uDE00", activity.binding.tvDetailTweetSentimentInfo.text.toString())

        val view = activity.binding.tvDetailTweetSentimentInfo.background as ColorDrawable
        val color = view.color
        Assert.assertEquals(activity.getColor(R.color.colorPositiveTweet), color)
    }

    @Test
    fun testWrapSentimentInfoBehaviorWhenNeutral(){
        activity.wrapSentimentInfoBehavior(SentimentCategoryEnum.NEUTRAL)

        Assert.assertEquals("Esse é um Tweet neutro \uD83D\uDE10", activity.binding.tvDetailTweetSentimentInfo.text.toString())

        val view = activity.binding.tvDetailTweetSentimentInfo.background as ColorDrawable
        val color = view.color
        Assert.assertEquals(activity.getColor(R.color.colorNeutralTweet), color)
    }

    @Test
    fun testWrapSentimentInfoBehaviorWhenPossiblyNegative(){
        activity.wrapSentimentInfoBehavior(SentimentCategoryEnum.POSSIBLY_NEGATIVE)

        Assert.assertEquals("Esse possivelmente é um Tweet negativo \uD83D\uDE14", activity.binding.tvDetailTweetSentimentInfo.text.toString())

        val view = activity.binding.tvDetailTweetSentimentInfo.background as ColorDrawable
        val color = view.color
        Assert.assertEquals(activity.getColor(R.color.colorNegativeTweet), color)
    }

    @Test
    fun testWrapSentimentInfoBehaviorWhenNegative(){
        activity.wrapSentimentInfoBehavior(SentimentCategoryEnum.NEGATIVE)

        Assert.assertEquals("Esse é um Tweet negativo \uD83D\uDE14", activity.binding.tvDetailTweetSentimentInfo.text.toString())

        val view = activity.binding.tvDetailTweetSentimentInfo.background as ColorDrawable
        val color = view.color
        Assert.assertEquals(activity.getColor(R.color.colorNegativeTweet), color)
    }
}