package fr.imacaron.motrelou.depot

import fr.imacaron.motrelou.type.TMajMot
import fr.imacaron.motrelou.type.TMot
import fr.imacaron.motrelou.type.TNouveauMot

/**
 * @author
 * Interface d'interaction avec la donnée mot
 */
interface DepotMot {

	/**
	 * Recherche en fonction de la chaine de caractère [demande] et renvoie la page n°[page] en limitant à [limit] résultat.
	 * @author MacaronFR
	 * @param demande Mot à rechercher
	 * @param limit Nombre de mots max à chercher
	 * @param page Numéro de la page à récupérer
	 * @return La liste des mots correspondant à la [demande]. S'il n'y a pas de résultat, une liste vide est retournée
	 * */
	fun recherche(demande: String, limit: Int, page: Int): List<TMot>

	/**
	 * Retourne le [mot] exact
	 * @author MacaronFR
	 * @param mot Le mot exact à récupérer
	 * @return Le mot demandé
	 * @throws ExceptionMotIntrouvableDepot lorsque le [mot] n'existe pas
	 */
	fun recuperer(mot: String): TMot

	/**
	 * Récupère la [page] avec [limit] résultat
	 * @author MacaronFR
	 * @param limit Le nombre de résultat par page
	 * @param page La page à récupérer
	 * @return Une liste de mot de taille max [limit], vide si aucun résultat.
	 */
	fun recuperer(limit: Int, page: Int): List<TMot>

	/**
	 * Met à jour un mot
	 * @author MacaronFR
	 * @param mot Mot à mettre à jour
	 * @return Le mot mis à jour
	 * @throws ExceptionMotIntrouvableDepot lorsque le [mot] n'est pas dans le dépôt
	 */
	fun modifier(mot: String, maj: TMajMot): TMot

	/**
	 * Supprime un [mot] exact
	 * @author MacaronFR
	 * @param mot Mot exact à supprimer
	 * @throws ExceptionMotIntrouvableDepot si le [mot] n'est pas présent dans le dépôt
	 */
	fun supprimer(mot: String)

	/**
	 * Choisi un mot aléatoire dans la base et le renvoie
	 * @author MacaronFR
	 * @return un mot aléatoire
	 * @throws ExceptionMotIntrouvableDepot quand aucun mot n'est présent dans le dépôt.
	 */
	fun aleatoire(): TMot

	/**
	 * Ajoute un mot à la base
	 * @author MacaronFR
	 * @param [mot] Nouveau mot à ajouter
	 * @return Le mot ajouté
	 * @throws ExceptionConflitDepot lorsque qu'un conflit intervient dans le dépôt
	 */
	fun ajouter(mot: TNouveauMot): TMot
}