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
import gov.pr.celepar.framework.exception.ApplicationException;
import gov.pr.celepar.framework.util.Data;
import gov.pr.celepar.tabeliao.core.validacao.TabeliaoResultadoValidacao;
import gov.pr.celepar.tabeliao.dao.CertificadoAcDAO;
import gov.pr.celepar.tabeliao.dao.CertificadoRevogadoDAO;
import gov.pr.celepar.tabeliao.dao.factory.DAOFactory;
import gov.pr.celepar.tabeliao.database.HibernateUtil;
import gov.pr.celepar.tabeliao.pojo.CertificadoAc;
import gov.pr.celepar.tabeliao.pojo.CertificadoPublico;
import gov.pr.celepar.tabeliao.pojo.CertificadoRevogado;
import gov.pr.celepar.tabeliao.util.Download;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.PolicyInformation;
import org.bouncycastle.asn1.x509.X509Extensions;

/**
 * Classe TabeliaoCertificate<br>
 * <br>
 * Essa classe encapsula um objeto do tipo X509Certificate e gera interfaces<br>
 * para leitura das informacoes contidas no certificado.
 * 
 * @author Thiago Meneghello <thiagomeneghello@ecelepar.pr.gov.br>
 *
 */
public class TabeliaoCertificate {

	private static Logger logger = Logger.getLogger(TabeliaoCertificate.class);
	
	public static final String OID_CERTIFICADO_A1 = "2.16.76.1.2.1";
	public static final String OID_CERTIFICADO_A2 = "2.16.76.1.2.2";
	public static final String OID_CERTIFICADO_A3 = "2.16.76.1.2.3";
	public static final String OID_CERTIFICADO_A4 = "2.16.76.1.2.4";
	public static final String OID_CERTIFICADO_S1 = "2.16.76.1.2.101";
	public static final String OID_CERTIFICADO_S2 = "2.16.76.1.2.102";
	public static final String OID_CERTIFICADO_S3 = "2.16.76.1.2.103";
	public static final String OID_CERTIFICADO_S4 = "2.16.76.1.2.104";

	private X509Certificate                 certificate             = null;
	private TabeliaoCertificate             certificadoGerador      = null;
	private TabeliaoSubjectAlternativeNames subjectAlternativeNames = null;
	private TabeliaoKeyUsage                keyUsage                = null;
	private TabeliaoDN                      certificadoDe           = null;
	private TabeliaoDN                      certificadoPara         = null;
	private TabeliaoLCR                     tabeliaoLCR             = null;
	private TabeliaoResultadoValidacao      tabeliaoValidacao       = null;

	/**********************************************************************************************
	 * Mehtodos Privados
	 *********************************************************************************************/
	/**
	 * 
	 * @param data -> array de bytes
	 * @return String 
	 */
	private String toString(byte[] data){
		if(data == null) {
			return null;
		}
		return toString(new BigInteger(1, data));
	}	
	/**
	 * 
	 * @param bi -> Inteiro longo
	 * @return String 
	 */
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

	/**
	 * 
	 * @param certificadoAc
	 * @see gov.pr.celepar.tabeliao.pojo.CertificadoAc
	 * @return X509Certificate
	 * @throws ApplicationException
	 * @throws Exception
	 * @throws IOException
	 */
	private X509Certificate getCertificate(CertificadoAc certificadoAc)
			throws ApplicationException, IOException, Exception {
		X509Certificate cert = null;

		cert = getCertificate(certificadoAc.getArquivo());

		return cert;
	}

	/**
	 * 
	 * @param data -> Array de byte
	 * @return X509Certificate
	 * @throws Exception 
	 */
	private X509Certificate getCertificate(byte[] data)
			throws Exception {
		X509Certificate cert = null;

		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		cert = getCertificate(bis);
		bis.close();
		bis = null;

		return cert;
	}

	/**
	 * 
	 * @param is -> InputStream
	 * @return X509Certificate
	 * @throws CertificateException
	 * @throws IOException
	 * @throws Exception
	 */
	private X509Certificate getCertificate(InputStream is)
			throws CertificateException, IOException, Exception {
		X509Certificate cert = null;

		CertificateFactory cf = CertificateFactory.getInstance("X509");
		cert = (X509Certificate)cf.generateCertificate(is);

		return cert;
	}
	
	/**********************************************************************************************
	 * Construtores da Classe
	 *********************************************************************************************/
	
	/**
	 * 
	 * @param certificate -> tipo X509Certificate
	 * @see java.security.cert.X509Certificate
	 */
	public TabeliaoCertificate(X509Certificate certificate) {
		this.certificate = certificate;
	}
	
	/**
	 * 
	 * @param certificadoAc
	 * @throws ApplicationException
	 * @throws Exception
	 */
	public TabeliaoCertificate(CertificadoAc certificadoAc) throws ApplicationException, Exception {
		this.certificate = getCertificate(certificadoAc);
	}
	
	/**
	 * 
	 * @param data
	 * @throws Exception 
	 */
	public TabeliaoCertificate(byte[] data) throws Exception {
		this.certificate = getCertificate(data);
	}
	
	/**
	 * 
	 * @param is
	 * @throws Exception 
	 * @throws IOException 
	 */
	public TabeliaoCertificate(InputStream is) throws IOException, Exception {
		this.certificate = getCertificate(is);
	}

	/**
	 * 
	 * @param certificadoPublico
	 * @throws Exception 
	 * @see gov.pr.celepar.tabeliao.pojo.CertificadoPublico
	 */
	public TabeliaoCertificate(CertificadoPublico certificadoPublico) throws Exception{
		this.certificate = getCertificate(certificadoPublico.getArquivo());
	}

