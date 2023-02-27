package fr.imacaron.motrelou.requete

import fr.imacaron.motrelou.type.TMajDefinition
import fr.imacaron.motrelou.type.TMot
import fr.imacaron.motrelou.type.TNouvelleDefinition

/**
 * @author MacaronFR
 * Interface avec la logique métier de l'API
 */
interface RequetesDefinition {

	/**
	 * Permet de créer une définition pour le [mot]
	 * @author MacaronFR
	 * @param definition La définition à créer
	 * @param mot Le mot auquel ajouter ma définition
	 * @return Un [TMot] si tout a fonctionner
	 * @throws ExceptionMotIntrouvable Si le [mot] est introuvable
	 */
	fun creer(definition: TNouvelleDefinition, mot: String): TMot

	/**
	 * Permet de mettre à jour une définition
	 * @author MacaronFR
	 * @param definition La définition à mettre à jour
	 * @param mot Le mot de la [definition] à mettre à jour
	 * @param index L'index de la [definition] à mettre à jour
	 * @return Le mot mis à jour
	 * @throws ExceptionMotIntrouvable Si le [mot] est introuvable
	 * @throws ExceptionDefinitionIntrouvable Si l'[index] de la définition est introuvable
	 */
	fun maj(definition: TMajDefinition, mot: String, index: Int): TMot

	/**
	 * Permet de supprimer une définition
	 * @author MacaronFR
	 * @param mot Le mot duquel supprimé la définition
	 * @param index L'index de la définition à supprimer
	 * @throws ExceptionMotIntrouvable Si le [mot] est introuvable
	 * @throws ExceptionDefinitionIntrouvable Si l'[index] de la définition est introuvable
	 */
	fun delete(mot: String, index: Int)
}