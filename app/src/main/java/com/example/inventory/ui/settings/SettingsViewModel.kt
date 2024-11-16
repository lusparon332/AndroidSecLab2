package com.example.inventory.ui.settings

import androidx.lifecycle.ViewModel
import com.example.inventory.data.SettingsRepository

class SettingsViewModel : ViewModel() {
    private val repository = SettingsRepository()

    fun isNoData(): Boolean { return repository.isNoData() }
    fun switchNoData() { repository.switchNoData() }
    fun isNoShare(): Boolean { return repository.isNoShare() }
    fun switchNoShare() { repository.switchNoShare() }
    fun isDefault(): Boolean { return repository.isDefault() }
    fun default(): Int { return repository.default() }
    fun switchDefault() { repository.switchDefault() }
    fun newDefault(it: String) { if (it.toIntOrNull() != null && it.toInt() > 0) repository.newDefault(it) }
}