	/**********************************************************************************************
	 * Metodos que estao revisados
	 *********************************************************************************************/

	/**
	 * Retorna o certificado no formato original X509Certificate<br>
	 * 
	 * @return X509Certificate
	 */
	public X509Certificate getX509Certificate(){
		return certificate;
	}

	/**
	 * Retorna o IssuerDn do certificado no formato TabeliaoDN que funciona<br>
	 * como um Properties.<br>
	 * O metodo toString dessa classe retorna o IssuerDn.getName()<br>
	 * 
	 * @return TabeliaoDN
	 * @see TabeliaoDN
	 * @throws IOException 
	 */
	public TabeliaoDN getCertificadoDe() throws IOException{
		if(certificadoDe == null) {
			certificadoDe = new TabeliaoDN(certificate.getIssuerDN().getName());
		}
		return certificadoDe;
	}

	/**
	 * Retorna o SerialNumber do certificado no formato de String<br>
	 * 
	 * @return String
	 */
	public String getSerialNumber(){
		return toString(certificate.getSerialNumber());
	}
	
	/**
	 * Retorna o SubjectDN do certificado no formato TabeliaoDN que funciona<br>
	 * como um Properties.<br>
	 * O Metodo toString dessa classe retorna o SubjectDN.getName()<br>
	 * 
	 * @return TabeliaoDN
	 * @see TabeliaoDN
	 * @throws IOException 
	 */
	public TabeliaoDN getCertificadoPara() throws IOException{
		if(certificadoPara == null) {
			certificadoPara = new TabeliaoDN(certificate.getSubjectDN().getName());
		}
		return certificadoPara;
	}

