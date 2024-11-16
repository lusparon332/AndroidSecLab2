package com.example.inventory.data

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.inventory.applicationContext

class SettingsRepository {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val sharedPreferences = EncryptedSharedPreferences.create(
        "PreferencesFilename",
        masterKeyAlias,
        applicationContext!!,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun isNoData(): Boolean {
        return (sharedPreferences.getString("isNoData", "") ?: return false) == "true"
    }

    fun switchNoData() {
        sharedPreferences.edit().putString("isNoData", (if(isNoData()) "false" else "true")).apply()
    }

    fun isNoShare(): Boolean {
        return (sharedPreferences.getString("isNoShare", "") ?: return false) == "true"
    }

    fun switchNoShare() {
        sharedPreferences.edit().putString("isNoShare", (if(isNoShare()) "false" else "true")).apply()
    }

    fun isDefault(): Boolean {
        return (sharedPreferences.getString("isDefault", "") ?: return false) == "true"
    }

    fun default(): Int {
        val t = sharedPreferences.getString("default", "") ?: return 10
        if (t == "") return 10
        return t.toInt()
    }

    fun switchDefault() {
        sharedPreferences.edit().putString("isDefault", (if(isDefault()) "false" else "true")).apply()
    }

    fun newDefault(toInt: String) {
        sharedPreferences.edit().putString("default", toInt).apply()
    }
}