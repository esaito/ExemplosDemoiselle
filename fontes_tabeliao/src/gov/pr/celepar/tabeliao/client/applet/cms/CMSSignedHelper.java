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
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.x509.NoSuchStoreException;
import org.bouncycastle.x509.X509CollectionStoreParameters;
import org.bouncycastle.x509.X509Store;
import org.bouncycastle.x509.X509V2AttributeCertificate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.cert.CRLException;
import java.security.cert.CertStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe com metodos auxiliares para efetuar Assinatura em formato PKCS#7/CMS 
 * 
 * @author Thiago Meneghello - GIC/CELEPAR
 *
 */


// Classe implementada para possibilitar a assinatura do Hash do Arquivo.
class CMSSignedHelper
{
    static final CMSSignedHelper INSTANCE = new CMSSignedHelper();

    private static final Map<String, String>     encryptionAlgs = new HashMap<String, String>();
    private static final Map<String, String>     digestAlgs = new HashMap<String, String>();


    static
    {
        encryptionAlgs.put(X9ObjectIdentifiers.id_dsa_with_sha1.getId(), "DSA");
        encryptionAlgs.put(X9ObjectIdentifiers.id_dsa.getId(), "DSA");
        encryptionAlgs.put(OIWObjectIdentifiers.dsaWithSHA1.getId(), "DSA");
        encryptionAlgs.put(PKCSObjectIdentifiers.rsaEncryption.getId(), "RSA");
        encryptionAlgs.put(PKCSObjectIdentifiers.sha1WithRSAEncryption.getId(), "RSA");
        encryptionAlgs.put(TeleTrusTObjectIdentifiers.teleTrusTRSAsignatureAlgorithm, "RSA");
        encryptionAlgs.put(X509ObjectIdentifiers.id_ea_rsa.getId(), "RSA");
        encryptionAlgs.put(CMSTabeliaoSignedDataGenerator.ENCRYPTION_ECDSA, "ECDSA");
        encryptionAlgs.put(X9ObjectIdentifiers.ecdsa_with_SHA2.getId(), "ECDSA");
        encryptionAlgs.put(X9ObjectIdentifiers.ecdsa_with_SHA224.getId(), "ECDSA");
        encryptionAlgs.put(X9ObjectIdentifiers.ecdsa_with_SHA256.getId(), "ECDSA");
        encryptionAlgs.put(X9ObjectIdentifiers.ecdsa_with_SHA384.getId(), "ECDSA");
        encryptionAlgs.put(X9ObjectIdentifiers.ecdsa_with_SHA512.getId(), "ECDSA");
        encryptionAlgs.put(CMSTabeliaoSignedDataGenerator.ENCRYPTION_RSA_PSS, "RSAandMGF1");
        encryptionAlgs.put(CryptoProObjectIdentifiers.gostR3410_94.getId(), "GOST3410");
        encryptionAlgs.put(CryptoProObjectIdentifiers.gostR3410_2001.getId(), "ECGOST3410");
        encryptionAlgs.put("1.3.6.1.4.1.5849.1.6.2", "ECGOST3410");
        encryptionAlgs.put("1.3.6.1.4.1.5849.1.1.5", "GOST3410");

        digestAlgs.put(PKCSObjectIdentifiers.md5.getId(), "MD5");
        digestAlgs.put(OIWObjectIdentifiers.idSHA1.getId(), "SHA1");
        digestAlgs.put(NISTObjectIdentifiers.id_sha224.getId(), "SHA224");
        digestAlgs.put(NISTObjectIdentifiers.id_sha256.getId(), "SHA256");
        digestAlgs.put(NISTObjectIdentifiers.id_sha384.getId(), "SHA384");
        digestAlgs.put(NISTObjectIdentifiers.id_sha512.getId(), "SHA512");
        digestAlgs.put(PKCSObjectIdentifiers.sha1WithRSAEncryption.getId(), "SHA1");
        digestAlgs.put(PKCSObjectIdentifiers.sha224WithRSAEncryption.getId(), "SHA224");
        digestAlgs.put(PKCSObjectIdentifiers.sha256WithRSAEncryption.getId(), "SHA256");
        digestAlgs.put(PKCSObjectIdentifiers.sha384WithRSAEncryption.getId(), "SHA384");
        digestAlgs.put(PKCSObjectIdentifiers.sha512WithRSAEncryption.getId(), "SHA512");
        digestAlgs.put(TeleTrusTObjectIdentifiers.ripemd128.getId(), "RIPEMD128");
        digestAlgs.put(TeleTrusTObjectIdentifiers.ripemd160.getId(), "RIPEMD160");
        digestAlgs.put(TeleTrusTObjectIdentifiers.ripemd256.getId(), "RIPEMD256");
        digestAlgs.put(CryptoProObjectIdentifiers.gostR3411.getId(),  "GOST3411");
        digestAlgs.put("1.3.6.1.4.1.5849.1.2.1",  "GOST3411");
    }
    
