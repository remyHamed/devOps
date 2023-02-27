package fr.imacaron.motrelou

import fr.imacaron.motrelou.type.TDefinition
import fr.imacaron.motrelou.type.TMot
import fr.imacaron.motrelou.type.TReponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.commons.dbcp2.DriverManagerConnectionFactory
import org.apache.commons.dbcp2.PoolableConnectionFactory
import org.apache.commons.dbcp2.PoolingDriver
import org.apache.commons.pool2.impl.GenericObjectPool
import java.sql.DriverManager
import java.sql.ResultSet
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

suspend inline fun <reified T>ApplicationCall.respondJson(payload: T, code: Int = 200){
	respondText(Json.encodeToString(payload), ContentType.Application.Json, HttpStatusCode.fromValue(code))
}

suspend fun ApplicationCall.respondJson(payload: TReponse){
	respondJson(payload, payload.code)
}

fun stringToLocalDateTime(string: String): LocalDateTime = LocalDateTime.parse(string, DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"))

fun ResultSet.getTDefinition(): TDefinition = TDefinition(
	getString("definition")!!,
	getString("DEFINITIONS.createur")!!,
	stringToLocalDateTime(getString("DEFINITIONS.creation")!!),
	getInt("index")
)

fun ResultSet.getTMot(): TMot = TMot(
	getString("mot")!!,
	getString("MOTS.createur")!!,
	stringToLocalDateTime(getString("MOTS.creation")!!),
	try{
		mutableListOf(getTDefinition())
	}catch(_: NullPointerException){
		mutableListOf()
	}
)

suspend inline fun <reified T>ApplicationCall.getBodyTyped(): T{
	val t = receiveText()
	return Json.decodeFromString(t)
}

fun setupDriver(){
	//jdbc:apache:commons:dbcp:motrelou
	//val bddUrl="jdbc:mariadb://localhost:3306"
	//val bddUser="devops"
	//val bddPassword="devops"
	//val connectionFactory = DriverManagerConnectionFactory(bddUrl, bddUser, bddPassword)
	val connectionFactory = DriverManagerConnectionFactory(System.getenv("BDD_URL"), System.getenv("BDD_USER")!!, System.getenv("BDD_PASSWORD")!!)
	val poolableConnexionFactory = PoolableConnectionFactory(connectionFactory, null)
	val connexionPool = GenericObjectPool(poolableConnexionFactory)
	poolableConnexionFactory.pool = connexionPool
	Class.forName("org.apache.commons.dbcp2.PoolingDriver")
	val driver = DriverManager.getDriver("jdbc:apache:commons:dbcp:") as PoolingDriver
	driver.registerPool("motrelou", connexionPool)
}