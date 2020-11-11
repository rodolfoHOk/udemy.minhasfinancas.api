package br.com.hioktec.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.hioktec.minhasfinancas.model.entity.Usuario;
import br.com.hioktec.minhasfinancas.repository.UsuarioRepository;

// @SpringBootTest removemos para não ter que carregar muita coisa para fazer teste
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest // adicionamos para melhorar performance dos testes
@AutoConfigureTestDatabase(replace = Replace.NONE) // necessário para não sobrescrever as configurações do db (test.profiles)
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager; // adicionamos para não usar repository.save() além de melhorar performance de testes
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		// cenário
		Usuario usuario = criarUsuario();
		// repository.save(usuario); para não usar a classe que necessita de teste
		entityManager.persist(usuario);
		
		// ação / execução
		boolean result = repository.existsByEmail("usuario@email.com");
		
		// verificação
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		// cenário
		// repository.deleteAll(); não mais necessário pois DataJpaTest faz o rollback do db após o primeiro teste.
		
		// ação
		boolean result = repository.existsByEmail("usuario@email.com");
		
		// verificação
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		//cenario
		Usuario usuario = criarUsuario();
		
		//acao
		Usuario usuarioSalvo = repository.save(usuario);
		
		//verificacao
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		//cenario
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		//acao
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		
		//verificacao
		Assertions.assertThat(result.isPresent()).isTrue();
		
	}
	
	@Test
	public void deveRetornarNuloAoBuscarUmUsuarioPorEmailQueNaoExisteNaBase() {
		//cenario
		
		//acao
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		
		//verificacao
		Assertions.assertThat(result.isPresent()).isFalse();
		
	}
	
	public static Usuario criarUsuario() {
		return new Usuario("usuario", "nomeUsuario", "test@email.com", "senha");
	}
	
}
