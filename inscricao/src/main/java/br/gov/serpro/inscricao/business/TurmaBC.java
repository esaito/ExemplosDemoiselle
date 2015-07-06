package br.gov.serpro.inscricao.business;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.inject.Inject;
import br.gov.frameworkdemoiselle.exception.ExceptionHandler;
import br.gov.frameworkdemoiselle.lifecycle.Startup;
import br.gov.frameworkdemoiselle.message.MessageContext;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.ResourceBundle;
import br.gov.serpro.inscricao.config.InscricaoConfig;
import br.gov.serpro.inscricao.entity.Aluno;
import br.gov.serpro.inscricao.exception.TurmaException;
//import br.gov.serpro.inscricao.message.CustomAppender;
import br.gov.frameworkdemoiselle.security.RequiredPermission;

@BusinessController
public class TurmaBC implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject 
	private Logger logger;
	
	@Inject 
	private ResourceBundle bundle;
	
	@Inject
	private InscricaoConfig config;
	
	@Inject 
	private AlunoBC alunoBC;
	
	@Inject 
	private MessageContext messageContext;
		
	@Startup
	public void iniciar(){
		logger.info(" ***************** Iniciando ...  ******");
	}
	
	
	@Transactional
	@RequiredPermission(resource="aluno", operation="matricular")
	public void matricular(Aluno aluno) {
		if (estaMatriculado(aluno) || obterAlunosMatriculados().size() == config.getCapacidadeTurma()) { 
			throw new TurmaException(); 
		}
		String mensagem = bundle.getString("matricula.sucesso", aluno.getNome()); 
		alunoBC.insert(aluno);
		logger.info(mensagem);
		messageContext.add(mensagem);
	}

	@RequiredPermission(resource="aluno", operation="consultar")
	public boolean estaMatriculado(Aluno aluno) {
		return obterAlunosMatriculados().contains(aluno);
	}
	
	public List<Aluno> obterAlunosMatriculados() {
		return alunoBC.findAll();
	}
	
	
	@ExceptionHandler
	public void tratar(TurmaException e){
		logger.warning(bundle.getString("matricula.erro"));
		throw e;
	}
	
	public void zeraBase(){
        List<Aluno> alunos = this.alunoBC.findAll();
         if (! alunos.isEmpty()){             
             for (Aluno a : alunos) {
                 this.alunoBC.delete(a.getMatricula());
                }
         }
    }
}
