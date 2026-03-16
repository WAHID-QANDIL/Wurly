package com.wahid.wurly.data.local.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.wahid.wurly.domain.model.settings.UserSettings
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.InputStream
import java.io.OutputStream
@OptIn(ExperimentalSerializationApi::class)
class SettingsPrefsSerializer(override val defaultValue: UserSettings) : Serializer<UserSettings> {
    private val json = Json { ignoreUnknownKeys = true }


    override suspend fun readFrom(input: InputStream): UserSettings =
        try {
            json.decodeFromStream(UserSettings.serializer(), input)
        } catch (exception: SerializationException) {
            throw CorruptionException("Cannot read user settings.", exception)
        }


    override suspend fun writeTo(
        t: UserSettings,
        output: OutputStream
    ) = json.encodeToStream(UserSettings.serializer(), t, output)
}