package jetpack.tutorial.firstattempt.data.dto.auth

data class RegisterDto(
    val userId: String? = null,
    val email: String? = null,
    val fullName: String? = null,
)