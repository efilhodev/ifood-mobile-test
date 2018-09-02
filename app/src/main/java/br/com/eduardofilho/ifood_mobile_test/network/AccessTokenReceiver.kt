package br.com.eduardofilho.ifood_mobile_test.network

import android.content.Context
import br.com.eduardofilho.ifood_mobile_test.R
import br.com.eduardofilho.ifood_mobile_test.utils.ACCESS_TOKEN_MAX_EXPIRES_TIME
import br.com.eduardofilho.ifood_mobile_test.utils.ACCESS_TOKEN_PREF
import br.com.eduardofilho.ifood_mobile_test.utils.PREFERENCES
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.services.language.v1.CloudNaturalLanguageScopes

class AccessTokenReceiver(val context: Context) {

    fun getOrRefreshServiceAccessTokenIfNeeded(): String? {
        val preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val currentToken = preferences.getString(ACCESS_TOKEN_PREF, "")

        if(!currentToken!!.isEmpty()){
            val credential = GoogleCredential()
                    .setAccessToken(currentToken)
                    .createScoped(CloudNaturalLanguageScopes.all())

            val seconds = credential.expiresInSeconds
            if(seconds != null && seconds > ACCESS_TOKEN_MAX_EXPIRES_TIME){
                return currentToken
            }
        }

        val stream = context.resources.openRawResource(R.raw.credentials)
        val credential = GoogleCredential.fromStream(stream).createScoped(CloudNaturalLanguageScopes.all())
        credential.refreshToken()

        val accessToken = credential.accessToken
        preferences.edit().putString(ACCESS_TOKEN_PREF, accessToken).apply()

        return accessToken
    }
}