package com.example.adoptie.service

import com.example.adoptie.dto.CreareUtilizatorDTO
import com.example.adoptie.dto.EditareUtilizatorDTO
import com.example.adoptie.dto.ForgotPasswordRequestDTO
import com.example.adoptie.dto.ResetPasswordDTO
import com.example.adoptie.dto.toEntity
import com.example.adoptie.model.PasswordResetToken
import com.example.adoptie.model.Utilizator
import com.example.adoptie.repository.AnunturiRepository
import com.example.adoptie.repository.LocalitateRepository
import com.example.adoptie.repository.PasswordResetTokenRepository
import com.example.adoptie.repository.UtilizatorRepository
import jakarta.transaction.Transactional
import org.jsoup.Jsoup
import org.jsoup.safety.Safelist
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.util.Locale.getDefault
import java.util.UUID

/**
 * Service pentru logica din spatele API-urilor specifice entitatii Utilizator
 */
@Service
class UtilizatorService(
    private val utilizatorRepository: UtilizatorRepository,
    private val passwordEncoder: PasswordEncoder,
    //private val localitateRepository: LocalitateRepository,
    private val anunturiRepository: AnunturiRepository,
    private val localitateService: LocalitateService,
    private val imagineService: ImagineService,
    private val tokenRepository: PasswordResetTokenRepository,
    private val emailService: EmailService,
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
            this.avatar = "/storage/v1/object/public/uploads/avatar.png"
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
//        val parolaEncodata = passwordEncoder.encode(dto.parola)
//        user.apply {
//            //this.email = dto.email
//            //this.parola = parolaEncodata
//            this.nume = dto.nume
//            this.localitate = localitateRepository.findById(dto.localitateId).orElse(null)
//            this.telefon = dto.telefon
//            if (avatar != null) {
//                this.avatar = saveImage(avatar)
//            }
//        }
        if (!dto.parolaVeche.isNullOrBlank() && !dto.parolaNoua.isNullOrBlank()){
            if (passwordEncoder.matches(dto.parolaVeche, user.parola)) {
                user.parola = passwordEncoder.encode(dto.parolaNoua)
            } else {
                throw IllegalArgumentException("Parola actuală este incorectă!")
            }
        }
        user.apply {
            this.nume = dto.nume
            this.telefon = dto.telefon
            if (avatar != null) {
                this.avatar = imagineService.saveImage(avatar)
            }
        }
        return utilizatorRepository.save(user)

    }

//    @Value("\${app.upload.dir}")
//    lateinit var uploadDir: String
//
//    fun saveImage(file: MultipartFile): String {
//        val dir = Paths.get(uploadDir)
//        Files.createDirectories(dir)
//
//        val filename = "${System.currentTimeMillis()}_${file.originalFilename}"
//        val target = dir.resolve(filename)
//
//        Files.write(target, file.bytes)
//
//        return "/imagini/$filename"
//    }

    /**
     * Metoda de stergere a contului utilizatorului autentificat
     */
    fun stergeContUtilizator(parolaTrimisa: String): Boolean{
        val auth = SecurityContextHolder.getContext().authentication
        val user = utilizatorRepository.findByUsername(auth.name)?: return false
        val anunturiUser = anunturiRepository.findByUtilizator_Id(user.id)

        anunturiUser.forEach { anunt ->
            imagineService.deleteImages(anunt.listaImagini)
        }
        if(user.avatar != "/storage/v1/object/public/uploads/avatar.png"){
            imagineService.deleteImage(user.avatar)
        }

        if (!passwordEncoder.matches(parolaTrimisa, user.parola)) {
            return false
        }
        utilizatorRepository.delete(user)
        return true
    }


    /**
     * Metoda de stergere a utilizatorului
     */
    fun stergereUtilizator(id: Long){
        val user = utilizatorRepository.findById(id).orElseThrow { IllegalArgumentException("Utilizatorul cu id $id nu s-a gasit.") }
        utilizatorRepository.delete(user)
    }

    @Transactional
    fun resetPassword(request: ResetPasswordDTO){
        val resetToken = tokenRepository.findValidByTokenHash(request.token) ?: throw RuntimeException("The token is incorrect!")
        val user = resetToken.user
        val hashedPassword = requireNotNull(passwordEncoder.encode(request.newPassword)) {
            "Password Encoder returned null for password reset!"
        }
        user.parola = hashedPassword
        resetToken.used = true
        utilizatorRepository.save(user)
        tokenRepository.save(resetToken)
    }

    @Transactional
    fun forgotPassword(request: ForgotPasswordRequestDTO){
        val user = utilizatorRepository.findByEmail(request.email) ?: throw RuntimeException("User not found!")
        tokenRepository.deleteByUser(user)
        val rawToken = (100000..999999).random().toString()
        val hashedToken = requireNotNull(passwordEncoder.encode(rawToken)){
            "Password Encoder returned null for token hashing"
        }
        val resetToken = PasswordResetToken(
            user = user,
            tokenHash = rawToken,
            used = false,
            createdAt = LocalDateTime.now(),
            expiresAt = LocalDateTime.now().plusMinutes(15)
        )
        tokenRepository.save(resetToken)
        emailService.sendPasswordResetEmail(user.email, rawToken)
    }

}