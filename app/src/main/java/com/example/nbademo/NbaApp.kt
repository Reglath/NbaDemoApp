package com.example.nbademo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Základní třída aplikace inicializující Dagger Hilt pro Dependency Injection.
 */
@HiltAndroidApp
class NbaApp : Application()