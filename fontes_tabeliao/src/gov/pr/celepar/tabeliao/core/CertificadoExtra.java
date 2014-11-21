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
import gov.pr.celepar.tabeliao.core.oid.OIDGenerico;
import gov.pr.celepar.tabeliao.core.oid.OID_2_16_76_1_3_1;
import gov.pr.celepar.tabeliao.core.oid.OID_2_16_76_1_3_2;
import gov.pr.celepar.tabeliao.core.oid.OID_2_16_76_1_3_3;
import gov.pr.celepar.tabeliao.core.oid.OID_2_16_76_1_3_4;
import gov.pr.celepar.tabeliao.core.oid.OID_2_16_76_1_3_5;
import gov.pr.celepar.tabeliao.core.oid.OID_2_16_76_1_3_6;
import gov.pr.celepar.tabeliao.core.oid.OID_2_16_76_1_3_7;
import gov.pr.celepar.tabeliao.core.oid.OID_2_16_76_1_3_8;

// import java.math.BigInteger;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe Certificado Extra <br>
 * <br>
 * Essa classe possui as informacoes extras definidas pela ICP-BRASIL para
 * os certificado de Pessoa Fisica, Pessoa Juridica e Equipamentos. <br>
 * Os campos estao definidos no DOC-ICP-04 v2.0 de 18/04/2006. <br>
 * 
 * @author thiagom
 *
 */
public class CertificadoExtra {
	
	private static final Integer ZERO = new Integer(0);
	private static final Integer UM = new Integer(1);
	
