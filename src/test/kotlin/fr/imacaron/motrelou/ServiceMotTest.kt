package fr.imacaron.motrelou

import fr.imacaron.motrelou.domaine.ServiceMot
import fr.imacaron.motrelou.requete.ExceptionConflit
import fr.imacaron.motrelou.requete.ExceptionMotIntrouvable
import fr.imacaron.motrelou.type.TMajMot
import fr.imacaron.motrelou.type.TNouveauMot
import org.junit.jupiter.api.assertThrows
import org.slf4j.LoggerFactory
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ServiceMotTest {
	private val service = ServiceMot(DepotMotMock(), LoggerFactory.getLogger("Test"))

	@Test
	fun rechercheNoMatch(){
		assertEquals(listOf(), service.recherche("Inexistant"))
	}

	@Test
	fun rechercheMatchExact(){
		assertEquals(DepotMotMock.liste.filter { it.mot == "Exact" }, service.recherche("Exact"))
	}

	@Test
	fun rechercheMatchPartiel(){
		assertEquals(DepotMotMock.liste.filter { "Partiel" in it.mot }, service.recherche("Partiel"))
	}

	@Test
	fun rechercheEmpty(){
		assertEquals(DepotMotMock.liste, service.recherche(null))
	}

	@Test
	fun recupererValid(){
		assertEquals(DepotMotMock.liste.find { it.mot == "Exact" }!!, service.recuperer("Exact"))
	}

	@Test
	fun recupererInvalid(){
		assertThrows<ExceptionMotIntrouvable>{
			service.recuperer("Invalid")
		}
	}

	@Test
	fun aleatoire(){
		assert(service.aleatoire() in DepotMotMock.liste)
	}

	@Test
	fun creer() {
		val newMot = service.creer(TNouveauMot("Add", "Denis", "Add"))
		assert(newMot in DepotMotMock.liste)
	}

	@Test
	fun creerConflit(){
		assertThrows<ExceptionConflit>{
			service.creer(TNouveauMot("Exact", "Denis", "Exact"))
		}
	}

	@Test
	fun modifier(){
		val motMaj = service.maj("Exact", TMajMot(createur = "Moi"))
		assertContains(DepotMotMock.liste, motMaj)
		assertEquals("Moi", motMaj.createur)
	}

	@Test
	fun modifierIntrouvable(){
		assertThrows<ExceptionMotIntrouvable> {
			service.maj("Introuvable", TMajMot())
		}
	}

	@Test
	fun supprimer(){
		val mot = service.creer(TNouveauMot("Add", "Denis", "Add"))
		assertContains(DepotMotMock.liste, mot)
		service.supprimer("Add")
		assertFalse(mot in DepotMotMock.liste)
	}

	@Test
	fun supprimerIntrouvable(){
		assertThrows<ExceptionMotIntrouvable> {
			service.supprimer("Inexact")
		}
	}
}