package jetpack.tutorial.firstattempt.domain.model.auth

data class RegisterModel(
    val userId: String,
    val email: String,
    val fullName: String,
)
