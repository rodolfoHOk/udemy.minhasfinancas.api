package br.com.hioktec.minhasfinancas.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.hioktec.minhasfinancas.api.dto.AtualizaStatusDTO;
import br.com.hioktec.minhasfinancas.api.dto.LancamentoDTO;
import br.com.hioktec.minhasfinancas.exception.RegraNegocioException;
import br.com.hioktec.minhasfinancas.model.entity.Lancamento;
import br.com.hioktec.minhasfinancas.model.entity.Usuario;
import br.com.hioktec.minhasfinancas.model.enums.StatusLancamento;
import br.com.hioktec.minhasfinancas.model.enums.TipoLancamento;
import br.com.hioktec.minhasfinancas.service.LancamentoService;
import br.com.hioktec.minhasfinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoResource {
	
	private final LancamentoService service; // inserimos final para usar @RequiredArgsConstructor
	private final UsuarioService usuarioService; // inserimos final para usar @RequiredArgsConstructor
	
	/* eliminando a necessidade de ficar inserindo as injeções no constuctor usaremos @RequiredArgsConstructor
	public LancamentoResource(LancamentoService service, UsuarioService usuarioService) {
		this.service = service;
		this.usuarioService = usuarioService;
	*/
	
	@PostMapping
	@PreAuthorize("hasAuthority('USUARIO')")
	public ResponseEntity<?> salvar( @RequestBody LancamentoDTO dto ) {
		try {
			Lancamento entidade = converter(dto);
			entidade = service.salvar(entidade);
			return new ResponseEntity<>(entidade, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping("{id}")
	public ResponseEntity<?> obterLancamento ( @PathVariable("id") Long id ) {
		return service.obterPorId(id)
				.map(lancamento -> new ResponseEntity<>(converterDTO(lancamento), HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@PutMapping("{id}")
	@PreAuthorize("hasAuthority('USUARIO')")
	public ResponseEntity<?> atualizar( @PathVariable("id") Long id, @RequestBody LancamentoDTO dto ) {
		return service.obterPorId(id).map( entity -> {
			try {
				Lancamento lancamento = converter(dto);
				lancamento.setId(entity.getId());
				service.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> 
			new ResponseEntity<>("Lancamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}
	
	@PutMapping("{id}/atualiza-status")
	@PreAuthorize("hasAuthority('USUARIO')")
	public ResponseEntity<?> atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizaStatusDTO dto) {
		return service.obterPorId(id).map( entity -> {
			StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());
			if(statusSelecionado == null)
				return new ResponseEntity<>("Status informado inválido", HttpStatus.BAD_REQUEST);
			try {
				entity.setStatus(statusSelecionado);
				service.atualizar(entity);
				return ResponseEntity.ok(entity);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> 
			new ResponseEntity<>("Lancamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}
		
	@DeleteMapping("{id}")
	@PreAuthorize("hasAuthority('USUARIO')")
	public ResponseEntity<?> deletar( @PathVariable("id") Long id ){
		return service.obterPorId(id).map( entity -> {
			service.deletar(entity);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> 
			new ResponseEntity<>("Lancamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}
	
	@GetMapping
	public ResponseEntity<?> buscar(
			@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano,
			@RequestParam("usuario") Long usuarioId // sempre requerido
			) {
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		
		Optional<Usuario> usuario = usuarioService.obterPorId(usuarioId);
		if(!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possível realizar a busca. Usuário não encontrado para o id informado");
		} else {
			lancamentoFiltro.setUsuario(usuario.get());
		}
		
		List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
		return ResponseEntity.ok(lancamentos);
	}
	
	private LancamentoDTO converterDTO(Lancamento lancamento) {
		return LancamentoDTO.builder()
				.id(lancamento.getId())
				.descricao(lancamento.getDescricao())
				.ano(lancamento.getAno())
				.mes(lancamento.getMes())
				.valor(lancamento.getValor())
				.tipo(lancamento.getTipo().name())
				.status(lancamento.getStatus().name())
				.usuario(lancamento.getUsuario().getId())
				.build();
				
	}
	
	private Lancamento converter(LancamentoDTO dto) {
		Lancamento lancamento = new Lancamento();
		lancamento.setId(dto.getId());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setMes(dto.getMes());
		lancamento.setAno(dto.getAno());
		lancamento.setValor(dto.getValor());
		lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		if(dto.getStatus() != null) {
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		}
		
		Usuario usuario = usuarioService
				.obterPorId(dto.getUsuario())
				.orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o id informado"));
		
		lancamento.setUsuario(usuario);
		
		return lancamento;
	}
}
