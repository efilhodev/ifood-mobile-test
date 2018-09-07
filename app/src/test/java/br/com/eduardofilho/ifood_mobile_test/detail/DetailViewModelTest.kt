package br.com.eduardofilho.ifood_mobile_test.detail

import android.content.Intent
import android.os.Build
import android.view.View
import br.com.eduardofilho.ifood_mobile_test.model.SentimentCategoryEnum
import br.com.eduardofilho.ifood_mobile_test.model.Tweet
import br.com.eduardofilho.ifood_mobile_test.model.User
import br.com.eduardofilho.ifood_mobile_test.ui.detail.DetailActivity
import br.com.eduardofilho.ifood_mobile_test.ui.detail.DetailViewModel
import com.google.api.services.language.v1.model.Sentiment
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
class DetailViewModelTest{

    private lateinit var viewModel : DetailViewModel

    @Before
    fun setup(){
        val user = User(1, "Mock", "Mock_Name", "")
        val tweet = Tweet(1,"Mock text", Date(), user)

        val intent = Intent()
        intent.putExtra(Tweet::class.simpleName, tweet)
        viewModel =  Robolectric.buildActivity(DetailActivity::class.java, intent).create().resume().get().binding.viewModel!!
    }

    @Test
    fun testOnRetrieveServiceStart(){
        viewModel.onRetrieveServiceStart()
        Assert.assertEquals(View.INVISIBLE, viewModel.detailAnalyzerButtonVisibility.value)
        Assert.assertEquals(View.VISIBLE, viewModel.detailAnalyzerLoadingVisibility.value)
    }

    @Test
    fun testOnRetrieveServiceFinish(){
        viewModel.onRetrieveServiceFinish()
        Assert.assertEquals(View.VISIBLE, viewModel.detailAnalyzerButtonVisibility.value)
        Assert.assertEquals(View.GONE, viewModel.detailAnalyzerLoadingVisibility.value)
    }

    @Test
    fun testCategorizePositiveSentiment(){
        val sentiment = Sentiment()
        sentiment.score = 0.2f
        sentiment.magnitude = 2.0f

        var result = viewModel.categorizeSentiment(sentiment)
        Assert.assertEquals(SentimentCategoryEnum.POSITIVE, result)

        sentiment.score = 1.0f
        sentiment.magnitude = 4.0f

        result = viewModel.categorizeSentiment(sentiment)

        Assert.assertEquals(SentimentCategoryEnum.POSITIVE, result)

        sentiment.score = -0.5f
        sentiment.magnitude = 4.0f

        result = viewModel.categorizeSentiment(sentiment)

        Assert.assertNotEquals(SentimentCategoryEnum.POSITIVE, result)

        result = viewModel.categorizeSentiment(sentiment)

        sentiment.score = -0.1f
        sentiment.magnitude = 1.0f

        Assert.assertNotEquals(SentimentCategoryEnum.POSITIVE, result)
    }

    @Test
    fun testCategorizePossiblyPositiveSentiment(){
        val sentiment = Sentiment()
        sentiment.score = 0.2f
        sentiment.magnitude = 1.9f

        var result = viewModel.categorizeSentiment(sentiment)
        Assert.assertEquals(SentimentCategoryEnum.POSSIBLY_POSITIVE, result)

        sentiment.score = 1.0f
        sentiment.magnitude = 0.0f

        result = viewModel.categorizeSentiment(sentiment)

        Assert.assertEquals(SentimentCategoryEnum.POSSIBLY_POSITIVE, result)

        sentiment.score = 1.5f
        sentiment.magnitude = 4.0f

        result = viewModel.categorizeSentiment(sentiment)

        Assert.assertNotEquals(SentimentCategoryEnum.POSSIBLY_POSITIVE, result)

        result = viewModel.categorizeSentiment(sentiment)

        sentiment.score = -0.1f
        sentiment.magnitude = 1.0f

        Assert.assertNotEquals(SentimentCategoryEnum.POSSIBLY_POSITIVE, result)
    }

    @Test
    fun testCategorizeNeutralSentiment(){
        val sentiment = Sentiment()
        sentiment.score = 0.1f
        sentiment.magnitude = 5f

        var result = viewModel.categorizeSentiment(sentiment)
        Assert.assertEquals(SentimentCategoryEnum.NEUTRAL, result)

        sentiment.score = -0.1f
        sentiment.magnitude = 0.0f

        result = viewModel.categorizeSentiment(sentiment)

        Assert.assertEquals(SentimentCategoryEnum.NEUTRAL, result)

        sentiment.score = 1.0f
        sentiment.magnitude = 4.0f

        result = viewModel.categorizeSentiment(sentiment)

        Assert.assertNotEquals(SentimentCategoryEnum.NEUTRAL, result)

        result = viewModel.categorizeSentiment(sentiment)

        sentiment.score = -1.0f
        sentiment.magnitude = 1.0f

        Assert.assertNotEquals(SentimentCategoryEnum.NEUTRAL, result)
    }

    @Test
    fun testCategorizePossiblyNegativeSentiment(){
        val sentiment = Sentiment()
        sentiment.score = -0.2f
        sentiment.magnitude = 1.9f

        var result = viewModel.categorizeSentiment(sentiment)
        Assert.assertEquals(SentimentCategoryEnum.POSSIBLY_NEGATIVE, result)

        sentiment.score = -1.0f
        sentiment.magnitude = 0.0f

        result = viewModel.categorizeSentiment(sentiment)

        Assert.assertEquals(SentimentCategoryEnum.POSSIBLY_NEGATIVE, result)

        sentiment.score = 1.0f
        sentiment.magnitude = 1.0f

        result = viewModel.categorizeSentiment(sentiment)

        Assert.assertNotEquals(SentimentCategoryEnum.POSSIBLY_NEGATIVE, result)

        result = viewModel.categorizeSentiment(sentiment)

        sentiment.score = -1.0f
        sentiment.magnitude = 4.0f

        Assert.assertNotEquals(SentimentCategoryEnum.POSSIBLY_NEGATIVE, result)
    }

    @Test
    fun testCategorizeNegativeSentiment(){
        val sentiment = Sentiment()
        sentiment.score = -1.0f
        sentiment.magnitude = 4.0f

        var result = viewModel.categorizeSentiment(sentiment)
        Assert.assertEquals(SentimentCategoryEnum.NEGATIVE, result)

        sentiment.score = -0.2f
        sentiment.magnitude = 2.0f

        result = viewModel.categorizeSentiment(sentiment)

        Assert.assertEquals(SentimentCategoryEnum.NEGATIVE, result)

        sentiment.score = -0.5f
        sentiment.magnitude = 1.0f

        result = viewModel.categorizeSentiment(sentiment)

        Assert.assertNotEquals(SentimentCategoryEnum.NEGATIVE, result)

        result = viewModel.categorizeSentiment(sentiment)

        sentiment.score = 0.0f
        sentiment.magnitude = 1.0f

        Assert.assertNotEquals(SentimentCategoryEnum.NEGATIVE, result)
    }
}