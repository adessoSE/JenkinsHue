package de.adesso.jenkinshue.config.security;

import de.adesso.jenkinshue.common.dto.user.UserDTO;
import de.adesso.jenkinshue.common.enumeration.Role;
import de.adesso.jenkinshue.common.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author wennier
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(proxyTargetClass = true, prePostEnabled = true/*, securedEnabled = true*/)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final UserAuthenticationProvider userAuthenticationProvider;

	@Autowired(required = false)
	private LdapAuthentication ldapAuthentication;

	@Autowired
	public SecurityConfiguration(UserAuthenticationProvider userAuthenticationProvider) {
		this.userAuthenticationProvider = userAuthenticationProvider;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http// .csrf().disable()

		.headers().cacheControl().disable().and().exceptionHandling()
				.authenticationEntryPoint(new RestAuthenticationEntryPoint("/")).and()

		/* old: "/bootstrap/**", "/libraries/**", "/scripts/**", "/styles/**" */
		.authorizeRequests().antMatchers("/", "/login", "/wro/**", "/images/**", "/views/**").permitAll().and()

		.authorizeRequests().anyRequest().authenticated().and()

		.formLogin().loginProcessingUrl("/login").successHandler(authenticationSuccessHandler())
				.failureHandler(authenticationFailureHandler()).and()

		.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler())
				.deleteCookies("JSESSIONID", "XSRF-TOKEN").invalidateHttpSession(true).and()

		.csrf().csrfTokenRepository(csrfTokenRepository()).and().addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
	}

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}

	private OncePerRequestFilter csrfHeaderFilter() {
		return new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
					FilterChain filterChain) throws ServletException, IOException {
				CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
				if (csrf != null) {
					Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
					String token = csrf.getToken();
					if (cookie == null || token != null && !token.equals(cookie.getValue())) {
						cookie = new Cookie("XSRF-TOKEN", token);
						cookie.setPath("/");
						response.addCookie(cookie);
					}
				}
				filterChain.doFilter(request, response);
			}
		};
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//noinspection ConstantConditions
		if(ldapAuthentication != null) {
			auth.authenticationProvider(userAuthenticationProvider);
			ldapAuthentication.enable(auth);
		} else {
			auth.userDetailsService(userDetailsService());
		}
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return userName -> {
            UserDTO user = userAuthenticationProvider.userService.findByLogin(userName.toLowerCase());
            if (user == null) {
                throw new UsernameNotFoundException(userName);
            }

            Set<SimpleGrantedAuthority> userAuthorities = new HashSet<>();
            List<Role> roles = user.getRoles();
            if (roles != null) {
                for (Role role : roles) {
                    userAuthorities.add(new SimpleGrantedAuthority(role.toString()));
                }
            }

            return new User(userName, userName /* TODO use password */, userAuthorities);
        };
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return (req, res, aut) -> res.setStatus(HttpServletResponse.SC_OK);
	}

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return (req, res, arg2) -> res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
	}

	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return (request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK);
	}

	class RestAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

		public RestAuthenticationEntryPoint(String loginURL) {
			super(loginURL);
		}

		@Override
		public void commence(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException authException) throws IOException, ServletException {
			if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
			} else {
				super.commence(request, response, authException);
			}
		}

	}

}
