package com.bilatchuk.auctionhouse

import platform.Foundation.NSUserDefaults

actual fun applyAppLanguage(languageTag: String) {
    // iOS читає мову з AppleLanguages; повністю підхоплюється при наступному запуску
    NSUserDefaults.standardUserDefaults.setObject(listOf(languageTag), forKey = "AppleLanguages")
}
