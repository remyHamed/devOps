package fr.imacaron.motrelou.type

import kotlinx.serialization.Serializable

@Serializable
data class TNouvelleDefinition(
	val definition: String,
	val createur: String
)