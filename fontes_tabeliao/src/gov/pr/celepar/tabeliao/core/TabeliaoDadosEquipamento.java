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

import gov.pr.celepar.tabeliao.core.oid.OID_2_16_76_1_3_2;
import gov.pr.celepar.tabeliao.core.oid.OID_2_16_76_1_3_3;
import gov.pr.celepar.tabeliao.core.oid.OID_2_16_76_1_3_4;
import gov.pr.celepar.tabeliao.core.oid.OID_2_16_76_1_3_8;

/**
 * Esta classe possui as informacoes definidas pela ICP-BRASIL para os
 * certificados de Equipamentos. Estes campos estao definidos no DOC-ICP-04
 * v2.0 de 18/04/2006. 
 * 
 * Veja tambem TabeliaoSubjectAlternativeNames.
 * @author Thiago Meneghello <thiagomeneghello@ecelepar.pr.gov.br> CELEPAR/GIC
 *
 */

public class TabeliaoDadosEquipamento {
	
	private OID_2_16_76_1_3_2 oID_2_16_76_1_3_2 = null;
	private OID_2_16_76_1_3_3 oID_2_16_76_1_3_3 = null;
	private OID_2_16_76_1_3_4 oID_2_16_76_1_3_4 = null;
	private OID_2_16_76_1_3_8 oID_2_16_76_1_3_8 = null;
	
	/**
	 * 
	 * @param oid1 -> 2.16.76.1.3.2 e conteudo = nome do responsavel pelo certificado
	 * 
	 * @param oid2 -> 2.16.76.1.3.3 e conteudo = Cadastro Nacional de Pessoa Juridica (CNPJ),
	 *  se o certificado for de pessoa juridica
	 * 
	 * @param oid3 -> 2.16.76.1.3.4 e conteudo = nas primeiras 8 (oito) posicoes, a data de nascimento do responsavel pelo certificado,
	 *  no formato ddmmaaaa; nas 11 (onze) posicoes subsequentes, o Cadastro de Pessoa Fisica (CPF) do responsavel;
	 *  nas 11 (onze) posicoes subsequentes, o numero de Identificacao Social - NIS (PIS, PASEP ou CI); nas 15 (quinze) posicoes
	 *  subsequentes, o numero do RG do responsavel; nas 6 (seis) posicoes subsequentes, as siglas do orgao expedidor do RG e respectiva UF
	 *  
	 * @param oid4 -> 2.16.76.1.3.8 e conteudo = nome empresarial constante do CNPJ (Cadastro Nacional de Pessoa Juridica),
	 *  sem abreviacoes, se o certificado for de pessoa juridica
	 *  
	 */
	public TabeliaoDadosEquipamento(OID_2_16_76_1_3_2 oid1, OID_2_16_76_1_3_3 oid2, OID_2_16_76_1_3_4 oid3, OID_2_16_76_1_3_8 oid4){
		this.oID_2_16_76_1_3_2 = oid1;
		this.oID_2_16_76_1_3_3 = oid2;
		this.oID_2_16_76_1_3_4 = oid3;
		this.oID_2_16_76_1_3_8 = oid4;
	}

	/**
	 * 
	 * @return string com o nome do responsavel pelo certificado
	 */
	public String getNomeResponsavel(){
		return oID_2_16_76_1_3_2.getNome();
	}
	
	/**
	 * 
	 * @return nome empresarial constante do CNPJ (Cadastro Nacional de Pessoa Juridica)
	 */
	public String getNomeEmpresarial(){
		return oID_2_16_76_1_3_8.getNome();
	}
	
	/**
	 * 
	 * @return numero do CNPJ (Cadastro Nacional de Pessoa Juridica)
	 */
	public String getCNPJ(){
		return oID_2_16_76_1_3_3.getCNPJ();
	}
	
	/**
	 * 
	 * @return data de nascimento do responsavel pelo certificado, no formato Date
	 */
	public Date getDataNascimento(){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			return sdf.parse(oID_2_16_76_1_3_4.getDataNascimento());
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
		return oID_2_16_76_1_3_4.getPIS();
	}
	
	/**
	 * 
	 * @return o numero de Identificacao Social - NIS (PIS, PASEP ou CI)
	 */
	public String getNis(){
		return oID_2_16_76_1_3_4.getNIS();
	}
	/**
	 * 
	 * @return o numero do RG do responsavel
	 */
	public String getRg(){
		return oID_2_16_76_1_3_4.getRg();
	}
	
	/**
	 * 
	 * @return as siglas do orgao expedidor do RG e respectiva UF
	 */
	public String getOrgaoExpedidorRg(){
		return oID_2_16_76_1_3_4.getOrgaoUfExpedidor();
	}

}
