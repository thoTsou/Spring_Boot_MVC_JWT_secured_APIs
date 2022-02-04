package com.example.demo.model

import javax.persistence.*

@Entity
class Role (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val name: String,
    @ManyToMany(fetch = FetchType.EAGER)
    val permissions: MutableList<Permission>,

//    @ManyToMany(mappedBy = "roles")
//    val users: MutableList<ApplicationUser>? = null
)