	private String email = null;
	private Map<String, OIDGenerico> extras = new HashMap<String, OIDGenerico>();

/*	
	private String toString(byte[] data){
		if(data == null) {
			return null;
		}
		return toString(new BigInteger(1, data));
	}

	
	private String toString(BigInteger bi) {
		if(bi == null) {
			return null;
		}
		
		String ret = bi.toString(16);
		
		if(ret.length() % 2 == 1) {
			ret = "0" + ret;
		}
		
		return ret.toUpperCase();
	}
*/	
	/**
	 * Construtor do CertificadoExtra
	 * 
	 * @param certificate
	 */
	public CertificadoExtra(X509Certificate certificate) {
		try {
			if(certificate.getSubjectAlternativeNames() == null) {
				return;
			}
			for (List<?> list : certificate.getSubjectAlternativeNames()){
				if(list.size() != 2) {
					throw new Exception("O tamanho das informacões extras dos certificados não estão corretas.");
				}
				
				Object e1, e2;
				
				e1 = list.get(0);
				e2 = list.get(1);
				
				if(! (e1 instanceof Integer)) {
					throw new Exception("O tipodo parâmetro da informação extra do certificado não e do tipo java.lang.Integer.");
				}
				
				Integer tipo = (Integer)e1;

				if(tipo.equals(ZERO)) {
					byte[] data = (byte[]) e2;
					OIDGenerico oid = OIDGenerico.getInstance(data);
					extras.put(oid.getOid(), oid);
				} else if(tipo.equals(UM)) {
					email = (String)e2;
				}
			}
		} catch (CertificateParsingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Verifica se o certificado e de pessoa fisica
	 * 
	 * @return true = se e um certificado de pessoa fisica.
	 */
	public boolean isCertificadoPF(){
		return extras.get("2.16.76.1.3.1") != null;
	}
	
	/**
	 * Verifica se o certificado e de pessoa juridica
	 * 
	 * @return true = se e um certificado de pessoa juridica.
	 */
	public boolean isCertificadoPJ(){
		return extras.get("2.16.76.1.3.7") != null;
	}
	
	/**
	 * Verifica se o certificado e de equipamento
	 * 
	 * @return true= se e um certificado de pessoa equipamento
	 */
	public boolean isCertificadoEquipamento(){
		return extras.get("2.16.76.1.3.8") != null;
	}
	
	/**
	 * Classe OID 2.16.76.1.3.1 <br>
	 * <br>
	 * Possui alguns atributos de pessoa fisica: <br>
	 * <b>*</b> Data de nascimento do titular "DDMMAAAA" <br>
	 * <b>*</b> Cadastro de pessoa fisica (CPF) do titular <br>
	 * <b>*</b> Numero de Identidade Social - NIS (PIS, PASEP ou CI) <br>
	 * <b>*</b> Numero do Registro Geral (RG) do titular <br>
	 * <b>*</b> Sigla do orgao expedidor do RG <br>
	 * <b>*</b> UF do orgao expedidor do RG <br>
	 * 
	 * @return OID_2_16_76_1_3_1
	 */
	public OID_2_16_76_1_3_1 getOID_2_16_76_1_3_1(){
		return (OID_2_16_76_1_3_1)extras.get("2.16.76.1.3.1");
	}
	
	/**
	 * Classe OID 2.16.76.1.3.5 <br>
	 * <br>
	 * Possui alguns atributos de pessoa fisica: <br>
	 * <b>*</b> Numero de inscricao do Titulo de Eleitor <br>
	 * <b>*</b> Zona Eleitoral <br>
	 * <b>*</b> Secao <br>
	 * <b>*</b> Municipio do titulo <br>
	 * <b>*</b> UF do titulo <br>
	 * 
	 * @return OID_2_16_76_1_3_5
	 */
	public OID_2_16_76_1_3_5 getOID_2_16_76_1_3_5(){
		return (OID_2_16_76_1_3_5)extras.get("2.16.76.1.3.5");
	}

	/**
	 * Classe OID 2.16.76.1.3.6 <br>
	 * <br>
	 * Possui alguns atributos de pessoa fisica: <br>
	 * <b>*</b> Numero do Cadastro Especifico do INSS (CEI) da pessoa fisica titular do certificado <br>
	 * 
	 * @return OID_2_16_76_1_3_6
	 */
	public OID_2_16_76_1_3_6 getOID_2_16_76_1_3_6(){
		return (OID_2_16_76_1_3_6)extras.get("2.16.76.1.3.6");
	}
	
	/**
	 * Classe OID 2.16.76.1.3.2 <br>
	 * <br>
	 * Possui alguns atributos de pessoa juridica ou equipamento: <br>
	 * <b>*</b> Nome do responsavel pelo certificado <br>
	 * 
	 * @return OID_2_16_76_1_3_2
	 */
	public OID_2_16_76_1_3_2 getOID_2_16_76_1_3_2(){
		return (OID_2_16_76_1_3_2)extras.get("2.16.76.1.3.2");
	}
	
	/**
	 * Classe OID 2.16.76.1.3.3 <br>
	 * <br>
	 * Possui alguns atributos de pessoa juridica ou equipamento: <br>
	 * <b>*</b> Cadastro Nacional de Pessoa Juridica (CNPJ) da pessoa juridica titular do certificado <br>
	 * 
	 * @return OID_2_16_76_1_3_3
	 */
	public OID_2_16_76_1_3_3 getOID_2_16_76_1_3_3(){
		return (OID_2_16_76_1_3_3)extras.get("2.16.76.1.3.3");
	}
	
	/**
	 * Classe OID 2.16.76.1.3.4 <br>
	 * <br>
	 * Possui alguns atributos de pessoa juridica ou equipamento: <br>
	 * <b>*</b> Data de nascimento do titular "DDMMAAAA" <br>
	 * <b>*</b> Cadastro de pessoa fisica (CPF) do titular <br>
	 * <b>*</b> Numero de Identidade Social - NIS (PIS, PASEP ou CI) <br>
	 * <b>*</b> Numero do Registro Geral (RG) do titular <br>
	 * <b>*</b> Sigla do orgao expedidor do RG <br>
	 * <b>*</b> UF do orgao expedidor do RG <br>
	 * 
	 * @return OID_2_16_76_1_3_4
	 */
	public OID_2_16_76_1_3_4 getOID_2_16_76_1_3_4(){
		return (OID_2_16_76_1_3_4)extras.get("2.16.76.1.3.4");
	}
	
	/**
	 * Classe OID 2.16.76.1.3.7 <br>
	 * <br>
	 * Possui alguns atributos de pessoa juridica: <br>
	 * <b>*</b> Numero do Cadastro Especifico do INSS (CEI) da pessoa juridica titular do certificado <br>
	 * 
	 * @return OID_2_16_76_1_3_7
	 */
	public OID_2_16_76_1_3_7 getOID_2_16_76_1_3_7(){
		return (OID_2_16_76_1_3_7)extras.get("2.16.76.1.3.7");
	}
	
	/**
	 * Classe OID 2.16.76.1.3.8 <br>
	 * <br>
	 * Possui alguns atributos de equipamento: <br>
	 * <b>*</b> Nome empresarial constante do Cadastro Nacional de Pessoa Juridica (CNPJ),
	 * sem abreviacoes, se o certificado for de pessoa juridica<br>
	 * 
	 * @return OID_2_16_76_1_3_8
	 */
	public OID_2_16_76_1_3_8 getOID_2_16_76_1_3_8(){
		return (OID_2_16_76_1_3_8)extras.get("2.16.76.1.3.8");
	}
	
	/**
	 * 
	 * @return endereco de e-mail armazenado no certificado.
	 */
	public String getEmail(){
		return email;
	}
}
