package fr.imacaron.motrelou.requete

/**
 * @author MacaronFR
 * @param message Le message de l'erreur
 * Classe d'erreur durant la requête en cas de conflit
 */
class ExceptionConflit(message: String): Throwable(message)

/**
 * @author MacaronFR
 * @param message Le message de l'erreur
 * Classe d'erreur durant la requête quand la donnée demandée n'est pas trouvé
 */
open class ExceptionIntrouvable(message: String): Throwable(message)

/**
 * @author MacaronFR
 * Classe d'erreur durant la requête quand le mot demandé n'est pas trouvé
 */
class ExceptionMotIntrouvable: ExceptionIntrouvable("Mot introuvable")

/**
 * @author MacaronFR
 * Classe d'erreur durant la requête quand la définition demandée n'est pas trouvée
 */
class ExceptionDefinitionIntrouvable: ExceptionIntrouvable("Définition introuvable")