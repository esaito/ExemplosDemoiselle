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
import gov.pr.celepar.tabeliao.core.validacao.TabeliaoResultadoValidacao;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import org.apache.log4j.Logger;
import org.jcp.xml.dsig.internal.dom.DOMReference;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import sun.security.x509.X509CertImpl;

/**
 * 
 * Classe para tratamento e validacao de assinatura em formato XML
 * 
 * @author Emerson Sachio Saito - GIC/CELEPAR
 *
 */

public class TabeliaoAssinaturaXML {
	
	private static Logger logger = Logger.getLogger(TabeliaoAssinaturaXML.class);

	private Document xmlAssinado = null;
	private List<XMLSignature> assinaturas = new ArrayList<XMLSignature>();
	private List<TabeliaoResultadoValidacao> resultadosValidacao = new ArrayList<TabeliaoResultadoValidacao>();
	private List<TabeliaoCertificate> certificadosDasAssinaturas = new ArrayList<TabeliaoCertificate>();	 
	
	
	public void setXmlAssinado(Document pXmlAssinado){
		xmlAssinado = pXmlAssinado;
	}
	
	public Document getXmlAssinado(){
		return xmlAssinado;
	}
	
	public List<XMLSignature> getAssinaturas(){
		return assinaturas;
	}
	
	public List<TabeliaoResultadoValidacao> getResultadosValidacao(){
		return resultadosValidacao;
	}
	
	public List<TabeliaoCertificate> getCertificadosDasAssinaturas(){
		return certificadosDasAssinaturas;
	}
	
	
	/**
	 * Executa a validacao da(s) assinatura(s) para o arquivo, fazendo com que as listas: 
	 * assinaturas, resultadosValidacao e certificadosDasAssinaturas sejam populadas.
	 * NAO Executa validacao de Data de Validade, Cadeia de Certificacao ou LCR (Lista de Certificados Revogados)
	 * MAS eh pre-requisito para uso das demais validacoes.
	 * @throws Exception 
	 */
	
	
	public void valida() throws Exception{
	
	XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

	NodeList nl =  xmlAssinado.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
	if (nl.getLength() == 0) {
		throw new Exception("Não foi possível encontrar nenhuma assinatura no arquivo!");
	}
	
		
	for (int ix=0; ix <nl.getLength(); ix++){

		boolean sv=false;
		DOMValidateContext valContext = new DOMValidateContext (new X509KeySelector(), nl.item(ix));
		valContext.setProperty("javax.xml.crypto.dsig.cacheReference", Boolean.TRUE);
	 // Unmarshal the XMLSignature.
		XMLSignature assinatura;
		try {
			assinatura = fac.unmarshalXMLSignature(valContext);
		} catch (MarshalException e1) {
			throw new Exception("Problemas com o conteúdo/arquivo XML!"+e1.getMessage());
		}
	 // valida a assinatura.
		boolean coreValidity;
		try {
			coreValidity = assinatura.validate(valContext);
		} catch (XMLSignatureException e1) {
			throw new Exception("Problemas com o conteúdo/arquivo XML!"+e1.getMessage());
		}
		// Checa core validation status. Em Co-Assinatura em formato Enveloping
		if (coreValidity == false) {
			try {
				sv = assinatura.getSignatureValue().validate(valContext);
			} catch (XMLSignatureException e) {
				throw new Exception("Problemas com o conteúdo/arquivo XML!"+e.getMessage());
			}
			if (sv) {
				// Checa o validation status para cada Reference.
				Iterator<?> i = assinatura.getSignedInfo().getReferences().iterator();
				for (int j=0; i.hasNext(); j++) {
					boolean refValid;
					try {
						refValid = ((Reference) i.next()).validate(valContext);
					} catch (XMLSignatureException e) {
						throw new Exception("Problemas com as referências do XML!"+e.getMessage());
					}
					if (!refValid){
						logger.info("Assinatura:"+ix+"ref["+j+"] status de validação: " + refValid);
						System.err.println("Assinatura:"+ix+"ref["+j+"] status de validação: " + refValid);
					}
				}
			} 
		}
		assinaturas.add(assinatura);		

		// recupera o certificado do assinante
		X509Certificate cert = null;
		X509CertImpl certImpl = null;
		Iterator<?> ki = assinatura.getKeyInfo().getContent().iterator();
        while (ki.hasNext()) {
            XMLStructure info = (XMLStructure) ki.next();
            if (!(info instanceof X509Data))
                continue;
            X509Data x509Data = (X509Data) info;
            Iterator<?> xi = x509Data.getContent().iterator();
            while (xi.hasNext()) {
                Object o = xi.next();
                if (!(o instanceof X509CertImpl))
                    continue;
                certImpl = (X509CertImpl)o;
                cert=certImpl;
            }
        }
		
		// Verifica se o certificado existe.
		if (cert == null) {
			throw new Exception(
			"Não foi possível encontrar o certificado da assinatura.");
		}			
		try {
			TabeliaoResultadoValidacao resultVal = new TabeliaoResultadoValidacao();
			resultadosValidacao.add(resultVal);
			TabeliaoCertificate certificado  = new TabeliaoCertificate(cert);
			certificadosDasAssinaturas.add(certificado);
			if (coreValidity){
				resultadosValidacao.get(ix).addOk(TabeliaoResultadoValidacao.VAL_CONTEUDO);
			}else {
				if (sv){
					//Não deve entrar neste ELSE.
					resultadosValidacao.get(ix).addErro(TabeliaoResultadoValidacao.VAL_CONTEUDO, 
					          "O conteúdo do arquivo foi corrompido, para o certificado de:\n"+cert.getSubjectDN().getName()+
					          "\n URI: "+getUriTagAssinada(ix));
				}else{
					resultadosValidacao.get(ix).addErro(TabeliaoResultadoValidacao.VAL_CONTEUDO, 
					          "A Assinatura no arquivo foi corrompida, para o certificado de:\n"+cert.getSubjectDN().getName()+
					          "\n URI: "+getUriTagAssinada(ix));
				}
			}
			} catch (Exception e) {
				resultadosValidacao.get(ix).addExcecao(TabeliaoResultadoValidacao.VAL_CONTEUDO, e.getMessage());
			}				
		}
	}
	
