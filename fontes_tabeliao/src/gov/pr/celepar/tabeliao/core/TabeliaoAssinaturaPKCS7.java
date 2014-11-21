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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import org.bouncycastle.asn1.cms.Attribute;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.cms.CMSAttributes;
import org.bouncycastle.asn1.cms.Time;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
/**
 * Classe de tratamento/validacao de Assinaturas em formato PKCS7/CADES
 * De acordo com as propriedades CADES reconhecipas pela ICP-BRASIL
 * 
 * @author Thiago Meneghello - GIC/CELEPAR
 * @author Emerson Sachio Saito - GIC/CELEPAR
 *
 */
public class TabeliaoAssinaturaPKCS7 {

	private CMSSignedData signedData = null;

	private SignerInformation signerInfo = null;

	private X509Certificate certificadoAssinante = null;
	
	/**
	 * Construtor para assiantura em array de byte
	 *  
	 * @param assinatura -> array de byte
	 * @throws CMSException
	 */
	public TabeliaoAssinaturaPKCS7(byte[] assinatura) throws CMSException {
		this(new ByteArrayInputStream(assinatura));
	}

	/**
	 * Construtor para assinatura em InputStream
	 *  
	 * @param assinatura
	 * @see InputStream
	 * @throws CMSException
	 */
	public TabeliaoAssinaturaPKCS7(InputStream assinatura) throws CMSException {
		signedData = new CMSSignedData(assinatura);
	}

	/**
	 * Construtor para conteudo e assinatura
	 *  
	 * @param conteudo -> InputStream
	 * @param assinatura -> InputStream
	 * @see InputStream
	 * @throws CMSException
	 * @throws IOException
	 */
	public TabeliaoAssinaturaPKCS7(InputStream conteudo, InputStream assinatura) throws CMSException, IOException {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		byte[] buffer = new byte[1024];
		int read;
		while((read = conteudo.read(buffer)) > 0) {
			bos.write(buffer, 0, read);
		}
		buffer = bos.toByteArray();
		
		CMSProcessableByteArray data = new CMSProcessableByteArray(buffer);
		
		signedData = new CMSSignedData(data, assinatura);
	}
	
	/**
	 * Construtor para conteudo e assinatura
	 *  
	 * @param conteudo -> array de byte
	 * @param assinatura -> InputStream
	 * @see InputStream
	 * @throws CMSException
	 * @throws IOException
	 */
	public TabeliaoAssinaturaPKCS7(byte[] conteudo, InputStream assinatura) throws CMSException {
		
		CMSProcessableByteArray data = new CMSProcessableByteArray(conteudo);
		
		signedData = new CMSSignedData(data, assinatura);
		
	}
	
	/**
	 * Construtor para conteudo e assinatura
	 *  
	 * @param conteudo -> InputStream
	 * @see InputStream
	 * @param assinatura -> Array de byte
	 * @throws CMSException
	 * @throws IOException
	 */
	public TabeliaoAssinaturaPKCS7(InputStream conteudo, byte[] assinatura) throws CMSException, IOException {
		
		this(conteudo, new ByteArrayInputStream(assinatura));
		
	}
	
	/**
	 * Construtor para conteudo e assinatura
	 *  
	 * @param conteudo -> Array de byte
	 * @param assinatura -> Array de byte
	 * @throws CMSException
	 * @throws IOException
	 */
	public TabeliaoAssinaturaPKCS7(byte[] conteudo, byte[] assinatura) throws CMSException, IOException {
		
		this(conteudo, new ByteArrayInputStream(assinatura));
		
	}
	
	/**
	 * retorna o certificado do assinante
	 *
	 * @return X509Certificate
	 * @throws Exception 
	 * @see X509Certificate
	 */

	@SuppressWarnings("unchecked")
	public X509Certificate getCertificadoAssinante() throws Exception {

		if (signedData == null) {
			return null;
		}
		if (certificadoAssinante != null) {
			return certificadoAssinante;
		}
		try {
			CertStore certStore = signedData.getCertificatesAndCRLs(
					"Collection", "SUN");
			SignerInformationStore sis = signedData.getSignerInfos();
			Collection<SignerInformation> signers = sis.getSigners();

			if (signers != null && signers.size() > 0) {
				SignerInformation sigInfo = signers.iterator().next();
				signerInfo = sigInfo;
				Collection<X509Certificate> chain = (Collection<X509Certificate>) certStore
						.getCertificates(sigInfo.getSID());
				if (chain != null && chain.size() > 0) {
					certificadoAssinante = chain.iterator().next();
					return certificadoAssinante;
				}
			}
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("Sem Algoritmo, no Certificado:"+e.getMessage());			
		} catch (NoSuchProviderException e) {
			throw new Exception("Sem Provider, no Certificado:"+e.getMessage());
		} catch (CMSException e) {
			throw new Exception("Exceção de CMS, no Certificado:"+e.getMessage());
		} catch (CertStoreException e) {
			throw new Exception("Exceção de CertStore, no Certificado:"+e.getMessage());
		}
		return null;
	}

