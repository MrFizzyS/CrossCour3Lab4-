package com.example.myapplication.data

import com.russhwolf.settings.Settings

class AppSettings(private val settings: Settings) {
    var darkTheme: Boolean
        get() = settings.getBoolean("dark_theme", false)
        set(value) = settings.putBoolean("dark_theme", value)
}