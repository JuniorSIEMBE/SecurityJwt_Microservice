package org.sid.securityservice.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JwtManager {
	
	private AuthenticationManager authenticationManager;
	private JwtDecoder jwtDecoder;
	private JwtEncoder jwtEncoder;
	private UserDetailsService userDetailsService;
	
	public ResponseEntity<Map<String, String>> getToken(String username, String password, boolean withRefreshToken, String grantType, String refreshToken){
		Authentication authentication = null;
		String subject = null;
		String issuer = "security service";
		String scope = null;
		if(grantType.equals("password")) {
			if(Objects.isNull(username)) {
				return new ResponseEntity<Map<String,String>>(Map.of("ErrorMessage","User's name cannot be null"), HttpStatus.UNAUTHORIZED);
			}
			if(Objects.isNull(password)) {
				return new ResponseEntity<Map<String,String>>(Map.of("ErrorMessage","password cannot be null"), HttpStatus.UNAUTHORIZED);
			}
			try {
				authentication = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(username, password));
				subject = authentication.getName();
				scope = authentication.getAuthorities().stream().map(auth -> auth.getAuthority())
						.collect(Collectors.joining(" "));
			} catch (BadCredentialsException e) {
				return new ResponseEntity<Map<String,String>>(Map.of("ErrorMessage","authentication failed"), HttpStatus.UNAUTHORIZED);
			}
		}else if (grantType.equals("refreshToken")) {
			if(Objects.isNull(refreshToken)) {
				return new ResponseEntity<Map<String,String>>(Map.of("ErrorMessage","Refresh's token cannot be null"), HttpStatus.UNAUTHORIZED);
			}
			Jwt jwtDecode = jwtDecoder.decode(refreshToken);
			subject = jwtDecode.getSubject();
			UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
			Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
			scope = authorities.stream().map(auth -> auth.getAuthority()).collect(Collectors.joining(" "));
		}else {
			return new ResponseEntity<Map<String,String>>(Map.of("ErrorMessage","Invalid grant's type"), HttpStatus.UNAUTHORIZED);
		}
		Map<String, String> result= new HashMap<String, String>();
		Instant now = Instant.now();
		JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
				.subject(subject)
				.issuedAt(now)
				.expiresAt(now.plus(withRefreshToken ? 5 : 30, ChronoUnit.MINUTES))
				.issuer(issuer)
				.claim("scope", scope)
				.build();
		String jwtAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
		result.put("accessToken", jwtAccessToken);
		if(withRefreshToken) {
			JwtClaimsSet jwtClaimsSetRefresh = JwtClaimsSet.builder()
					.subject(subject)
					.issuedAt(now)
					.expiresAt(now.plus(5, ChronoUnit.MINUTES))
					.issuer(issuer)
					.build();
			String jwtRefreshToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSetRefresh)).getTokenValue();
			result.put("refreshToken", jwtRefreshToken);
		}

		return new ResponseEntity<Map<String,String>>(result, HttpStatus.OK);
	}

}
