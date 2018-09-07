package br.com.eduardofilho.ifood_mobile_test.start

import android.content.Context
import android.os.Build
import android.view.View
import br.com.eduardofilho.ifood_mobile_test.App
import br.com.eduardofilho.ifood_mobile_test.model.TwitterOAuthToken
import br.com.eduardofilho.ifood_mobile_test.ui.start.StartActivity
import br.com.eduardofilho.ifood_mobile_test.ui.start.StartViewModel
import br.com.eduardofilho.ifood_mobile_test.utils.PREFERENCES
import br.com.eduardofilho.ifood_mobile_test.utils.TWITTER_ACCESS_TOKEN_PREF
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class StartViewModelTest{
    
    private lateinit var viewModel : StartViewModel
    
    @Before
    fun setup(){
        viewModel =  Robolectric.buildActivity(StartActivity::class.java).create().resume().get().binding.viewModel!!
    }

    @Test
    fun testViewModel(){
        Assert.assertNotNull(viewModel)
    }


    @Test
    fun testValidateUsernameInput(){
        Assert.assertTrue(viewModel.validateUsernameInput("ifood"))
        Assert.assertTrue(viewModel.validateUsernameInput("1234"))
        Assert.assertTrue(viewModel.validateUsernameInput("1234*"))
        Assert.assertFalse(viewModel.validateUsernameInput(""))
        Assert.assertFalse(viewModel.validateUsernameInput("i food"))
    }

    @Test
    fun testOnRetrieveOAuthTokenStart(){
        viewModel.onRetrieveOAuthTokenStart()

        Assert.assertEquals(View.VISIBLE, viewModel.startLoadingVisibility.value)
        Assert.assertEquals(View.INVISIBLE, viewModel.startButtonVisibility.value)

        val preferences = App.applicationContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val result = preferences.getString(TWITTER_ACCESS_TOKEN_PREF, "")
        Assert.assertEquals("", result)
    }

    @Test
    fun testOnRetrieveOAuthTokenFinish(){
        viewModel.onRetrieveOAuthTokenFinish()

        Assert.assertEquals(View.GONE, viewModel.startLoadingVisibility.value)
        Assert.assertEquals(View.VISIBLE, viewModel.startButtonVisibility.value)
    }

    @Test
    fun testOnRetrieveTwitterOAuthTokenSuccess(){
        val twitterOAuthToken = TwitterOAuthToken("MOCK-1337-TOKEN", "MockType")
        Assert.assertEquals("MockType MOCK-1337-TOKEN", twitterOAuthToken.getAuthorization())

        viewModel.onRetrieveTwitterOAuthTokenSuccess(twitterOAuthToken)

        val preferences = App.applicationContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val result = preferences.getString(TWITTER_ACCESS_TOKEN_PREF, "")
        Assert.assertEquals("MockType MOCK-1337-TOKEN", result)
    }

    @Test
    fun testOnRetrieveTwitterOAuthTokenError(){
        val ex = Exception()
        viewModel.onRetrieveTwitterOAuthTokenError(ex)

        val ex2 = Exception("MockException")
        viewModel.onRetrieveTwitterOAuthTokenError(ex2)
    }


    @Test
    fun testSaveTwitterOAuthTokenAuthorization(){
        val twitterOAuthToken = TwitterOAuthToken("MOCK-1337-TOKEN", "MockType")
        Assert.assertEquals("MockType MOCK-1337-TOKEN", twitterOAuthToken.getAuthorization())

        val preferences = App.applicationContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        var result = preferences.getString(TWITTER_ACCESS_TOKEN_PREF, "")
        Assert.assertEquals("", result)

        viewModel.saveTwitterOAuthTokenAuthorization(twitterOAuthToken.getAuthorization())

        result = preferences.getString(TWITTER_ACCESS_TOKEN_PREF, "")
        Assert.assertEquals("MockType MOCK-1337-TOKEN", result)
    }

    @Test
    fun testRemoveOldTwitterOAuthTokenAuthorization(){
        val preferences = App.applicationContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        preferences.edit().putString(TWITTER_ACCESS_TOKEN_PREF, "MockType MOCK-1337-TOKEN").apply()

        var result = preferences.getString(TWITTER_ACCESS_TOKEN_PREF, "")
        Assert.assertEquals("MockType MOCK-1337-TOKEN", result)

        viewModel.removeOldTwitterOAuthTokenAuthorization()

        result = preferences.getString(TWITTER_ACCESS_TOKEN_PREF, "")
        Assert.assertEquals("", result)
    }
}