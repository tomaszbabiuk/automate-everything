# Logging

Automate Everything is using SLF4J for logging

## Usage

```kotlin
import org.slf4j.LoggerFactory

object Example {
    private val logger = LoggerFactory.getLogger(Main::class.java)

    fun logSomething() {
        logger.info("Message to log")
    }
}
```