    /**
     * 
     * Return the digest algorithm using one of the standard JCA string
     * representations rather than the algorithm identifier (if possible).
     * 
     * @param digestAlgOID
     * @return Retorna o algoritimo de resumo usando um dos formatos padroes de representacao JCA em vez do 
     * identificador (se possivel). 
     */
    String getDigestAlgName(
        String digestAlgOID)
    {
        String algName = (String)digestAlgs.get(digestAlgOID);

        if (algName != null)
        {
            return algName;
        }

        return digestAlgOID;
    }
    
    /**
     * 
     * Return the digest encryption algorithm using one of the standard
     * JCA string representations rather the the algorithm identifier (if
     * possible).
     * 
     * @param encryptionAlgOID
     * @return Retorma o algoritimo de encriptacao de resumo usando um dos padroes de representacao JCA
     * em vez do identificador (se possivel).
     */
    String getEncryptionAlgName(
        String encryptionAlgOID)
    {
        String algName = (String)encryptionAlgs.get(encryptionAlgOID);

        if (algName != null)
        {
            return algName;
        }

        return encryptionAlgOID;
    }
    
    /**
     * 
     * @param algorithm
     * @param provider
     * @return instancia do resumo -> MessageDigest
     * @see java.security.MessageDigest 
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     */
    MessageDigest getDigestInstance(
        String algorithm, 
        String provider) 
        throws NoSuchProviderException, NoSuchAlgorithmException
    {
        if (provider != null)
        {
            try
            {
                return MessageDigest.getInstance(algorithm, provider);
            }
            catch (NoSuchAlgorithmException e)
            {
                return MessageDigest.getInstance(algorithm); // tentar retornar
            }
        }
        else
        {
            return MessageDigest.getInstance(algorithm);
        }
    }
    
    /**
     * 
     * @param algorithm
     * @param provider
     * @return Instancia da Assinatura -> Signature 
     * @see java.security.Signature
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     */
    Signature getSignatureInstance(
        String algorithm, 
        String provider) 
        throws NoSuchProviderException, NoSuchAlgorithmException
    {
        if (provider != null)
        {
            return Signature.getInstance(algorithm, provider);
        }
        else
        {
            return Signature.getInstance(algorithm);
        }
    }
    
    /**
     * 
     * @param type
     * @param provider
     * @param certSet -> conjunto/cadeia de cerficados 
     * @return X509Store
     * @see org.bouncycastle.x509.X509Store
     * @throws NoSuchStoreException
     * @throws NoSuchProviderException
     * @throws CMSException
     */
    X509Store createAttributeStore(
        String type,
        String provider,
        ASN1Set certSet)
        throws NoSuchStoreException, NoSuchProviderException, CMSException
    {
        List<Object> certs = new ArrayList<Object>();

        if (certSet != null)
        {
            Enumeration<?> e = certSet.getObjects();

            while (e.hasMoreElements())
            {
                try
                {
                    DERObject obj = ((DEREncodable)e.nextElement()).getDERObject();

                    if (obj instanceof ASN1TaggedObject)
                    {
                        ASN1TaggedObject tagged = (ASN1TaggedObject)obj;

                        if (tagged.getTagNo() == 2)
                        {
                            certs.add(new X509V2AttributeCertificate(ASN1Sequence.getInstance(tagged, false).getEncoded()));
                        }
                    }
                }
                catch (IOException ex)
                {
                    throw new CMSException(
                            "Nao e possivel recodificar o atributo do certificado: ", ex);
                }
            }
        }

        try
        {
            return X509Store.getInstance(
                         "AttributeCertificate/" +type, new X509CollectionStoreParameters(certs), provider);
        }
        catch (IllegalArgumentException e)
        {
            throw new CMSException("Não é possivel setar/gerar o X509Store", e);
        }
    }
    
    /**
     * 
     * @param type
     * @param provider
     * @param certSet -> conjunto/cadeia de certificados
     * @return X509Store
     * @see org.bouncycastle.x509.X509Store
     * @throws NoSuchStoreException
     * @throws NoSuchProviderException
     * @throws CMSException
     */
    X509Store createCertificateStore(
        String type,
        String provider,
        ASN1Set certSet)
        throws NoSuchStoreException, NoSuchProviderException, CMSException
    {
        List<Object> certs = new ArrayList<Object>();

        if (certSet != null)
        {
            addCertsFromSet(certs, certSet, provider);
        }

        try
        {
            return X509Store.getInstance(
                         "Certificate/" +type, new X509CollectionStoreParameters(certs), provider);
        }
        catch (IllegalArgumentException e)
        {
            throw new CMSException("Nao e possivel setar/gerar o X509Store", e);
        }
    }

