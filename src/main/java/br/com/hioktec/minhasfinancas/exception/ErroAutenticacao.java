package br.com.hioktec.minhasfinancas.exception;

public class ErroAutenticacao extends RuntimeException {

	/**
	 * serialVersionUID autogerado
	 */
	private static final long serialVersionUID = 1L;

	public ErroAutenticacao(String msg) {
		super(msg);
	}
	
}
