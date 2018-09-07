package br.com.eduardofilho.ifood_mobile_test.home

import android.content.Intent
import android.os.Build
import android.view.View
import br.com.eduardofilho.ifood_mobile_test.App
import br.com.eduardofilho.ifood_mobile_test.R
import br.com.eduardofilho.ifood_mobile_test.model.Tweet
import br.com.eduardofilho.ifood_mobile_test.model.User
import br.com.eduardofilho.ifood_mobile_test.ui.home.HomeActivity
import br.com.eduardofilho.ifood_mobile_test.ui.home.HomeViewModel
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
class HomeViewModelTest{

    private lateinit var viewModel : HomeViewModel

    @Before
    fun setup(){
        val intent = Intent()
        intent.putExtra(String::class.simpleName, "jimmyfallon")
        viewModel =  Robolectric.buildActivity(HomeActivity::class.java, intent).create().resume().get().binding.viewModel!!
    }

    @Test
    fun testViewModel(){
        Assert.assertNotNull(viewModel)
    }

    @Test
    fun testOnRetrieveTweetListStart(){
        viewModel.onRetrieveTweetListStart()

        Assert.assertEquals(View.GONE, viewModel.homeTweetListVisibility.value)
        Assert.assertEquals(View.GONE, viewModel.homeTweetListInfoVisibility.value)
        Assert.assertEquals(View.VISIBLE, viewModel.homeLoadingVisibility.value)
    }

    @Test
    fun testOnRetrieveTweetListFinish(){
        viewModel.onRetrieveTweetListFinish()

        Assert.assertEquals(View.GONE, viewModel.homeLoadingVisibility.value)
        Assert.assertEquals(View.VISIBLE, viewModel.homeTweetListVisibility.value )
    }

    @Test
    fun testOnRetrieveTweetListSuccess(){
        val user = User(1, "Mock", "Mock_Name", "")
        val tweet = Tweet(1,"Mock text", Date(), user)

        val mockTweets = listOf(tweet, tweet, tweet, tweet, tweet )

        viewModel.onRetrieveTweetListSuccess(mockTweets)
        Assert.assertEquals(5, viewModel.homeTweetAdapter.itemCount)

        val emptyMockTweets = emptyList<Tweet>()
        viewModel.onRetrieveTweetListSuccess(emptyMockTweets)

        Assert.assertEquals(App.applicationContext().getString(R.string.info_tweet_list_empty),
                viewModel.homeTweetListInfoText.value)
        Assert.assertEquals(View.VISIBLE, viewModel.homeTweetListInfoVisibility.value)

    }

    @Test
    fun testOnRetrieveTweetListError(){
        val ex = Exception()
        viewModel.onRetrieveTweetListError(ex)

        Assert.assertEquals(App.applicationContext().getString(R.string.err_something_wrong),
                viewModel.homeTweetListInfoText.value)
        Assert.assertEquals(View.VISIBLE, viewModel.homeTweetListInfoVisibility.value)
    }

    @Test
    fun testShowTweetListInfoMessage(){
        viewModel.showTweetListInfoMessage("Mock Message")

        Assert.assertEquals(viewModel.homeTweetListInfoText.value, "Mock Message")
        Assert.assertEquals(View.VISIBLE, viewModel.homeTweetListInfoVisibility.value)
    }
}