	/**
	 * Retorna o nome que esta definido no CN do CertificadoPara.<br>
	 * Equivalente a chamada getCertificadoPara().getProperty("CN") ignorando<br>
	 * a informacao que aparece depois dos dois pontos.<br>
	 * 
	 * @return String
	 */
	public String getNome(){
		try {
			String nome = this.getCertificadoPara().getProperty("CN");
			int pos;
			
			pos = nome.indexOf(":");
			if(pos > 0) {
				return nome.substring(0, pos);
			}
			return nome;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @return Date -> Inicio da Validade
	 */
	public Date getValidadeDe(){
		return certificate.getNotBefore();
	}
	
	/**
	 * 
	 * @return Date -> Fim da validade
	 */
	public Date getValidadeAte(){
		return certificate.getNotAfter();
	}
	
	/**
	 * Retorna um Objeto TabeliaoKeyUsage com as informacoes sobre o Uso<br>
	 * do certificado.<br>
	 * 
	 * @return TabeliaoKeyUsage
	 * @see TabeliaoKeyUsage
	 */
	public TabeliaoKeyUsage getTabeliaoKeyUsage(){
		if(keyUsage == null) {
			keyUsage = new TabeliaoKeyUsage(certificate);
		}
		return keyUsage;
	}
	
	/**
	 * Retorna o SubjectAlternativeNames do certificado no formato<br>
	 * TabeliaoSubjectAlternativeNames.<br>
	 * Caso nao exista essa informacao retorna o valor <b>null</b>.<br>
	 * 
	 * @return TabeliaoSubjectAlternativeNames
	 * @see TabeliaoSubjectAlternativeNames
	 */
	public TabeliaoSubjectAlternativeNames getTabeliaoSubjectAlternativeNames(){
		if(this.subjectAlternativeNames == null) {
			this.subjectAlternativeNames = new TabeliaoSubjectAlternativeNames(this.certificate);
		}
		return this.subjectAlternativeNames;
	}

	/**
	 * Retorna o email que esta definido no SubjectAlternativeNames do certificado.<br>
	 * Equivalente a chamada getTabeliaoSubjectAlternativeNames().getEmail()<br>
	 * Caso nao exista essa informacao retorna o valor <b>null</b>.<br>
	 * 
	 * @return String
	 */
	public String getEmail(){
		if(getTabeliaoSubjectAlternativeNames() == null) {
			return null;
		}
		return getTabeliaoSubjectAlternativeNames().getEmail();
	}

	/**
	 * Verifica se o certificado possui dados de Pessoa Fisica.<br>
	 * Equivalente a chamada getTabeliaoSubjectAlternativeNames().isDadosPF()<br>
	 * 
	 * @return boolean
	 */
	public boolean hasDadosPF(){
		if(getTabeliaoSubjectAlternativeNames() == null) {
			return false;
		}
		return getTabeliaoSubjectAlternativeNames().isDadosPF();
	}
	
	/**
	 * Retorna os dados de Pessoa Fisica do certificado no formato<br>
	 * TabeliaoDadosPF.<br>
	 * Equivalente a chamada getTabeliaoSubjectAlternativeNames().getTabeliaoDadosPF()<br>
	 * Retorna o valor <b>null</b> caso o certificado nao possua os dados de Pessoa<br>
	 * Fisica.<br>
	 *
	 * @return TabeliaoDadosPF
	 * @see TabeliaoDadosPF
	 */
	public TabeliaoDadosPF getTabeliaoDadosPF(){
		if(getTabeliaoSubjectAlternativeNames() == null) {
			return null;
		}
		return getTabeliaoSubjectAlternativeNames().getTabeliaoDadosPF();
	}

	/**
	 * Verifica se o certificado possui dados de Pessoa Juridica.<br>
	 * Equivalente a chamada getTabeliaoSubjectAlternativeNames().isDadosPJ()<br>
	 * 
	 * @return boolean
	 */
	public boolean hasDadosPJ(){
		if(getTabeliaoSubjectAlternativeNames() == null) {
			return false;
		}
		return getTabeliaoSubjectAlternativeNames().isDadosPJ();
	}
	
	/**
	 * Retorna os dados de Pessoa Juridica do certificado no formato<br>
	 * TabeliaoDadosPJ.<br>
	 * Equivalente a chamada getTabeliaoSubjectAlternativeNames().getTabeliaoDadosPJ()<br>
	 * Retorna o valor <b>null</b> caso o certificado nao possua os dados de Pessoa<br>
	 * Juridica.<br>
	 * 
	 * @return TabeliaoDadosPJ
	 * @see TabeliaoDadosPJ
	 */
	public TabeliaoDadosPJ getTabeliaoDadosPJ(){
		if(getTabeliaoSubjectAlternativeNames() == null) {
			return null;
		}
		return getTabeliaoSubjectAlternativeNames().getTabeliaoDadosPJ();
	}
	
	/**
	 * Verifica se o certificado possui dados de Equipamento.<br>
	 * Equivalente a chamada getTabeliaoSubjectAlternativeNames().isDadosEquipamento()<br>
	 * 
	 * @return boolean
	 */
	public boolean hasDadosEquipamento(){
		if(getTabeliaoSubjectAlternativeNames() == null) {
			return false;
		}
		return getTabeliaoSubjectAlternativeNames().isDadosEquipamento();
	}
	
	/**
	 * Retorna os dados de Equipamento do certificado no formato<br>
	 * TabeliaoDadosEquipamento.<br>
	 * Equivalente a chamada getTabeliaoSubjectAlternativeNames().getTabeliaoDadosEquipamento()<br>
	 * Retorna o valor <b>null</b> caso o certificado nao possua os dados de Equipamento.<br>
	 * 
	 * @return TabeliaoDadosEquipamento
	 * @see TabeliaoDadosEquipamento
	 */
	public TabeliaoDadosEquipamento geTabeliaoDadosEquipamento(){
		if(getTabeliaoSubjectAlternativeNames() == null) {
			return null;
		}
		return getTabeliaoSubjectAlternativeNames().getTabeliaoDadosEquipamento();
	}

	/**
	 * Retorna o valor PathLength do BasicConstraint do certificado.<br>
	 * * <b>0</b> - Caso seja de uma AC.<br>
	 * * <b>1</b> - Caso seja de Usuario Final.<br>
	 * 
	 * @return int 
	 */
	public int getPathLength(){
		return certificate.getBasicConstraints();
	}
	
	/**
	 * Verifica se o certificado eh de uma Autoridade Certificadora (AC).<br>
	 * * <b>true</b> - Caso seja de uma AC.<br>
	 * * <b>false</b> - Caso seja de Usuario Final.<br>
	 * 
	 * @return boolean 
	 */
	public boolean isCertificadoAc(){
		return certificate.getBasicConstraints() >= 0;
	}
	
	/**
	 * Verifica qual o tipo do certificado (A1, A2, A3, A4, S1, S2, S3, S4).<br>
	 * Retorna o valor <b>null</b> caso nao possua a extensao CertificatePolicies.
	 * 
	 * @return String
	 */
	public String getTipoCertificado() {
		try {
			DERSequence seq = (DERSequence)getExtensionValue(X509Extensions.CertificatePolicies.getId());
			if(seq == null) {
				return null;
			}
	        for(int pos = 0 ; pos < seq.size() ; pos++) {
		        PolicyInformation pol = new PolicyInformation((DERSequence) seq.getObjectAt(pos));
		        
		        String id = pol.getPolicyIdentifier().getId();
		        if(id == null) {
		        	continue;
		        }
		        
				if(id.startsWith(OID_CERTIFICADO_A1)) {
					return "A1";
				}
				if(id.startsWith(OID_CERTIFICADO_A2)) {
					return "A2";
				}
				if(id.startsWith(OID_CERTIFICADO_A3)) {
					return "A3";
				}
				if(id.startsWith(OID_CERTIFICADO_A4)) {
					return "A4";
				}
				if(id.startsWith(OID_CERTIFICADO_S1)) {
					return "S1";
				}
				if(id.startsWith(OID_CERTIFICADO_S2)) {
					return "S2";
				}
				if(id.startsWith(OID_CERTIFICADO_S3)) {
					return "S3";
				}
				if(id.startsWith(OID_CERTIFICADO_S4)) {
					return "S4";
				}
	        }
	        return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Retorna o valor da extensao AuthorityKeyIdentifier no formato String.<br>
	 * Caso essa informacao nao esteja no certificado, retorna <b>null</b>.<br>
	 * 
	 * @return String
	 * @throws IOException
	 */
	public String getAuthorityKeyIdentifier() throws IOException {
		//TODO - Precisa validar este metodo com a RFC
		DERSequence seq = (DERSequence) getExtensionValue(X509Extensions.AuthorityKeyIdentifier.getId());
        if(seq == null || seq.size() == 0) {
        	return null;
        }
        DERTaggedObject tag = (DERTaggedObject)seq.getObjectAt(0);
        DEROctetString oct = (DEROctetString)DEROctetString.getInstance(tag);
        
        return toString(oct.getOctets());
	}

	/**
	 * Retorna o valor da extensao SubjectKeyIdentifier no formato String.<br>
	 * Caso essa informacao nao esteja no certificado, retorna <b>null</b>.<br>
	 * 
	 * @return String
	 */
	public String getSubjectKeyIdentifier() throws IOException {
		//TODO - Precisa validar este metodo com a RFC
		DEROctetString oct = (DEROctetString)getExtensionValue(X509Extensions.SubjectKeyIdentifier.getId());
        if(oct == null) {
        	return null;
        }
        
        return toString(oct.getOctets());
	}

	/**
	 * Retorna URL da Lista de Certificados Revogados (CRL). Pode haver mais de uma dependendo do 
	 * emissor do certificado<br>
	 * Mesmo que a CRL possua mais que uma fonte, retorna apenas a URL.<br>
	 * Caso essa informacao nao esteja no certificado, retorna <b>null</b>.<br>
	 * 
	 * @return String
	 * @throws IOException 
	 */
	public List<String> getCRLDistributionPoint() throws IOException{
		//TODO - Precisa validar este metodo com a RFC
		
		List<String> lcrS = new ArrayList<String>();
		DERObject derObj = getExtensionValue(X509Extensions.CRLDistributionPoints.getId());
		if(derObj == null) {
			return null;
		}
		CRLDistPoint crlDistPoint = CRLDistPoint.getInstance(derObj);
		DistributionPoint[] dp = crlDistPoint.getDistributionPoints();
		for(int i=0 ; i<dp.length ; i++) {
			DERSequence seq = (DERSequence)new ASN1InputStream(dp[i].getDistributionPoint().getName().getDEREncoded()).readObject();
			DERTaggedObject tag = (DERTaggedObject) seq.getObjectAt(0);
			try{
				ASN1OctetString oct = (DEROctetString)DEROctetString.getInstance(tag);
				lcrS.add( new String(oct.getOctets()));
			}catch (Exception e){
				// Não é um objeto com informação de DistributionPoint
			}
			// codificação antiga feita pelo Thiago, pegava apenas a primeira URL.
			//URL da WEB
			//if(tag.getTagNo() == 6) {
			//	DEROctetString oct = (DEROctetString)DEROctetString.getInstance(tag);
			//	return new String(oct.getOctets());
			//}
			
		}
		return lcrS;
	}
	
	/**
	 * Retorna um Objeto do tipo DERObject contendo o valor da extensao passado<br>
	 * atraves do OID.<br>
	 * 
	 * @param oid
	 * @return DERObject
	 * @see DERObject
	 */
	public DERObject getExtensionValue(String oid){
		byte[] extvalue = certificate.getExtensionValue(oid);
        if (extvalue == null) {
            return null;
        }
        try {
			DEROctetString oct = (DEROctetString) (new ASN1InputStream(extvalue).readObject());
			return (new ASN1InputStream(oct.getOctets()).readObject());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**********************************************************************************************
	 * Metodos Publicos Complementares sobre os certificados 
	 *********************************************************************************************/
	
	/**
	 * Retorna o certificado da AC que assinou 
	 * @return TabeliaoCertificate
	 * @see TabeliaoCertificate
	 * @throws Exception
	 */
	public TabeliaoCertificate getCertificadoGerador() throws Exception {
		if(certificadoGerador == null) {
			//Se eh um certificado auto-assinado retorna ele mesmo
			if(certificate.getSubjectDN().getName().equals(certificate.getIssuerDN().getName())) {
				certificadoGerador = this;
			} else {
				try {
					HibernateUtil.currentSession();
					
					CertificadoAcDAO certificadoAcDAO = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getCertificadoAcDAO();
					CertificadoAc certificadoAc = certificadoAcDAO.buscarPorKeyId(getAuthorityKeyIdentifier());
					
					if(certificadoAc == null) {
						throw new Exception("Não foi possível localizar o certificado da Autoridade Certificadora: " + getX509Certificate().getIssuerDN().getName());
					}
					
					certificadoGerador = new TabeliaoCertificate(certificadoAc);
				}catch (Exception e) {
					throw new Exception("Problemas ao tentar localizar o certificado da Autoridade Certificadora: " + getX509Certificate().getIssuerDN().getName());
				}finally {
					HibernateUtil.closeSession();
				}
			}
		}
		return certificadoGerador;
	}

	/**
	 * Carrega uma List com a cadeia de certificados, onde o primeiro certificado <br>
	 * e o certificado Raiz, e o ultimo certificado eh este.<br>
	 * 
	 * @return List<TabeliaoCertificate>
	 * @throws Exception 
	 * @see TabeliaoCertificate
	 */
	public List<TabeliaoCertificate> getCadeiaCertificados() throws Exception {
		List<TabeliaoCertificate> list = null;
		
		if(certificate.getSubjectDN().getName()
				.equals(certificate.getIssuerDN().getName())) {
			list = new LinkedList<TabeliaoCertificate>();
			
			list.add(this);
			return list;
		}
		
		try {
			list = getCertificadoGerador().getCadeiaCertificados();
		} catch (Exception e) {
			//list = new LinkedList<TabeliaoCertificate>();
			throw new Exception("Problemas na geração da Cadeia"+e.getMessage());
		}
		list.add(this);
		
		return list;
	}
	
	/**
	 * Carrega uma List com a cadeia de certificados na ordem inversa, ou seja, <br>
	 * o primeiro certificado eh este e o ultimo certificado eh o Raiz.<br>
	 * 
	 * @return List<TabeliaoCertificate>
	 * @throws Exception 
	 * @see TabeliaoCertificate
	 */
	public List<TabeliaoCertificate> getCadeiaCertificadosInversa() throws Exception {
		List<TabeliaoCertificate> list = null;
		
		if(certificate.getSubjectDN().getName()
				.equals(certificate.getIssuerDN().getName())) {
			list = new LinkedList<TabeliaoCertificate>();
			
			list.add(this);
			return list;
		}
		
		try {
			list = getCertificadoGerador().getCadeiaCertificadosInversa();
		} catch (Exception e) {
			//list = new LinkedList<TabeliaoCertificate>();
			throw new Exception("Problemas na geração da Cadeia"+e.getMessage());
		}
		list.add(0, this);
		
		return list;
	}
	
	/**
	 * Recupera a Lista de Certificados Revogados (LCR), primeiramente com uma pesquisa na base de 
	 * dados do Tabeliao, caso nao seja encontrada a mesma sera recuperada atraves do atributo
	 * CRLDistributionPoint armazenado no Certificado, neste caso sera necessario o acesso a internet.
	 * A nova lista sera atualizada na Base.
	 *  
	 * @return TabeliaoLCR
	 * @see TabeliaoLCR
	 * @throws Exception
	 */
	public TabeliaoLCR getTabeliaoLCR() throws Exception {
		if(tabeliaoLCR == null) {
			// Primeiro tenta encontrar a LCR na base de dados do Tabelião
			try {				
				HibernateUtil.currentSession();
				CertificadoRevogadoDAO certRevogadoDAO = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getCertificadoRevogadoDAO();
				CertificadoRevogado certRevogado = certRevogadoDAO.buscarPorKeyId(getAuthorityKeyIdentifier());
				// Caso não encontre a LCR será baixada uma do Ponto de Distribuição
				if(certRevogado == null) {
					List<String> pontosLCR = getCRLDistributionPoint();
					if(pontosLCR.size() == 0) {
						return null;
					}
					// Iniciando a busca nos pontos
					for (int i=0; i < pontosLCR.size(); i++ ){
						boolean baixou = false;
						String urlNova = null;
						try {
							urlNova = pontosLCR.get(i);
							logger.info("\n Tentando Baixar Lista, ..."+urlNova+"\n");
							byte[] buffer = Download.downloadByteArray(urlNova);
							TabeliaoLCR lcr = new TabeliaoLCR(buffer);
							logger.info("\n Baixou a lista, ... Busca gerador e valida LCR \n");
							TabeliaoCertificate cert = getCertificadoGerador();					
							lcr.getCRL().verify(cert.getX509Certificate().getPublicKey());
							logger.info("\n Busca Autoridade!\n");
							CertificadoAcDAO certificadoAcDAO = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getCertificadoAcDAO();					
							CertificadoAc certificadoAc = certificadoAcDAO.buscarPorKeyId(getCertificadoGerador().getSubjectKeyIdentifier());
							logger.info("\n Gravando nova lista: "+urlNova+"\n");
							certRevogado = new CertificadoRevogado();
							certRevogado.setArquivo(buffer);
							certRevogado.setAutoAtualizacao(true);
							certRevogado.setCertificadoAc(certificadoAc);
							certRevogado.setDtInclusao(new Date());
							certRevogado.setDtProximaAtualizacao(lcr.getCRL().getNextUpdate());
							certRevogado.setDtUltimaAtualizacao(lcr.getCRL().getThisUpdate());
							certRevogado.setEnderecoWeb(urlNova);
							certRevogado.setVersao(1);
							baixou = true;
							try {
								HibernateUtil.currentTransaction();
								certRevogadoDAO.salvar(certRevogado);
								HibernateUtil.commitTransaction();
							} catch (Exception e) {
								HibernateUtil.rollbackTransaction();
								logger.error("Erro ao salvar a lista de certificados revogados no banco de dados.");
							}
						}catch (Exception e) {
							logger.warn("Lista não encontrada, ou problemas."+urlNova);
						}
						// se já encontrou a LCR não percorrerá todos os pontos
						if (baixou){
							break;
						}
					}				
					tabeliaoLCR = new TabeliaoLCR(certRevogado.getArquivo());
				}
				// A LCR foi encontrada no Banco
				else{
					TabeliaoLCR lcr =  new TabeliaoLCR(certRevogado.getArquivo());
					// Verifica a validade da lista encontrada no Banco
					// Se estiver vencida será baixada uma nova.
					if(lcr.getCRL().getNextUpdate().before(new Date())) {
						List<String> pontosLCR = getCRLDistributionPoint();
						for (int i=0; i < pontosLCR.size(); i++ ){
							String urlVencida = null;
							boolean baixou = false;
							try {
								urlVencida = pontosLCR.get(i);
								logger.warn("Lista vencida, recuperando nova: " + urlVencida);
								byte[] buffer = Download.downloadByteArray(urlVencida);
								TabeliaoLCR lcrN = new TabeliaoLCR(buffer);
								TabeliaoCertificate cert = new TabeliaoCertificate(certRevogado.getCertificadoAc());
								lcrN.getCRL().verify(cert.getX509Certificate().getPublicKey());
								// verifica se a lista baixada e mais atual que a da Base e atualiza
								if(lcrN.getCRL().getThisUpdate().compareTo(certRevogado.getDtUltimaAtualizacao()) > 0) {
									certRevogado.setVersao(certRevogado.getVersao() + 1);
									certRevogado.setDtUltimaAtualizacao(lcrN.getCRL().getThisUpdate());
									certRevogado.setDtProximaAtualizacao(lcrN.getCRL().getNextUpdate());
									certRevogado.setArquivo(buffer);
									baixou = true;
									try {
										HibernateUtil.currentTransaction();
										certRevogadoDAO.salvar(certRevogado);
										HibernateUtil.commitTransaction();
									} catch (Exception e) {
										HibernateUtil.rollbackTransaction();
										System.err.println("Erro ao salvar a lista de certificados revogados no banco de dados."+e.getMessage());
									}
									tabeliaoLCR = new TabeliaoLCR(certRevogado.getArquivo());
								}
							}catch (Exception e) {
								logger.error("Lista não encontrada, ou problemas."+urlVencida);
							}
							// Se foi baixada a nova lista, não é necessário percorrer os demais pontos.
							if (baixou){
								break;
							}								
						}
					}
					// Lista encontrada na Base está vigente.
					else{
						tabeliaoLCR = new TabeliaoLCR(certRevogado.getArquivo());
					}
				}				
			}catch (Exception e) {
				logger.info("\n Exceção na busca de LCR: !"+e.getMessage());
				HibernateUtil.closeSession();
				throw new Exception("Erro na busca da LCR: Lista Indisponível ou Atributo de URL Incorreto.\n"+ getCRLDistributionPoint()+"\nAutoridade:"+getAuthorityKeyIdentifier());
			}
				finally {
				HibernateUtil.closeSession();
			}
		}
		return tabeliaoLCR;
	}
	
	/**
	 * Efetua a validacao completa (data de validade, cadeia e LCR) do certificado
	 * @return TabeliaoResultadoValidacao
	 * @throws Exception 
	 * @see TabeliaoResultadoValidacao
	 * @throws Exception
	 */	
	public TabeliaoResultadoValidacao valida() throws Exception{
		
		if(tabeliaoValidacao == null) {
			tabeliaoValidacao = new TabeliaoResultadoValidacao();
				validaValidadeCertificado(tabeliaoValidacao);
				validaCadeiaCertificado(tabeliaoValidacao);
				validaCertificadoRevogado(tabeliaoValidacao);
		}		
		return tabeliaoValidacao;
	}
	
	
	/**
	 * Recupera o resultado da validacao completa (data de validade, cadeia e LCR) do certificado
	 * @return TabeliaoResultadoValidacao
	 * @throws Exception 
	 * @see TabeliaoResultadoValidacao
	 */
	public TabeliaoResultadoValidacao getResultadoValidacao() throws Exception{
		return valida();
	}

	/**
	 * Efetua a verificacao da data de vigencia do Certificado comparando com a data Atual 
	 * do sistema.
	 * @return TabeliaoResultadoValidacao
	 * @throws Exception 
	 * @see TabeliaoResultadoValidacao
	 */
	public TabeliaoResultadoValidacao validaDataVigencia() throws Exception{
		
		if(tabeliaoValidacao == null) {
			tabeliaoValidacao = new TabeliaoResultadoValidacao();
				validaValidadeCertificado(tabeliaoValidacao);
		}		
		return tabeliaoValidacao;
	}
	
	/**
	 * Recupera o resultado da validacao da data de validade do certificado
	 * @return TabeliaoResultadoValidacao
	 * @throws Exception 
	 * @see TabeliaoResultadoValidacao
	 */
	public TabeliaoResultadoValidacao getResultadoDataVigencia() throws Exception{
		return validaDataVigencia();
	}
	
	
	/**
	 * Efetua a validacao da cadeia de certificacao
	 * @return TabeliaoResultadoValidacao
	 * @throws Exception 
	 * @see TabeliaoResultadoValidacao
	 * @throws Exception
	 */	
	public TabeliaoResultadoValidacao validaCadeia() throws Exception{
		
		if(tabeliaoValidacao == null) {
			tabeliaoValidacao = new TabeliaoResultadoValidacao();
				validaCadeiaCertificado(tabeliaoValidacao);
		}		
		return tabeliaoValidacao;
	}
	
	
	/**
	 * Recupera o resultado da validacao da cadeia de certificacao
	 * @return TabeliaoResultadoValidacao
	 * @throws Exception 
	 * @see TabeliaoResultadoValidacao
	 */
	public TabeliaoResultadoValidacao getResultadoCadeia() throws Exception{
		return validaCadeia();
	}
	
	/**
	 * Efetua a validacao da LCR (Lista de Certificados Revogados)
	 * @return TabeliaoResultadoValidacao
	 * @throws Exception 
	 * @see TabeliaoResultadoValidacao
	 * @throws Exception
	 */	
	public TabeliaoResultadoValidacao validaLCR() throws Exception{
		
		if(tabeliaoValidacao == null) {
			tabeliaoValidacao = new TabeliaoResultadoValidacao();
				validaCertificadoRevogado(tabeliaoValidacao);
		}		
		return tabeliaoValidacao;
	}
	
	
	/**
	 * Recupera o resultado da validacao da LCR (Lista de Certificados Revogados)
	 * @return TabeliaoResultadoValidacao
	 * @throws Exception 
	 * @see TabeliaoResultadoValidacao
	 */
	public TabeliaoResultadoValidacao getResultadoLCR() throws Exception{
		return validaLCR();
	}
		
	/**
	 * Efetua uma verificacao da validade do certificado, compara com a data atual
	 * @param trv -> TabeliaoResultadoValidacao
	 * @throws Exception 
	 * @see TabeliaoResultadoValidacao
	 * @throws Exception
	 */
	private void validaValidadeCertificado(TabeliaoResultadoValidacao trv) throws Exception{
		validaValidadeCertificado(trv, new Date());
	}
	
	/**
	 * Efetua uma verificacao da validade do certificado, compara com a data informada no parametro
	 * e informa a validade com relacao a esta data.
	 * Gera um AVISO quando o prazo de validade se aproxima.
	 * @param trv -> TabeliaoResultadoValidacao
	 * @param date -> Data com a qual sera comparada a validade do certificado.
	 * @throws Exception 
	 * @throws Exception 
	 * @throws Exception 
	 * @see TabeliaoResultadoValidacao
	 * @throws Exception
	 */
	private void validaValidadeCertificado(TabeliaoResultadoValidacao trv, Date date) throws Exception{
		
		try {
			certificate.checkValidity(date);
			Date date30 = new Date(date.getTime() + (30 * 86400000l)); // 86400000l == 1 dia em milisegundos 
			try {
				certificate.checkValidity(date30);
				trv.addOk(TabeliaoResultadoValidacao.VAL_VALIDADE);
			} catch (CertificateExpiredException e1) {
				trv.addAviso(TabeliaoResultadoValidacao.VAL_VALIDADE, "Este certificado está para vencer em " + Data.formataData(certificate.getNotAfter()));
				return;
			}
		} catch (CertificateExpiredException e1) {
			trv.addErro(TabeliaoResultadoValidacao.VAL_VALIDADE, "Este certificado está vencido! \n"+e1.getMessage());
			return;
		} catch (CertificateNotYetValidException e1) {
			trv.addErro(TabeliaoResultadoValidacao.VAL_VALIDADE, "Este certificado ainda não está habilitado! \n"+e1.getMessage());
			return;
		} catch (Exception e) {
			trv.addExcecao(TabeliaoResultadoValidacao.VAL_VALIDADE, e.getMessage());
			return;
		}
			
	}
	
	/**
	 * Efetua a validacao completa da cadeia do certificado ate a raiz
	 * @param trv -> TabeliaoResultadoValidacao
	 * @throws Exception 
	 * @throws Exception 
	 * @see TabeliaoResultadoValidacao
	 */
	private void validaCadeiaCertificado(TabeliaoResultadoValidacao trv) throws Exception {
		
		if (certificate.getSubjectDN().getName()
				.equals(certificate.getIssuerDN().getName())) {
			// Eh um certificado de uma AC Raiz
			//logger.debug("*** Verificando Certificado Raiz: " + certificate.getSubjectDN().getName());
			
			try {
				certificate.verify(certificate.getPublicKey());
			} catch (InvalidKeyException e) {
				trv.addErro(TabeliaoResultadoValidacao.VAL_CADEIA, "Chave Inválida:"+e.getMessage());
				return;
			} catch (CertificateException e) {
				trv.addErro(TabeliaoResultadoValidacao.VAL_CADEIA, "Certificado com Problema: "+e.getMessage());
				return;
			} catch (NoSuchAlgorithmException e) {
				trv.addErro(TabeliaoResultadoValidacao.VAL_CADEIA, "Sem Algoritimo: "+e.getMessage());
				return;
			} catch (NoSuchProviderException e) {
				trv.addErro(TabeliaoResultadoValidacao.VAL_CADEIA, "Sem provider:"+e.getMessage());
				return;
			} catch (SignatureException e) {
				trv.addErro(TabeliaoResultadoValidacao.VAL_CADEIA, "Erro de Assinatura:"+e.getMessage());
				return;
			}catch (Exception e) {
				trv.addExcecao(TabeliaoResultadoValidacao.VAL_CADEIA, e.getMessage());
				return;
			}			
			
		} else {
			// Eh um certificado normal
			//logger.debug("*** Verificando Certificado de nível: " + certificate.getSubjectDN().getName());
			//logger.debug("*** Assinado por: " + certificate.getIssuerDN().getName());
			TabeliaoCertificate certGer = null;
			try {
				certGer = getCertificadoGerador();
			} catch (Exception e) {
				trv.addExcecao(TabeliaoResultadoValidacao.VAL_CADEIA, e.getMessage());
				return;
			}
			X509Certificate cert = certGer.getX509Certificate();				
			try {
				certificate.verify(cert.getPublicKey());
			} catch (InvalidKeyException e) {
				trv.addErro(TabeliaoResultadoValidacao.VAL_CADEIA, "Chave Inválida:"+e.getMessage());
				return;
			} catch (CertificateException e) {
				trv.addErro(TabeliaoResultadoValidacao.VAL_CADEIA, "Certificado com Problema: "+e.getMessage());
				return;
			} catch (NoSuchAlgorithmException e) {
				trv.addErro(TabeliaoResultadoValidacao.VAL_CADEIA, "Sem Algoritimo: "+e.getMessage());
				return;
			} catch (NoSuchProviderException e) {
				trv.addErro(TabeliaoResultadoValidacao.VAL_CADEIA, "Sem provider:"+e.getMessage());
				return;
			} catch (SignatureException e) {
				trv.addErro(TabeliaoResultadoValidacao.VAL_CADEIA, "Erro de Assinatura:"+e.getMessage());
				return;
			} catch (Exception e) {
				trv.addExcecao(TabeliaoResultadoValidacao.VAL_CADEIA, e.getMessage());
				return;
			}
		}
		trv.addOk(TabeliaoResultadoValidacao.VAL_CADEIA);
		

	}
	
	/**
	 * Verifica se o certificado nao foi revogado, de acordo com a LCR.
	 * Gera um Aviso em TabeliaoResultadoValidacao nos seguintes casos:
	 * - A lista nao foi localizada no Banco ou nem na URL
	 * - A lista da base esta desatualizada.
	 * @param trv -> TabeliaoResultadoValidacao
	 * @throws Exception 
	 * @throws Exception 
	 * @see TabeliaoResultadoValidacao
	 */
	private void validaCertificadoRevogado(TabeliaoResultadoValidacao trv) throws Exception {
		
		if (certificate.getSubjectDN().getName()
				.equals(certificate.getIssuerDN().getName())) {
			// Eh um certificado de uma AC Raiz, portanto não há validação de Revogação.
			//logger.debug("*** Verificando Certificado Raiz: " + certificate.getSubjectDN().getName());
			
		} else {
			// Eh um certificado normal
			//logger.debug("*** Verificando Certificado de nível: " + certificate.getSubjectDN().getName());
			//logger.debug("*** Assinado por: " + certificate.getIssuerDN().getName());
			
				TabeliaoLCR lcr;
				try {
					lcr = getTabeliaoLCR();
				} catch (Exception e) {
					trv.addExcecao(TabeliaoResultadoValidacao.VAL_LCR, e.getMessage());
					return;
				} 
				if(lcr == null) {
					trv.addExcecao(TabeliaoResultadoValidacao.VAL_LCR, "Não foi possível localizar a lista de certificados revogados.\n "+ getCRLDistributionPoint());
					return;
				}
				
				if(lcr.getCRL().isRevoked(certificate)) {
					trv.addErro(TabeliaoResultadoValidacao.VAL_LCR, "Este certificado está revogado.");
					return;
				}
				
				if(lcr.getCRL().getNextUpdate().before(new Date())) {
					trv.addAviso(TabeliaoResultadoValidacao.VAL_LCR, "A Lista de Certificados Revogados está vencida.\n" + getCRLDistributionPoint());
					return;
				}
				
		}
		trv.addOk(TabeliaoResultadoValidacao.VAL_LCR);
	}
	
	public String toString(){
		StringBuffer sBuffer = new StringBuffer();
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			
			sBuffer.append("*********************************\n");
			sBuffer.append("Certificado DE  : " + this.getCertificadoDe() + "\n");
			sBuffer.append("Serial Number . : " + this.getSerialNumber() + "\n");
			sBuffer.append("Certificado PARA: " + this.getCertificadoPara() + "\n");
			sBuffer.append("Nome do Certif  : " + this.getNome() + "\n");
			sBuffer.append("Validade  . . . : de " + sdf.format(this.getValidadeDe()) + " ate " + sdf.format(this.getValidadeAte()) + "\n");
			sBuffer.append("*********************************\n");
			sBuffer.append("Email . . . . . : " + this.getEmail() + "\n");
			sBuffer.append("*********************************\n");
			sBuffer.append("Tem dados PF  . : " + this.hasDadosPF() + "\n");
			if(this.hasDadosPF()) {
				TabeliaoDadosPF tdPF = this.getTabeliaoDadosPF();
				sBuffer.append("CPF . . . . . . : " + tdPF.getCPF() + "\n");
				sBuffer.append("Data Nascimento : " + sdf.format(tdPF.getDataNascimento()) + "\n");
				sBuffer.append("PIS . . . . . . : " + tdPF.getNis() + "\n");
				sBuffer.append("Rg  . . . . . . : " + tdPF.getRg() + " " + tdPF.getOrgaoUfExpedidorRg() + "\n");
				sBuffer.append("Inss  . . . . . : " + tdPF.getInss() + "\n");
				sBuffer.append("Titulo  . . . . : " + tdPF.getTituloEleitor() + "\n");
				sBuffer.append("Seção . . . . . : " + tdPF.getSecaoTituloEleitor() + "\n");
				sBuffer.append("Zona  . . . . . : " + tdPF.getZonaTituloEleitor() + "\n");
				sBuffer.append("Municipio UF. . : " + tdPF.getMunicipioUfTituloEleitor() + "\n");
			}
			
			sBuffer.append("*********************************\n");
			sBuffer.append("Tem dados PJ  . : " + this.hasDadosPJ() + "\n");
			if(this.hasDadosPJ()) {
				TabeliaoDadosPJ tdPJ = this.getTabeliaoDadosPJ();
				sBuffer.append("CNPJ. . . . . . : " +tdPJ.getCNPJ()+ "\n");
				sBuffer.append("INSS. . . . . . : " +tdPJ.getINSS()+ "\n");
				sBuffer.append("NIS . . . . . . : " +tdPJ.getNis()+ "\n");
				sBuffer.append("Responsável . . : " +tdPJ.getNomeResponsavel()+ "\n");
				}
			
			sBuffer.append("*********************************\n");
			sBuffer.append("Tem dados Equip : " + this.hasDadosEquipamento() + "\n");
			if(this.hasDadosEquipamento()) {
				TabeliaoDadosEquipamento tdEq = this.geTabeliaoDadosEquipamento();
				sBuffer.append("CNPJ. . . . . . : " +tdEq.getCNPJ()+ "\n");
				sBuffer.append("NIS . . . . . . : " +tdEq.getNis()+ "\n");
				sBuffer.append("Nome Empresa. . : " +tdEq.getNomeEmpresarial()+ "\n");
				sBuffer.append("Responsável . . : " +tdEq.getNomeResponsavel()+ "\n");
			}
			
			sBuffer.append("*********************************\n");
			sBuffer.append("Eh CertificadoAC: " + this.isCertificadoAc() + "\n");
			sBuffer.append("PathLength  . . : " + this.getPathLength() + "\n");
			sBuffer.append("Tipo Certificado: " + this.getTipoCertificado() + "\n");
			sBuffer.append("Tipo de Uso . . : " + this.getTabeliaoKeyUsage() + "\n");
			
			sBuffer.append("*********************************\n");
			sBuffer.append("Authority KeyID : " + this.getAuthorityKeyIdentifier() + "\n");
			sBuffer.append("Subject KeyID . : " + this.getSubjectKeyIdentifier() + "\n");
			sBuffer.append("CRL DistPoint . : " + this.getCRLDistributionPoint() + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sBuffer.toString();
	}
}
