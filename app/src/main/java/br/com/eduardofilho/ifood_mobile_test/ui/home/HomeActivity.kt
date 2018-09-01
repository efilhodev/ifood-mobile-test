package br.com.eduardofilho.ifood_mobile_test.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.eduardofilho.ifood_mobile_test.R
import br.com.eduardofilho.ifood_mobile_test.databinding.ActivityHomeBinding
import br.com.eduardofilho.ifood_mobile_test.ui.detail.DetailActivity


class HomeActivity : AppCompatActivity(){
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBindingView()
        setupViewBehavior()
    }

    private fun setupBindingView(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding.viewModel = viewModel
    }

    private fun setupViewBehavior(){
        binding.rvHomeTweets.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        viewModel.refreshAccessTokenIfNeeded()
        viewModel.loadTweets("HeyDuds")

        viewModel.homeTweetAdapter.onItemClick ={tweet -> DetailActivity.navigate(this)}
    }
}