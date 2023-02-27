package fr.imacaron.motrelou.domaine

import fr.imacaron.motrelou.depot.DepotDefinition
import fr.imacaron.motrelou.depot.ExceptionDefinitionIntrouvableDepot
import fr.imacaron.motrelou.depot.ExceptionIntrouvableDepot
import fr.imacaron.motrelou.depot.ExceptionMotIntrouvableDepot
import fr.imacaron.motrelou.requete.ExceptionDefinitionIntrouvable
import fr.imacaron.motrelou.requete.ExceptionIntrouvable
import fr.imacaron.motrelou.requete.ExceptionMotIntrouvable
import fr.imacaron.motrelou.requete.RequetesDefinition
import fr.imacaron.motrelou.type.TMajDefinition
import fr.imacaron.motrelou.type.TMot
import fr.imacaron.motrelou.type.TNouvelleDefinition
import org.slf4j.Logger

/**
 * Implémentation de [RequetesDefinition] pour la logique métier de l'API
 * @author MacaronFR
 * @param depot Le [DepotDefinition] pour interagir avec le dépôt de donnée
 * @param logger [Logger] pour journaliser toutes les actions
 */
class ServiceDefinition(private val depot: DepotDefinition, private val logger: Logger): RequetesDefinition {
	override fun creer(definition: TNouvelleDefinition, mot: String): TMot {
		return try {
			depot.ajouter(mot, definition)
		} catch(e: ExceptionMotIntrouvableDepot) {
			logger.info("Erreur lors de l'ajout d'une définition: ${e.message}")
			throw ExceptionMotIntrouvable()
		}
	}

	override fun maj(definition: TMajDefinition, mot: String, index: Int): TMot {
		return try {
			depot.modifier(mot, index, definition)
		} catch(e: ExceptionIntrouvableDepot) {
			logger.info("Erreur lors de la modification d'une définition: ${e.message}")
			throw when(e){
				is ExceptionMotIntrouvableDepot -> ExceptionMotIntrouvable()
				is ExceptionDefinitionIntrouvableDepot -> ExceptionDefinitionIntrouvable()
				else -> ExceptionIntrouvable(e.message ?: "")
			}
		}
	}

	override fun delete(mot: String, index: Int) {
		return try {
			depot.supprimer(mot, index)
		}catch(e: ExceptionIntrouvableDepot) {
			logger.info("Erreur lors de la supression d'une définition: ${e.message}")
			throw when(e){
				is ExceptionMotIntrouvableDepot -> ExceptionMotIntrouvable()
				is ExceptionDefinitionIntrouvableDepot -> ExceptionDefinitionIntrouvable()
				else -> ExceptionIntrouvable(e.message ?: "")
			}
		}
	}

}