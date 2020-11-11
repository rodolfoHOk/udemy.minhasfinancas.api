package br.com.hioktec.minhasfinancas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.hioktec.minhasfinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	/* usaremos o método abaixo ao invés deste!
	Optional<Usuario> findByEmail(String email); // spring implementará este método em tempo de execução. 
	obs: findByEmailAndNome(String email, String nome) com acima também e uma convenção do spring data.
	*/
	
	Boolean existsByEmail(String email);
	
	Boolean existsByNomeUsuario(String nomeUsuario); // adicionado para segurança JWT
	
	Optional<Usuario> findByEmail(String email);
	
	Optional<Usuario> findByNomeUsuario(String nomeUsuario); // adicionado para segurança JWT
	
	Optional<Usuario> findByNomeUsuarioOrEmail(String nomeUsuario, String email); // adisionado para segurança JWT
	
}
