package br.com.hioktec.minhasfinancas.service;

import java.util.List;
import java.util.Optional;

import br.com.hioktec.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {
	
	// Usuario autenticar(String email, String senha); removemos para implementar segurança JWT
	
	Usuario salvarUsuario(Usuario usuario);
	
	// void validarEmail(String email); refatoramos para melhorar validação.
	
	Optional<Usuario> obterPorId(Long id);
	
	Boolean existeNomeUsuario(String nomeUsuario);
	
	Boolean existeEmail(String email);
	
	// adicionando gerenciamento de usuarios pelos admins
	
	List<Usuario> buscar(Usuario usuarioFiltro);
	
	Usuario atualizar(Usuario usuario);
	
	void deletar(Usuario usuario);
}
