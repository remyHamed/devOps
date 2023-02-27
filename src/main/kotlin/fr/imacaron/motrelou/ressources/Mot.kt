package fr.imacaron.motrelou.ressources

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("/mot")
class Mot{
	@Serializable
	@Resource("random")
	class Random(val parent: Mot = Mot())

	@Serializable
	@Resource("{mot}")
	class Id(val parent: Mot = Mot(), val mot: String) {
		@Serializable
		@Resource("definition")
		class Definition(val parent: Id) {
			@Serializable
			@Resource("{index}")
			class Index(val parent: Definition, val index: Int)
		}
	}
}