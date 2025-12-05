package com.example.pbo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pbo.data.repository.UserRepository
import kotlinx.coroutines.launch

// SRP ViewModel hanya mengurus LOGIKA bisnis dan state, tidak mengurus View/UI.

class ChangeNameViewModel(private val repository: UserRepository) : ViewModel() {

    private val _updateStatus = MutableLiveData<Boolean>()
    val updateStatus: LiveData<Boolean> get() = _updateStatus

    // OCP - Open/Closed Principle
    // Jika kita ingin mengubah cara validasi nama, kita ubah di sini tanpa mengganggu UI Activity.
    fun validateAndSaveName(first: String, last: String): Boolean {
        if (first.isEmpty() || last.isEmpty()) {
            return false // Invalid
        }

        val loginKey = repository.getLoginKey()

        // Simpan ke Prefs
        repository.saveUserToPrefs(first, last)

        // Simpan ke DB jika user login
        if (loginKey != null) {
            viewModelScope.launch {
                repository.updateName(loginKey, first, last)
                _updateStatus.value = true
            }
        } else {
            _updateStatus.value = true
        }
        return true
    }
}

class ChangePasswordViewModel(private val repository: UserRepository) : ViewModel() {

    private val _passwordStatus = MutableLiveData<String>() // "SUCCESS", "WRONG_OLD", "MISMATCH"
    val passwordStatus: LiveData<String> get() = _passwordStatus

    fun changePassword(oldPass: String, newPass: String, confirmPass: String) {
        if (newPass != confirmPass) {
            _passwordStatus.value = "MISMATCH"
            return
        }

        val loginKey = repository.getLoginKey() ?: return

        viewModelScope.launch {
            val account = repository.getAccount(loginKey)
            if (account != null && account.password == oldPass) {
                repository.updatePassword(loginKey, newPass)
                _passwordStatus.value = "SUCCESS"
            } else {
                _passwordStatus.value = "WRONG_OLD"
            }
        }
    }
}

// Factory untuk inject Repository ke ViewModel (Pendukung DIP)
class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChangeNameViewModel::class.java)) {
            return ChangeNameViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(ChangePasswordViewModel::class.java)) {
            return ChangePasswordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}