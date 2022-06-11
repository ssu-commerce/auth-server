package com.ssu.commerce.auth.domain

import com.ssu.commerce.core.security.UserRole
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "accounts")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val accountId: Long? = null,
    @Column(nullable = false)
    val userId: String,
    @Column(nullable = false)
    val password: String,

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    var roles: MutableSet<UserRole>
)
