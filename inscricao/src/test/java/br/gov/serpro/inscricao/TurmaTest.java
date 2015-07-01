package br.gov.serpro.inscricao;

import javax.inject.Inject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import br.gov.frameworkdemoiselle.junit.DemoiselleRunner;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.serpro.inscricao.business.TurmaBC;
import br.gov.serpro.inscricao.entity.Aluno;
import br.gov.serpro.inscricao.exception.TurmaException;
import br.gov.serpro.inscricao.security.Credenciais;

@RunWith(DemoiselleRunner.class)
public class TurmaTest {

	@Inject
	private TurmaBC turma;
	
	@Inject
	private SecurityContext securityContext;
	
	@Inject
	private Credenciais credenciais;
	 
	@Before
	public void setUp(){
		credenciais.setNome("secretaria");
		credenciais.setSenha("segredo");
		securityContext.login();
		turma.zeraBase();
	}

	@Test
	public void matricularAlunoComSucesso(){
		
		Aluno aluno = new Aluno("Santos Dumont");
		
		turma.matricular(aluno);
		Assert.assertTrue(turma.estaMatriculado(aluno));
	}
	

	@Test(expected = TurmaException.class)
	public void falhaAoTentarMatricularAlunoDuplicado() { 
		turma.matricular(new Aluno("Orville Wright")); 
		turma.matricular(new Aluno("Orville Wright")); 
	}
	

	@Test(expected = TurmaException.class)
	public void falhaAoTentarMatricularAlunoNaTurmaCheia() {
		for (int i = 1; i <= 5; i++) {
			turma.matricular(new Aluno("Aluno " + i));
		}

		turma.matricular(new Aluno("Aluno 6"));
	}

}
