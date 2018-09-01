package br.com.eduardofilho.ifood_mobile_test.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.eduardofilho.ifood_mobile_test.R
import br.com.eduardofilho.ifood_mobile_test.base.BaseViewHolder
import br.com.eduardofilho.ifood_mobile_test.databinding.RowTweetBinding
import br.com.eduardofilho.ifood_mobile_test.model.Tweet
import javax.inject.Inject

class TweetAdapter : RecyclerView.Adapter<TweetAdapter.ViewHolder>(){

    var onItemClick: ((Tweet) -> Unit)? = null

    private lateinit var tweets : List<Tweet>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowTweetBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.row_tweet, parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if(::tweets.isInitialized) tweets.size else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tweets[position])
    }

    fun updateTweetList(tweets : List<Tweet>){
        this.tweets = tweets
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding : RowTweetBinding) : BaseViewHolder(binding.root){
        @Inject
        lateinit var viewModel : TweetViewModel

        init {

                itemView.setOnClickListener {
                    onItemClick?.invoke(tweets[adapterPosition])
            }
        }
        fun bind(tweet: Tweet){
            viewModel.bind(tweet)
            binding.viewModel = viewModel
        }
    }
}