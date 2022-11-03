package com.abernard.firstproject

import java.io.Serializable

class UserModel(
    var userName: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var birthDate: String? = null,
    var gender: String? = null,
    var country: String? = null,
    var state: String? = null,
    var address: String? = null,
    var phone: String? = null,
    var email: String? = null,
    ) : Serializable {
}