	/**
	 * Retorna o conteudo do arquivo que foi assinado.
	 *
	 * @return array de byte
	 */
	public byte[] getConteudoAssinado() {

		if (signedData == null || signedData.getSignedContent() == null) {
			return null;
		}

		return (byte[]) signedData.getSignedContent().getContent();
	}
	
	/**
	 * Executa as validacoes da assinatura
	 *
	 * @return TabeliaoResultadoValidacao
	 * @throws Exception 
	 * @see TabeliaoResultadoValidacao
	 * @throws Exception
	 */
	public TabeliaoResultadoValidacao valida() throws Exception {

		X509Certificate cert = getCertificadoAssinante();

		// Verifica se o certificado está junto com a assinatura
		if (cert == null) {
			throw new Exception(
					"Não foi possível encontrar o certificado na assinatura PKCS7.");
		}
		
		byte[] conteudo = getConteudoAssinado();

		if (conteudo == null) {
			throw new Exception(
					"O conteúdo assinado não está anexado com a assinatura.");
		}
		
		TabeliaoResultadoValidacao resultVal = new TabeliaoResultadoValidacao();

		// Verifica se no momento da assinatura o certificado estava expirado
		
			try {
				if(signerInfo.verify(cert, null)){
					resultVal.addOk(TabeliaoResultadoValidacao.VAL_CONTEUDO);
					resultVal.addOk(TabeliaoResultadoValidacao.VAL_VALIDADE);
				} else {
					//Não deve entrar neste ELSE.
					resultVal.addErro(TabeliaoResultadoValidacao.VAL_VALIDADE, "A informação assinada não confere com o certificado do assinante.");
				}
			} catch (CertificateExpiredException e) {
				resultVal.addErro(TabeliaoResultadoValidacao.VAL_VALIDADE, e.getMessage());
			} catch (CertificateNotYetValidException e) {
				resultVal.addErro(TabeliaoResultadoValidacao.VAL_VALIDADE, e.getMessage());
			} catch (NoSuchAlgorithmException e) {
				resultVal.addErro(TabeliaoResultadoValidacao.VAL_CONTEUDO, e.getMessage());
			} catch (NoSuchProviderException e) {
				resultVal.addErro(TabeliaoResultadoValidacao.VAL_CONTEUDO, e.getMessage());
			} catch (CMSException e) {
				resultVal.addErro(TabeliaoResultadoValidacao.VAL_CONTEUDO, e.getMessage());
			} catch (Exception e) {
				resultVal.addExcecao(TabeliaoResultadoValidacao.VAL_CONTEUDO, e.getMessage());
			}		

		return resultVal;
	}

	/**
	 * Executa as validacoes da assinatura (.valida)
	 * @return TabeliaoResultadoValidacao
	 * @see TabeliaoResultadoValidacao
	 * @throws Exception
	 */
	public TabeliaoResultadoValidacao getResultadoValidacao() throws Exception {
		return valida();
	}

	/**
	 * Retorna o conteudo assinado
	 * @return CMSSignedData
	 * @see CMSSignedData
	 */
	public CMSSignedData getCMSSignedData(){
		return signedData;
	}
	/**
	 * Retorna a data da criacao da assinatura
	 * NAO EH CARIMBO DE TEMPO 
	 * @return Date
	 * @see Date
	 */
	public Date getDataAssinatura(){
		
		try {
			getCertificadoAssinante();
		} catch (Exception e) {
			return null;
		}
        AttributeTable attr = signerInfo.getSignedAttributes();

        if (attr != null)
        {
            Attribute t = attr.get(CMSAttributes.signingTime);
            if (t != null)
            {
                Time   time = Time.getInstance(
                                    t.getAttrValues().getObjectAt(0).getDERObject());
                return time.getDate();
            }
        }
        return null;
	}
}
