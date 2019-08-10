package ru.vanchikov.fitnesinmylife.ui.login

import ru.vanchikov.fitnesinmylife.data.model.LoggedInUser

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val user: LoggedInUser
    //... other data fields that may be accessible to the UI
)
