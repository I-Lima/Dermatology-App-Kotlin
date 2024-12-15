package br.com.ilstudio.dermatologyapp.ui.shared

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.ilstudio.dermatologyapp.data.model.user.UserData

class UserSharedViewModel: ViewModel() {
    val userData = MutableLiveData<UserData>()
}
