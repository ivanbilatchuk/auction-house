package com.bilatchuk.auctionhouse

import java.util.Locale

actual fun applyAppLanguage(languageTag: String) {
    Locale.setDefault(Locale.forLanguageTag(languageTag))
}
