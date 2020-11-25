package br.com.hioktec.minhasfinancas.service;

import java.util.Optional;

import br.com.hioktec.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {
	
	// Usuario autenticar(String email, String senha); removemos para implementar segurança JWT
	
	Usuario salvarUsuario(Usuario usuario);
	
	// void validarEmail(String email); refatoramos para melhorar validação.
	
	Optional<Usuario> obterPorId(Long id);
	
	Boolean existeNomeUsuario(String nomeUsuario);
	
	Boolean existeEmail(String email);
	
}
