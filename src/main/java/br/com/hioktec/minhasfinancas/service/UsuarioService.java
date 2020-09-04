package br.com.hioktec.minhasfinancas.service;

import br.com.hioktec.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {
	
	Usuario autenticar(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);
	
}
