package org.demoiselle.applet_customizada;

import java.applet.Applet;
import java.io.*;
import java.security.*;
import javax.swing.JOptionPane;
import org.demoiselle.signer.signature.applet.action.AbstractAppletExecute;
import org.demoiselle.signer.signature.applet.certificate.ICPBrasilCertificate;
import org.demoiselle.signer.signature.signer.factory.PKCS7Factory;
import org.demoiselle.signer.signature.signer.pkcs7.PKCS7Signer;
import org.demoiselle.signer.signature.policy.engine.factory.PolicyFactory;
import org.slf4j.*;

public class App extends AbstractAppletExecute {

	private static final Logger logger = LoggerFactory.getLogger(App.class);

	@Override
	public void execute(KeyStore keystore, String alias, Applet applet) {
		try {

			/* Carregando o conteudo a ser assinado */
			String documento = AbstractAppletExecute.getFormField(applet,
					"mainForm", "documento");
			
			if (documento.length() == 0) {
				JOptionPane.showMessageDialog(applet,
						"Por favor, escolha um documento para assinar",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String user_home = System.getProperty("user.home");
			File file = new File(documento);
			String doc = documento;

			logger.info("user.home......: {}", user_home);
			logger.info("parm documento......: {}", doc);

			String canonicalPath = file.getCanonicalPath();
			logger.info("canonicalPath...........: {}", canonicalPath);

			String pathAbs = file.getAbsolutePath();
			logger.info("pathAbs...........: {}", pathAbs);

			String path = file.getPath();
			logger.info("Path...........: {}", path);

			byte[] content = readContent(canonicalPath);

			/* Parametrizando o objeto doSign */

			PKCS7Signer signer = PKCS7Factory.getInstance().factoryDefault();
			signer.setCertificates(keystore.getCertificateChain(alias));
			signer.setPrivateKey((PrivateKey) keystore.getKey(alias, null));
			signer.setSignaturePolicy(PolicyFactory.Policies.AD_RB_CADES_2_2);
			signer.setAttached(false);

			/* Realiza a assinatura do conteudo */

			logger.info("Efetuando a  assinatura do conteudo");

			byte[] signed = signer.doSign(content);
			/* Grava o conteudo assinado no disco */

			writeContent(signed, documento.concat(".p7s"));
			/* Valida o conteudo */

			logger.info("Efetuando a validacao da assinatura.");
			boolean checked = signer.check(content, signed);

			if (checked) {
				logger.info("A assinatura e valida.");
				JOptionPane.showMessageDialog(applet,
						"O arquivo foi assinado e validado com sucesso.",
						"Mensagem", JOptionPane.INFORMATION_MESSAGE);
			} else {
				logger.info("A assinatura nao e valida!");
			}

			/* Exibe alguns dados do certificado */

			ICPBrasilCertificate certificado = super.getICPBrasilCertificate(
					keystore, alias, false);
			AbstractAppletExecute.setFormField(applet, "mainForm", "cpf",
					certificado.getCpf());
			AbstractAppletExecute.setFormField(applet, "mainForm", "nome",
					certificado.getNome());
			AbstractAppletExecute.setFormField(applet, "mainForm",
					"nascimento", certificado.getDataNascimento());
			AbstractAppletExecute.setFormField(applet, "mainForm", "email",
					certificado.getEmail());
		} catch (KeyStoreException | NoSuchAlgorithmException
				| UnrecoverableKeyException | IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(applet, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} 
	}

	@Override
	public void cancel(KeyStore keystore, String alias, Applet applet) {
		/* Seu codigo customizado aqui... */

	}

	private byte[] readContent(String arquivo) {
		
		byte[] result = null;
		try {
			File file = new File(arquivo);
			FileInputStream is = new FileInputStream(file);
			result = new byte[(int) file.length()];
			is.read(result);
			is.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println();
			logger.info(ex.getMessage());
		}
		return result;
	}

	private void writeContent(byte[] conteudo, String arquivo) {
		try {
			File file = new File(arquivo);
			FileOutputStream os = new FileOutputStream(file);
			os.write(conteudo);
			os.flush();
			os.close();
		} catch (IOException ex) {
			logger.info(ex.getMessage());
		}
	}
}