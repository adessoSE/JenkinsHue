package de.adesso.jenkinshue.util;

import de.adesso.jenkinshue.config.LdapValue;
import de.adesso.jenkinshue.entity.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Hashtable;

/**
 * @author wennier
 */
@Log4j2
@Component
@ConditionalOnBean(LdapValue.class)
public class LDAPManager implements Serializable {

	private static final long serialVersionUID = -9087546539808571648L;

	@Autowired
	private LdapValue ldapValue;

	private InitialDirContext createContext() throws NamingException {
		Hashtable<String, String> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, ldapValue.getLdapUrl());
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, ldapValue.getUserName() + "@" + ldapValue.getLdapDomain());
		env.put(Context.SECURITY_CREDENTIALS, ldapValue.getPassword());
		return new InitialDirContext(env);
	}

	private NamingEnumeration<SearchResult> getLDAPInformation(InitialDirContext context, String login) throws NamingException {
		SearchControls cons = new SearchControls();
		cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
		cons.setDerefLinkFlag(false);
		return context.search(ldapValue.getUserSearchBase(), MessageFormat.format(ldapValue.getUserSearchFilter(), login), cons);
	}

	public User getUserForLoginName(String login) {
		try {
			InitialDirContext ctx = createContext();

			User user = new User();
			SearchResult next = getLDAPInformation(ctx, login.toLowerCase()).nextElement();
			user.setLogin(login.toLowerCase());
			user.setSurname(next.getAttributes().get("sn").get().toString());
			user.setForename(next.getAttributes().get("givenName").get().toString());
			user.setEmail(next.getAttributes().get("mail").get().toString().toLowerCase());

			ctx.close();
			return user;
		} catch (Exception e) {
			log.info("Login " + login + " nicht gefunden!");
		}
		return null;
	}
}
