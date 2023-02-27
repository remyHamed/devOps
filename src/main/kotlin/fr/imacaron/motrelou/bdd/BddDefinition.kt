package fr.imacaron.motrelou.bdd

import fr.imacaron.motrelou.depot.DepotDefinition
import fr.imacaron.motrelou.depot.ExceptionDefinitionIntrouvableDepot
import fr.imacaron.motrelou.type.TMajDefinition
import fr.imacaron.motrelou.type.TMot
import fr.imacaron.motrelou.type.TNouvelleDefinition
import java.sql.Connection

/**
 * @author MacaronFR
 * Implémentation de [DepotDefinition] pour interagir avec une base de données MariaDB
 */
class BddDefinition(val mot: BddMot): DepotDefinition {
	companion object{
		/**
		 * @author MacaronFR
		 * Connection à la BDD
		 */
		val connection: Connection get() = BddMot.getConnection()
	}

	override fun ajouter(mot: String, definition: TNouvelleDefinition): TMot {
		val id = this.mot.recupererId(mot)
		val stmt = connection.prepareStatement("INSERT INTO DEFINITIONS (createur, `index`, id_mot, `definition`) VALUE (?, ?, ?, ?)")
		stmt.setString(1, definition.createur)
		stmt.setInt(2, dernierIndex(id) + 1)
		stmt.setInt(3, id)
		stmt.setString(4, definition.definition)
		stmt.executeUpdate()
		return this.mot.recuperer(mot)
	}

	override fun modifier(mot: String, index: Int, definition: TMajDefinition): TMot {
		val id = this.mot.recupererId(mot)
		val stmt = connection.prepareStatement("UPDATE DEFINITIONS set `definition` = ? WHERE id_mot = ? AND `index` = ?")
		stmt.setString(1, definition.definition)
		stmt.setInt(2, id)
		stmt.setInt(3, index)
		if(stmt.executeUpdate() == 1) {
			return this.mot.recuperer(mot)
		}else{
			throw ExceptionDefinitionIntrouvableDepot()
		}
	}

	override fun supprimer(mot: String, index: Int) {
		val id = this.mot.recupererId(mot)
		val stmt = connection.prepareStatement("DELETE FROM DEFINITIONS WHERE id_mot = ? AND `index` = ?")
		stmt.setInt(1, id)
		stmt.setInt(2, index)
		if(stmt.executeUpdate() != 1){
			throw ExceptionDefinitionIntrouvableDepot()
		}
	}

	/**
	 * Permet de récupérer l'index max des définitions du [mot]. Si le [mot] n'a pas de définition, renvoie 0
	 * @author MacaronFR
	 * @param mot L'id du mot
	 * @return Le dernier index
	 */
	private fun dernierIndex(mot: Int): Int{
		val stmt = connection.prepareStatement("SELECT MAX(`index`) as max FROM DEFINITIONS WHERE id_mot = ?")
		stmt.setInt(1, mot)
		return stmt.executeQuery().let{
			if(it.next()) {
				it.getInt("max")
			}else{
				0
			}
		}
	}
}