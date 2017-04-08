package de.adesso.jenkinshue.config.security;

import de.adesso.jenkinshue.common.enumeration.Role;
import de.adesso.jenkinshue.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import java.util.*;

/**
 *
 * @author wennier
 *
 */
@Configuration
public class LdapAuthentication {

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
    private UserRepository userRepository;

    public void enable(AuthenticationManagerBuilder auth) throws Exception {
        auth.ldapAuthentication()
                .userSearchFilter(userSearchFilter)
                .userSearchBase(userSearchBase)
                .userDetailsContextMapper(new LdapAuthentication.LdapGroupAuthorityMapper())
                .contextSource(contextSource())
                .ldapAuthoritiesPopulator(ldapAuthoritiesPopulator());
    }

    @Bean
    public LdapAuthoritiesPopulator ldapAuthoritiesPopulator() {
        DefaultLdapAuthoritiesPopulator populator = new DefaultLdapAuthoritiesPopulator(contextSource(), groupSearchBase);
        populator.setSearchSubtree(true);
        populator.setIgnorePartialResultException(true);
        return populator;
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

}
