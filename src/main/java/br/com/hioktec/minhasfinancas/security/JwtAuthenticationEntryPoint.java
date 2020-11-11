package br.com.hioktec.minhasfinancas.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Classe usada para retornar um erro 401 (não autorizado) para clientes que tentam acessar um recurso protegido
 * sem autenticação adequada.
 * @author rodolfo
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

	@Override
	public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			AuthenticationException authException) throws IOException, ServletException {
		logger.error("\"Respondendo com erro não autorizado. Mensagem - {}", authException.getMessage());
		httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
	}
	
}
