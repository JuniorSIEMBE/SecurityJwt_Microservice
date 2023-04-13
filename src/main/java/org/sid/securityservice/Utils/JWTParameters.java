package org.sid.securityservice.Utils;

public class JWTParameters {
	
	public static final String SECRET = "MySecret1234567890°+";
	
	public static final int EXPIRATION_ACCESS_TOKEN = 15*60*1000;
	
	public static final int EXPIRATION_REFRESH_TOKEN = 360*24*60*60*1000;
	
	public static final String PREFIX = "Baerer ";
	
	public static final String HEADER = "Authorization";

}
