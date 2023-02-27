package fr.imacaron.motrelou.type

import kotlinx.serialization.Serializable

@Serializable
data class TNouveauMot(
	val mot: String,
	val createur: String,
	val definition: String
)
