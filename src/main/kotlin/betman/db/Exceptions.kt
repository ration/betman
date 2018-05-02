package betman.db

class UnknownUserException : RuntimeException("Unknown user")
class InvalidKeyException(msg: String) : RuntimeException(msg)

class InvalidRequestException(reason: String) : RuntimeException(reason)

class UserAlreadyTakenException : RuntimeException("Username already taken")