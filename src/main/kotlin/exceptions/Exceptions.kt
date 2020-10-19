package exceptions

class ExceptionPersistingEventsToFile(message: String): Throwable(message)
class ExceptionSubscribingToQueue(message: String): Throwable(message)