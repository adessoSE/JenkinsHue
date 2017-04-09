package de.adesso.jenkinshue.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static de.adesso.jenkinshue.config.LdapValue.*;

/**
 * @author wennier
 */
@Getter
@Component
@ConditionalOnProperty(name = {LDAP_DOMAIN, LDAP_URL, LDAP_USER_SEARCH_FILTER, LDAP_USER_SEARCH_BASE, LDAP_GROUP_SEARCH_BASE, LDAP_USER_DN, LDAP_USER_NAME, LDAP_PASSWORD})
public class LdapValue {

	public static final String LDAP_DOMAIN = "ldap.server.domain";
	public static final String LDAP_URL = "ldap.server.url";
	public static final String LDAP_USER_SEARCH_FILTER = "ldap.server.userSearchFilter";
	public static final String LDAP_USER_SEARCH_BASE = "ldap.server.userSearchBase";
	public static final String LDAP_GROUP_SEARCH_BASE = "ldap.server.groupSearchBase";
	public static final String LDAP_USER_DN = "ldap.server.userDn";
	public static final String LDAP_USER_NAME = "ldap.server.userName";
	public static final String LDAP_PASSWORD = "ldap.server.password";

	@Value("${" + LDAP_DOMAIN + "}")
	private String ldapDomain;

	@Value("${" + LDAP_URL + "}")
	private String ldapUrl;

	@Value("${" + LDAP_USER_SEARCH_FILTER + "}")
	private String userSearchFilter;

	@Value("${" + LDAP_USER_SEARCH_BASE + "}")
	private String userSearchBase;

	@Value("${" + LDAP_GROUP_SEARCH_BASE + "}")
	private String groupSearchBase;

	@Value("${" + LDAP_USER_DN + "}")
	private String userDn;

	@Value("${" + LDAP_USER_NAME + "}")
	private String userName;

	@Value("${" + LDAP_PASSWORD + "}")
	private String password;

}
