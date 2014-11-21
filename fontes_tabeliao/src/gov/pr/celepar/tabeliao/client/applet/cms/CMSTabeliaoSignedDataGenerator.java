package gov.pr.celepar.tabeliao.client.applet.cms;
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
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.BERConstructedOctetString;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.asn1.cms.SignedData;
import org.bouncycastle.asn1.cms.SignerIdentifier;
import org.bouncycastle.asn1.cms.SignerInfo;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.TBSCertificateStructure;
import org.bouncycastle.cms.CMSAttributeTableGenerator;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedGenerator;
import org.bouncycastle.cms.DefaultSignedAttributeTableGenerator;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SimpleAttributeTableGenerator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * classe generica para geracao de assinatura no formato pkcs7/cms.
 * <p>
 * Um exemplo de uso...
 *
 * <pre>
 *      CertStore               certs...
 *      CMSSignedDataGenerator    gen = new CMSSignedDataGenerator();
 *
 *      gen.addSigner(privKey, cert, CMSSignedGenerator.DIGEST_SHA1);
 *      gen.addCertificatesAndCRLs(certs);
 *
 *      CMSSignedData           data = gen.generate(content, "BC");
 * </pre>
 * @author Thiago Meneghello - GIC/CELEPAR
 */
public class CMSTabeliaoSignedDataGenerator
    extends CMSSignedGenerator
{
    List<SignerInf>                        signerInfs = new ArrayList<SignerInf>();

    static class DigOutputStream
    	extends OutputStream
	{
	    MessageDigest   dig;
	
	    public DigOutputStream(
	        MessageDigest   dig)
	    {
	        this.dig = dig;
	    }
	
	    public void write(
	        byte[]  b,
	        int     off,
	        int     len)
	        throws IOException
	    {
	        dig.update(b, off, len);
	    }
	
	    public void write(
	        int b)
	        throws IOException
	    {
	        dig.update((byte)b);
	    }
	}
	
	static class SigOutputStream
	    extends OutputStream
	{
	    Signature   sig;
	
	    public SigOutputStream(
	        Signature   sig)
	    {
	        this.sig = sig;
	    }
	
	    public void write(
	        byte[]  b,
	        int     off,
	        int     len)
	        throws IOException
	    {
	        try
	        {
	            sig.update(b, off, len);
	        }
	        catch (SignatureException e)
	        {
	            throw new IOException("Problema na assinatura: " + e);
	        }
	    }
	
	    public void write(
	        int b)
	        throws IOException
	    {
	        try
	        {
	            sig.update((byte)b);
	        }
	        catch (SignatureException e)
	        {
	            throw new IOException("Problema na assinatura: " + e);
	        }
	    }
	}
    
    private class SignerInf
    {
        PrivateKey                  key;
        X509Certificate             cert;
        String                      digestOID;
        String                      encOID;
        CMSAttributeTableGenerator  sAttr;
        CMSAttributeTableGenerator  unsAttr;
        AttributeTable              baseSignedTable;
        
        /**
         * 
         * @param key
         * @param cert
         * @param digestOID
         * @param encOID
         */
        SignerInf(
            PrivateKey      key,
            X509Certificate cert,
            String          digestOID,
            String          encOID)
        {
            this.key = key;
            this.cert = cert;
            this.digestOID = digestOID;
            this.encOID = encOID;
        }
        
        /**
         * 
         * @param key
         * @param cert
         * @param digestOID
         * @param encOID
         * @param sAttr
         * @param unsAttr
         * @param baseSigneTable
         */
        SignerInf(
            PrivateKey                 key,
            X509Certificate            cert,
            String                     digestOID,
            String                     encOID,
            CMSAttributeTableGenerator sAttr,
            CMSAttributeTableGenerator unsAttr,
            AttributeTable             baseSigneTable)
        {
            this.key = key;
            this.cert = cert;
            this.digestOID = digestOID;
            this.encOID = encOID;
            this.sAttr = sAttr;
            this.unsAttr = unsAttr;
            this.baseSignedTable = baseSigneTable;
        }

        PrivateKey getKey()
        {
            return key;
        }

        X509Certificate getCertificate()
        {
            return cert;
        }

        String getDigestAlgOID()
        {
            return digestOID;
        }

        byte[] getDigestAlgParams()
        {
            return null;
        }

        String getEncryptionAlgOID()
        {
            return encOID;
        }

        CMSAttributeTableGenerator getSignedAttributes()
        {
            return sAttr;
        }

        CMSAttributeTableGenerator getUnsignedAttributes()
        {
            return unsAttr;
        }   

        
        /**
         * 
         * @param contentType
         * @param content
         * @param sigProvider
         * @param addDefaultAttributes
         * @param contentIsHash
         * @return SignerInfo
         * @see org.bouncycastle.asn1.cms.SignerInfo
         * @throws IOException
         * @throws SignatureException
         * @throws InvalidKeyException
         * @throws NoSuchProviderException
         * @throws NoSuchAlgorithmException
         * @throws CertificateEncodingException
         * @throws CMSException
         */
        @SuppressWarnings("unchecked")
        SignerInfo toSignerInfo(
            DERObjectIdentifier contentType,
            CMSProcessable      content,
            String              sigProvider,
            boolean             addDefaultAttributes,
            boolean				contentIsHash)
            throws IOException, SignatureException, InvalidKeyException, NoSuchProviderException, NoSuchAlgorithmException, CertificateEncodingException, CMSException
        {
            AlgorithmIdentifier digAlgId = new AlgorithmIdentifier(
                  new DERObjectIdentifier(this.getDigestAlgOID()), new DERNull());
            AlgorithmIdentifier encAlgId = getEncAlgorithmIdentifier(this.getEncryptionAlgOID());
            String              digestName = CMSSignedHelper.INSTANCE.getDigestAlgName(digestOID);
            String              signatureName = digestName + "with" + CMSSignedHelper.INSTANCE.getEncryptionAlgName(encOID);
            Signature           sig = CMSSignedHelper.INSTANCE.getSignatureInstance(signatureName, sigProvider);
            MessageDigest       dig = CMSSignedHelper.INSTANCE.getDigestInstance(digestName, sigProvider);               

            byte[]      hash = null;
            
            if (content != null) {
                if(contentIsHash) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    content.write(bos);
                    hash = bos.toByteArray();
                } else {
                    content.write(new DigOutputStream(dig));
                    hash = dig.digest();
                }
                _digests.put(digestOID, hash.clone());
            }

            AttributeTable signed;

            if (addDefaultAttributes)
            {
                Map<String, ?> parameters = getBaseParameters(contentType, digAlgId, hash);
                signed = (sAttr != null) ? sAttr.getAttributes(Collections.unmodifiableMap(parameters)) : null;
            }
            else
            {
                signed = baseSignedTable;
            }

            ASN1Set signedAttr = getAttributeSet(signed);

            //
            // sig must be composed from the DER encoding.
            //
            ByteArrayOutputStream   bOut = new ByteArrayOutputStream();
 
            if (signedAttr != null) 
            {
                DEROutputStream         dOut = new DEROutputStream(bOut);
                dOut.writeObject(signedAttr);
            } 
            else
            {
                content.write(bOut);
            }

            sig.initSign(key);

            sig.update(bOut.toByteArray());

            ASN1OctetString         encDigest = new DEROctetString(sig.sign());

            Map parameters = getBaseParameters(contentType, digAlgId, hash);
            parameters.put(CMSAttributeTableGenerator.SIGNATURE, encDigest.getOctets().clone());

            AttributeTable unsigned = (unsAttr != null) ? unsAttr.getAttributes(Collections.unmodifiableMap(parameters)) : null;

            ASN1Set unsignedAttr = getAttributeSet(unsigned);

            X509Certificate         cert = this.getCertificate();
            ByteArrayInputStream    bIn = new ByteArrayInputStream(cert.getTBSCertificate());
            ASN1InputStream         aIn = new ASN1InputStream(bIn);
            TBSCertificateStructure tbs = TBSCertificateStructure.getInstance(aIn.readObject());
            IssuerAndSerialNumber   encSid = new IssuerAndSerialNumber(tbs.getIssuer(), tbs.getSerialNumber().getValue());

            return new SignerInfo(new SignerIdentifier(encSid), digAlgId,
                        signedAttr, encAlgId, encDigest, unsignedAttr);
        }
    }
    
    /**
     * construtor basico
     */
    public CMSTabeliaoSignedDataGenerator()
    {
    }
    
    /**
     * Adiciona o assinante, nenhum outro atributo alem do padrao serao providos aqui 
     *
     * @param key
     * @param cert
     * @param digestOID
     * @throws IllegalArgumentException
     */
    public void addSigner(
        PrivateKey      key,
        X509Certificate cert,
        String          digestOID)
        throws IllegalArgumentException
    {
        String  encOID = getEncOID(key, digestOID);

        signerInfs.add(new SignerInf(key, cert, digestOID, encOID, new DefaultSignedAttributeTableGenerator(), null, null));
    }

    /**
     * Adiciona o assinante com os atributos extras assinados e nao assinados.
     * 
     * @param key
     * @param cert
     * @param digestOID
     * @param signedAttr
     * @param unsignedAttr
     * @throws IllegalArgumentException
     */
    public void addSigner(
        PrivateKey      key,
        X509Certificate cert,
        String          digestOID,
        AttributeTable  signedAttr,
        AttributeTable  unsignedAttr)
        throws IllegalArgumentException
    {
        String  encOID = getEncOID(key, digestOID);

        signerInfs.add(new SignerInf(key, cert, digestOID, encOID, new DefaultSignedAttributeTableGenerator(signedAttr), new SimpleAttributeTableGenerator(unsignedAttr), signedAttr));
    }

    /**
     * Adiciona o assinante com os atributos extras assinados e nao assiandos baseados nos geradores.
     * 
     * @param key
     * @param cert
     * @param digestOID
     * @param signedAttrGen
     * @param unsignedAttrGen
     * @throws IllegalArgumentException
     */
    public void addSigner(
        PrivateKey                  key,
        X509Certificate             cert,
        String                      digestOID,
        CMSAttributeTableGenerator  signedAttrGen,
        CMSAttributeTableGenerator  unsignedAttrGen)
        throws IllegalArgumentException
    {
        String  encOID = getEncOID(key, digestOID);

        signerInfs.add(new SignerInf(key, cert, digestOID, encOID, signedAttrGen, unsignedAttrGen, null));
    }

    /**
     * 
     * @param encoding
     * @return DERObject
     * @see org.bouncycastle.asn1.DERObject
     * @throws IOException
     */
    private DERObject makeObj(
        byte[]  encoding)
        throws IOException
    {
        if (encoding == null)
        {
            return null;
        }

        ByteArrayInputStream    bIn = new ByteArrayInputStream(encoding);
        ASN1InputStream         aIn = new ASN1InputStream(bIn);

        return aIn.readObject();
    }
    
    /**
     * 
     * @param oid
     * @param params
     * @return AlgorithmIdentifier
     * @see org.bouncycastle.asn1.x509.AlgorithmIdentifier
     * @throws IOException
     */
    private AlgorithmIdentifier makeAlgId(
        String  oid,
        byte[]  params)
        throws IOException
    {
        if (params != null)
        {
            return new AlgorithmIdentifier(
                            new DERObjectIdentifier(oid), makeObj(params));
        }
        else
        {
            return new AlgorithmIdentifier(
                            new DERObjectIdentifier(oid), new DERNull());
        }
    }

    /**
     * 
     * Gera um objeto assinado.
     * generate a signed object that for a CMS Signed Data
     * object using the given provider.
     *
     * @param content
     * @param sigProvider
     * @return CMSSignedData
     * @see org.bouncycastle.cms.CMSSignedData
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws CMSException
     */
    public CMSSignedData generate(
        CMSProcessable content,
        String         sigProvider)
        throws NoSuchAlgorithmException, NoSuchProviderException, CMSException
    {
        return generate(content, false, sigProvider);
    }

    /**
     * Gera um objeto CMS assinado, usando o provedor informado em sigProvider.
     * Se o parametro encapsulate e true uma copia do conteudo sera incluida/anexada na assinatura.
     * O tipo do conteudo sera setado de acodo com o OID representado pelo parametro signedContentType. 
     * 
     * @param signedContentType
     * @param content
     * @param encapsulate -> true para assinatura anexada
     * @param sigProvider
     * @return CMSSignedData
     * @see org.bouncycastle.cms.CMSSignedData
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws CMSException
     */
    public CMSSignedData generate(
        String          signedContentType,
        CMSProcessable  content,
        boolean         encapsulate,
        String          sigProvider)
        throws NoSuchAlgorithmException, NoSuchProviderException, CMSException
    {
        return generate(signedContentType, content, encapsulate, sigProvider, true, false);
    }

    /**
     * Gera um objeto CMS assinado, usando o provedor informado em sigProvider.
     * pode fazer a assinatura utilizando o hash como conteudo, se o parametro contentIsHash eh true.
     * 
     * @param signedContentType
     * @param content
     * @param encapsulate -> true para assinatura anexada
     * @param sigProvider
     * @param contentIsHash -> true se o conteudo eh um hash
     * @return CMSSignedData
     * @see org.bouncycastle.cms.CMSSignedData
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws CMSException
     */
    public CMSSignedData generate(
            String          signedContentType,
            CMSProcessable  content,
            boolean         encapsulate,
            String          sigProvider,
            boolean			contentIsHash)
            throws NoSuchAlgorithmException, NoSuchProviderException, CMSException
        {
            return generate(signedContentType, content, encapsulate, sigProvider, true, contentIsHash);
        }
    
    
    /**
     * Similar aos outros metodos, com o argumento adicional addDefaultAttributes que indica se serao ou nao
     * necessarios adicionar automaticamente os atributos assinados. True=Adiciona e False= Nao adiciona.
     * 
     *     
     * @param signedContentType
     * @param content
     * @param encapsulate
     * @param sigProvider
     * @param addDefaultAttributes
     * @param contentIsHash
     * @return CMSSignedData
     * @see org.bouncycastle.cms.CMSSignedData
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws CMSException
     */
    @SuppressWarnings("unchecked")
	public CMSSignedData generate(
        String                  signedContentType,
        CMSProcessable          content,
        boolean                 encapsulate,
        String                  sigProvider,
        boolean                 addDefaultAttributes,
        boolean					contentIsHash)
        throws NoSuchAlgorithmException, NoSuchProviderException, CMSException
    {
        ASN1EncodableVector  digestAlgs = new ASN1EncodableVector();
        ASN1EncodableVector  signerInfos = new ASN1EncodableVector();

        DERObjectIdentifier      contentTypeOID = new DERObjectIdentifier(signedContentType);

        _digests.clear();  // limpa o estado do resumo guardado atualmente

        //
        // adiciona os objetos SignerInfo pre-calculados.
        //
        Iterator            it = _signers.iterator();
        
        while (it.hasNext())
        {
            SignerInformation        signer = (SignerInformation)it.next();
            AlgorithmIdentifier     digAlgId;
            
            try
            {
                digAlgId = makeAlgId(signer.getDigestAlgOID(),
                                                       signer.getDigestAlgParams());
            }
            catch (IOException e)
            {
                throw new CMSException("Erro na codificacao.", e);
            }

           digestAlgs.add(digAlgId);

           signerInfos.add(signer.toSignerInfo());
        }
        
        //
        // adiciona os objetos SignerInfo
        //
        it = signerInfs.iterator();

        while (it.hasNext())
        {
            SignerInf                   signer = (SignerInf)it.next();
            AlgorithmIdentifier     digAlgId;

            try
            {
                digAlgId = makeAlgId(signer.getDigestAlgOID(),
                                            signer.getDigestAlgParams());

                digestAlgs.add(digAlgId);

                signerInfos.add(signer.toSignerInfo(contentTypeOID, content, sigProvider, addDefaultAttributes, contentIsHash));
            }
            catch (IOException e)
            {
                throw new CMSException("Erro de codificacao.", e);
            }
            catch (InvalidKeyException e)
            {
                throw new CMSException("Chave sem permissao de assinatura.", e);
            }
            catch (SignatureException e)
            {
                throw new CMSException("Erro na criacao da assinatura.", e);
            }
            catch (CertificateEncodingException e)
            {
                throw new CMSException("Erro ao criar o id da assinatura.", e);
            }
        }

        ASN1Set certificates = null;

        if (_certs.size() != 0)
        {
            certificates = CMSUtils.createBerSetFromList(_certs);
        }

        ASN1Set certrevlist = null;

        if (_crls.size() != 0)
        {
            certrevlist = CMSUtils.createBerSetFromList(_crls);
        }

        ContentInfo    encInfo;
        
        if (encapsulate && !contentIsHash) {
            ByteArrayOutputStream   bOut = new ByteArrayOutputStream();

            try
            {
                content.write(bOut);
            }
            catch (IOException e)
            {
                throw new CMSException("Erro de encapsulamento.", e);
            }

            ASN1OctetString  octs = new BERConstructedOctetString(
                                                    bOut.toByteArray());

            encInfo = new ContentInfo(contentTypeOID, octs);
        }
        else
        {
            encInfo = new ContentInfo(contentTypeOID, null);
        }

        SignedData  sd = new SignedData(
                                 new DERSet(digestAlgs),
                                 encInfo, 
                                 certificates, 
                                 certrevlist, 
                                 new DERSet(signerInfos));

        ContentInfo contentInfo = new ContentInfo(
                PKCSObjectIdentifiers.signedData, sd);

        return new CMSSignedData(content, contentInfo);
    }
    
    /**
     * Gera um objeto assinado (CMS Signed Data) utilizando o provider informado em sigProvider
     * Se o parametro encapsulate eh true uma copia do conteudo sera incluida/anexada na assinatura.
     * O tipo do conteudo sera "data" 
     *
     * @param content
     * @param encapsulate -> se True gera assinatura anexada.
     * @param sigProvider
     * @return CMSSignedData
     * @see org.bouncycastle.cms.CMSSignedData
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws CMSException
     */
    public CMSSignedData generate(
        CMSProcessable  content,
        boolean         encapsulate,
        String          sigProvider)
        throws NoSuchAlgorithmException, NoSuchProviderException, CMSException
    {
        return this.generate(DATA, content, encapsulate, sigProvider);
    }
}
