package fr.imacaron.motrelou

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.resources.*

fun main(args: Array<String>){
	setupDriver()
	EngineMain.main(args)
}

fun Application.installCORS(){
	install(CORS){
		anyHost()
		allowMethod(HttpMethod.Head)
		allowMethod(HttpMethod.Options)
		allowMethod(HttpMethod.Get)
		allowMethod(HttpMethod.Post)
		allowMethod(HttpMethod.Put)
		allowMethod(HttpMethod.Delete)
	}
}

fun Application.installCallLogging(){
	install(CallLogging)
}

fun Application.installResources(){
	install(Resources)
}