package com.krishnajewellers.app

import android.app.Application
import com.krishnajewellers.app.data.local.AppDatabaseHelper
import com.krishnajewellers.app.data.local.PreferencesManager
import com.krishnajewellers.app.data.repository.JewelleryRepository

class KrishnaJewellersApp : Application() {

    lateinit var databaseHelper: AppDatabaseHelper
        private set

    lateinit var preferencesManager: PreferencesManager
        private set

    lateinit var repository: JewelleryRepository
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        preferencesManager = PreferencesManager(this)
        databaseHelper = AppDatabaseHelper(this)
        repository = JewelleryRepository(databaseHelper, preferencesManager)
    }

    companion object {
        lateinit var instance: KrishnaJewellersApp
            private set
    }
}
