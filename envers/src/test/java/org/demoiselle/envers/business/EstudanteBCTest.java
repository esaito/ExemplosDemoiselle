

package org.demoiselle.envers.business;

import static org.junit.Assert.*;
import java.util.*;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import br.gov.frameworkdemoiselle.junit.DemoiselleRunner;
import org.demoiselle.envers.domain.Estudante;
import org.demoiselle.envers.business.EstudanteBC;

@RunWith(DemoiselleRunner.class)
public class EstudanteBCTest {

    @Inject
	private EstudanteBC estudanteBC;
	
	@Before
	public void before() {
		for (Estudante estudante : estudanteBC.findAll()) {
			estudanteBC.delete(estudante.getId());
		}
	}	
	
	
	@Test
	public void testInsert() {
				
		// modifique para inserir dados conforme o construtor
		Estudante estudante = new Estudante(new Date(),Integer.valueOf(1),Long.valueOf(1),"nome",new Date(),null);
		estudanteBC.insert(estudante);
		List<Estudante> listOfEstudante = estudanteBC.findAll();
		assertNotNull(listOfEstudante);
		assertEquals(1, listOfEstudante.size());
	}	
	
	@Test
	public void testDelete() {
		
		// modifique para inserir dados conforme o construtor
		Estudante estudante = new Estudante(new Date(),Integer.valueOf(1),Long.valueOf(1),"nome",new Date(),null);
		estudanteBC.insert(estudante);
		
		List<Estudante> listOfEstudante = estudanteBC.findAll();
		assertNotNull(listOfEstudante);
		assertEquals(1, listOfEstudante.size());
		
		estudanteBC.delete(estudante.getId());
		listOfEstudante = estudanteBC.findAll();
		assertEquals(0, listOfEstudante.size());
	}
	
	@Test
	public void testUpdate() {
		// modifique para inserir dados conforme o construtor
		Estudante estudante = new Estudante(new Date(),Integer.valueOf(1),Long.valueOf(1),"nome",new Date(),null);
		estudanteBC.insert(estudante);
		
		List<Estudante> listOfEstudante = estudanteBC.findAll();
		Estudante estudante2 = (Estudante)listOfEstudante.get(0);
		assertNotNull(listOfEstudante);

		// alterar para tratar uma propriedade existente na Entidade Estudante
		estudante2.setNome("novo valor");
		estudanteBC.update(estudante2);
		
		listOfEstudante = estudanteBC.findAll();
		Estudante estudante3 = (Estudante)listOfEstudante.get(0);
		
		// alterar para tratar uma propriedade existente na Entidade Estudante
		assertEquals("novo valor", estudante3.getNome());
	}

}