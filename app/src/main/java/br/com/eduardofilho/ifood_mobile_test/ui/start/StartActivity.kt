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
        viewModel.onServiceError={message -> showErrorSnackBar(binding.root, message)}
        viewModel.onServiceSuccess={HomeActivity.navigate(this)}

        binding.bntStartAuthenticateAndEnter.setOnClickListener {
            if (validate()) viewModel.getTwitterOAuthToken()
        }
    }

    private fun validate() : Boolean{
        if(!viewModel.validateUsernameInput(binding.etStartTwitterUsername.text.toString())){
            binding.tilStartTwitterUsername.isErrorEnabled = true
            binding.tilStartTwitterUsername.error = "Campo inválido"
        }

        return true
    }


    override fun onNetworkConnectionChangedStatus(isConnected: Boolean) {
    }

}