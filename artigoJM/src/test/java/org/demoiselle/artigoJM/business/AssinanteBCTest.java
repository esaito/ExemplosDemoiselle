package org.demoiselle.artigoJM.business;

import static org.junit.Assert.*;
import java.util.*;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import br.gov.frameworkdemoiselle.junit.DemoiselleRunner;
import org.demoiselle.artigoJM.domain.Assinante;
import org.demoiselle.artigoJM.domain.Assinatura;
import org.demoiselle.artigoJM.business.AssinanteBC;

@RunWith(DemoiselleRunner.class)
public class AssinanteBCTest {

    @Inject
	private AssinanteBC assinanteBC;
	
	@Before
	public void before() {
		for (Assinante assinante : assinanteBC.findAll()) {
			assinanteBC.delete(assinante.getCodigoAssinante());
		}
	}	
		
	@Test
	public void testInsert() {
				
		// modifique para inserir dados conforme o construtor
		
		Assinante assinante = new Assinante("nome",new Date(),"000000000000",null,null);
		Assinatura assinatura = new Assinatura("revista",new Date(),null, assinante);
		List <Assinatura> assinaturas = new ArrayList<Assinatura>();
		assinaturas.add(assinatura);
		assinante.setAssinaturas(assinaturas);
		assinanteBC.insert(assinante);
		List<Assinante> listOfAssinante = assinanteBC.findAll();
		assertNotNull(listOfAssinante);
		assertEquals(1, listOfAssinante.size());
	}	
	
	@Test
	public void testDelete() {
		
		// modifique para inserir dados conforme o construtor
		Assinante assinante2 = new Assinante("nome2",new Date(),"000000000000",null,null);
		Assinatura assinatura2 = new Assinatura("revista2",new Date(),null, assinante2);
		List <Assinatura> assinaturas2 = new ArrayList<Assinatura>();
		assinaturas2.add(assinatura2);
		assinante2.setAssinaturas(assinaturas2);
		assinanteBC.insert(assinante2);
		
		List<Assinante> listOfAssinante2 = assinanteBC.findAll();
		assertNotNull(listOfAssinante2);
		assertEquals(false, listOfAssinante2.isEmpty());
		
		assinanteBC.delete(assinante2.getCodigoAssinante());
		listOfAssinante2 = assinanteBC.findAll();
		assertEquals(true, listOfAssinante2.isEmpty());
	}
	
	@Test
	public void testUpdate() {
		// modifique para inserir dados conforme o construtor
		Assinante assinante = new Assinante("nome",new Date(),"000000000000",null,null);
		Assinatura assinatura = new Assinatura("revista",new Date(),null, assinante);
		List <Assinatura> assinaturas = new ArrayList<Assinatura>();
		assinaturas.add(assinatura);
		assinante.setAssinaturas(assinaturas);
		assinanteBC.insert(assinante);
		
		List<Assinante> listOfAssinante = assinanteBC.findAll();
		Assinante assinante2 = (Assinante)listOfAssinante.get(0);
		assertNotNull(listOfAssinante);

		// alterar para tratar uma propriedade existente na Entidade Assinante
		//assinante2.setNomePropriedade("novo valor");
		//assinanteBC.update(assinante2);
		
		listOfAssinante = assinanteBC.findAll();
		Assinante assinante3 = (Assinante)listOfAssinante.get(0);
		
		// alterar para tratar uma propriedade existente na Entidade Assinante
		// assertEquals("novo valor", assinante3.getNomePropriedade());
	}

}