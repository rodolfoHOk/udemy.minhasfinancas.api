package br.com.hioktec.minhasfinancas.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.hioktec.minhasfinancas.repository.UsuarioRepository;
import br.com.hioktec.minhasfinancas.service.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	// @Autowired não precisa mais injetar pois usaremos mocks
	@SpyBean
	UsuarioServiceImpl service;
	
	// @Autowired não precisa mais injetar pois usaremos mocks
	@MockBean // mockando instância injetada
	UsuarioRepository repository;
	
	/* nao precisaremos mais pois usaremos @MockBean e @SpyBean
	@Before
	public void setUp() {
		// repository = Mockito.mock(UsuarioRepository.class); // mock = instância falsa; // não precisa mais com @MockBean
		// service = new UsuarioServiceImpl(repository); // usaremos spy para mockar service para salvarUsuario()
		// service = Mockito.spy(UsuarioServiceImpl.class); usaremos @SpyBean
	}
	*/
	
	/* Refatorado para usar segurança JWT
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		// cenário
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		// acao
		Usuario result = service.autenticar(email, senha);
		
		// verificacao
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEnailInformado() {
		// cenario
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		// acao e verificacao
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "senha"));
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class)
			.hasMessage("Usuário não encontrado pelo e-mail informado!");
	}
	
	@Test
	public void deveLancarErroQuandoSenhaNaoCoincidir() {
		//cenario
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		// acao e verificacao
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar(email, "senhaErrada"));
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida!");
	}
	
	@Test
	public void deveSalvarUmUsuario() {
		// cenario
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder()
				.nome("nome")
				.email("email@email.com")
				.senha("senha")
				.id(1l)
				.build();
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		// acao
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		// verificacao
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
	}
	
	@Test
	public void naoDeveSalvarUsuarioComEmailJaCastrado() {
		//cenario
		String email = "email@email.com";
		Usuario usuario = Usuario.builder()
				.email(email)
				.build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		// acao
		org.junit.jupiter.api.Assertions
			.assertThrows(RegraNegocioException.class, () -> service.salvarUsuario(usuario));
		
		// verificacao
		Mockito.verify(repository, Mockito.never()).save(usuario);
	}
	
	@Test
	public void deveValidarEmail() {
		// cenario
		// repository.deleteAll(); não precisa mais com mocks
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//acao
		service.validarEmail("usuario@email.com");
	}
	
	@Test
	public void deveLancarUmErroAoValidarEmailQuandoJaExistirOEmailCadastrado() {
		// cenario
		// Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
		// repository.save(usuario); idens não precisa mais com mocks
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		//ação
		org.junit.jupiter.api.Assertions
			.assertThrows(RegraNegocioException.class, () -> service.validarEmail("usuario@email.com"));
	}
	*/
}
