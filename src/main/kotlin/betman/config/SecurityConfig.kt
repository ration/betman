package betman.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
             http.csrf().disable()
//        http.authorizeRequests().antMatchers("/betting/*").hasAnyRole("ADMIN","USER").
//                and().formLogin()
    }

    @Autowired
    fun registerAuth(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication().withUser("betman").password("manbet").roles("ADMIN")
    }
}