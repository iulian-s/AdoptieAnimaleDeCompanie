package com.example.adoptie.service

import com.example.adoptie.dto.CreareUtilizatorDTO
import com.example.adoptie.dto.EditareUtilizatorDTO
import com.example.adoptie.dto.toEntity
import com.example.adoptie.model.Utilizator
import com.example.adoptie.repository.AnunturiRepository
import com.example.adoptie.repository.LocalitateRepository
import com.example.adoptie.repository.UtilizatorRepository
import org.jsoup.Jsoup
import org.jsoup.safety.Safelist
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
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
            this.email = Jsoup.clean(dto.email.trim(), Safelist.none())
            this.username = Jsoup.clean(dto.username.trim(), Safelist.none())
            this.nume = Jsoup.clean(dto.nume.trim(), Safelist.none())
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
    fun editInfoUtilizator(dto: EditareUtilizatorDTO, avatar: MultipartFile?):Utilizator{
        val auth = SecurityContextHolder.getContext().authentication
        val user = utilizatorRepository.findByUsername(auth.name)?: throw IllegalArgumentException("Nu am gasit informatii pentru utilizatorul ${auth.name}")
//        val localitate = localitateRepository.findById(dto.localitateId).orElseThrow{IllegalArgumentException("Localitatea cu id ${dto.localitateId} nu exista!")}
//        val anunturi = anunturiRepository.findByUtilizator_Id(user.id).toMutableList()
//        val utilizator = dto.toEntity(localitate, anunturi)
//        val oldAvatar = user.avatar
//
        val parolaEncodata = passwordEncoder.encode(dto.parola)
        user.apply {
            this.email = dto.email
            this.parola = parolaEncodata
            this.nume = dto.nume
            this.localitate = localitateRepository.findById(dto.localitateId).orElse(null)
            this.telefon = dto.telefon
            if (avatar != null) {
                this.avatar = saveImage(avatar)
            }
        }
        return utilizatorRepository.save(user)

    }

    @Value("\${app.upload.dir}")
    lateinit var uploadDir: String

    fun saveImage(file: MultipartFile): String {
        val dir = Paths.get(uploadDir)
        Files.createDirectories(dir)

        val filename = "${System.currentTimeMillis()}_${file.originalFilename}"
        val target = dir.resolve(filename)

        Files.write(target, file.bytes)

        return "/imagini/$filename"
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
     * Metoda de stergere a utilizatorului
     */
    fun stergereUtilizator(id: Long){
        val user = utilizatorRepository.findById(id).orElseThrow { IllegalArgumentException("Utilizatorul cu id $id nu s-a gasit.") }
        utilizatorRepository.delete(user)
    }
}