package com.ssu.commerce.auth.domain

import com.ssu.commerce.core.security.UserRole
import javax.persistence.*

@Entity
@Table(name = "accounts")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val accountId:Long? = null,
    @Column(nullable = false)
    val userId:String,
    @Column(nullable = false)
    val password:String,

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    var roles: MutableSet<UserRole>
)