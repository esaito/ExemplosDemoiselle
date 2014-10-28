package br.org.frameworkdemoiselle.duasbases.persistence;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.gov.frameworkdemoiselle.annotation.Name;
import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;

@PersistenceController
public class GenericTabelaDAO <T,I> extends JPACrud<T, I>  {
	
	
	private static final long serialVersionUID = 1L;
	@Inject
	@Name("bancoDois-ds")
	private EntityManager em;
	
	@Override
	public EntityManager getEntityManager() {
		return em;		
	}
}