	/**
	 * Executa a validacao da(s) Data(s) de Validade(s), para o(s) certificado(s) da(s) 
	 * assinatura(s) no arquivo.
	 * Tem como base para validade o atributo ValidadeAte, e toma como base a data atual do sistema.
	 * Eh preciso que o metodo valida() tenha sido executado.
	 * @return boolean -> false se nao efetuou a validacao
	 * @throws Exception
	 */
	public boolean validarVigencias() throws Exception{
		
		if (certificadosDasAssinaturas.isEmpty()){
			return false;
		}else{
			for (int i=0; i < certificadosDasAssinaturas.size(); i++){
				TabeliaoResultadoValidacao resultVal = certificadosDasAssinaturas.get(i).getResultadoDataVigencia();
				String res = resultVal.getMensagem(TabeliaoResultadoValidacao.VAL_VALIDADE);
				if(TabeliaoResultadoValidacao.MSG_ERRO.equals(res)) {
					resultadosValidacao.get(i).addErro(TabeliaoResultadoValidacao.VAL_VALIDADE,resultVal.getDescricao(TabeliaoResultadoValidacao.VAL_VALIDADE));
				}else{
					resultadosValidacao.get(i).addOk(TabeliaoResultadoValidacao.VAL_VALIDADE);
				}		
			}
			return true;	
		}		
	}

	/**
	 * Executa a validacao da(s) cadeia(s) de certificacao, para o(s) certificado(s) da(s) 
	 * assinatura(s) no arquivo
	 * Eh preciso que o metodo valida() tenha sido executado.
	 * Este metodo fara acesso a base de dados de cadeias do Tabeliao.
	 * @return boolean -> false se nao efetuou a validacao
	 * @throws Exception
	 */
	public boolean validarCadeias() throws Exception{
		
		if (certificadosDasAssinaturas.isEmpty()){
			return false;
		}else{
			for (int i=0; i < certificadosDasAssinaturas.size(); i++){
				TabeliaoResultadoValidacao resultVal = certificadosDasAssinaturas.get(i).getResultadoCadeia();
				String res = resultVal.getMensagem(TabeliaoResultadoValidacao.VAL_CADEIA);
				if(TabeliaoResultadoValidacao.MSG_ERRO.equals(res)) {
					resultadosValidacao.get(i).addErro(TabeliaoResultadoValidacao.VAL_CADEIA,resultVal.getDescricao(TabeliaoResultadoValidacao.VAL_CADEIA));
				}else{
					resultadosValidacao.get(i).addOk(TabeliaoResultadoValidacao.VAL_CADEIA);
				}		
			}
			return true;	
		}		
	}

	/**
	 * Executa a validacao da LRC (Lista de Certificados Revogados), para o(s) certificado(s) da(s) 
	 * assinatura(s) no arquivo
	 * Eh preciso que o metodo valida() tenha sido executado.
	 * Este metodo fara acesso a base de dados LCR do Tabeliao.
	 * @return boolean -> false se nao efetuou a validacao
	 * @throws Exception
	 */
	public boolean validarLCR() throws Exception{
		
		if (certificadosDasAssinaturas.isEmpty()){
			return false;
		}else{
			for (int i=0; i < certificadosDasAssinaturas.size(); i++){
				TabeliaoResultadoValidacao resultVal = certificadosDasAssinaturas.get(i).getResultadoLCR();
				String res = resultVal.getMensagem(TabeliaoResultadoValidacao.VAL_LCR);
				if(TabeliaoResultadoValidacao.MSG_ERRO.equals(res)) {
					resultadosValidacao.get(i).addErro(TabeliaoResultadoValidacao.VAL_LCR,resultVal.getDescricao(TabeliaoResultadoValidacao.VAL_LCR));
				}else{
					resultadosValidacao.get(i).addOk(TabeliaoResultadoValidacao.VAL_LCR);
				}		
			}
			return true;	
		}		
	}
	
