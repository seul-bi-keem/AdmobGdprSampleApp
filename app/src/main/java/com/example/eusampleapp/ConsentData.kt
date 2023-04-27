package com.example.eusampleapp

import com.google.ads.consent.AdProvider
import com.google.ads.consent.ConsentStatus
import com.google.gson.annotations.SerializedName


class ConsentData {
    @SerializedName("providers")
    var adProviders: HashSet<AdProvider> = HashSet()

    @SerializedName("is_request_in_eea_or_unknown")
    var isRequestLocationInEeaOrUnknown: Boolean

    @SerializedName("consented_providers")
    var consentedAdProviders: HashSet<AdProvider> = HashSet()

    @SerializedName("tag_for_under_age_of_consent")
    var isTaggedForUnderAgeOfConsent: Boolean
        private set

    @SerializedName("consent_state")
    private var consentStatus: ConsentStatus

    @SerializedName("pub_ids")
    var publisherIds: HashSet<String> = HashSet()

    @SerializedName("has_any_npa_pub_id")
    private var hasNonPersonalizedPublisherId: Boolean

    @SerializedName("consent_source")
    var consentSource: String? = null

    @SerializedName("version")
    val sDKVersionString: String

    @SerializedName("plat")
    val sDKPlatformString: String

    @SerializedName("raw_response")
    var rawResponse: String

    init {
        isTaggedForUnderAgeOfConsent = false
        consentStatus = ConsentStatus.UNKNOWN
        isRequestLocationInEeaOrUnknown = false
        hasNonPersonalizedPublisherId = false
        sDKVersionString = SDK_VERSION
        sDKPlatformString = SDK_PLATFORM
        rawResponse = ""
    }

    fun tagForUnderAgeOfConsent(underAgeOfConsent: Boolean) {
        isTaggedForUnderAgeOfConsent = underAgeOfConsent
    }

    fun getConsentStatus(): ConsentStatus {
        return consentStatus
    }

    fun setConsentStatus(consentStatus: ConsentStatus) {
        this.consentStatus = consentStatus
    }

    fun hasNonPersonalizedPublisherId(): Boolean {
        return hasNonPersonalizedPublisherId
    }

    fun setHasNonPersonalizedPublisherId(hasNonPersonalizedPublisherId: Boolean) {
        this.hasNonPersonalizedPublisherId = hasNonPersonalizedPublisherId
    }

    companion object {
        private const val SDK_PLATFORM = "android"
        private const val SDK_VERSION = "1.0.8"
    }
}