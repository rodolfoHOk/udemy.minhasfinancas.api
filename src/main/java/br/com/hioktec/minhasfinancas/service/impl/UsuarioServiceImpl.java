package br.com.hioktec.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.hioktec.minhasfinancas.exception.ErroAutenticacao;
import br.com.hioktec.minhasfinancas.exception.RegraNegocioException;
import br.com.hioktec.minhasfinancas.model.entity.Usuario;
import br.com.hioktec.minhasfinancas.model.repository.UsuarioRepository;
import br.com.hioktec.minhasfinancas.service.UsuarioService;

@Service // bean gerenciado
public class UsuarioServiceImpl implements UsuarioService {
	
	private UsuarioRepository repository;
	
	// @Autowired não necessário mais nesta versão do spring boot pois implementamos UsuarioRepository de jpaRepository e declaramos a dependência.
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

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

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		
		if(existe) {
			throw new RegraNegocioException("Já existe usuário cadastrado com este e-mail.");
		}
	}

	@Override
	public Optional<Usuario> obterPorId(Long usuarioId) {
		return repository.findById(usuarioId);
	}
	
}
