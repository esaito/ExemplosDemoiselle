package gov.pr.celepar.tabeliao.core.oid;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERUTF8String;

import sun.security.util.DerValue;
import sun.security.x509.OtherName;

/**
 * 
 * Classe Generica   para   tratamento   de   atributos   de  alguns   atributos   de   Pessoa 
 * Fisica, Pessoa Juridica   e   Equipamento   de   acordo   com   os   padroes  definidos   no 
 * DOC­ICP­04 v2.0 de 18/04/2006, pela ICP­BRASIL
 * 
 * @author Thiago Meneghello - CELEPAR/GIC
 *
 */

public class OIDGenerico {

	private String oid  = null;
	private String data = null;
	protected Map<String, String> propriedades = new HashMap<String, String>();
	
	protected OIDGenerico() {
	}
	
	/**
	 * Instancia um objeto.
	 * @param data -> array de bytes contendo o certificado.
	 * @return Objeto OIDGenerico
	 * @throws IOException
	 * @throws Exception
	 */
	public static OIDGenerico getInstance(byte[] data) throws IOException, Exception {
		ASN1InputStream     is       = new ASN1InputStream(data);
		DERSequence         sequence = (DERSequence)is.readObject();
		DERObjectIdentifier oid      = (DERObjectIdentifier) sequence.getObjectAt(0);
		DERTaggedObject     tag      = (DERTaggedObject) sequence.getObjectAt(1);
		
		DEROctetString      octet    = null;
		DERPrintableString  print    = null;
		DERUTF8String		utf8	 = null;

		try {
			octet    = (DEROctetString)DEROctetString.getInstance(tag);
		} catch (Exception e) {
			try{
				print    = (DERPrintableString)DERPrintableString.getInstance(tag);
			}catch (Exception e1) {
				utf8 	= (DERUTF8String) DERUTF8String.getInstance(tag); 
			}			
		}

		String className = "gov.pr.celepar.tabeliao.core.oid.OID_" + oid.getId().replaceAll("[.]", "_");
		OIDGenerico oidGenerico;
		try {
			oidGenerico = (OIDGenerico)Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			throw new Exception("Não foi possível instanciar a classe '" + className + "'.", e);
		} catch (IllegalAccessException e) {
			throw new Exception("Não foi possível instanciar a classe '" + className + "'.", e);
		} catch (ClassNotFoundException e) {
			oidGenerico = new OIDGenerico();
		}

		oidGenerico.oid  = oid.getId();
		
		if(octet != null) {
			oidGenerico.data = new String(octet.getOctets());
		} else {
			if (print != null){
				oidGenerico.data = print.getString();
			}else{
				oidGenerico.data = utf8.getString();
			}
		}
		
		oidGenerico.inicializa();
		
		return oidGenerico;
	}
	
	/**
	 * 
	 * @param der -> Certificado no formato DER (sun.security.util.DerValue)
	 * @return OIDGenerico
	 * @throws IOException
	 * @throws Exception
	 */
	public static OIDGenerico getInstance(DerValue der) throws IOException, Exception{
		OtherName on = new OtherName(der);
		String className = "gov.pr.celepar.tabeliao.core.oid.OID_" + on.getOID().toString().replaceAll("[.]", "_");
		
		OIDGenerico oidGenerico;
		try {
			oidGenerico = (OIDGenerico)Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			throw new Exception("Não foi possível instanciar a classe '" + className + "'.", e);
		} catch (IllegalAccessException e) {
			throw new Exception("Não foi possível instanciar a classe '" + className + "'.", e);
		} catch (ClassNotFoundException e) {
			oidGenerico = new OIDGenerico();
		}
		
		oidGenerico.oid  = on.getOID().toString();
		oidGenerico.data = new String(on.getNameValue()).substring(6);
		oidGenerico.inicializa();
		
		return oidGenerico;
	}

	protected void inicializa(){
		//Inicializa as propriedades do conteudo DATA
	}
	
	/**
	 * 
	 * @param campos -> campos do certificado
	 */
	protected void inicializa(Object[] campos){
		int tmp = 0;
		for(int i=0 ; i<campos.length ; i+=2) {
			String key = (String)campos[i];
			int tam = ((Integer)campos[i+1]).intValue();
			
			propriedades.put(key, data.substring(tmp, Math.min(tmp + tam, data.length())));
			
			tmp += tam;
		}
	}

	/**
	 * 
	 * @return conjunto de OID em formato String
	 */
	public String getOid(){
		return oid;
	}
	
	/**
	 * 
	 * @return conteudo em formato String
	 */
	public String getData(){
		return data;
	}
	
}
