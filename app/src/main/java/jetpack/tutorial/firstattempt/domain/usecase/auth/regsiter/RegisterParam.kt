package jetpack.tutorial.firstattempt.domain.usecase.auth.regsiter

data class RegisterParam(
    val fullName: String,
    val email: String,
    val password: String
)
