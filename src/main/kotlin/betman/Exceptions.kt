package betman

class UnknownUserException : RuntimeException("Unknown user")
class InvalidKeyException(msg: String) : RuntimeException(msg)
class UnknownGameException : RuntimeException("Unknown game id")
class UnknownMatchException(msg: String) : RuntimeException(msg)

class InvalidRequestException(reason: String) : RuntimeException(reason)

class UserAlreadyTakenException : RuntimeException("Username already taken")

class InvalidTokenException(msg: String) : RuntimeException(msg)
