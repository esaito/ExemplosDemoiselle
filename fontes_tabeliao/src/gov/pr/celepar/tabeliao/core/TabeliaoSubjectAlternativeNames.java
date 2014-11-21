package gov.pr.celepar.tabeliao.core;
/*
Este programa é licenciado de acordo com a 
LPG-AP (LICENÇA PÚBLICA GERAL PARA PROGRAMAS DE COMPUTADOR DA ADMINISTRAÇÃO PÚBLICA), 
versão 1.1 ou qualquer versão posterior.
A LPG-AP deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa.
Caso uma cópia da LPG-AP não esteja disponível junto com este Programa, você pode contatar o LICENCIANTE ou então acessar diretamente:
http://www.celepar.pr.gov.br/licenca/LPG-AP.pdf
Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa 
é preciso estar de acordo com os termos da LPG-AP
*/
import java.security.cert.X509Certificate;

/**
 * Classe que permite instanciar um certificado de modo mais genehrico, sem pre­-identificacao 
 * do tipo (PF,PJ ou Equipamento) atravehs de seus OID's. A classe retorna o objeto conforme 
 * o tipo encontrado
 * 
 * @author Thiago Meneghello <thiagomeneghello@ecelepar.pr.gov.br> CELEPAR/GIC
 *
 */
public class TabeliaoSubjectAlternativeNames {

	private String                   email = null;
	private TabeliaoDadosPF          tabeliaoDadosPF = null;
	private TabeliaoDadosPJ          tabeliaoDadosPJ = null;
	private TabeliaoDadosEquipamento tabeliaoDadosEquipamento = null;
	
	/**
	 * Construtor unico
	 * @param certificate -> certificado no formato X509Certificate
	 * @see java.security.cert.X509Certificate
	 */
	public TabeliaoSubjectAlternativeNames(X509Certificate certificate) {
		CertificadoExtra ce = new CertificadoExtra(certificate);
		if(ce.isCertificadoPF()) {
			tabeliaoDadosPF = new TabeliaoDadosPF(ce.getOID_2_16_76_1_3_1(),
					ce.getOID_2_16_76_1_3_5(),
					ce.getOID_2_16_76_1_3_6());
		} else if(ce.isCertificadoPJ()) {
			tabeliaoDadosPJ = new TabeliaoDadosPJ(ce.getOID_2_16_76_1_3_2(),
					ce.getOID_2_16_76_1_3_3(),
					ce.getOID_2_16_76_1_3_4(),
					ce.getOID_2_16_76_1_3_7());
		} else if(ce.isCertificadoEquipamento()) {
			tabeliaoDadosEquipamento = new TabeliaoDadosEquipamento(ce.getOID_2_16_76_1_3_2(),
					ce.getOID_2_16_76_1_3_3(),
					ce.getOID_2_16_76_1_3_4(),
					ce.getOID_2_16_76_1_3_8());
		}
		this.email = ce.getEmail();
	}
	
	/**
	 * 
	 * @return se eh um certificado de Pessoa Fisica
	 */
	public boolean isDadosPF(){
		return tabeliaoDadosPF != null;
	}
	
	/**
	 * 
	 * @return Objeto do tipo TabeliaoDadosPF
	 * @see gov.pr.celepar.tabeliao.core.TabeliaoDadosPF
	 */
	public TabeliaoDadosPF getTabeliaoDadosPF(){
		return tabeliaoDadosPF;
	}
	/**
	 * 
	 * @return se eh um certificado de Pessoa Juridica
	 */
	public boolean isDadosPJ(){
		return tabeliaoDadosPJ != null;
	}
	
	/**
	 * 
	 * @return Objeto do tipo TabeliaoDadosPJ
	 * @see gov.pr.celepar.tabeliao.core.TabeliaoDadosPJ
	 */
	public TabeliaoDadosPJ getTabeliaoDadosPJ(){
		return tabeliaoDadosPJ;
	}
	
	/**
	 * 
	 * @return se eh um certificado de Equipamento
	 */
	public boolean isDadosEquipamento(){
		return tabeliaoDadosEquipamento != null;
	}
	
	/**
	 * 
	 * @return Objeto do tipo TabeliaoDadosEquipamento
	 * @see gov.pr.celepar.tabeliao.core.TabeliaoDadosEquipamento
	 */
	public TabeliaoDadosEquipamento getTabeliaoDadosEquipamento(){
		return tabeliaoDadosEquipamento;
	}
	
	/**
	 * 
	 * @return endereco de E-mail contido no certificado
	 */
	public String getEmail(){
		return email;
	}
}
