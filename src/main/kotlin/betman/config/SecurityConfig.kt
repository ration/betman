package betman.config

import betman.security.jwt.JwtAuthenticationFilter
import betman.security.jwt.JwtAuthorizationFilter
import betman.security.jwt.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig @Autowired constructor(private val jwtTokenProvider: JwtTokenProvider,
                                            @Qualifier("dbUser") private val userDetailsService: UserDetailsService,
                                            private val encoder: PasswordEncoder) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        // Allow api/users (signup) and anything under Angular
        http.cors().and().csrf().disable().authorizeRequests().antMatchers("/*").permitAll()
            .antMatchers("/api/users/register").permitAll().antMatchers("/api/**").authenticated().and()
            .addFilter(getJWTAuthenticationFilter()).addFilter(getJWTAuthorizationFilter()).sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }


    public override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailsService)?.passwordEncoder(encoder)
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", CorsConfiguration().applyPermitDefaultValues())
        return source
    }


    @Bean
    fun getJWTAuthenticationFilter(): JwtAuthenticationFilter {
        val filter = JwtAuthenticationFilter(authenticationManager(), jwtTokenProvider)
        filter.setFilterProcessesUrl("/api/users/login")
        return filter
    }

    @Bean
    fun getJWTAuthorizationFilter(): JwtAuthorizationFilter {
        return JwtAuthorizationFilter(authenticationManager(), jwtTokenProvider)
    }
}