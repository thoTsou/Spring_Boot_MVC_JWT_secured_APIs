package com.example.demo.model

import javax.persistence.*

@Entity
class Permission (
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val description: String,

//    @ManyToMany(mappedBy = "permissions")
//    val roles: MutableList<Role>? = null
)