package br.gov.serpro.inscricao.exception;

import br.gov.frameworkdemoiselle.exception.ApplicationException;

@ApplicationException(rollback = true)
public class TurmaException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public TurmaException() {
		super("Erro na matrícula!");
	}
}