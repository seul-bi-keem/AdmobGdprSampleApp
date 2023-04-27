package com.example.eusampleapp

import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ads.consent.AdProvider
import com.google.ads.consent.ConsentForm
import com.google.ads.consent.ConsentFormListener
import com.google.ads.consent.ConsentInfoUpdateListener
import com.google.ads.consent.ConsentInformation
import com.google.ads.consent.ConsentStatus
import com.google.ads.consent.DebugGeography
import com.google.gson.Gson
import java.net.MalformedURLException
import java.net.URL


class GDPRActivity : AppCompatActivity() {

    companion object {
        private const val PREFERENCES_FILE_KEY = "mobileads_consent"
        private const val CONSENT_DATA_KEY = "consent_string"
    }

    var form : ConsentForm? = null
    var showButton : Button? = null
    var errorText : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gdpractivity)

        errorText = findViewById(R.id.error_textview)
        showButton = findViewById(R.id.show)

        showButton!!.isEnabled = false

        val consentInformation: ConsentInformation = ConsentInformation.getInstance(this@GDPRActivity)
        val publisherIds = arrayOf("ADMOB-PUBLISHER-ID")
        consentInformation.addTestDevice("TEST-DEVICE-HASH-ID")
        consentInformation.debugGeography = DebugGeography.DEBUG_GEOGRAPHY_EEA
        consentInformation.requestConsentInfoUpdate(publisherIds, object : ConsentInfoUpdateListener {
            override fun onConsentInfoUpdated(consentStatus: ConsentStatus) {
                // User's consent status successfully updated.
                Toast.makeText(this@GDPRActivity, "onConsentInfoUpdated ${consentStatus.name}", Toast.LENGTH_SHORT).show()

                // To edit Ad providers, modify parameter
                saveProviders(listOf(AdProviderData("google", "https://google.com"), AdProviderData("facebook", "https://facebook.com")))
                loadForm()
            }

            override fun onFailedToUpdateConsentInfo(errorDescription: String) {
                // User's consent status failed to update.
                Toast.makeText(this@GDPRActivity, "onConsentInfoUpdated", Toast.LENGTH_SHORT).show()
                errorText!!.text = errorDescription
            }
        })

        showButton!!.setOnClickListener {
            form!!.show()
            Toast.makeText(this@GDPRActivity, "show!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadConsentData(): ConsentData {
        val sharedPref: SharedPreferences = getSharedPreferences(PREFERENCES_FILE_KEY, MODE_PRIVATE)
        val consentDataString = sharedPref.getString(CONSENT_DATA_KEY, "")
        return if (TextUtils.isEmpty(consentDataString)) {
            ConsentData()
        } else {
            Gson().fromJson(consentDataString, ConsentData::class.java)
        }
    }

    private fun saveConsentData(consentData: ConsentData) {
        val sharedPref: SharedPreferences = getSharedPreferences(PREFERENCES_FILE_KEY, MODE_PRIVATE)
        val editor = sharedPref.edit()
        val consentDataString = Gson().toJson(consentData)
        editor.putString(CONSENT_DATA_KEY, consentDataString)
        editor.apply()
    }

    private fun saveProviders(providerList : List<AdProviderData>) {
        val consentData = loadConsentData()
        val providers = hashSetOf<AdProvider>()

        for ((id, providerItem) in providerList.withIndex()) {
            val provider = AdProvider()
            provider.id = id.toString()
            provider.name = providerItem.name
            provider.privacyPolicyUrlString = providerItem.url
            providers.add(provider)
        }

        consentData.adProviders = providers
        saveConsentData(consentData)
    }

    private fun loadForm() {
        var privacyUrl: URL? = null
        try {
            privacyUrl = URL("https://google.com")
        } catch (e: MalformedURLException) {
            e.printStackTrace() // Handle error.
        }
        form = ConsentForm.Builder(this, privacyUrl).withListener(object : ConsentFormListener() {
            override fun onConsentFormLoaded() {
                // Consent form loaded successfully.
                Toast.makeText(this@GDPRActivity, "loaded", Toast.LENGTH_SHORT).show()
                showButton?.isEnabled = true
            }

            override fun onConsentFormOpened() {
                // Consent form was displayed.
                Toast.makeText(this@GDPRActivity, "opened", Toast.LENGTH_SHORT).show()
            }

            override fun onConsentFormClosed(consentStatus: ConsentStatus?, userPrefersAdFree: Boolean?) {
                // Consent form was closed.
                Toast.makeText(this@GDPRActivity, "closed status : ${consentStatus.toString()}", Toast.LENGTH_SHORT).show()
            }

            override fun onConsentFormError(errorDescription: String?) {
                // Consent form error.
                errorText?.text = errorDescription
            }
        }).withPersonalizedAdsOption()
                .build()

        form?.load()
    }

    data class AdProviderData(val name : String, val url : String)
}