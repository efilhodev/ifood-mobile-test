package br.com.eduardofilho.ifood_mobile_test.utils

import br.com.eduardofilho.ifood_mobile_test.model.Tweet
import br.com.eduardofilho.ifood_mobile_test.model.User
import java.util.*

class Mock{
    companion object {

        fun getMockTweets(count : Int) : List<Tweet> {

            //TODO objects created to test
            val tweets: ArrayList<Tweet> = ArrayList()

            for (i in 1..count) {
                val user = User(i.toLong(), "Eduardo Julio $i", "Duds $i", "")
                val tweet = Tweet(i.toLong(), "Text Content $i", Date(), user)
                tweets.add(tweet)

            }
            return tweets
        }
    }
}