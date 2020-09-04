package br.com.hioktec.minhasfinancas.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.hioktec.minhasfinancas.api.dto.UsuarioDTO;
import br.com.hioktec.minhasfinancas.exception.ErroAutenticacao;
import br.com.hioktec.minhasfinancas.exception.RegraNegocioException;
import br.com.hioktec.minhasfinancas.model.entity.Usuario;
import br.com.hioktec.minhasfinancas.service.UsuarioService;

@RestController // controller do spring já injeta a dependencia UsuarioService do construtor
@RequestMapping("/api/usuarios")
public class UsuarioResource {
	
	/* teste do controller
	@GetMapping("/")
	public String helloWorld() {
		return "Hello World!";
	}
	*/
	
	private UsuarioService service;
	
	public UsuarioResource(UsuarioService service) {
		this.service = service;
	}
	
	@PostMapping
	public ResponseEntity<String> salvar(@RequestBody UsuarioDTO dto) { //usaremos insomnia para testar (https://insomnia.rest)
		
		Usuario usuario = Usuario.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha())
				.build();
		
		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity<String>(usuarioSalvo.toString(), HttpStatus.CREATED);
		} catch(RegraNegocioException e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/autenticar")
	public ResponseEntity<String> autenticar(@RequestBody UsuarioDTO dto) {
		try {
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
			return ResponseEntity.ok(usuarioAutenticado.toString());
		} catch (ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
