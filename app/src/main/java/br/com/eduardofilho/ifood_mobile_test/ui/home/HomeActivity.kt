package br.com.eduardofilho.ifood_mobile_test.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.eduardofilho.ifood_mobile_test.R
import br.com.eduardofilho.ifood_mobile_test.base.BaseActivity
import br.com.eduardofilho.ifood_mobile_test.databinding.ActivityHomeBinding
import br.com.eduardofilho.ifood_mobile_test.ui.detail.DetailActivity
import br.com.eduardofilho.ifood_mobile_test.utils.DividerItemDecoration


class HomeActivity : BaseActivity(){

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel

    companion object {
        fun navigate(activity : Activity, twitterScreenName : String){

            val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra(String::class.simpleName, twitterScreenName)

            activity.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBindingView()
        setupViewBehavior()
        setupTweetListRecyclerView()
    }

    private fun setupBindingView(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding.viewModel = viewModel
    }

    private fun setupViewBehavior(){
        viewModel.homeTweetAdapter.onItemClick ={
            tweet, view -> DetailActivity.navigate(this, tweet, view)
        }

        viewModel.retrieveTweetListError={
            message -> showErrorSnackBar(binding.root, message)
        }
    }

    private fun setupTweetListRecyclerView(){
        binding.rvHomeTweets.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvHomeTweets.addItemDecoration(DividerItemDecoration(this))

        val twitterScreenName = intent.getStringExtra(String::class.simpleName)
        viewModel.loadTweets(twitterScreenName)
    }


    override fun onNetworkConnectionChangedStatus(isConnected: Boolean) {
        if (!isConnected) {
            showErrorSnackBar(binding.root, getString(R.string.err_not_connected))
        } else {
            hideErrorSnackBar()
        }
    }
}