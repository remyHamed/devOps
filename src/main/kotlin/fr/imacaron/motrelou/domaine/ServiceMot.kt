package fr.imacaron.motrelou.domaine

import fr.imacaron.motrelou.depot.ExceptionConflitDepot
import fr.imacaron.motrelou.depot.DepotMot
import fr.imacaron.motrelou.depot.ExceptionMotIntrouvableDepot
import fr.imacaron.motrelou.requete.ExceptionConflit
import fr.imacaron.motrelou.requete.ExceptionMotIntrouvable
import fr.imacaron.motrelou.requete.RequetesMot
import fr.imacaron.motrelou.type.TMajMot
import fr.imacaron.motrelou.type.TMot
import fr.imacaron.motrelou.type.TNouveauMot
import org.slf4j.Logger

/**
 * @author MacaronFR
 * @param depot Le [DepotMot] pour interagir avec la donnée
 * @param logger Le [Logger] pour journaliser les différentes erreurs
 * @param limit Le max de mot à retourner dans la recherche en liste
 * Implémentation de [RequetesMot] pour la logique métier
 */
class ServiceMot(private val depot: DepotMot, private val logger: Logger, private val limit: Int = 20): RequetesMot {

	override fun recherche(recherche: String?, page: Int): List<TMot> {
		return recherche?.let{
			depot.recherche(recherche, limit, page)
		} ?: depot.recuperer(limit, page)
	}

	override fun creer(mot: TNouveauMot): TMot {
		return try{
			depot.ajouter(mot)
		}catch(e: ExceptionConflitDepot){
			logger.info("Erreur lors de la creation d'un mot: ${e.message}")
			throw ExceptionConflit("Le mot existe déjà")
		}
	}

	override fun recuperer(mot: String): TMot {
		return try{
			depot.recuperer(mot)
		}catch(e: ExceptionMotIntrouvableDepot){
			logger.info("Erreur lors de la récupération d'un mot: ${e.message}")
			throw ExceptionMotIntrouvable()
		}
	}

	override fun maj(mot: String, maj: TMajMot): TMot {
		return try {
			depot.modifier(mot, maj)
		}catch(e: ExceptionMotIntrouvableDepot){
			logger.info("Erreur lors de la modification d'un mot: ${e.message}")
			throw ExceptionMotIntrouvable()
		}
	}

	override fun supprimer(mot: String) {
		try{
			depot.supprimer(mot)
		}catch(e: ExceptionMotIntrouvableDepot){
			logger.info("Erreur lors de la supression d'un mot: ${e.message}")
			throw ExceptionMotIntrouvable()
		}
	}

	override fun aleatoire(): TMot {
		return try{
			depot.aleatoire()
		}catch(e: ExceptionMotIntrouvableDepot){
			logger.info("Erreur lors de la récupération d'un mot: ${e.message}")
			throw ExceptionMotIntrouvable()
		}
	}
}