package de.adesso.jenkinshue.util;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import de.adesso.jenkinshue.entity.User;
import lombok.extern.log4j.Log4j2;

/**
 * 
 * @author wennier
 *
 */
@Log4j2
@Component
public class LDAPManager implements Serializable {
	
	private static final long serialVersionUID = -9087546539808571648L;

	@Value("${ldap.server.domain}")
	private String ldapDomain;
	
	@Value("${ldap.server.userSearchBase}")
	private String userSearchBase;

	@Value("${ldap.server.userSearchFilter}")
	private String userSearchFilter;

	@Value("${ldap.server.userName}")
	private String userName;
	
	@Value("${ldap.server.password}")
	private String password;

	@Value("${ldap.server.url}")
	private String ldapUrl;
	
	private InitialDirContext createContext() throws NamingException {
		Hashtable<String, String> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, ldapUrl);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, userName + "@" + ldapDomain);
		env.put(Context.SECURITY_CREDENTIALS, password);
		return new InitialDirContext(env);
	}

	private NamingEnumeration<SearchResult> getLDAPInformation(InitialDirContext context, String login) throws NamingException {
		SearchControls cons = new SearchControls();
		cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
		cons.setDerefLinkFlag(false);
		return context.search(userSearchBase, MessageFormat.format(userSearchFilter, login), cons);
	}
	
	public User getUserForLoginName(String login) {
		try {
			InitialDirContext ctx = createContext();
			
			User user = new User();
			SearchResult next = getLDAPInformation(ctx, login).nextElement();
			user.setLogin(login);
			user.setSurname(next.getAttributes().get("sn").get().toString());
			user.setForename(next.getAttributes().get("givenName").get().toString());
			user.setEmail(next.getAttributes().get("mail").get().toString());
			
			ctx.close();
			return user;
		} catch (Exception e) {
			log.info("Login " + login + " nicht gefunden!");
		}
		return null;
	}
}