	/**
	 * Retorna o resultado da validacao para indice ix referente a assinatura correspondente. 
	 * Se nao houver validacao para o indice retornara nulo.
	 * 
	 * @param ix indica qual a assinatura validar.
	 * @return TabeliaoResultadoValidacao
	 * @see TabeliaoResultadoValidacao
	 * @throws Exception
	 */
	public TabeliaoResultadoValidacao getResultadoValidacao(int ix) throws Exception {

		if (resultadosValidacao.isEmpty()) {
			return null;
		}
		return resultadosValidacao.get(ix);
	}
	
	/**
	 * Retorna todos os resultados das validacoes. Se nao houver validacoes retornara nulo. 
	 * 
	 * @return Lista de TabeliaoResultadoValidacao 
	 * @see TabeliaoResultadoValidacao
	 * @throws Exception
	 */
	public List<TabeliaoResultadoValidacao> getResultadosValidacoes() throws Exception {
				
		if (resultadosValidacao.isEmpty()) {
			return null;
		}
		return resultadosValidacao;
	}

	/**
	 * Retorna valor URI/ID da TAG assinada, na assinatura de indice ix, 
	 * se vazio ou null significa que o arquivo todo foi assinado.
	 * 
	 * @param ix indica de qual a assinatura.
	 * @return String com o ID ou a situacao da TAG.
	 * 
	 * @throws Exception
	 */
	public String getUriTagAssinada(int ix) throws Exception {
		
		String uriTagAssinada = "";
		
		if (assinaturas.isEmpty()){
			return uriTagAssinada;
		}
		if (assinaturas.get(ix).getSignedInfo() == null) {
				return uriTagAssinada;
		}
		if (assinaturas.get(ix).getSignedInfo().getReferences().isEmpty()){
			return uriTagAssinada;
		}else{
			DOMReference objDR = null;
	        Iterator<?> it = getAssinaturas().get(ix).getSignedInfo().getReferences().iterator();
		    while (it.hasNext()) {
		       	Object ob1 = it.next();
			   	if (ob1 instanceof DOMReference){
		       		objDR = (DOMReference) ob1;
		       		uriTagAssinada = objDR.getURI();
			   	}
		    }		
		}		
		return uriTagAssinada;			
	}
	
	/**
	 * Retorna a quantidade de assinaturas no arquivo XML, se igual a 0(zero) nao ha assinatura. 
	 * 
	 * @return int com a quantidade de assinaturas no arquivo
	 * 
	 * @throws Exception
	 */
	public int getQuantidadeAssinaturas() throws Exception{
		int quantidade=0;
		// pode não existir a tag de assinatura.		
		NodeList nl =  xmlAssinado.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
		quantidade = nl.getLength();
		return quantidade;		
	}
		
	/**
	 * Retorna a assinatura XML do arquivo conforme o indice (ix) 
	 * 
	 * @param ix indica de qual a assinatura.
	 * @return XMLSignature
	 * 
	 * @throws Exception
	 */
	public XMLSignature getAssinatura(int ix) throws Exception {
		
		// Se vazio não há assinaturas no arquivo.
		if (assinaturas.isEmpty()){
			return null;
		}
		if (assinaturas.get(ix).getSignedInfo() == null) {
				return null;
		}
		return assinaturas.get(ix);
	}
	
	/**
	 * Retorna a lista da assinaturas contidas no arquivo XML 
	 * 
	 * @return Lista de XMLSignature
	 * 
	 * @throws Exception
	 */
	public List<XMLSignature> getAssinaturasAssinantes() throws Exception {

		if (assinaturas.isEmpty()) {
			return null;
		}
		else{
			return assinaturas;
		}
	}
	
	/**
	 * Retorna o certificado do assinante conforme o indice (ix) 
	 * 
	 * @param ix indica de qual a assinatura.
	 * @return certificado na classe TabeliaoCertificate
	 * @see TabeliaoCertificate
	 * @throws Exception
	 */
	public TabeliaoCertificate getCertificadoAssinante(int ix) throws Exception {
		
		if (certificadosDasAssinaturas.isEmpty()){
			return null;
		}
		if (certificadosDasAssinaturas.get(ix) == null) {
			return null;
		}		
		return certificadosDasAssinaturas.get(ix);
	}
	
	/**
	 * Retorna a lista de Certificados contidos no arquivos XML 
	 * 
	 * @return Lista de certificados na classe TabeliaoCertificate
	 * @see TabeliaoCertificate
	 * @throws Exception
	 */
	public  List<TabeliaoCertificate> getCertificadosAssinantes () throws Exception{
		if (certificadosDasAssinaturas.isEmpty()) {
			return null;
		}
			return certificadosDasAssinaturas;
	}
}