package fr.imacaron.motrelou.depot

import fr.imacaron.motrelou.type.TMajDefinition
import fr.imacaron.motrelou.type.TMot
import fr.imacaron.motrelou.type.TNouvelleDefinition

/**
 * @author MacaronFR
 * Interface d'interaction avec la donnée définition
 */
interface DepotDefinition {

	/**
	 * Permet d'ajouter une définition à un mot
	 * @author MacaronFR
	 * @param mot Le mot auquel ajouter une définition
	 * @param definition La definition à ajouter
	 * @return Le [TMot] avec la nouvelle définition
	 * @throws ExceptionMotIntrouvableDepot Lorsque le [mot] n'est pas trouvé
	 */
	fun ajouter(mot: String, definition: TNouvelleDefinition): TMot

	/**
	 * Permet de modifier une définition d'un mot
	 * @author MacaronFR
	 * @param mot Le mot duquel modifier la définition
	 * @param index L'index de la définititon à modifier
	 * @param definition La modification à apporter
	 * @return Le [TMot] avec la définition modifier
	 * @throws ExceptionMotIntrouvableDepot Lorsque le [mot] n'est pas trouvé
	 * @throws ExceptionDefinitionIntrouvableDepot Lorsque la définition à l'[index] n'est pas trouvé
	 */
	fun modifier(mot: String, index: Int, definition: TMajDefinition): TMot

	/**
	 * Permet de supprimer la définition d'un mot
	 * @author MacaronFR
	 * @param mot Le mot duquel supprimer la définition
	 * @param index L'index de la définition à supprimer
	 * @throws ExceptionMotIntrouvableDepot Lorsque le [mot] n'est pas trouvé
	 * @throws ExceptionDefinitionIntrouvableDepot Lorsque la définition à l'[index] n'est pas trouvé
	 */
	fun supprimer(mot: String, index: Int)
}