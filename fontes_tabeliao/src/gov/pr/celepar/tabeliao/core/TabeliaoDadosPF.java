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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import gov.pr.celepar.tabeliao.core.oid.OID_2_16_76_1_3_1;
import gov.pr.celepar.tabeliao.core.oid.OID_2_16_76_1_3_5;
import gov.pr.celepar.tabeliao.core.oid.OID_2_16_76_1_3_6;

/**
 * Esta classe possui as informacoes definidas pela ICP-BRASIL para os
 * certificados de Pessoa Fisica. Estes campos estao definidos no DOC-ICP-04
 * v2.0 de 18/04/2006. 
 * 
 * @author Thiago Meneghello <thiagomeneghello@ecelepar.pr.gov.br> CELEPAR/GIC
 *
 */

public class TabeliaoDadosPF {
	
	private OID_2_16_76_1_3_1 oID_2_16_76_1_3_1 = null;
	private OID_2_16_76_1_3_5 oID_2_16_76_1_3_5 = null;
	private OID_2_16_76_1_3_6 oID_2_16_76_1_3_6 = null;
	
	/**
	 * 
	 * @param oid1 -> 2.16.76.1.3.1 e conteudo = nas primeiras 8 (oito) posicoes, a data de nascimento
	 *  do titular, no formato ddmmaaaa; nas 11 (onze) posicoes subsequentes, o Cadastro de Pessoa Fisica (CPF) do titular;
	 *  nas 11 (onze) posicoes subsequentes, o numero de Identificacao Social - NIS (PIS, PASEP ou CI);
	 *  nas 15 (quinze) posicoes subsequentes, o numero do Registro Geral - RG do titular;
	 *  nas 6 (seis) posicoes subsequentes, as siglas do orgao expedidor do RG e respectiva UF.
	 *  
	 * @param oid2 -> 2.16.76.1.3.5 e conteudo = nas primeiras 12 (onze) posicoes, o numero de inscricao do Titulo de Eleitor;
	 *  nas 3 (tres) posicoes subsequentes, a Zona Eleitoral; nas 4 (quatro) posicoes seguintes, a Secao; 
	 *  nas 22 (vinte e duas) posicoes subsequentes, o municipio e a UF do Titulo de Eleitor
	 * 
	 * @param oid3 -> 2.16.76.1.3.6 e conteudo = nas 12 (doze) posicoes o numero do Cadastro Especifico do INSS (CEI) 
	 *  da pessoa fisica titular do certificado
	 */
	public TabeliaoDadosPF(OID_2_16_76_1_3_1 oid1, OID_2_16_76_1_3_5 oid2, OID_2_16_76_1_3_6 oid3){
		this.oID_2_16_76_1_3_1 = oid1;
		this.oID_2_16_76_1_3_5 = oid2;
		this.oID_2_16_76_1_3_6 = oid3;
	}
	
	/**
	 * 
	 * @return o numero do Cadastro de Pessoa Fisica (CPF) do titular
	 */
	public String getCPF(){
		return oID_2_16_76_1_3_1.getCPF();
	}
	
	/**
	 * 
	 * @return  data de nascimento do titular
	 */
	public Date getDataNascimento(){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			return sdf.parse(oID_2_16_76_1_3_1.getDataNascimento());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 
	 * @deprecated Mudou para getNis para seguir a nomenclatura da ICP-BRASIL
	 * @return o numero de Identificacao Social - NIS (PIS, PASEP ou CI)
	 */
	public String getPis(){
		return oID_2_16_76_1_3_1.getPIS();
	}
	
	/**
	 * 
	 * @return o numero de Identificacao Social - NIS (PIS, PASEP ou CI)
	 */
	public String getNis(){
		return oID_2_16_76_1_3_1.getNIS();
	}
	
	/**
	 * 
	 * @return o numero do Registro Geral - RG do titular
	 */
	public String getRg(){
		return oID_2_16_76_1_3_1.getRg();
	}
	
	/**
	 * 
	 * @return as siglas do orgao expedidor do RG e respectiva UF 
	 */
	public String getOrgaoUfExpedidorRg(){
		return oID_2_16_76_1_3_1.getOrgaoUfExpedidor();
	}
	
	/**
	 * 
	 * @return o numero de inscricao do Titulo de Eleitor
	 */
	public String getTituloEleitor(){
		return oID_2_16_76_1_3_5.getTitulo();
	}
	
	/**
	 * 
	 * @return o numero da Secao do Titulo de Eleitor
	 */
	public String getSecaoTituloEleitor(){
		return oID_2_16_76_1_3_5.getSecao();
	}
	
	/**
	 * 
	 * @return numero da Zona Eleitoral do Titulo de Eleitor
	 */
	public String getZonaTituloEleitor(){
		return oID_2_16_76_1_3_5.getZona();
	}
	
	/**
	 * 
	 * @return o municipio e a UF do Titulo de Eleitor
	 */
	public String getMunicipioUfTituloEleitor(){
		return oID_2_16_76_1_3_5.getMunicipioUf();
	}
	
	/**
	 * 
	 * @return o numero do Cadastro Especifico do INSS (CEI) da pessoa fisica titular do certificado
	 */
	public String getInss(){
		return oID_2_16_76_1_3_6.getInss();
	}
	
	/* TODO - Campo opcional e nao obrigatorio
	 campos otherName, não obrigatórios, contendo:
	OID = 2.16.76.1.4.n e conteúdo = de tamanho variavel correspondente ao número de 
	habilitação ou identificação profissional emitido por conselho de classe ou órgão competente. 
	A AC Raiz, por meio do documento ATRIBUICAO DE OID NA ICPBRASIL [2] regulamentara a correspondência de
	cada conselho de classe ou órgão competente ao conjunto de OID acima definido.	 
	 */
	
}
