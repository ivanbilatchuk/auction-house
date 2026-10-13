package com.bilatchuk.auctionhouse.data

import co.touchlab.kermit.Logger
import com.russhwolf.settings.Settings

class AppSettings(private val settings: Settings = Settings()) {
    private val log = Logger.withTag("AppSettings")

    var onboardingSeen: Boolean
        get() = settings.getBoolean(KEY_ONBOARDING_SEEN, false)
        set(value) {
            log.i { "onboardingSeen = $value" }
            settings.putBoolean(KEY_ONBOARDING_SEEN, value)
        }

    var language: String?
        get() = settings.getStringOrNull(KEY_LANGUAGE)
        set(value) {
            log.i { "language = $value" }
            if (value == null) settings.remove(KEY_LANGUAGE) else settings.putString(KEY_LANGUAGE, value)
        }

    private companion object {
        const val KEY_ONBOARDING_SEEN = "onboarding_seen"
        const val KEY_LANGUAGE = "language"
    }
}
