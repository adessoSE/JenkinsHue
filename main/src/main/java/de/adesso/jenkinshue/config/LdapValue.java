package de.adesso.jenkinshue.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author wennier
 */
@Getter
@Component
public class LdapValue {

	@Value("${ldap.server.domain}")
	private String ldapDomain;

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

	@Value("${ldap.server.userName}")
	private String userName;

	@Value("${ldap.server.password}")
	private String password;

}
