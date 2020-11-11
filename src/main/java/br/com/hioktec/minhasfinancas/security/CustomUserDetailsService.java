package br.com.hioktec.minhasfinancas.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.hioktec.minhasfinancas.model.entity.Usuario;
import br.com.hioktec.minhasfinancas.repository.UsuarioRepository;

/**
 * Classe de serviço utilizado para requisitar detalhes do usuário pelas classes de segurança.
 * @author rodolfo
 */
@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String nomeUsuarioOuEmai) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByNomeUsuarioOrEmail(nomeUsuarioOuEmai, nomeUsuarioOuEmai)
				.orElseThrow(() -> new UsernameNotFoundException(
						"Usuario não encontrado com o nome de usuário ou email informado"));
		return UsuarioPrincipal.criar(usuario);
	}
	
	@Transactional
	public UserDetails loadUserById(Long id) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException(
						"Usuario não encontrado com o id: " + id));
		return UsuarioPrincipal.criar(usuario);
	}

}
