package br.com.eduardofilho.ifood_mobile_test.network

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.GenericJson
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.language.v1.CloudNaturalLanguage
import com.google.api.services.language.v1.CloudNaturalLanguageRequest
import com.google.api.services.language.v1.CloudNaturalLanguageScopes
import com.google.api.services.language.v1.model.AnalyzeSentimentRequest
import com.google.api.services.language.v1.model.AnalyzeSentimentResponse
import com.google.api.services.language.v1.model.Document
import com.google.api.services.language.v1.model.Sentiment
import java.io.IOException

class NaturalLanguageAnalyzer{

    private lateinit var credential : GoogleCredential

    private val cloudNaturalLanguage = CloudNaturalLanguage.Builder(NetHttpTransport(),
            JacksonFactory.getDefaultInstance(),
            HttpRequestInitializer { request -> credential.initialize(request) }).build()

    lateinit var request : CloudNaturalLanguageRequest<out GenericJson>

    private fun setAccessTokenIntoGoogleCredential(token : String){
        credential = GoogleCredential()
                .setAccessToken(token)
                .createScoped(CloudNaturalLanguageScopes.all())
    }

    fun analyzeSentimentByService(text: String, token: String) : Sentiment {

        if(token.isEmpty()) throw IllegalStateException("Credential access token cant be empty")

        setAccessTokenIntoGoogleCredential(token)

        try {
            request = cloudNaturalLanguage
                    .documents()
                    .analyzeSentiment(AnalyzeSentimentRequest()
                            .setDocument(Document()
                                    .setContent(text)
                                    .setType("PLAIN_TEXT")))
        } catch (e: IOException) {
           e.printStackTrace()
        }

        val analyzeSentimentResponse = request.execute() as AnalyzeSentimentResponse

        return analyzeSentimentResponse.documentSentiment
    }
}