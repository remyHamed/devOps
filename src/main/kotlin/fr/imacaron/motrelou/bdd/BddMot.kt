package fr.imacaron.motrelou.bdd

import fr.imacaron.motrelou.depot.DepotMot
import fr.imacaron.motrelou.depot.ExceptionConflitDepot
import fr.imacaron.motrelou.depot.ExceptionMotIntrouvableDepot
import fr.imacaron.motrelou.getTDefinition
import fr.imacaron.motrelou.getTMot
import fr.imacaron.motrelou.type.TMajMot
import fr.imacaron.motrelou.type.TMot
import fr.imacaron.motrelou.type.TNouveauMot
import org.mariadb.jdbc.Driver
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLIntegrityConstraintViolationException
import java.util.Random

/**
 * Implémentation de [DepotMot] pour interagir avec une base de donné MySQL
 * @author MacaronFR
 */
class BddMot: DepotMot {

	companion object {
		/**
		 * Connection à la base de donnée
		 * @author MacaronFR
		 */
		private var connection: Connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:motrelou", "devops","devops")

		/**
		 * Fonction permettant de récupérer une connexion et de la renouveler en cas d'invalidation de la connexion
		 * @author MacaronFR
		 * @return La connection **valide**
		 */
		fun getConnection(): Connection{
			if(!connection.isValid(10)){
				connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:motrelou", "devops","devops")
			}
			return connection
		}

		/**
		 * Les champs minium à récupérer lors d'une requête
		 * @author MacaronFR
		 */
		const val fields = "mot, MOTS.creation, MOTS.createur, definition, DEFINITIONS.creation, DEFINITIONS.createur, `index`"
	}

	override fun recherche(demande: String, limit: Int, page: Int): List<TMot> {
		val stmt = getConnection().prepareStatement("SELECT $fields FROM MOTS LEFT JOIN DEFINITIONS ON DEFINITIONS.id_mot = MOTS.id_mot WHERE mot LIKE ? LIMIT ${page*limit}, $limit")
		stmt.setString(1, "%$demande%")
		return stmt.executeQuery().extraireListeMot()
	}

	override fun recuperer(mot: String): TMot {
		val stmt = getConnection().prepareStatement("SELECT $fields FROM MOTS LEFT JOIN DEFINITIONS ON DEFINITIONS.id_mot = MOTS.id_mot WHERE mot = ?")
		println(mot)
		stmt.setString(1, mot)
		return stmt.executeQuery().extraireMot() ?: throw ExceptionMotIntrouvableDepot()
	}

	override fun recuperer(limit: Int, page: Int): List<TMot> {
		val stmt = getConnection().createStatement()
		return stmt.executeQuery("SELECT mot, MOTS.creation, MOTS.createur, definition, DEFINITIONS.creation, DEFINITIONS.createur, `index` FROM MOTS LEFT JOIN DEFINITIONS ON DEFINITIONS.id_mot = MOTS.id_mot LIMIT ${page*limit}, $limit").extraireListeMot()
	}

	override fun modifier(mot: String, maj: TMajMot): TMot {
		val preMot = recuperer(mot)
		val stmt = getConnection().prepareStatement("UPDATE MOTS SET mot = ?, createur = ? WHERE mot = ?")
		stmt.setString(1, maj.mot ?: preMot.mot)
		stmt.setString(2, maj.createur ?: preMot.createur)
		stmt.setString(3, mot)
		stmt.executeUpdate()
		return recuperer(maj.mot ?: mot)
	}

	override fun supprimer(mot: String) {
		val stmt = getConnection().prepareStatement("DELETE FROM MOTS WHERE mot = ?")
		stmt.setString(1, mot)
		if(stmt.executeUpdate() == 0){
			throw ExceptionMotIntrouvableDepot()
		}
	}

	/**
	 * Retourne le nombre de mots en base
	 * @author MacaronFR
	 * @return Total de mots
	 */
	private fun compte(): Int{
		return getConnection().createStatement().executeQuery("SELECT COUNT(id_mot) as tot FROM MOTS").let {
			it.next()
			it.getInt("tot")
		}
	}

	override fun aleatoire(): TMot {
		var res = getConnection().createStatement().executeQuery("SELECT id_mot FROM MOTS LIMIT ${Random().nextInt(0, compte())}, 1")
		return if(res.next()){
			val id = res.getInt("id_mot")
			res = getConnection().createStatement().executeQuery("SELECT $fields FROM MOTS LEFT JOIN DEFINITIONS ON MOTS.id_mot = DEFINITIONS.id_mot WHERE MOTS.id_mot = $id")
			res.extraireMot() ?: throw ExceptionMotIntrouvableDepot()
		}else{
			throw ExceptionMotIntrouvableDepot()
		}
	}

	override fun ajouter(mot: TNouveauMot): TMot {
		var stmt = getConnection().prepareStatement("INSERT INTO MOTS (mot, createur) VALUES (?, ?)")
		stmt.setString(1, mot.mot)
		stmt.setString(2, mot.createur)
		try {
			stmt.executeUpdate()
		}catch(_: SQLIntegrityConstraintViolationException){
			throw ExceptionConflitDepot("Le mot existe déjà")
		}
		val idMot = recupererId(mot.mot)
		stmt = getConnection().prepareStatement("INSERT INTO DEFINITIONS (definition, createur, id_mot, `index`) VALUES (?, ?, ?, 1)")
		stmt.setString(1, mot.definition)
		stmt.setString(2, mot.createur)
		stmt.setInt(3, idMot)
		stmt.execute()
		stmt = getConnection().prepareStatement("SELECT $fields, MOTS.id_mot FROM MOTS LEFT JOIN DEFINITIONS ON MOTS.id_mot = DEFINITIONS.id_mot WHERE mot = ?")
		stmt.setString(1, mot.mot)
		return stmt.executeQuery().extraireMot()!!
	}

	/**
	 * Permet de récupérer l'id en base du [mot]
	 * @author MacaronFR
	 * @param mot Le mot dont on veut l'id
	 * @return L'id du [mot]
	 * @throws ExceptionMotIntrouvableDepot Lorsque le [mot] n'existe pas en base
	 */
	fun recupererId(mot: String): Int{
		val stmt = getConnection().prepareStatement("SELECT id_mot FROM MOTS WHERE mot = ?")
		stmt.setString(1, mot)
		return stmt.executeQuery().let{
			if(it.next()) {
				it.getInt("id_mot")
			}else{
				throw ExceptionMotIntrouvableDepot()
			}
		}
	}

	/**
	 * Permet d'extraire les données d'un mot à partir d'un [ResultSet]. Renvoie **null** si aucun mot n'est présent
	 * @author MacaronFR
	 * @return Le mot et ses définitions, **null** si le [ResultSet] est vide
	 */
	private fun ResultSet.extraireMot(): TMot?{
		return if(next()){
			val mot = getTMot()
			while(next()){
				mot.definitions.add(getTDefinition())
			}
			mot
		}else{
			null
		}
	}

	/**
	 * Permet d'extraire une liste de mots à partir d'un [ResultSet]. Renvoie une liste vide si aucun mot n'est présent
	 * @author MacaronFR
	 * @return La liste des mots présents dans le [ResultSet]
	 */
	private fun ResultSet.extraireListeMot(): List<TMot>{
		val list = mutableListOf<TMot>()
		while(next()){
			list.find { it.mot == getString("mot")!! }?.let{ mot ->
				getString("definition")?.let{
					mot.definitions.add(getTDefinition())
				}
			} ?: list.add(getTMot())
		}
		return list
	}
}