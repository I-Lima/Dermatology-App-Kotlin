package br.com.ilstudio.dermatologyapp.storage

import br.com.ilstudio.dermatologyapp.domain.model.User

object SessionManager {
    var currentUser: User? = null
}
