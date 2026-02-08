package com.example.adoptie.config

import com.example.adoptie.model.Utilizator
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * Clasa de tip adaptor pentru spring security care imi stocheaza
 * id
 * username
 * parola
 * autorizari pentru gestiunea obiectelor in functie de rol
 */
class DetaliiUtilizator(private val user: Utilizator): UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority("ROLE_${user.rol.name}"))

    override fun getUsername(): String = user.username
    override fun getPassword(): String = user.parola
    fun getId(): Long = user.id
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true

}