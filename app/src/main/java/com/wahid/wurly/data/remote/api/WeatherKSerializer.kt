package com.wahid.wurly.data.remote.api

import com.wahid.wurly.data.common.model.Weather
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.Serializable

object WeatherKSerializer : KSerializer<Weather> {
    @Serializable
    private data class WeatherDto(
        val id: Long,
        val main: String,
        val description: String,
        val icon: String
    )

    private val delegate = WeatherDto.serializer()


    override val descriptor: SerialDescriptor
        get() = delegate.descriptor

    override fun serialize(
        encoder: Encoder,
        value: Weather
    ) {
        val dto = WeatherDto(
            id = value.id,
            main = value.main,
            description = value.description,
            icon = value.icon
        )
        delegate.serialize(encoder = encoder, value = dto)
    }


    override fun deserialize(decoder: Decoder): Weather {
        val jsonDecoder =
            decoder as? JsonDecoder ?: throw SerializationException("Expected JsonDecoder")
        val element = jsonDecoder.decodeJsonElement()
        val target = when (element) {
            is JsonArray -> element.firstOrNull()
            is JsonObject -> element
            else -> null
        } ?: throw SerializationException("Unexpected JSON element type: ${element::class}")
        val dto = jsonDecoder.json.decodeFromJsonElement(delegate, target)
        return Weather(
            id = dto.id,
            main = dto.main,
            description = dto.description,
            icon = dto.icon
        )
    }
}