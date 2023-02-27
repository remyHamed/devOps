package fr.imacaron.motrelou.requete

import fr.imacaron.motrelou.type.TMajMot
import fr.imacaron.motrelou.type.TMot
import fr.imacaron.motrelou.type.TNouveauMot

interface RequetesMot {
	/**
	 * Permet de rechercher une chaine dans le dictionnaire et d'afficher une certaine page de résultat. Si [recherche] est null renvoie tout les mots du dictionnaire
	 * @author MacaronFR
	 * @param recherche La chaine à rechercher, si null, renvoie tous les mots
	 * @param page La page de résultat à retourner
	 * @return Une [List] de [TMot] contenant le résultat de la recherche. La liste est vide si rien n'est trouvé
	 */
	fun recherche(recherche: String?, page: Int = 0): List<TMot>

	/**
	 * Permet de créer un mot dans le dictionnaire
	 * @author MacaronFR
	 * @param mot Le mot à créer
	 * @return Le mot créer
	 * @throws ExceptionConflit Lorsque le [mot] existe déjà dans le dictionnaire
	 */
	fun creer(mot: TNouveauMot): TMot

	/**
	 * Permet de récupérer un mot précis du dictionnaire
	 * @author MacaronFR
	 * @param mot Le mot **précis** (non sensible à la casse) à récupérer
	 * @return Le mot en question
	 * @throws ExceptionMotIntrouvable Si le [mot] n'est pas présent dans le dictionnaire
	 */
	fun recuperer(mot: String): TMot

	/**
	 * Permet de mettre à jour un [mot]
	 * @author MacaronFR
	 * @param mot Le mot exact à mettre à jour
	 * @param maj Les données à mettre à jour
	 * @return Le mot mis à jour
	 * @throws ExceptionMotIntrouvable Si le [mot] n'est pas dans le dictionnaire
	 * @throws ExceptionConflit Si le [mot] existe déjà dans le dictionnaire
	 */
	fun maj(mot: String, maj: TMajMot): TMot

	/**
	 * Permet de supprimer le [mot]
	 * @author MacaronFR
	 * @param mot Le mot **exact** à supprimer
	 * @throws ExceptionMotIntrouvable Si le [mot] n'est pas déjà dans le dictionnaire
	 */
	fun supprimer(mot: String)

	/**
	 * Permet de récupérer un mot aléatoire dans le dictionnaire
	 * @author MacaronFR
	 * @return Le mot aléatoire
	 * @throws ExceptionMotIntrouvable
	 */
	fun aleatoire(): TMot
}