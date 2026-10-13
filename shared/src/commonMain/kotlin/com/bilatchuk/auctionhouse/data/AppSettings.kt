package com.bilatchuk.auctionhouse.data

import co.touchlab.kermit.Logger
import com.russhwolf.settings.Settings

class AppSettings(private val settings: Settings = Settings()) {
    private val log = Logger.withTag("AppSettings")

    var language: String?
        get() = settings.getStringOrNull(KEY_LANGUAGE)
        set(value) {
            log.i { "language = $value" }
            if (value == null) settings.remove(KEY_LANGUAGE) else settings.putString(KEY_LANGUAGE, value)
        }

    private companion object {
        const val KEY_LANGUAGE = "language"
    }
}
