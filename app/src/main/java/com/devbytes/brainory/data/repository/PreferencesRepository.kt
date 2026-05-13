package com.devbytes.brainory.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "brainory_prefs")

@Singleton
class PreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val IS_LOGGED_IN        = booleanPreferencesKey("is_logged_in")
        val ONBOARDING_DONE     = booleanPreferencesKey("onboarding_done")
        val LOGGED_IN_EMAIL     = stringPreferencesKey("logged_in_email")
        val DARK_MODE           = booleanPreferencesKey("dark_mode")
    }

    val isLoggedIn: Flow<Boolean>    = context.dataStore.data.map { it[IS_LOGGED_IN] ?: false }
    val onboardingDone: Flow<Boolean>= context.dataStore.data.map { it[ONBOARDING_DONE] ?: false }
    val loggedInEmail: Flow<String>  = context.dataStore.data.map { it[LOGGED_IN_EMAIL] ?: "" }
    val darkMode: Flow<Boolean>      = context.dataStore.data.map { it[DARK_MODE] ?: false }

    suspend fun setLoggedIn(email: String) {
        context.dataStore.edit {
            it[IS_LOGGED_IN]    = true
            it[LOGGED_IN_EMAIL] = email
        }
    }

    suspend fun setLoggedOut() {
        context.dataStore.edit {
            it[IS_LOGGED_IN]    = false
            it[LOGGED_IN_EMAIL] = ""
        }
    }

    suspend fun setOnboardingDone() {
        context.dataStore.edit { it[ONBOARDING_DONE] = true }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[DARK_MODE] = enabled }
    }
}