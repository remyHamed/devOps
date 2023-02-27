package fr.imacaron.motrelou.serveur

import fr.imacaron.motrelou.getBodyTyped
import fr.imacaron.motrelou.requete.ExceptionIntrouvable
import fr.imacaron.motrelou.requete.ExceptionMotIntrouvable
import fr.imacaron.motrelou.requete.RequetesDefinition
import fr.imacaron.motrelou.respondJson
import fr.imacaron.motrelou.ressources.Mot
import fr.imacaron.motrelou.type.TReponse
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.resources.put
import io.ktor.server.resources.post
import io.ktor.server.routing.*

fun Application.definition(reqDef: RequetesDefinition){
	routing {
		post<Mot.Id.Definition>{
			try{
				call.respondJson(reqDef.creer(call.getBodyTyped(), it.parent.mot), 201)
			}catch(e: ExceptionMotIntrouvable){
				call.respondJson(TReponse.NotFound)
			}
		}
		put<Mot.Id.Definition.Index>{
			try{
				call.respondJson(reqDef.maj(call.getBodyTyped(), it.parent.parent.mot, it.index))
			} catch(e: ExceptionIntrouvable) {
				call.respondJson(TReponse.NotFound)
			}
		}
		delete<Mot.Id.Definition.Index>{
			try{
				reqDef.delete(it.parent.parent.mot, it.index)
				call.respondJson(TReponse.NoContent)
			} catch(e: ExceptionIntrouvable){
				call.respondJson(TReponse.NotFound)
			}
		}
	}
}