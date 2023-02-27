package fr.imacaron.motrelou

import fr.imacaron.motrelou.depot.DepotMot
import fr.imacaron.motrelou.depot.ExceptionConflitDepot
import fr.imacaron.motrelou.depot.ExceptionMotIntrouvableDepot
import fr.imacaron.motrelou.type.TDefinition
import fr.imacaron.motrelou.type.TMajMot
import fr.imacaron.motrelou.type.TMot
import fr.imacaron.motrelou.type.TNouveauMot
import java.time.LocalDateTime

class DepotMotMock: DepotMot{
	companion object {
		val liste = mutableListOf(
			TMot("Exact", "Denis", LocalDateTime.now(), mutableListOf(TDefinition("Définition", "Denis", LocalDateTime.now(), 1))),
			TMot("Partiel1", "Denis", LocalDateTime.now(), mutableListOf(TDefinition("Définition", "Denis", LocalDateTime.now(), 1))),
			TMot("Partiel2", "Denis", LocalDateTime.now(), mutableListOf(TDefinition("Définition", "Denis", LocalDateTime.now(), 1))),
			TMot("Partiel3", "Denis", LocalDateTime.now(), mutableListOf(TDefinition("Définition", "Denis", LocalDateTime.now(), 1))),
			TMot("Partiel4", "Denis", LocalDateTime.now(), mutableListOf(TDefinition("Définition", "Denis", LocalDateTime.now(), 1))),
			TMot("1Partiel", "Denis", LocalDateTime.now(), mutableListOf(TDefinition("Définition", "Denis", LocalDateTime.now(), 1)))
		)

		val date = LocalDateTime.of(2022, 5, 8, 14, 45, 58)
	}

	override fun recherche(demande: String, limit: Int, page: Int): List<TMot> = recuperer(limit, page).filter { demande in it.mot }

	override fun recuperer(mot: String): TMot = liste.find { it.mot == mot } ?: throw ExceptionMotIntrouvableDepot()

	override fun recuperer(limit: Int, page: Int): List<TMot> =
		if(limit >= liste.size){
			liste
		}else if(limit * page >= liste.size){
			emptyList()
		}else if(limit * (page+1) >= liste.size){
			liste.subList(limit * page, liste.size - 1)
		}else {
			liste.subList(limit * page, limit * (page + 1))
		}

	override fun modifier(mot: String, maj: TMajMot): TMot = liste.find { it.mot == mot }?.apply {
		maj.createur?.let {
			createur = it
		}
	} ?: throw ExceptionMotIntrouvableDepot()

	override fun supprimer(mot: String) {
		if(!liste.removeIf { it.mot == mot }){
			throw ExceptionMotIntrouvableDepot()
		}
	}

	override fun aleatoire(): TMot = liste.random()

	override fun ajouter(mot: TNouveauMot): TMot {
		try {
			recuperer(mot.mot)
			throw ExceptionConflitDepot("Le mot existe déjà")
		}catch(e: ExceptionMotIntrouvableDepot){
			return TMot(
				mot.mot,
				mot.createur,
				date,
				mutableListOf(TDefinition(mot.definition, mot.createur, date, 1))
			).apply { liste.add(this) }
		}
	}
}