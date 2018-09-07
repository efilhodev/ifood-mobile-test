package br.com.eduardofilho.ifood_mobile_test.home

import android.content.Intent
import android.os.Build
import br.com.eduardofilho.ifood_mobile_test.ui.home.HomeActivity
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class HomeActivityTest{

    private lateinit var activity : HomeActivity

    @Before
    fun setup(){
        val intent = Intent()
        intent.putExtra(String::class.simpleName, "jimmyfallon")
        activity =  Robolectric.buildActivity(HomeActivity::class.java, intent).create().resume().get()
    }

    @Test
    fun testSetupBindingView(){
        Assert.assertNotNull(activity.binding)
        Assert.assertNotNull(activity.viewModel)
    }

    @Test
    fun testSetupTweetListRecyclerView(){
        activity.setupTweetListRecyclerView()
        Assert.assertNotNull(activity.binding.rvHomeTweets)
    }
}