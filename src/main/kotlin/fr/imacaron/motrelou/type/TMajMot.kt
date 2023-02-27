package fr.imacaron.motrelou.type

import kotlinx.serialization.Serializable

@Serializable
data class TMajMot(
	val mot: String? = null,
	val createur: String? = null
)
