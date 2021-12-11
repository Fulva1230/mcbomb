package com.gmail.noxdawn

interface Logger {
    fun info(content: String)

    fun debug(content: String)
}

class VerboseLoggerImpl(private val logger: java.util.logging.Logger) : Logger {
    override fun info(content: String) {
        logger.info(content)
    }

    override fun debug(content: String) {
        logger.info("[DEBUG] $content")
    }
}