    /**
     * 
     * @param type
     * @param provider
     * @param crlSet -> Conjunto de listas de certificados revogados
     * @return X509Store
     * @see org.bouncycastle.x509.X509Store
     * @throws NoSuchStoreException
     * @throws NoSuchProviderException
     * @throws CMSException
     */
    X509Store createCRLsStore(
        String type,
        String provider,
        ASN1Set crlSet)
        throws NoSuchStoreException, NoSuchProviderException, CMSException
    {
        List<Object> crls = new ArrayList<Object>();

        if (crlSet != null)
        {
            addCRLsFromSet(crls, crlSet, provider);
        }

        try
        {
            return X509Store.getInstance(
                         "CRL/" +type, new X509CollectionStoreParameters(crls), provider);
        }
        catch (IllegalArgumentException e)
        {
            throw new CMSException("Nao e possivel setar/gerar o X509Store", e);
        }
    }

    /**
     * carrega os certificados e listas de revogacao se houver
     * @param type
     * @param provider
     * @param certSet -> Conjunto/cadeia de certificados
     * @param crlSet -> Conjunto de listas de certificados revogados
     * @return CertStore
     * @see java.security.cert.CertStore
     * @throws NoSuchProviderException
     * @throws CMSException
     * @throws NoSuchAlgorithmException
     */
    CertStore createCertStore(
        String type,
        String provider,
        ASN1Set certSet,
        ASN1Set crlSet)
        throws NoSuchProviderException, CMSException, NoSuchAlgorithmException
    {
        List<Object> certsAndcrls = new ArrayList<Object>();

        //
        // carrega os certificados e listas de revogação se houver
        //

        if (certSet != null)
        {
            addCertsFromSet(certsAndcrls, certSet, provider);
        }

        if (crlSet != null)
        {
            addCRLsFromSet(certsAndcrls, crlSet, provider);
        }

        try
        {
            return CertStore.getInstance(type, new CollectionCertStoreParameters(certsAndcrls), provider);
        }
        catch (InvalidAlgorithmParameterException e)
        {
            throw new CMSException("Nao e possivel gerar/setar o CertStore", e);
        }
    }

    /**
     * 
     * @param certs -> lista de objetos
     * @param certSet -> conjunto/cadeia de certificados
     * @param provider
     * @throws NoSuchProviderException
     * @throws CMSException
     */
    private void addCertsFromSet(List<Object> certs, ASN1Set certSet, String provider)
        throws NoSuchProviderException, CMSException
    {
        CertificateFactory cf;

        try
        {
            cf = CertificateFactory.getInstance("X.509", provider);
        }
        catch (CertificateException ex)
        {
            throw new CMSException("Nao e possivel pegar o certificate factory.", ex);
        }
        Enumeration<?> e = certSet.getObjects();

        while (e.hasMoreElements())
        {
            try
            {
                DERObject obj = ((DEREncodable)e.nextElement()).getDERObject();

                if (obj instanceof ASN1Sequence)
                {
                    certs.add(cf.generateCertificate(
                        new ByteArrayInputStream(obj.getEncoded())));
                }
            }
            catch (IOException ex)
            {
                throw new CMSException(
                        "Nao e possivel recodificar o certificado!", ex);
            }
            catch (CertificateException ex)
            {
                throw new CMSException(
                        "Nao e possivel recodificar o certificado!", ex);
            }
        }
    }

    /**
     * 
     * @param crls -> lista de certificados revogados
     * @param certSet -> conjunto/cadeia de certificados
     * @param provider
     * @throws NoSuchProviderException
     * @throws CMSException
     */
    private void addCRLsFromSet(List<Object> crls, ASN1Set certSet, String provider)
        throws NoSuchProviderException, CMSException
    {
        CertificateFactory cf;

        try
        {
            cf = CertificateFactory.getInstance("X.509", provider);
        }
        catch (CertificateException ex)
        {
            throw new CMSException("Nao e possivel pegar o certificate factory.", ex);
        }
        Enumeration<?> e = certSet.getObjects();

        while (e.hasMoreElements())
        {
            try
            {
                DERObject obj = ((DEREncodable)e.nextElement()).getDERObject();

                crls.add(cf.generateCRL(
                    new ByteArrayInputStream(obj.getEncoded())));
            }
            catch (IOException ex)
            {
                throw new CMSException("Nao e possivel recodificar o certificado!", ex);
            }
            catch (CRLException ex)
            {
                throw new CMSException("Nao e possivel recodificar o certificado!", ex);
            }
        }
    }
/*
    private boolean anyCertHasTypeOther()
    {
        // not supported
        return false;
    }

    private boolean anyCertHasV1Attribute()
    {
        // obsolete 
        return false;
    }

    private boolean anyCertHasV2Attribute()
    {
        // TODO - Não implmentado Bouncy
        return false;
    }

    private boolean anyCrlHasTypeOther()
    {
        // not supported
        return false;
    }
*/
}
