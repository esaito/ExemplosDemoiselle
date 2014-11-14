

package org.demoiselle.artigoJM.business;

import static org.junit.Assert.*;
import java.util.*;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import br.gov.frameworkdemoiselle.junit.DemoiselleRunner;
import org.demoiselle.artigoJM.domain.Assinatura;
import org.demoiselle.artigoJM.business.AssinaturaBC;

@RunWith(DemoiselleRunner.class)
public class AssinaturaBCTest {

    @Inject
	private AssinaturaBC assinaturaBC;
	
	@Before
	public void before() {
		for (Assinatura assinatura : assinaturaBC.findAll()) {
			assinaturaBC.delete(assinatura.getCodigoAssinatura());
		}
	}	
	
	
	@Test
	public void testInsert() {
				
		// modifique para inserir dados conforme o construtor
		Assinatura assinatura = new Assinatura("nomeRevista",new Date(),new Date(),null);
		assinaturaBC.insert(assinatura);
		List<Assinatura> listOfAssinatura = assinaturaBC.findAll();
		assertNotNull(listOfAssinatura);
		assertEquals(1, listOfAssinatura.size());
	}	
	
	@Test
	public void testDelete() {
		
		// modifique para inserir dados conforme o construtor
		Assinatura assinatura = new Assinatura("nomeRevista",new Date(),new Date(),null);
		assinaturaBC.insert(assinatura);
		
		List<Assinatura> listOfAssinatura = assinaturaBC.findAll();
		assertNotNull(listOfAssinatura);
		assertEquals(1, listOfAssinatura.size());
		
		assinaturaBC.delete(assinatura.getCodigoAssinatura());
		listOfAssinatura = assinaturaBC.findAll();
		assertEquals(0, listOfAssinatura.size());
	}
	
	@Test
	public void testUpdate() {
		// modifique para inserir dados conforme o construtor
		Assinatura assinatura = new Assinatura("nomeRevista",new Date(),new Date(),null);
		assinaturaBC.insert(assinatura);
		
		List<Assinatura> listOfAssinatura = assinaturaBC.findAll();
		Assinatura assinatura2 = (Assinatura)listOfAssinatura.get(0);
		assertNotNull(listOfAssinatura);

		// alterar para tratar uma propriedade existente na Entidade Assinatura
		assinatura2.setNomeRevista("novo valor");
		assinaturaBC.update(assinatura2);
		
		listOfAssinatura = assinaturaBC.findAll();
		Assinatura assinatura3 = (Assinatura)listOfAssinatura.get(0);
		
		// alterar para tratar uma propriedade existente na Entidade Assinatura
		 assertEquals("novo valor", assinatura3.getNomeRevista());
	}

}