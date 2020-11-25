package br.com.hioktec.minhasfinancas.api.resource;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.hioktec.minhasfinancas.exception.RegraNegocioException;
import br.com.hioktec.minhasfinancas.model.entity.Autoridade;
import br.com.hioktec.minhasfinancas.model.entity.Usuario;
import br.com.hioktec.minhasfinancas.model.enums.AutoridadeNome;
import br.com.hioktec.minhasfinancas.repository.AutoridadeRepository;
import br.com.hioktec.minhasfinancas.request.CadastroUsuarioRequest;
import br.com.hioktec.minhasfinancas.request.LoginRequest;
import br.com.hioktec.minhasfinancas.response.JwtResponse;
import br.com.hioktec.minhasfinancas.response.UsuarioResponse;
import br.com.hioktec.minhasfinancas.security.JwtTokenProvider;
import br.com.hioktec.minhasfinancas.security.UsuarioAtual;
import br.com.hioktec.minhasfinancas.security.UsuarioPrincipal;
import br.com.hioktec.minhasfinancas.service.LancamentoService;
import br.com.hioktec.minhasfinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;

@RestController // controller do spring já injeta a dependencia UsuarioService do construtor
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioResource {
	
	/* teste do controller
	@GetMapping("/")
	public String helloWorld() {
		return "Hello World!";
	}
	*/
	
	private final UsuarioService service; // inserimos final para usar @RequiredArgsConstructor
	
	/* eliminando a necessidade de ficar inserindo as injeções no constuctor usaremos @RequiredArgsConstructor
	public UsuarioResource(UsuarioService service) {
		this.service = service;
	}
	*/
	
	private final LancamentoService lancamentoService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtTokenProvider tokenProvider;
	
	@Autowired
	AutoridadeRepository autoridadeRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@PostMapping("/cadastrar")
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<?> salvar(@RequestBody CadastroUsuarioRequest cadastroUsuarioRequest) { //usaremos insomnia para testar (https://insomnia.rest)
		/* Refatoramos para incluir validação e também por implementar segurança Jwt e mudamos UsuarioDTO para CadastroUsuarioRequest 
		 * Usuario usuario = new Usuario ();
		 * usuario.setNome(dto.getNome());
		 * usuario.setEmail(dto.getEmail());
		 * usuario.setSenha(dto.getSenha());
		 *
		 * try {
		 * 	Usuario usuarioSalvo = service.salvarUsuario(usuario);
		 *	return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		 * } catch(RegraNegocioException e){
		 *	return ResponseEntity.badRequest().body(e.getMessage());
		 * }
		 */
		if (service.existeNomeUsuario(cadastroUsuarioRequest.getNomeUsuario())) {
			return new ResponseEntity<>("Nome de usuário já existe", HttpStatus.BAD_REQUEST);
		}
		
		if (service.existeEmail(cadastroUsuarioRequest.getEmail())) {
			return new ResponseEntity<>("Email já existe", HttpStatus.BAD_REQUEST);
		}
		
		Usuario usuario = new Usuario(
				cadastroUsuarioRequest.getNome(),
				cadastroUsuarioRequest.getNomeUsuario(),
				cadastroUsuarioRequest.getEmail(),
				cadastroUsuarioRequest.getSenha());
		
		Set<Autoridade> autoridades = new HashSet<Autoridade>();
		
		Autoridade autoridadeUsuar = autoridadeRepository.findByNome(AutoridadeNome.USUARIO)
				.orElseThrow(() -> new RegraNegocioException("Autoridade de usuário não configurado"));
		
		autoridades.add(autoridadeUsuar);
		
		if(cadastroUsuarioRequest.getAutoridade().equals(AutoridadeNome.ADMINISTRADOR.name())) {
			Autoridade autoridadeAdmin = autoridadeRepository.findByNome(AutoridadeNome.ADMINISTRADOR)
					.orElseThrow(() -> new RegraNegocioException("Autoridade de usuário não configurado"));
			autoridades.add(autoridadeAdmin);
		}
		
		usuario.setAutoridades(autoridades);
		
		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
				
		Usuario usuarioSalvo = service.salvarUsuario(usuario);
		
		return new ResponseEntity<>(usuarioSalvo, HttpStatus.CREATED);
	}
	
	@PostMapping("/autenticar")
	public ResponseEntity<?> autenticar(@Valid @RequestBody LoginRequest loginRequest) { // mudamos de UsuarioDTO para LoginRequest: JWT
		/* removermos esta parte para implementar a segurança JWT
		 * try {
		 *	Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
		 *	return ResponseEntity.ok(usuarioAutenticado);
		 * } catch (ErroAutenticacao e) {
		 *	return ResponseEntity.badRequest().body(e.getMessage());
		 * }
		 */
		Authentication autenticacao = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getNomeUsuarioOuEmail(),
						loginRequest.getSenha()));
		
		SecurityContextHolder.getContext().setAuthentication(autenticacao);
		
		String jwt = tokenProvider.gerarToken(autenticacao);
		
		return ResponseEntity.ok(new JwtResponse(jwt));
	}
	
	@GetMapping("/eu")
	@PreAuthorize("hasAuthority('USUARIO')")
	public UsuarioResponse getUsuarioAtual(@UsuarioAtual UsuarioPrincipal usuarioAtual) {
		boolean isAdmin = false;
		for(GrantedAuthority autoridade : usuarioAtual.getAuthorities()) {
			if(autoridade.getAuthority().equals("ADMINISTRADOR")) {
				isAdmin = true;
			}
		}
		return new UsuarioResponse(usuarioAtual.getId(), usuarioAtual.getNome(), usuarioAtual.getUsername(), isAdmin);
	}
	
	@GetMapping("{id}/saldo")
	public ResponseEntity<?> obterSaldo( @PathVariable("id") Long id) {
		Optional<Usuario> usuario = service.obterPorId(id);
		
		if(!usuario.isPresent())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
		BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
		return ResponseEntity.ok(saldo);
	}

}
