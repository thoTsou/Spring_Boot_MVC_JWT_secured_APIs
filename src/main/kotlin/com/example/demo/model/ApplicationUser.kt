package com.example.demo.model


import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
class ApplicationUser (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private val id: Long? = null,
    //private val name: String,

    // userdetails from spring security
    private val username: String,
    private val password: String,
    private val isAccountNonExpired: Boolean,
    private val isAccountNonLocked: Boolean,
    private val isCredentialsNonExpired: Boolean,
    private val isEnabled: Boolean,
    @ElementCollection
    var grantedAuthorities: MutableSet<GrantedAuthority>,
    // end
    @ManyToMany(fetch = FetchType.EAGER)
    val roles: MutableList<Role>,
): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = this.grantedAuthorities

    override fun getPassword(): String = this.password

    override fun getUsername(): String = this.username

    override fun isAccountNonExpired(): Boolean = this.isAccountNonExpired

    override fun isAccountNonLocked(): Boolean = this.isAccountNonLocked

    override fun isCredentialsNonExpired(): Boolean = this.isCredentialsNonExpired

    override fun isEnabled(): Boolean = this.isEnabled

}