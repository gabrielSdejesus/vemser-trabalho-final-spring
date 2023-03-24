package br.com.dbc.javamosdecolar.secutiry;

import br.com.dbc.javamosdecolar.entity.UsuarioEntity;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;

    public String gerarToken(UsuarioEntity usuarioEncontrado) throws RegraDeNegocioException {
        String meuToken = Jwts.builder()
                .claim("login", usuarioEncontrado.getLogin())
                .claim(Claims.ID, String.valueOf(usuarioEncontrado.getIdUsuario()))
                .setIssuedAt(Date.valueOf(LocalDate.now()))
                .setExpiration(Date.valueOf(LocalDate.now().plusDays(Long.parseLong(expiration))))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        return meuToken;
    }

    public UsernamePasswordAuthenticationToken isValid(String meuToken) {
        try {
            if (meuToken == null) {
                return null;
            }
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(meuToken)
                    .getBody();
            String user = body.get(Claims.ID, String.class);
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
