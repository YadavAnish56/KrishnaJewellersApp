package com.krishnajewellers.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "kj_user_prefs")

class PreferencesManager(private val context: Context) {

    companion object {
        private val KEY_ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        private val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val KEY_IS_GUEST = booleanPreferencesKey("is_guest")
        private val KEY_USER_NAME = stringPreferencesKey("user_name")
        private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
        private val KEY_USER_PHONE = stringPreferencesKey("user_phone")
        private val KEY_IS_ADMIN_MODE = booleanPreferencesKey("is_admin_mode")
    }

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_ONBOARDING_COMPLETED] ?: false
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_IS_LOGGED_IN] ?: false
    }

    val isGuest: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_IS_GUEST] ?: false
    }

    val userName: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_USER_NAME] ?: "Kunal Mehta"
    }

    val userEmail: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_USER_EMAIL] ?: "kunal.mehta@example.com"
    }

    val userPhone: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_USER_PHONE] ?: "+91 98251 98765"
    }

    val isAdminMode: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_IS_ADMIN_MODE] ?: false
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun setLoggedIn(name: String, email: String, phone: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_IS_LOGGED_IN] = true
            prefs[KEY_IS_GUEST] = false
            prefs[KEY_USER_NAME] = name
            prefs[KEY_USER_EMAIL] = email
            prefs[KEY_USER_PHONE] = phone
        }
    }

    suspend fun setGuestMode() {
        context.dataStore.edit { prefs ->
            prefs[KEY_IS_LOGGED_IN] = false
            prefs[KEY_IS_GUEST] = true
            prefs[KEY_USER_NAME] = "Guest Explorer"
            prefs[KEY_USER_EMAIL] = ""
            prefs[KEY_USER_PHONE] = ""
        }
    }

    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs[KEY_IS_LOGGED_IN] = false
            prefs[KEY_IS_GUEST] = false
            prefs[KEY_IS_ADMIN_MODE] = false
        }
    }

    suspend fun setAdminMode(isAdmin: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_IS_ADMIN_MODE] = isAdmin
        }
    }
}
