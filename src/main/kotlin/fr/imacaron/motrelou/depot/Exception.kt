package fr.imacaron.motrelou.depot

/**
 * @author MacaronFR
 * @param message Le message de l'erreur
 * Classe d'erreur dans l'interaction avec le depot en cas de conflit
 */
class ExceptionConflitDepot(message: String): Throwable(message)

/**
 * @author MacaronFR
 * @param message Le message de l'erreur
 * Classe d'erreur dans l'interaction avec le depot quand la donnée demandée n'est pas trouvée
 */
open class ExceptionIntrouvableDepot(message: String): Throwable(message)

/**
 * @author MacaronFR
 * Classe d'erreur dans l'interaction avec le dépôt quand le mot demandé n'est pas trouvé
 */
class ExceptionMotIntrouvableDepot: ExceptionIntrouvableDepot("Mot introuvable")

/**
 * @author MacaronFR
 * Classe d'erreur dans l'interaction avec le dépôt quand la définition demandée n'est pas trouvée
 */
class ExceptionDefinitionIntrouvableDepot: ExceptionIntrouvableDepot("Définition introuvable")