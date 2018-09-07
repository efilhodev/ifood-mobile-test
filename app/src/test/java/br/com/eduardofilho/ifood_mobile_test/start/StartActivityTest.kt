package br.com.eduardofilho.ifood_mobile_test.start

import android.os.Build
import br.com.eduardofilho.ifood_mobile_test.ui.start.StartActivity
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class StartActivityTest{

    private lateinit var activity: StartActivity

    @Before
    fun setup(){
        activity = Robolectric.buildActivity(StartActivity::class.java).create().resume().get()
    }

    @Test
    fun testSetupBindingView(){
        Assert.assertNotNull(activity.binding)
        Assert.assertNotNull(activity.viewModel)
    }

    @Test
    fun testInputValidate(){
        Assert.assertFalse(activity.binding.tilStartTwitterUsername.isErrorEnabled)
        Assert.assertTrue(activity.validate("ifood"))
        Assert.assertFalse(activity.binding.tilStartTwitterUsername.isErrorEnabled)

        Assert.assertFalse(activity.validate("i food"))
        Assert.assertTrue(activity.binding.tilStartTwitterUsername.isErrorEnabled)
    }

    @Test
    fun testResetTwitterUsernameField(){
        Assert.assertTrue(activity.binding.etStartTwitterUsername.text!!.isEmpty())
        activity.binding.etStartTwitterUsername.setText("Teste")
        Assert.assertFalse(activity.binding.etStartTwitterUsername.text!!.isEmpty())
        activity.resetTwitterUsernameField()
        Assert.assertTrue(activity.binding.etStartTwitterUsername.text!!.isEmpty())
    }
}