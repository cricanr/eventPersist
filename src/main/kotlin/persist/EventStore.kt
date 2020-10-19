package persist

import com.google.inject.Inject
import exceptions.ExceptionPersistingEventsToFile
import mu.KotlinLogging

interface IEventStore {
    fun persist(eventsAsJson: List<String>)
}

class EventStore @Inject constructor(private val filePersist: IFilePersist) : IEventStore {
    private val logger = KotlinLogging.logger {}

    override fun persist(eventsAsJson: List<String>): Unit = run {
        try {
            eventsAsJson.forEach { eventAsJson ->
                if (eventAsJson.isNotEmpty()) filePersist.writeLine(eventAsJson)
            }
        } catch (exception: Throwable) {
            val message = "Failure ${exception.message} occurred while persisting events to file"
            logger.error(message)
            throw ExceptionPersistingEventsToFile(message)
        }
    }
}