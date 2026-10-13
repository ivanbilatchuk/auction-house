package com.bilatchuk.auctionhouse

import platform.Foundation.NSUserDefaults

actual fun applyAppLanguage(languageTag: String) {
    NSUserDefaults.standardUserDefaults.setObject(listOf(languageTag), forKey = "AppleLanguages")
}
