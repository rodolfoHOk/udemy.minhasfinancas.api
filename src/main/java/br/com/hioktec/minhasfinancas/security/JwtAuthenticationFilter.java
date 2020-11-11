package br.com.hioktec.minhasfinancas.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filtro para validar o token de autenticação, incluindo o usuario. 
 * @author rodolfo
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = getJwtFromRequest(request);
			if(StringUtils.hasText(jwt) && tokenProvider.validarToken(jwt)) {
				Long usuarioId = tokenProvider.getUsuarioIdfromJWT(jwt);
				
				UserDetails userDetails = customUserDetailsService.loadUserById(usuarioId);
				
				UsernamePasswordAuthenticationToken autenticacao = 
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				autenticacao.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(autenticacao);
			}
		} catch (Exception ex){
			logger.error("Não foi possível definir a autenticação do usuário no contexto de segurança", ex);
		}
		filterChain.doFilter(request, response);
	}
	
	private String getJwtFromRequest(HttpServletRequest request) {
		String portadorToken = request.getHeader("autorizacao");
		if(StringUtils.hasText(portadorToken) && portadorToken.startsWith("Portador ")) {
			return portadorToken.substring(9, portadorToken.length());
		}
		return null;
	}
	
}
