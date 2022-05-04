package com.example.asystent.application

import android.app.Application
import com.example.asystent.database.AsystentDatabase
import com.example.asystent.repository.AsystentRepository

class AsystentApplication : Application() {
    private val asystentDatabase by lazy { AsystentDatabase.getDatabase(this) }
    val asystentRepository by lazy { AsystentRepository(asystentDatabase.asystentDao()) }
}