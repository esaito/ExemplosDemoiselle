package gov.pr.celepar.tabeliao.util;
/*
Este programa é licenciado de acordo com a 
LPG-AP (LICENÇA PÚBLICA GERAL PARA PROGRAMAS DE COMPUTADOR DA ADMINISTRAÇÃO PÚBLICA), 
versão 1.1 ou qualquer versão posterior.
A LPG-AP deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa.
Caso uma cópia da LPG-AP não esteja disponível junto com este Programa, 
você pode contatar o LICENCIANTE ou então acessar diretamente:
http://www.celepar.pr.gov.br/licenca/LPG-AP.pdf
Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa 
é preciso estar de acordo com os termos da LPG-AP
*/
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.List;

/**
 * Prove utilitarios para codificar ou decodificar dados binarios para base64
 * @author Thiago Meneghello - GIC/CELEPAR
 */
public class Base64Utils {

    private static final String X509_CERTIFICATE_TYPE = "X.509";
    private static final String CERTIFICATION_CHAIN_ENCODING = "PkiPath";
    private static byte[] mBase64EncMap, mBase64DecMap;

    /**
     * Class initializer. Initializes the Base64 alphabet (specified in RFC-2045).
     */
    static {
        byte[] base64Map = {
            (byte)'A', (byte)'B', (byte)'C', (byte)'D', (byte)'E', (byte)'F',
            (byte)'G', (byte)'H', (byte)'I', (byte)'J', (byte)'K', (byte)'L',
            (byte)'M', (byte)'N', (byte)'O', (byte)'P', (byte)'Q', (byte)'R',
            (byte)'S', (byte)'T', (byte)'U', (byte)'V', (byte)'W', (byte)'X',
            (byte)'Y', (byte)'Z',
            (byte)'a', (byte)'b', (byte)'c', (byte)'d', (byte)'e', (byte)'f',
            (byte)'g', (byte)'h', (byte)'i', (byte)'j', (byte)'k', (byte)'l',
            (byte)'m', (byte)'n', (byte)'o', (byte)'p', (byte)'q', (byte)'r',
            (byte)'s', (byte)'t', (byte)'u', (byte)'v', (byte)'w', (byte)'x',
            (byte)'y', (byte)'z',
            (byte)'0', (byte)'1', (byte)'2', (byte)'3', (byte)'4', (byte)'5',
            (byte)'6', (byte)'7', (byte)'8', (byte)'9', (byte)'+', (byte)'/' };
        mBase64EncMap = base64Map;
        mBase64DecMap = new byte[128];
        for (int i=0; i<mBase64EncMap.length; i++)
            mBase64DecMap[mBase64EncMap[i]] = (byte) i;
    }

    /**
     * Esta classe nao deveria ser instanciada
     */
    private Base64Utils() {
    }

    /**
     * Codifica um dado array de bytes usando codificacao Base64
     * como especificado na RFC-2045 (sessao 6.8)
     *
     * @param aData os dados a serem codificados
     * @return os dados de <var>aData</var> codificados em Base64
     * @exception IllegalArgumentException se NULL ou um array vazio foi passado
     */
    public static String base64Encode(byte[] aData) {
        if ((aData == null) || (aData.length == 0))
            throw new IllegalArgumentException("Can not encode NULL or empty byte array.");

        byte encodedBuf[] = new byte[((aData.length+2)/3)*4];

        // 3-byte to 4-byte conversion
        int srcIndex, destIndex;
        for (srcIndex=0, destIndex=0; srcIndex < aData.length-2; srcIndex += 3) {
            encodedBuf[destIndex++] = mBase64EncMap[(aData[srcIndex] >>> 2) & 077];
            encodedBuf[destIndex++] = mBase64EncMap[(aData[srcIndex+1] >>> 4) & 017 |
                        (aData[srcIndex] << 4) & 077];
            encodedBuf[destIndex++] = mBase64EncMap[(aData[srcIndex+2] >>> 6) & 003 |
                        (aData[srcIndex+1] << 2) & 077];
            encodedBuf[destIndex++] = mBase64EncMap[aData[srcIndex+2] & 077];
        }

        // Convert the last 1 or 2 bytes
        if (srcIndex < aData.length) {
            encodedBuf[destIndex++] = mBase64EncMap[(aData[srcIndex] >>> 2) & 077];
            if (srcIndex < aData.length-1) {
                encodedBuf[destIndex++] = mBase64EncMap[(aData[srcIndex+1] >>> 4) & 017 |
                    (aData[srcIndex] << 4) & 077];
                encodedBuf[destIndex++] = mBase64EncMap[(aData[srcIndex+1] << 2) & 077];
            }
            else {
                encodedBuf[destIndex++] = mBase64EncMap[(aData[srcIndex] << 4) & 077];
            }
        }

        // Add padding to the end of encoded data
        while (destIndex < encodedBuf.length) {
            encodedBuf[destIndex] = (byte) '=';
            destIndex++;
        }

        String result = new String(encodedBuf);
        return result;
    }


