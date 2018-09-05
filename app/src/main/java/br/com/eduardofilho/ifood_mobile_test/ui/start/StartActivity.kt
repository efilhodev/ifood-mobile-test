package br.com.eduardofilho.ifood_mobile_test.ui.start

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import br.com.eduardofilho.ifood_mobile_test.R
import br.com.eduardofilho.ifood_mobile_test.base.BaseActivity
import br.com.eduardofilho.ifood_mobile_test.databinding.ActivityStartBinding
import br.com.eduardofilho.ifood_mobile_test.ui.home.HomeActivity

class StartActivity : BaseActivity(){
    private lateinit var binding: ActivityStartBinding
    private lateinit var viewModel: StartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBindingView()
        setupViewBehavior()
    }

    private fun setupBindingView(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start)
        viewModel = ViewModelProviders.of(this).get(StartViewModel::class.java)
        binding.viewModel = viewModel
    }

    private fun setupViewBehavior(){
        binding.bntStartAuthenticateAndEnter.setOnClickListener {
            if (validate()) viewModel.getTwitterOAuthToken()
        }

        viewModel.retrieveTwitterOAuthTokenSuccess = {
            HomeActivity.navigate(this, binding.etStartTwitterUsername.text.toString())
            resetField()
        }

        viewModel.retrieveTwitterOAuthTokenError = {
            message -> showErrorSnackBar(binding.root, message)
        }
    }

    private fun validate() : Boolean{
        if(!viewModel.validateUsernameInput(binding.etStartTwitterUsername.text.toString())){
            binding.tilStartTwitterUsername.isErrorEnabled = true
            binding.tilStartTwitterUsername.error = getString(R.string.err_invalid_field)
            return false
        }

        return true
    }

    private fun resetField(){
        binding.etStartTwitterUsername.text?.clear()
        binding.tilStartTwitterUsername.isErrorEnabled = false
    }

    override fun onNetworkConnectionChangedStatus(isConnected: Boolean) {
        if (!isConnected) {
            showErrorSnackBar(binding.root, getString(R.string.err_not_connected))
        } else {
            hideErrorSnackBar()
        }
    }

}