package org.sid.securityservice.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

@Data
@ConfigurationProperties(prefix = "rsa")
public class RSAKeyConfig {
	
	private RSAPrivateKey privateKey;
	
	private RSAPublicKey publicKey;

}
