package br.com.hioktec.minhasfinancas.service;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.hioktec.minhasfinancas.exception.RegraNegocioException;
import br.com.hioktec.minhasfinancas.model.entity.Lancamento;
import br.com.hioktec.minhasfinancas.model.entity.Usuario;
import br.com.hioktec.minhasfinancas.model.enums.StatusLancamento;
import br.com.hioktec.minhasfinancas.model.enums.TipoLancamento;
import br.com.hioktec.minhasfinancas.model.repository.LancamentoRepository;
import br.com.hioktec.minhasfinancas.model.repository.LancamentoRepositoryTest;
import br.com.hioktec.minhasfinancas.service.impl.LancamentoServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {
	
	@SpyBean
	LancamentoServiceImpl service;
	
	@MockBean
	LancamentoRepository repository;
	
	@Test
	public void deveSalvarUmLancamento() {
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		lancamentoASalvar.setStatus(null);
		Mockito.doNothing().when(service).validar(lancamentoASalvar);
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
		
		Lancamento lancamento = service.salvar(lancamentoASalvar);
		
		assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
	}
	
	@Test //(expected = RegraNegocioException.class)
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		lancamentoASalvar.setStatus(null);
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);
		
		catchThrowableOfType(() -> service.salvar(lancamentoASalvar), RegraNegocioException.class);
		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.doNothing().when(service).validar(lancamentoSalvo);
		Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
		
		service.atualizar(lancamentoSalvo);
		
		Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);
	}
	
	@Test //(expected = RegraNegocioException.class)
	public void naoDeveAtualizarUmLancamentoQuandoHouverErroDeValidacao() {
		Lancamento lancamentoAAtualizar = LancamentoRepositoryTest.criarLancamento();
		lancamentoAAtualizar.setId(1l);
		lancamentoAAtualizar.setStatus(StatusLancamento.PENDENTE);
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoAAtualizar);
		
		catchThrowableOfType(() -> service.atualizar(lancamentoAAtualizar), RegraNegocioException.class);
		Mockito.verify(repository, Mockito.never()).save(lancamentoAAtualizar);
	}
	
	@Test //(expected = RegraNegocioException.class)
	public void deveLancarUmErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
		Lancamento lancamentoAAtualizar = LancamentoRepositoryTest.criarLancamento();
		
		catchThrowableOfType(() -> service.atualizar(lancamentoAAtualizar), NullPointerException.class);
		Mockito.verify(repository, Mockito.never()).save(lancamentoAAtualizar);
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		Lancamento lancamentoADeletar = LancamentoRepositoryTest.criarLancamento();
		lancamentoADeletar.setId(1l);
		
		service.deletar(lancamentoADeletar);
		
		Mockito.verify(repository, Mockito.times(1)).delete(lancamentoADeletar);
	}
	
	@Test
	public void deveLancarUmErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
		Lancamento lancamentoADeletar = LancamentoRepositoryTest.criarLancamento();
		
		catchThrowableOfType(() -> service.deletar(lancamentoADeletar), NullPointerException.class);
		Mockito.verify(repository, Mockito.never()).delete(lancamentoADeletar);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void deveFiltrarLancamentos() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		List<Lancamento> lista = Arrays.asList(lancamento);
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);
		
		List<Lancamento> resultado= service.buscar(lancamento);
		
		assertThat(resultado).isNotEmpty().hasSize(1).contains(lancamento);
	}
	
	@Test
	public void deveAtualizarUmStatusDeLancamento() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		Mockito.doReturn(lancamento).when(service).atualizar(lancamento);
		
		service.atualizarStatus(lancamento, novoStatus);
		
		assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
		Mockito.verify(service).atualizar(lancamento);
	}
	
	@Test
	public void deveObterUmLancamentoPorID() {
		Long id = 1l;
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));
		
		Optional<Lancamento> resultado = service.obterPorId(id);
		
		assertThat(resultado.isPresent()).isTrue();
	}
	
	@Test
	public void deveRetornarVazioQuandoOLancamentoNaoExiste() {
		Long id = 1l;
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
		
		Optional<Lancamento> resultado = service.obterPorId(id);
		
		assertThat(resultado.isPresent()).isFalse();
	}
	
	@Test
	public void deveLancarErrosAoValidarLancamento() {
		Lancamento lancamento = new Lancamento();
		
		Throwable exception = catchThrowable(() -> service.validar(lancamento));
		assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição válida.");
		
		lancamento.setDescricao("  ");
		exception = catchThrowable(() -> service.validar(lancamento));
		assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição válida.");
		
		lancamento.setDescricao("um lancamento qualquer");
		exception = catchThrowable(() -> service.validar(lancamento));
		assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido (número inteiro).");
		
		lancamento.setMes(0);
		exception = catchThrowable(() -> service.validar(lancamento));
		assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido (número inteiro).");
		
		lancamento.setMes(13);
		exception = catchThrowable(() -> service.validar(lancamento));
		assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido (número inteiro).");
		
		lancamento.setMes(5);
		exception = catchThrowable(() -> service.validar(lancamento));
		assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido (número inteiro com 4 casas).");
		
		lancamento.setAno(20);
		exception = catchThrowable(() -> service.validar(lancamento));
		assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido (número inteiro com 4 casas).");
		
		lancamento.setAno(2020);
		exception = catchThrowable(() -> service.validar(lancamento));
		assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");
		
		lancamento.setUsuario(Usuario.builder().nome("usuario").email("email@email.com").senha("senha").build());
		exception = catchThrowable(() -> service.validar(lancamento));
		assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");
		
		lancamento.setUsuario(Usuario.builder().id(1l).nome("usuario").email("email@email.com").senha("senha").build());
		exception = catchThrowable(() -> service.validar(lancamento));
		assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido (número positivo).");
		
		lancamento.setValor(BigDecimal.valueOf(0));
		exception = catchThrowable(() -> service.validar(lancamento));
		assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido (número positivo).");
		
		lancamento.setValor(BigDecimal.valueOf(-10));
		exception = catchThrowable(() -> service.validar(lancamento));
		assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido (número positivo).");
		
		lancamento.setValor(BigDecimal.valueOf(0.01));
		exception = catchThrowable(() -> service.validar(lancamento));
		assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Tipo válido.");
		
		lancamento.setTipo(TipoLancamento.RECEITA);
		exception = catchThrowable(() -> service.validar(lancamento));
		assertThat(exception).isNull();
	}
	
	@Test
	public void deveObterSaldoPorUsuario() {
		Long id = 1l;
		Mockito.when(repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.RECEITA))
			.thenReturn(BigDecimal.valueOf(200));
		Mockito.when(repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.DESPESA))
			.thenReturn(BigDecimal.valueOf(100));
		
		BigDecimal saldo = service.obterSaldoPorUsuario(id);
		
		assertThat(saldo).isEqualTo(BigDecimal.valueOf(100));
		}
	
}
