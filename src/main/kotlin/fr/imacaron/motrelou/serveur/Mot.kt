package fr.imacaron.motrelou.serveur

import fr.imacaron.motrelou.*
import fr.imacaron.motrelou.bdd.BddDefinition
import fr.imacaron.motrelou.bdd.BddMot
import fr.imacaron.motrelou.domaine.ServiceDefinition
import fr.imacaron.motrelou.domaine.ServiceMot
import fr.imacaron.motrelou.requete.ExceptionConflit
import fr.imacaron.motrelou.requete.ExceptionMotIntrouvable
import fr.imacaron.motrelou.requete.RequetesDefinition
import fr.imacaron.motrelou.requete.RequetesMot
import fr.imacaron.motrelou.ressources.Mot
import fr.imacaron.motrelou.type.TMajMot
import fr.imacaron.motrelou.type.TNouveauMot
import fr.imacaron.motrelou.type.TReponse
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.resources.put
import io.ktor.server.resources.post
import io.ktor.server.routing.*

fun Application.mot(){
	installCORS()
	installCallLogging()
	installResources()
	val bddMot = BddMot()
	val reqMot: RequetesMot = ServiceMot(bddMot, log, 5)
	val reqDef: RequetesDefinition = ServiceDefinition(BddDefinition(bddMot), log)
	definition(reqDef)
	routing {
		get<Mot>{
			try {
				call.request.queryParameters["recherche"]?.let{
					call.respondJson(reqMot.recherche(it, call.request.queryParameters["page"]?.toInt() ?: 0))
				} ?: run{
					call.respondJson(reqMot.recherche(null, call.request.queryParameters["page"]?.toInt() ?: 0))
				}
			}catch(_: NumberFormatException){
				call.respondJson(TReponse(400, "Le numéro de page n'est pas un entier"), 400)
			}
		}
		get<Mot.Id>{
			try{
				call.respondJson(reqMot.recuperer(it.mot))
			} catch(e: ExceptionMotIntrouvable) {
				call.respondJson(TReponse.NotFound)
			}
		}
		put<Mot.Id>{
			call.getBodyTyped<TMajMot>().let { maj ->
				try{
					call.respondJson(reqMot.maj(it.mot, maj))
				}catch(e: ExceptionMotIntrouvable){
					call.respondJson(TReponse.NotFound)
				}catch(e: ExceptionConflit){
					call.respondJson(TReponse.Conflict)
				}
			}
		}
		delete<Mot.Id>{
			try{
				reqMot.supprimer(it.mot)
				call.respondJson(TReponse.NoContent)
			}catch(e: ExceptionMotIntrouvable){
				call.respondJson(TReponse.NotFound)
			}
		}
		get<Mot.Random>{
			try {
				call.respondJson(reqMot.aleatoire())
			}catch(e: ExceptionMotIntrouvable){
				call.respondJson(TReponse.NoContent)
			}
		}
		post<Mot>{
			call.getBodyTyped<TNouveauMot>().let { nouveau ->
				try{
					call.respondJson(reqMot.creer(nouveau), 201)
				} catch(e: ExceptionConflit) {
					call.respondJson(TReponse.Conflict)
				}
			}
		}
	}
}