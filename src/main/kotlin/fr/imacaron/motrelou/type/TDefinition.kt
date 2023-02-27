package fr.imacaron.motrelou.type

import fr.imacaron.motrelou.serializeur.SerializeurLocalDateTime
import java.time.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class TDefinition(
	var definition: String,
	val createur: String,
	@Serializable(with = SerializeurLocalDateTime::class)
	val creation: LocalDateTime,
	var index: Int
)
