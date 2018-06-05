package betman

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
open class BadRequestException(msg: String) : RuntimeException(msg)

class UnknownUserException : BadRequestException("Unknown user")
class InvalidUserException(msg: String = "Invalid user") : BadRequestException(msg)

class UnknownGroupException : BadRequestException("Unknown group")
class InvalidKeyException(msg: String) : BadRequestException(msg)
class UnknownGameException : BadRequestException("Unknown game id")
class UnknownMatchException(msg: String) : BadRequestException(msg)
class InvalidRequestException(reason: String) : BadRequestException(reason)
class UserAlreadyTakenException(msg: String = "Username already taken") : BadRequestException(msg)
class InvalidTokenException(msg: String) : BadRequestException(msg)

