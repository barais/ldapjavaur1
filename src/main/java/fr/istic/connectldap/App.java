package fr.istic.connectldap;

import java.security.GeneralSecurityException;

import javax.net.ssl.SSLSocketFactory;

import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws LDAPException, GeneralSecurityException {

		SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
		SSLSocketFactory sslSocketFactory = sslUtil.createSSLSocketFactory();

		// Establish a secure connection using the socket factory.
		LDAPConnection c = new LDAPConnection(sslSocketFactory);
		System.err.println("app");
//		LDAPConnection c = new LDAPConnection();
		c.connect("ldap.univ-rennes1.fr", 636);

	Filter filter = Filter.createEqualityFilter("uid", "obarais");
	SearchRequest searchRequest = new SearchRequest("ou=People,dc=univ-rennes1,dc=fr", SearchScope.SUB, filter, "*");
	SearchResult searchResult;

	try
	{
		searchResult = c.search(searchRequest);
		System.err.println(searchResult.getEntryCount());

		for (SearchResultEntry entry : searchResult.getSearchEntries()) {
			for (Attribute a : entry.getAttributes()) {
				System.err.println(a.getName() + " : " + a.getValue());
			}
			
		}
	}catch(
	LDAPSearchException lse)
	{
		// The search failed for some reason.
		searchResult = lse.getSearchResult();
		ResultCode resultCode = lse.getResultCode();
		String errorMessageFromServer = lse.getDiagnosticMessage();
		lse.printStackTrace();

	}
}}
