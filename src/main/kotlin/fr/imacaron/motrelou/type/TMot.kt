package fr.imacaron.motrelou.type

import fr.imacaron.motrelou.serializeur.SerializeurLocalDateTime
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class TMot(
	val mot: String,
	var createur: String,
	@Serializable(with = SerializeurLocalDateTime::class)
	var creation: LocalDateTime,
	val definitions: MutableList<TDefinition>
)
