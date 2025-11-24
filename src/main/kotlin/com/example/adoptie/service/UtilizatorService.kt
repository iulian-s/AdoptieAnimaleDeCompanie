package com.example.adoptie.service

import com.example.adoptie.dto.CreareUtilizatorDTO
import com.example.adoptie.dto.EditareUtilizatorDTO
import com.example.adoptie.dto.toEntity
import com.example.adoptie.model.Utilizator
import com.example.adoptie.repository.AnunturiRepository
import com.example.adoptie.repository.LocalitateRepository
import com.example.adoptie.repository.UtilizatorRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.Locale.getDefault

/**
 * Service pentru logica din spatele API-urilor specifice entitatii Utilizator
 */
@Service
class UtilizatorService(
    private val utilizatorRepository: UtilizatorRepository,
    private val passwordEncoder: PasswordEncoder,
    private val localitateRepository: LocalitateRepository,
    private val anunturiRepository: AnunturiRepository,
    private val localitateService: LocalitateService,
) {
    /**
     * Metoda de inregistrare a utilizatorului - creare cont
     */
    fun inregistrare(dto: CreareUtilizatorDTO): Utilizator {
        if(utilizatorRepository.findByUsername(dto.username) != null) throw IllegalArgumentException("Username luat")

        val parolaEncodata = passwordEncoder.encode(dto.parola)

        val localitate = localitateService.getLocalitateByJudetAndNume(dto.judet, dto.localitate)
        val user = dto.toEntity(localitate).apply {
            this.parola = parolaEncodata
            this.nume = dto.nume.ifBlank { dto.username.replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() }.replace("[^A-Za-z]".toRegex(), "")  }
            this.avatar = "/imagini/avatar.png"
        }
        return(utilizatorRepository.save(user))
    }

    /**
     * Metoda de listare a tuturor utilizatorilor
     */
    fun listareUtilizatori(): List<Utilizator> = utilizatorRepository.findAll()

    fun citireUtilizatorById(id: Long): Utilizator =
        utilizatorRepository.findById(id).orElseThrow { IllegalArgumentException("Utilizatorul cu id $id nu exista!") }


    /**
     * Metoda de intoarcere a informatiilor utilizatorului autentificat
     */
    fun infoUtilizator(): Utilizator{
        val auth = SecurityContextHolder.getContext().authentication
        val user = utilizatorRepository.findByUsername(auth.name)?: throw IllegalArgumentException("Nu am gasit informatii pentru utilizatorul ${auth.name}")
        val anunturi = anunturiRepository.findByUtilizator_Id(user.id).toMutableList()
        user.anunturi = anunturi
        return user
    }

    /**
     * Metoda de editare a informatiilor utilizatorului autentificat
     */
    fun editInfoUtilizator(dto: EditareUtilizatorDTO):Utilizator{
        val auth = SecurityContextHolder.getContext().authentication
        val user = utilizatorRepository.findByUsername(auth.name)?: throw IllegalArgumentException("Nu am gasit informatii pentru utilizatorul ${auth.name}")
        return actualizareUtilizator(user.id, dto)
    }

    /**
     * Metoda de stergere a contului utilizatorului autentificat
     */
    fun stergeContUtilizator(){
        val auth = SecurityContextHolder.getContext().authentication
        val user = utilizatorRepository.findByUsername(auth.name)?: throw IllegalArgumentException("Nu am gasit informatii pentru utilizatorul ${auth.name}")
        utilizatorRepository.delete(user)
    }

    /**
     * Metoda de actualizare a informatiilor unui utilizator
     */
    fun actualizareUtilizator(id: Long, dto: EditareUtilizatorDTO): Utilizator {
        val utilizator = utilizatorRepository.findById(id).orElseThrow { IllegalArgumentException("Utilizatorul cu id $id nu s-a gasit.") }
        val parolaEncodata = passwordEncoder.encode(dto.parola)
        utilizator.apply {
            this.username = dto.username
            this.parola = dto.parola
            this.email = dto.email
            this.parola = parolaEncodata
            this.rol = dto.rol
            this.nume = dto.nume
            this.localitate = localitateRepository.findById(dto.localitateId).orElse(null)
            this.telefon = dto.telefon
            this.avatar = dto.avatar
            this.anunturi = this.anunturi
        }
        return utilizatorRepository.save(utilizator)
    }

    /**
     * Metoda de stergere a utilizatorului
     */
    fun stergereUtilizator(id: Long){
        val user = utilizatorRepository.findById(id).orElseThrow { IllegalArgumentException("Utilizatorul cu id $id nu s-a gasit.") }
        utilizatorRepository.delete(user)
    }
}