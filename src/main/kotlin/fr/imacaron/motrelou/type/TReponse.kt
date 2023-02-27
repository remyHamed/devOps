package fr.imacaron.motrelou.type

import kotlinx.serialization.Serializable

@Serializable
data class TReponse(
	val code: Int,
	val message: String,
	val donnee: String? = null
){
	companion object{
		val NotFound = TReponse(404, "Not found")
		val BadRequest = TReponse(400, "Mauvaise requête, paramètre invalides")
		val NoContent = TReponse(204, "Pas de contenu")
		val Conflict = TReponse(409, "Conflit")
	}
}
