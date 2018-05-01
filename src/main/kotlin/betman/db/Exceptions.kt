package betman.db

class InvalidRequestException(reason: String) : RuntimeException(reason)

class UserAlreadyTakenException : RuntimeException("Username already taken")