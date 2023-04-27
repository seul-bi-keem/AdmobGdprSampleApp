package com.example.eusampleapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.ads.consent.AdProvider
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.google.gson.Gson


class MainActivity : ComponentActivity() {

    private var consentInformation: ConsentInformation? = null
    private var consentForm: ConsentForm? = null
    private var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textview)

        val debugSettings = ConsentDebugSettings.Builder(this).setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .addTestDeviceHashedId("TEST-DEVICE-HASH-ID").build()

        val params = ConsentRequestParameters.Builder().setConsentDebugSettings(debugSettings).setTagForUnderAgeOfConsent(false).build()

        findViewById<Button>(R.id.button).setOnClickListener {
            consentInformation = UserMessagingPlatform.getConsentInformation(this)
            consentInformation!!.requestConsentInfoUpdate(this, params, { // The consent information state was updated.
                if (consentInformation!!.isConsentFormAvailable) {
                    loadForm()
                }
            }, { // Handle the error.
                textView!!.text = it.message
                                                          })
        }

        findViewById<Button>(R.id.button2).setOnClickListener {
            consentInformation?.reset()
        }

        findViewById<Button>(R.id.button3).setOnClickListener {
            startActivity(Intent(this@MainActivity, GDPRActivity::class.java))
        }

    }

    private fun loadForm() {
        UserMessagingPlatform.loadConsentForm(this@MainActivity, { consentForm ->
            this@MainActivity.consentForm = consentForm
            if (consentInformation!!.consentStatus == ConsentInformation.ConsentStatus.REQUIRED) {
                consentForm.show(this@MainActivity, ConsentForm.OnConsentFormDismissedListener {
                    if (consentInformation!!.consentStatus == ConsentInformation.ConsentStatus.OBTAINED) {
                        // App can start requesting ads.
                    }
                    // Handle dismissal by reloading form.
                    loadForm()
                })
            }

        }, {
            textView!!.text = it.message
        })
    }
}

