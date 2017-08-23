package com.zm.gateway.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 
 * ClassName: JWTUtil <br/>
 * Function: JWT工具包. <br/>
 * date: Aug 21, 2017 6:43:48 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class JWTUtil {
	
	private final static String SECRET = "zmteambuilder";

	public final static String USER_NAME = "username";

	public final static String PASSWORD = "password";

	public static String generateToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).setExpiration(DateUtil.getDateAfter(new Date(), 365))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
	}

	private static Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	public static String getUsernameFromToken(String authToken) {
		return (String) getClaimsFromToken(authToken).get(USER_NAME);
	}

	public static boolean validateToken(String authToken, UserDetails userDetails) {
		if (authToken == null) {
			return false;
		}

		Map<String, Object> claim = new HashMap<String, Object>();
		claim.put(PASSWORD, userDetails.getPassword());
		claim.put(USER_NAME, userDetails.getUsername());

		return authToken.equals(generateToken(claim));
	}

}
