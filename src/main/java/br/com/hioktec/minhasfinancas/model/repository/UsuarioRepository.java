package br.com.hioktec.minhasfinancas.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.hioktec.minhasfinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	/* usaremos o método abaixo ao invés deste!
	Optional<Usuario> findByEmail(String email); // spring implementará este método em tempo de execução. 
	obs: findByEmailAndNome(String email, String nome) com acima também e uma convenção do spring data.
	*/
	
	boolean existsByEmail(String email);
	
	Optional<Usuario> findByEmail(String email);
}
