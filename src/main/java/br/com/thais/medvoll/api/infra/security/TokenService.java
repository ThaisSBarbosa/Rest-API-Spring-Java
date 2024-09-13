package br.com.thais.medvoll.api.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;

import br.com.thais.medvoll.api.domain.usuario.Usuario;

@Service
public class TokenService {
	
	private static final String CONST_ISSUER = "API Voll.med";
	
	@Value("${api.security.token.secret}")
	private String secret;
	
	public String gerarToken(Usuario usuario) {
		try {
		    Algorithm algorithm = Algorithm.HMAC256(secret);
		    return JWT.create()
		           .withIssuer(CONST_ISSUER)
		           .withSubject(usuario.getLogin())
		           .withExpiresAt(dataExpiracao())
		           .sign(algorithm);
		} catch (JWTCreationException exception){
		    throw new RuntimeException("Erro ao gerar token JWT", exception);
		}
	}
	
	public String getSubject(String tokenJwt) {
		try {
		    Algorithm algorithm = Algorithm.HMAC256(secret);
		    return JWT.require(algorithm)
		    	   .withIssuer(CONST_ISSUER)
		           .build()
		           .verify(tokenJwt)
		           .getSubject();
		} catch (JWTVerificationException exception){
		    throw new RuntimeException("Token JWT inválido ou expirado!", exception);
		}
	}

	private Instant dataExpiracao() {
		return LocalDateTime.now().plusHours(5).toInstant(ZoneOffset.of("-03:00"));
	}
}
