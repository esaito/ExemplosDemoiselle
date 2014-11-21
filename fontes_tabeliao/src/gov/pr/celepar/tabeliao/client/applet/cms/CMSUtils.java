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
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.BERSet;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.cms.CMSException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CRLException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Classe utilitaria para efetuar Assinatura PKCS#7/CMS
 * 
 * @author Thiago Meneghello - GIC/CELEPAR
 *
 */
class CMSUtils
{
    private static final Runtime RUNTIME = Runtime.getRuntime();
    
    static int getMaximumMemory()
    {
        long maxMem = RUNTIME.maxMemory();
        
        if (maxMem > Integer.MAX_VALUE)
        {
            return Integer.MAX_VALUE;
        }
        
        return (int)maxMem;
    }
    
    /**
     * 
     * @param input -> array de byte
     * @return ContentInfo
     * @see org.bouncycastle.asn1.cms.ContentInfo
     * @throws CMSException
     */
    static ContentInfo readContentInfo(
        byte[] input)
        throws CMSException
    {
        // enforce limit checking as from a byte array
        return readContentInfo(new ASN1InputStream(input));
    }

    /**
     * 
     * @param input -> InputStream
     * @return ContentInfo
     * @see org.bouncycastle.asn1.cms.ContentInfo
     * @throws CMSException
     */
    static ContentInfo readContentInfo(
        InputStream input)
        throws CMSException
    {
        // enforce some limit checking
        return readContentInfo(new ASN1InputStream(input, getMaximumMemory()));
    } 

    /**
     * 
     * @param certStore
     * @return Lista de X509CertificateStructure
     * @see org.bouncycastle.asn1.x509.X509CertificateStructure
     * @throws CertStoreException
     * @throws CMSException
     */
    static List<X509CertificateStructure> getCertificatesFromStore(CertStore certStore)
        throws CertStoreException, CMSException
    {
        List<X509CertificateStructure> certs = new ArrayList<X509CertificateStructure>();

        try
        {
            for (Iterator<?> it = certStore.getCertificates(null).iterator(); it.hasNext();)
            {
                X509Certificate c = (X509Certificate)it.next();

                certs.add(X509CertificateStructure.getInstance(
                                                       ASN1Object.fromByteArray(c.getEncoded())));
            }

            return certs;
        }
        catch (IllegalArgumentException e)
        {
            throw new CMSException("Erro ao processar os certificados", e);
        }
        catch (IOException e)
        {
            throw new CMSException("Erro ao processar os certificados", e);
        }
        catch (CertificateEncodingException e)
        {
            throw new CMSException("Erro ao codificar os certificados", e);
        }
    }

    /**
     * Retorna lista de certificados revogados
     * @param certStore
     * @return lista de CertificateList
     * @see org.bouncycastle.asn1.x509.CertificateList 
     * @throws CertStoreException
     * @throws CMSException
     */
    static List<CertificateList> getCRLsFromStore(CertStore certStore)
        throws CertStoreException, CMSException
    {
        List<CertificateList> crls = new ArrayList<CertificateList>();

        try
        {
            for (Iterator<?> it = certStore.getCRLs(null).iterator(); it.hasNext();)
            {
                X509CRL c = (X509CRL)it.next();

                crls.add(CertificateList.getInstance(ASN1Object.fromByteArray(c.getEncoded())));
            }

            return crls;
        }
        catch (IllegalArgumentException e)
        {
            throw new CMSException("Erro ao processar as listas de certificados revogados", e);
        }
        catch (IOException e)
        {
            throw new CMSException("Erro ao processar as listas de certificados revogados", e);
        }
        catch (CRLException e)
        {
            throw new CMSException("Erro ao codificar as listas de certificados revogados", e);
        }
    }

    /**
     * Gera em formato BER ASN.1
     * @param derObjects -> lista sem tipo definido
     * @return ASN1Set
     * @see org.bouncycastle.asn1.ASN1Set 
     */
    static ASN1Set createBerSetFromList(List<?> derObjects)
    {
        ASN1EncodableVector v = new ASN1EncodableVector();

        for (Iterator<?> it = derObjects.iterator(); it.hasNext();)
        {
            v.add((DEREncodable)it.next());
        }

        return new BERSet(v);
    }

    /**
     * Gera em formato DER ASN.1
     * @param derObjects -> lista sem tipo definido
     * @return ASN1Set
     * @see org.bouncycastle.asn1.ASN1Set 
     */
    static ASN1Set createDerSetFromList(List<?> derObjects)
    {
        ASN1EncodableVector v = new ASN1EncodableVector();

        for (Iterator<?> it = derObjects.iterator(); it.hasNext();)
        {
            v.add((DEREncodable)it.next());
        }

        return new DERSet(v);
    }

    /**
     * 
     * @param in ASN1InputStream
     * @see org.bouncycastle.asn1.ASN1InputStream
     * @return ContentInfo
     * @see org.bouncycastle.asn1.cms.ContentInfo
     * @throws CMSException
     */
    private static ContentInfo readContentInfo(
        ASN1InputStream in)
        throws CMSException
    {
        try
        {
            return ContentInfo.getInstance(in.readObject());
        }
        catch (IOException e)
        {
            throw new CMSException("Erro de IOException ao ler o conteudo.", e);
        }
        catch (ClassCastException e)
        {
            throw new CMSException("Conteudo mal formatado.", e);
        }
        catch (IllegalArgumentException e)
        {
            throw new CMSException("Conteudo mal formatado.", e);
        }
    }
    
    /**
     * 
     * @param in -> InputStream
     * @return array de byte
     * @throws IOException
     */
    public static byte[] streamToByteArray(
        InputStream in) 
        throws IOException
    {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        int ch;
        
        while ((ch = in.read()) >= 0)
        {
            bOut.write(ch);
        }
        
        return bOut.toByteArray();
    }
}
