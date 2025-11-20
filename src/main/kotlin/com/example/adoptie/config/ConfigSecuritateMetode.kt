package com.example.adoptie.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity


/**
 * Clasa ce intercepteaza anotarile de securitate pentru fiecare endpoint al controllerelor
 */
@EnableMethodSecurity
@Configuration
class ConfigSecuritateMetode
