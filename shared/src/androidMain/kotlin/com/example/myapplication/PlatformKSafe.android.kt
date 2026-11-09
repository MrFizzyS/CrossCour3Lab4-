package com.example.myapplication

import eu.anifantakis.lib.ksafe.KSafe

actual fun createPlatformKSafe(): KSafe =
    error("On Android pass KSafe(applicationContext) from MainActivity")