    /**
     * Decodifica uma string de dados codificados em Base64,
     * conforme especificado na RFC-2045 (Seccao 6.8).
     *
     * @param aData dados codificados em Base64.
     * @return os dados<var>aData</var> decodificados em um array de byte.
     * @exception IllegalArgumentException se NULL ou foi passado vazio.
     */
    public static byte[] base64Decode(String aData) {
        if ((aData == null) || (aData.length() == 0))
            throw new IllegalArgumentException("Can not decode NULL or empty string.");

        byte[] data = aData.getBytes();

        // Skip padding from the end of encoded data
        int tail = data.length;
        while (data[tail-1] == '=')
            tail--;

        byte decodedBuf[] = new byte[tail - data.length/4];

        // ASCII-printable to 0-63 conversion
        for (int i = 0; i < data.length; i++)
            data[i] = mBase64DecMap[data[i]];

        // 4-byte to 3-byte conversion
        int srcIndex, destIndex;
        for (srcIndex = 0, destIndex=0; destIndex < decodedBuf.length-2;
                srcIndex += 4, destIndex += 3) {
            decodedBuf[destIndex] = (byte) ( ((data[srcIndex] << 2) & 255) |
                ((data[srcIndex+1] >>> 4) & 003) );
            decodedBuf[destIndex+1] = (byte) ( ((data[srcIndex+1] << 4) & 255) |
                ((data[srcIndex+2] >>> 2) & 017) );
            decodedBuf[destIndex+2] = (byte) ( ((data[srcIndex+2] << 6) & 255) |
                (data[srcIndex+3] & 077) );
        }

        // Handle last 1 or 2 bytes
        if (destIndex < decodedBuf.length)
            decodedBuf[destIndex] = (byte) ( ((data[srcIndex] << 2) & 255) |
                ((data[srcIndex+1] >>> 4) & 003) );
        if (++destIndex < decodedBuf.length)
            decodedBuf[destIndex] = (byte) ( ((data[srcIndex+1] << 4) & 255) |
                ((data[srcIndex+2] >>> 2) & 017) );

        return decodedBuf;
    }
    
    /**
	 * @return a representacao ASN.1 DER codificado em Base64, de uma cadeia de
	 * certificado X.509 informado.	 * 
	 */
	public static String encodeX509CertChainToBase64(Certificate[] aCertificationChain)
			throws CertificateException {
	    List<Certificate> certList = Arrays.asList(aCertificationChain);
	    CertificateFactory certFactory =
	        CertificateFactory.getInstance(X509_CERTIFICATE_TYPE);
	    CertPath certPath = certFactory.generateCertPath(certList);
	    byte[] certPathEncoded = certPath.getEncoded(CERTIFICATION_CHAIN_ENCODING);
	    String base64encodedCertChain = Base64Utils.base64Encode(certPathEncoded);
	    return base64encodedCertChain;
	}

}
