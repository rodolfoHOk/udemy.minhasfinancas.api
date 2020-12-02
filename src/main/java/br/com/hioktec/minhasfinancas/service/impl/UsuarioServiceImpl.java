package br.com.hioktec.minhasfinancas.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.hioktec.minhasfinancas.model.entity.Usuario;
import br.com.hioktec.minhasfinancas.repository.UsuarioRepository;
import br.com.hioktec.minhasfinancas.service.UsuarioService;

@Service // bean gerenciado
public class UsuarioServiceImpl implements UsuarioService {
	
	private UsuarioRepository repository;
	
	// @Autowired não necessário mais nesta versão do spring boot pois implementamos UsuarioRepository de jpaRepository e declaramos a dependência.
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	/* removemos para implementar segunrança JWT
	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado pelo e-mail informado!");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida!");
		}
		
		return usuario.get();
	}
	*/

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		// validarEmail(usuario.getEmail()); removemos refatoração melhorar validação.
		return repository.save(usuario);
	}
	
	/* Removemos para melhorar validação adicionamos o método existeEmail
	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		
		if(existe) {
			throw new RegraNegocioException("Já existe usuário cadastrado com este e-mail.");
		}
	}
	*/

	@Override
	public Optional<Usuario> obterPorId(Long usuarioId) {
		return repository.findById(usuarioId);
	}

	@Override
	public Boolean existeNomeUsuario(String nomeUsuario) {
		return repository.existsByNomeUsuario(nomeUsuario);
	}

	@Override
	public Boolean existeEmail(String email) {
		return repository.existsByEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> buscar(Usuario usuarioFiltro) {
		Example<Usuario> example = Example.of(
				usuarioFiltro,
				ExampleMatcher.matching()
					.withIgnoreCase()
					.withStringMatcher(StringMatcher.CONTAINING));
		return repository.findAll(example);
	}

	@Override
	@Transactional
	public Usuario atualizar(Usuario usuario) {
		Objects.requireNonNull(usuario.getId());
		return repository.save(usuario);
	}

	@Override
	@Transactional
	public void deletar(Usuario usuario) {
		Objects.requireNonNull(usuario.getId());
		repository.delete(usuario);		
	}
}
