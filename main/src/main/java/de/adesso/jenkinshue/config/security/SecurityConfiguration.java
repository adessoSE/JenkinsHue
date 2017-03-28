package de.adesso.jenkinshue.config.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
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

import de.adesso.jenkinshue.common.enumeration.Role;
import de.adesso.jenkinshue.repository.UserRepository;

/**
 * 
 * @author wennier
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(proxyTargetClass = true, prePostEnabled = true/*, securedEnabled = true*/)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Value("${ldap.server.url}")
	private String ldapUrl;
	
	@Value("${ldap.server.userSearchFilter}")
	private String userSearchFilter;
	
	@Value("${ldap.server.userSearchBase}")
	private String userSearchBase;

	@Value("${ldap.server.groupSearchBase}")
	private String groupSearchBase;

	@Value("${ldap.server.userDn}")
	private String userDn;
	
	@Value("${ldap.server.password}")
	private String password;
	
	@Autowired
	private UserAuthenticationProvider userAuthenticationProvider;
	
	@Autowired
	private UserRepository userRepository;
	
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

	@Bean
	public LdapAuthoritiesPopulator ldapAuthoritiesPopulator() {
		DefaultLdapAuthoritiesPopulator populator = new DefaultLdapAuthoritiesPopulator(contextSource(), groupSearchBase);
		populator.setSearchSubtree(true);
		populator.setIgnorePartialResultException(true);
		return populator;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(userAuthenticationProvider);
		auth.ldapAuthentication()
			.userSearchFilter(userSearchFilter)
			.userSearchBase(userSearchBase)
			.userDetailsContextMapper(new LdapGroupAuthorityMapper())
			.contextSource(contextSource())
			.ldapAuthoritiesPopulator(ldapAuthoritiesPopulator());
	}
	
	class LdapGroupAuthorityMapper implements UserDetailsContextMapper {
		
		private List<String> checkAuthorities = new ArrayList<String>();

		public LdapGroupAuthorityMapper(String... checkAuthorities) {
			for (String checkAuthority : checkAuthorities) {
				this.checkAuthorities.add(checkAuthority.toLowerCase());
			}
		}

		@Override
		public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
				Collection<? extends GrantedAuthority> authorities) {
			Set<SimpleGrantedAuthority> userAuthorities = new HashSet<>();
			
//			for (GrantedAuthority a : authorities) {
//				String userGroup = a.getAuthority().toLowerCase().substring("ROLE_".length());
//				if (checkAuthorities.contains(userGroup)) {
//					userAuthorities.add(new SimpleGrantedAuthority(a.getAuthority().replace("-", "_")));
//				}
//			}
			
			List<Role> roles = userRepository.findByLogin(username.toLowerCase()).getRoles();
			if(roles != null) {
				for(Role role : roles) {
					userAuthorities.add(new SimpleGrantedAuthority(role.toString()));
				}
			}
			
			return new User(username, "", true, true, true, true, userAuthorities);
		}

		@Override
		public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
		}

	}

	@Bean
	public BaseLdapPathContextSource contextSource() {
		DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(ldapUrl);
		contextSource.setUserDn(userDn);
		contextSource.setPassword(password);
		contextSource.afterPropertiesSet();
		return contextSource;
	}
	
	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new AuthenticationSuccessHandler() {

			@Override
			public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication aut)
					throws IOException, ServletException {
				res.setStatus(HttpServletResponse.SC_OK);
			}
		};
	}

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new AuthenticationFailureHandler() {
			@Override
			public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res,
					AuthenticationException arg2) throws IOException, ServletException {
				res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
			}
		};
	}

	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new LogoutSuccessHandler() {
			@Override
			public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				response.setStatus(HttpServletResponse.SC_OK);
			}
		};
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
