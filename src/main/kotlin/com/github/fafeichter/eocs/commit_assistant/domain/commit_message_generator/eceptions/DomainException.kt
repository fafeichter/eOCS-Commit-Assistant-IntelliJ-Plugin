package com.github.fafeichter.eocs.commit_assistant.domain.commit_message_generator.eceptions

class DomainException(message: String, cause: Throwable? = null) : Exception(message, cause)