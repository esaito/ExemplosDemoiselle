package gov.pr.celepar.tabeliao.client.applet;
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
import gov.pr.celepar.tabeliao.client.applet.action.ActionHandler;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import java.io.*;
import java.util.Properties;

/**
 * Classe da interface grafica da applet para autenticacao e assinatura.
 * 
 * @version 1.0
 * @author Thiago Meneghello - GIC/CELEPAR
 * @author Emerson Sachio Saito (alteracoes) - GIC/CELEPAR - Alterou.
 *
 */
public class CertificadoPanel extends JPanel {

	private static final long serialVersionUID = 3285410887543077887L;
	
	/* Constantes para o arquivo de configuracao */
	private static final String CONFIG_FILE_NAME         = ".tabeliao_applet.config";
	private static final String TIPO_CERTIFICADO_DEFAULT = "last-certificate-type";
	private static final String PKCS11_LIBRARY_FILE_NAME = "last-PKCS11-file-name";
    private static final String PKCS12_FILE_NAME         = "last-PKCS12-file-name";

    public static final String DEFAULT_LABEL_BUTTON      = "Ok";
	public static final String DEFAULT_LIB_NAME          = "/usr/lib/opensc/opensc-pkcs11.so";
    
	/* Constantes para os labels do dialogo */
	private static final String LABEL_PKCS11_BIBLIOTECA  = "Selecione o arquivo da biblioteca PKCS#11 (.dll / .so) :";
	private static final String LABEL_PKCS11_PIN         = "Digite o PIN (senha) para acessar o cartão ou token:";
	private static final String LABEL_PKCS12_CERTIFICADO = "Selecione o arquivo de certificado (.pfx / .p12) :";
	private static final String LABEL_PKCS12_SENHA       = "Digite a senha para acessar a chave privada:";
	/* Constantes para o construtor da classe */
    public static final int AUTENTICACAO_TOKEN           = 1;
    public static final int AUTENTICACAO_ARQUIVO         = 2;
    public static final int AUTENTICACAO_TOKEN_ARQUIVO   = 3;
    
	private String labelButton;
	private String biblioteca;
	private JPanel panelPKCS11;
	private JPanel panelPKCS12;
	private JPanel panelConfig;
	
	private ActionHandler actionHandler;
	
	/* Objetos do form */
	 
	public JTabbedPane tabPane;
	private ImageIcon iconeTabeliao;
	private JLabel labelIconeTabeliao1 = new JLabel();
	private JLabel labelIconeTabeliao2 = new JLabel();
	private JLabel labelIconeTabeliao3 = new JLabel();
	private boolean certificadoHardware = true;
	
	/* Objetos do Panel PKCS11 */
	private JTextField textPKCS11PIN          = new JPasswordField();
	private JLabel     labelPKCS11PIN         = new JLabel();
	private JProgressBar pb11                 = new JProgressBar(0, 8);
	private JButton    buttonPKCS11Ok         = new JButton();

	/* Objetos do Panel PKCS12 */
    private JTextField textPKCS12NomeArquivo  = new JTextField();
    private JLabel     labelPKCS12NomeArquivo = new JLabel();
    private JButton    buttonPKCS12Localizar  = new JButton();
    private JTextField textPKCS12Senha        = new JPasswordField();
    private JLabel     labelPKCS12Senha       = new JLabel();
    private JProgressBar pb12                 = new JProgressBar(0, 8);
    private JButton    buttonPKCS12Ok         = new JButton();

	/* Objetos do Panel Config */
	private JTextField textConfigNomeArquivo  = new JTextField();
	private JLabel     labelConfigNomeArquivo = new JLabel();
	private JButton    buttonConfigLocalizar  = new JButton();
    
	// Construtores
	
	public CertificadoPanel(ActionHandler actionHandler, int valCorFundo, int valCorDentro, int valCorAbaSelecionada, int valCorAba) {
		this(actionHandler, AUTENTICACAO_TOKEN_ARQUIVO, DEFAULT_LABEL_BUTTON, DEFAULT_LIB_NAME, valCorFundo, valCorDentro, valCorAbaSelecionada, valCorAba);
	}
	
	/**
	 * Inicializa o Painel de dialogo - Cria e configura os controles.
	 */
	public CertificadoPanel(ActionHandler actionHandler, int autenticacao, String labelButton, String biblioteca, int valCorFundo, int valCorDentro, int valCorAbaSelecionada, int valCorAba) {
		super();

		this.labelButton = labelButton;
		this.actionHandler = actionHandler;
		this.biblioteca = biblioteca;
		
		UIManager.put("TabbedPane.selected", new Color(valCorAbaSelecionada));
		UIManager.put("TabbedPane.contentAreaColor", new Color(valCorAbaSelecionada));
		
// Mostra todos os atributos do UIManager...
//		UIDefaults defaults = UIManager.getDefaults();
//        String[ ] colName = {"Key", "Value"};
//        String[ ][ ] rowData = new String[ defaults.size() ][ 2 ];
//        int i = 0;
//        for(Enumeration e = defaults.keys(); e.hasMoreElements(); i++){
//            Object key = e.nextElement();
//            rowData[ i ] [ 0 ] = key.toString();
//            rowData[ i ] [ 1 ] = ""+defaults.get(key);
//        }
		
		tabPane = new JTabbedPane();

		// Initialize the dialog
        this.setLayout(null);
        this.setSize(450, 210);
        this.setBackground(new Color(valCorFundo));

        //Carrega o Icone do Tabelião
        iconeTabeliao = new ImageIcon(CertificadoPanel.class.getResource("icon_tabeliao.png"));
        labelIconeTabeliao1.setBounds(new Rectangle(385, 98, 45, 45));
        labelIconeTabeliao1.setIcon(iconeTabeliao);
        labelIconeTabeliao2.setBounds(new Rectangle(385, 98, 45, 45));
        labelIconeTabeliao2.setIcon(iconeTabeliao);
        labelIconeTabeliao3.setBounds(new Rectangle(385, 98, 45, 45));
        labelIconeTabeliao3.setIcon(iconeTabeliao);

        labelIconeTabeliao1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        labelIconeTabeliao2.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        labelIconeTabeliao3.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
  
        
        tabPane.setBounds(new Rectangle(0, 0, 440, 181));
    	tabPane.setBackground(new Color(valCorAba));
       
        tabPane.setDoubleBuffered(true);

        ImageIcon iconSmartCard = new ImageIcon(CertificadoPanel.class.getResource("icon_token_all.png"));
        ImageIcon iconArquivo   = new ImageIcon(CertificadoPanel.class.getResource("icon_arquivo_all.png"));
        ImageIcon iconConfig    = new ImageIcon(CertificadoPanel.class.getResource("icon_config_all.png"));
        
        //Cria a aba para autenticação via TOKEN
        if(autenticacao == AUTENTICACAO_TOKEN   || autenticacao == AUTENTICACAO_TOKEN_ARQUIVO) {
        	panelPKCS11 = carregaPanelPKCS11();
        	panelPKCS11.setBackground(new Color(valCorDentro));
        	panelPKCS11.add(labelIconeTabeliao1);
            tabPane.addTab("Cartão / Token", iconSmartCard, panelPKCS11);            
        }
        
        //Cria a aba para autenticação via ARQUIVO
        if(autenticacao == AUTENTICACAO_ARQUIVO || autenticacao == AUTENTICACAO_TOKEN_ARQUIVO) {
        	panelPKCS12 = carregaPanelPKCS12();
        	panelPKCS12.setBackground(new Color(valCorDentro));
        	panelPKCS12.add(labelIconeTabeliao2);
        	tabPane.addTab("Arquivo", iconArquivo, panelPKCS12);
        }

        //Cria a aba para configurações
        if(autenticacao == AUTENTICACAO_TOKEN   || autenticacao == AUTENTICACAO_TOKEN_ARQUIVO) {
        	panelConfig = carregaPanelConfig();
        	panelConfig.setBackground(new Color(valCorDentro));
        	panelConfig.add(labelIconeTabeliao3);
            tabPane.addTab("Configuração", iconConfig, panelConfig);
        }
        
        //Listener para mudar o Tab
        tabPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(tabPane.getSelectedComponent() == panelPKCS11){
					tabPane.getRootPane().setDefaultButton(buttonPKCS11Ok);
					KeyListener[] kl = textPKCS11PIN.getKeyListeners();
					if (kl.length > 0){
						textPKCS11PIN.removeKeyListener(kl[0]);
					}					
					textPKCS11PIN.requestFocus();
				} else if(tabPane.getSelectedComponent() == panelPKCS12) {
					tabPane.getRootPane().setDefaultButton(buttonPKCS12Ok);
					textPKCS12Senha.requestFocus();
				} else if(tabPane.getSelectedComponent() == panelConfig) {
					textConfigNomeArquivo.requestFocus();
				}
			}
        });
        
        tabPane.validate();
        add(tabPane, null);
    }
	
	/**
	 * Inicializa
	 * 
	 */
	public void init(){
		try {
			loadSettings();
		} catch (IOException ioex) {
			// Loading settings failed. Default settings will be used.
		}

		this.setVisible(true);
	}

	/**
	 * Mostra aba para uso de certificado armazenado em smartcard/token
	 * 
	 * @return JPanel
	 * @see javax.swing.JPanel
	 *
	 */
	private JPanel carregaPanelPKCS11(){
		
		
        labelPKCS11PIN.setText(LABEL_PKCS11_PIN);
        labelPKCS11PIN.setBounds(new Rectangle(10, 35, 350, 15));
        labelPKCS11PIN.setFont(new Font("Dialog", 0, 12));
        pb11.setValue(0);
        pb11.setStringPainted(true);
        pb11.setBackground(Color.WHITE);
        pb11.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        pb11.setBounds(new Rectangle(10, 105, 100, 20));
        pb11.setVisible(false);
                
        // Campo de digitação de senha
        textPKCS11PIN.setText("");
        textPKCS11PIN.setBounds(new Rectangle(10, 55, 420, 20));
        textPKCS11PIN.setFont(new Font("DialogInput", 0, 12));
        textPKCS11PIN.setBorder(BorderFactory.createEtchedBorder(Color.WHITE, Color.GRAY));
        textPKCS11PIN.setToolTipText("Clique aqui e digite a senha do Cartão/Token");
        
        // Botão de execução da ação
        buttonPKCS11Ok.setText(labelButton);
        buttonPKCS11Ok.setBounds(new Rectangle(170, 85, 95, 25));
        buttonPKCS11Ok.setToolTipText("Clique neste botão, após digitar a sua senha!");
        buttonPKCS11Ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {            
        		buttonPKCS11Ok.setText("Aguarde...");
        		pb11.setVisible(true);
        		pb11.setForeground(Color.GREEN);
        		pb11.setValue(1);
        		tabPane.paintImmediately(0, 0, 440, 181);
        		if (textPKCS11PIN.getText().trim().length() > 0){
        			pb11.setValue(2);
        			tabPane.paintImmediately(0, 0, 440, 181);
        			certificadoHardware = true;
        			if (signButton_actionPerformed()){
            			pb11.setValue(8);
            		}else{
            			pb11.setForeground(Color.RED);            			
            		}            		
            		buttonPKCS11Ok.setText(labelButton);
            		tabPane.paintImmediately(0, 0, 440, 181);
            	}else{
            		pb11.setValue(0);
            		buttonPKCS11Ok.setText(labelButton);
		            //JOptionPane.showMessageDialog(tabPane, errorMessage, errorMessage, JOptionPane.ERROR_MESSAGE);
		    		String[] botao = new String[]{"Fechar"};
		    		JOptionPane.showOptionDialog(tabPane,  
		    				"Digite sua senha!",  
		                    "Senha não informada",  
		                    JOptionPane.YES_OPTION,  
		                    JOptionPane.ERROR_MESSAGE,   
		                    null,  
		                    botao,
		                    botao[0]);
		            buttonPKCS11Ok.setText(labelButton);
		    	}        		        		
            }
        });
        // Para capturar o "ENTER" na digitação da senha.
        
        textPKCS11PIN.addKeyListener(new KeyListener() {
        	@Override
			public void keyPressed(KeyEvent e) {
        		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        			buttonPKCS11Ok.setText("Aguarde...");
            		pb11.setVisible(true);
            		pb11.setForeground(Color.GREEN);
            		pb11.setValue(1);
            		tabPane.paintImmediately(0, 0, 440, 181);
    				if (textPKCS11PIN.getText().trim().length() > 0){
        				pb11.setValue(2);
        				tabPane.paintImmediately(0, 0, 440, 181);
            			certificadoHardware = true;
                		if (signButton_actionPerformed()){
                			pb11.setValue(8);
                		}else{
                			pb11.setForeground(Color.RED);
                		}                		
                		buttonPKCS11Ok.setText(labelButton);
                		tabPane.paintImmediately(0, 0, 440, 181);
        			}else{
        				pb11.setValue(0);
        				buttonPKCS11Ok.setText(labelButton);
    		            //JOptionPane.showMessageDialog(tabPane, errorMessage, errorMessage, JOptionPane.ERROR_MESSAGE);
    		            String[] botao = new String[]{"Fechar"};
    		    		JOptionPane.showOptionDialog(tabPane,  
    		    				"Digite sua senha!",  
    		                    "Senha não informada",  
    		                    JOptionPane.YES_OPTION,  
    		                    JOptionPane.ERROR_MESSAGE,   
    		                    null,  
    		                    botao,
    		                    botao[0]);
    		    	}       			
        		}  		       		
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				// sem implementação				
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
				// sem implementação			
			}			
        });


        JPanel panelPKCS11 = new JPanel(true);
        
        panelPKCS11.setName("Cartão / Token");
        panelPKCS11.setLayout(null);
        panelPKCS11.add(labelPKCS11PIN, null);
        panelPKCS11.add(pb11, null);
        panelPKCS11.add(textPKCS11PIN, null);
        panelPKCS11.add(buttonPKCS11Ok, null);
        
        return panelPKCS11;
	}

	/**
	 * Mostra aba de configuracao, onde sera informado o caminho da biblioteca (.so ou .dll)
	 * Para a leitora/token
	 * 
	 * @return JPanel
	 * @see javax.swing.JPanel
	 *
	 */
	private JPanel carregaPanelConfig(){
		
		labelConfigNomeArquivo.setText(LABEL_PKCS11_BIBLIOTECA);
        labelConfigNomeArquivo.setBounds(new Rectangle(10, 35, 400, 15));
        labelConfigNomeArquivo.setFont(new Font("Dialog", 0, 12));

        // Campo com o nome e localização da bibliteca/driver do token ou smartcard
        textConfigNomeArquivo.setBounds(new Rectangle(10, 55, 315, 20));
        textConfigNomeArquivo.setFont(new Font("DialogInput", 0, 12));
    	textConfigNomeArquivo.setText(biblioteca);
        textConfigNomeArquivo.setEditable(false);
        textConfigNomeArquivo.setBackground(new Color(0xcccccc));
        textConfigNomeArquivo.setBorder(BorderFactory.createEtchedBorder(Color.WHITE, Color.GRAY));

        // Botão de pesquisa/localização
        buttonConfigLocalizar.setText("Localizar");
        buttonConfigLocalizar.setBounds(new Rectangle(330, 55, 100, 20));
        buttonConfigLocalizar.setToolTipText("Clique neste botão para localizar o driver do cartão/token");
        buttonConfigLocalizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browseForLibraryButton_actionPerformed();
            }
        });

		JPanel panelConfig = new JPanel(true);
        
        panelConfig.setName("Configuração");
        panelConfig.setLayout(null);

        panelConfig.add(labelConfigNomeArquivo, null);
        panelConfig.add(textConfigNomeArquivo, null);
        panelConfig.add(buttonConfigLocalizar, null);

		return panelConfig;
	}
	
	/**
	 * Mostra aba para uso de certificado armazenado em arquivo PKCS#12
	 * 
	 * @return JPanel
	 * @see javax.swing.JPanel
	 *
	 */
	private JPanel carregaPanelPKCS12(){
		
        labelPKCS12NomeArquivo.setText(LABEL_PKCS12_CERTIFICADO);
		labelPKCS12NomeArquivo.setBounds(new Rectangle(10, 5, 350, 15));
		labelPKCS12NomeArquivo.setFont(new Font("Dialog", 0, 12));
		
		// Barra de progresso
		pb12.setBounds(new Rectangle(10, 105, 100, 20));
		pb12.setValue(0);
		pb12.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        pb12.setBackground(Color.WHITE);
        pb12.setStringPainted(true);
        pb12.setVisible(false);        
                
		// Iniciliza Campo texto com o nome e localização do arquivo do certificado
		textPKCS12NomeArquivo.setBounds(new Rectangle(10, 25, 315, 20));
		textPKCS12NomeArquivo.setFont(new Font("DialogInput", 0, 12));
		textPKCS12NomeArquivo.setEditable(false);
		textPKCS12NomeArquivo.setBackground(new Color(0xcccccc));
		textPKCS12NomeArquivo.setBorder(BorderFactory.createEtchedBorder(Color.WHITE, Color.GRAY));
		
		// Inicializa o botão de busca/localização
		buttonPKCS12Localizar.setText("Localizar");
		buttonPKCS12Localizar.setBounds(new Rectangle(330, 25, 100, 20));
		buttonPKCS12Localizar.setToolTipText("Clique neste botão para localizar o Arquivo do seu Certificado");
		buttonPKCS12Localizar.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        browseForCertButton_actionPerformed();
		    }
		});
		
		labelPKCS12Senha.setText(LABEL_PKCS12_SENHA);
		labelPKCS12Senha.setBounds(new Rectangle(10, 55, 350, 15));
		labelPKCS12Senha.setFont(new Font("Dialog", 0, 12));
		
		// Inicializa o campo de digitação de senha
		textPKCS12Senha.setText("");
		textPKCS12Senha.setBounds(new Rectangle(10, 75, 420, 20));
		textPKCS12Senha.setFont(new Font("DialogInput", 0, 12));
		textPKCS12Senha.setBorder(BorderFactory.createEtchedBorder(Color.WHITE, Color.GRAY));
		textPKCS12Senha.setToolTipText("Clique aqui e digite a senha do seu certificado");

		// Inicializa o botão de ação.
		buttonPKCS12Ok.setText(labelButton);
		buttonPKCS12Ok.setBounds(new Rectangle(170, 105, 95, 25));
		buttonPKCS12Ok.setToolTipText("Clique neste botão, após digitar a sua senha!");
		buttonPKCS12Ok.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	buttonPKCS12Ok.setText("Aguarde...");
	    		pb12.setVisible(true);
	    		pb12.setForeground(Color.GREEN);
	    		pb12.setValue(1);
	    		tabPane.paintImmediately(0, 0, 440, 181);	    		
		    	if (textPKCS12Senha.getText().trim().length() > 0)
		    	{
		    		pb12.setValue(2);
		    		tabPane.paintImmediately(0, 0, 440, 181);
		    		certificadoHardware = false;
		    		if (signButton_actionPerformed()){
            			pb12.setValue(8);
            		}else{
            			pb12.setForeground(Color.RED);
            		}
		    		buttonPKCS12Ok.setText(labelButton);
		    		tabPane.paintImmediately(0, 0, 440, 181);
		    	}else{
		    		pb12.setValue(0);
		    		buttonPKCS12Ok.setText(labelButton);
		            //JOptionPane.showMessageDialog(tabPane, errorMessage, errorMessage, JOptionPane.ERROR_MESSAGE);
		            String[] botao = new String[]{"Fechar"};
		    		JOptionPane.showOptionDialog(tabPane,  
		    				"Digite sua senha!",  
		                    "Senha não informada",  
		                    JOptionPane.YES_OPTION,  
		                    JOptionPane.ERROR_MESSAGE,   
		                    null,  
		                    botao,
		                    botao[0]);
		    	}
		    }
		});
		
        JPanel panelPKCS12 = new JPanel(true);
        
        panelPKCS12.setName("Arquivo");
        panelPKCS12.setLayout(null);

		// Adiciona todo os componentes na aba
		panelPKCS12.add(labelPKCS12NomeArquivo, null);
		panelPKCS12.add(textPKCS12NomeArquivo, null);
		panelPKCS12.add(buttonPKCS12Localizar, null);
		panelPKCS12.add(labelPKCS12Senha, null);
		panelPKCS12.add(textPKCS12Senha, null);
		panelPKCS12.add(pb12, null);
		panelPKCS12.add(buttonPKCS12Ok, null);
		
		return panelPKCS12;
	}

	/**
	 * Executa o navegador de arquivos do sistema operacional, quando botao pesquisar e pressionado.
	 * Permite que o usuario informe o caminho da biblioteca para acesso a leitora de smartcard ou Token
	 */
	private void browseForLibraryButton_actionPerformed() {
		JFileChooser fileChooser = new JFileChooser();
		LibraryFileFilter libraryFileFilter = new LibraryFileFilter();
		fileChooser.addChoosableFileFilter(libraryFileFilter);
		String libraryFileName = textConfigNomeArquivo.getText();
		File directory = new File(libraryFileName).getParentFile();
		fileChooser.setCurrentDirectory(directory);
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String selectedLibFile = fileChooser.getSelectedFile()
					.getAbsolutePath();
			textConfigNomeArquivo.setText(selectedLibFile);
		}
	}

    /**
	 * Executa o navegador de arquivos do sistema operacional, quando botao pesquisar e pressionado.
	 * Permite que o usuario informe o caminho para localizacao do arquivo de certificado no formato PKCS#12
     */
    private void browseForCertButton_actionPerformed() {
        JFileChooser fileChooser = new JFileChooser();
        PFXFileFilter pfxFileFilter = new PFXFileFilter();
        fileChooser.addChoosableFileFilter(pfxFileFilter);
        String certFileName = textPKCS12NomeArquivo.getText();
        File directory = new File(certFileName).getParentFile();
        fileChooser.setCurrentDirectory(directory);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String selectedCertFile = fileChooser.getSelectedFile().getAbsolutePath();
            textPKCS12NomeArquivo.setText(selectedCertFile);
        }
    }
	
	/**
	 * Executado quando o botao de acao e pressionado. Fecha o dialogo aberto e seta o resultado da operacao.
	 * Se nao houver erro seta em true, caso contrario sera false
	 * @throws Throwable 
	 */
	private boolean signButton_actionPerformed(){
		String arquivo;
		String pin;
		boolean resultado;
		
		this.setCursor (new Cursor (Cursor.WAIT_CURSOR));
		this.setEnabled(false);
						
		if(certificadoHardware) {
			arquivo = textConfigNomeArquivo.getText();
			pin     = textPKCS11PIN.getText();
		} else {			
			arquivo = textPKCS12NomeArquivo.getText();
			pin     = textPKCS12Senha.getText();
		}		
		try {
			saveSettings();
		} catch (IOException e) {
			e.printStackTrace();			
			}
		
		pb11.setValue(4);
		pb12.setValue(4);
		tabPane.paintImmediately(0, 0, 440, 181);
		resultado = actionHandler.execute(arquivo, pin, certificadoHardware);
		if (!resultado){
			this.setEnabled(true);
		}
		pb11.setValue(6);
		pb12.setValue(6);
		tabPane.paintImmediately(0, 0, 440, 181);
		this.setCursor (new Cursor (Cursor.DEFAULT_CURSOR));		
		return resultado;
	}

	/**
	 * Busca pelo arquivo de configuração, no ambiente do usuario
	 * @return o nome do arquivo local do usuario com as ultimas configuracoes usadas pelo usuario
	 * e contem o caminho completo da biblioteca que foi informado na aba de configuracao 
	 *  
	 *  the file name with full path to it where the dialog settings are
	 *         stored.
	 */
	private String getConfigFileName() {
		String configFileName = System.getProperty("user.home")
				+ System.getProperty("file.separator") + CONFIG_FILE_NAME;
		return configFileName;
	}

	/**
	 * Carrega as configuracoes ja armazenadas no ambiente local do usuario.
	 * Estara armazendo em um arquivo local.
	 * Loads the dialog settings from the dialog configuration file. These
	 * settings consist of a single value - the last used library file name with
	 * its full path.
	 */
	private void loadSettings() throws IOException {
		String configFileName = getConfigFileName();
		FileInputStream configFileStream = new FileInputStream(configFileName);
		try {
			Properties configProps = new Properties();
			configProps.load(configFileStream);

			String lastLibraryFileName = configProps.getProperty(PKCS11_LIBRARY_FILE_NAME);
			if (lastLibraryFileName != null && lastLibraryFileName.length() > 4)
				textConfigNomeArquivo.setText(lastLibraryFileName);
			else
				textConfigNomeArquivo.setText(biblioteca);
			
	        String lastCertificateFileName = configProps.getProperty(PKCS12_FILE_NAME);
	        if (lastCertificateFileName != null)
	            textPKCS12NomeArquivo.setText(lastCertificateFileName);
	        else
	        	textPKCS12NomeArquivo.setText("");
	        
	        String lastCertificateType = configProps.getProperty(TIPO_CERTIFICADO_DEFAULT, "SOFTWARE");
	        if(lastCertificateType.equals("SOFTWARE")) {
	        	//  Certificado Hardware deve ser false
	        	// somente caso  panelPKCS12 seja null,
	        	// deve ser true.
		        certificadoHardware = panelPKCS12 == null;
	        } else {
	        	//  Certificado Hardware deve ser true
	        	// mas caso o panelPKCS12 seja null,
	        	// deve ser false.
		        certificadoHardware = panelPKCS11 != null;
	        }
	        
	        if(certificadoHardware) {
		        tabPane.setSelectedComponent(panelPKCS11);
	        } else {
		        tabPane.setSelectedComponent(panelPKCS12);
	        }
		} finally {
			configFileStream.close();
		}
	}

	/**
	 * Salva as ultimas configuracoes utilizadas pelo usuario em um arquivo texto no ambiente
	 * local (desktop) do usuario.
	 * 
	 * Saves the dialog settings to the dialog configuration file. These
	 * settings consist of a single value - the last used library file name with
	 * its full path.
	 */
	private void saveSettings() throws IOException {
		// Cria uma lista de preferências para serem gravadas em arquivo.
		Properties configProps = new Properties();
		
		String currentLibraryFileName = textConfigNomeArquivo.getText();
		configProps.setProperty(PKCS11_LIBRARY_FILE_NAME, currentLibraryFileName);
		
        String currentCertificateFileName = textPKCS12NomeArquivo.getText();
        configProps.setProperty(PKCS12_FILE_NAME, currentCertificateFileName);
        
        if(certificadoHardware) {
            configProps.setProperty(TIPO_CERTIFICADO_DEFAULT, "HARDWARE");
        } else {
            configProps.setProperty(TIPO_CERTIFICADO_DEFAULT, "SOFTWARE");
        }

		// Salva as preferências no arquivo de configuração
		String configFileName = getConfigFileName();
		FileOutputStream configFileStream = new FileOutputStream(configFileName);
		try {
			configProps.store(configFileStream, "");
		} finally {
			configFileStream.close();
		}
	}

	/**
	 * Busca o nome do arquivo de biblioteca
	 * @return o arquivo de biblioteca informado pelo usuario 
	 */
	public String getLibraryFileName() {
		String libraryFileName = textConfigNomeArquivo.getText();
		return libraryFileName;
	}

	/**
	 * Recuperar o PIN digitado para Smartcard
	 * @return O PIN informado pelo usuario 
	 */
	public String getSmartCardPINCode() {
		String pinCode = textPKCS11PIN.getText();
		return pinCode;
	}

	/**
	 * Nome do arquivo de certificado
	 * @return Nome do arquivo de certificado no formato PKCS12
	 */
	public String getCertificateFileName(){
		String certificateFileName = textPKCS12NomeArquivo.getText();
		return certificateFileName;
	}

	/**
	 * Pega a senha digitada para arquivo.
	 * @return Senha do Certificado.
	 */
	public String getCertificatePassword(){
		String certificatePassword = textPKCS12Senha.getText();
		return certificatePassword;
	}
	
	/**
	 * Verifica se eh um certificado armazenado em hardaware
	 * @return true se o certificado e armazenado em Hardware (smartcard/token)
	 */
	public boolean isCertificateHardware(){
		return certificadoHardware;
	}

	/**
	 * Filtro para a classe, onde estarao os formatos aceitos para as bibliotecas
	 * Aqui somente .dll e .so
	 */
	private static class LibraryFileFilter extends FileFilter {
		/**
		 * 
		 */
		public boolean accept(File aFile) {
			if (aFile.isDirectory()) {
				return true;
			}

			String fileName = aFile.getName().toLowerCase();
			boolean accepted = (fileName.endsWith(".dll") || fileName.endsWith(".so"));
			return accepted;
		}

		/**
		 *retorna descricao
		 */
		public String getDescription() {
			return "PKCS#11 Biblioteca v2.0 ou superior (.dll, .so)";
		}
	}

    /**
     * Filtro para a classe, onde estarao os formato aceitos para o arquivos de certificados no formato
     * PKCS#12
     * Aqui somente .PFX e .P12
     */
    private static class PFXFileFilter extends FileFilter {
        public boolean accept(File aFile) {
            if (aFile.isDirectory()) {
                return true;
            }

            String fileName = aFile.getName().toUpperCase();
            boolean accepted = (fileName.endsWith(".PFX") || fileName.endsWith(".P12"));
            return accepted;
        }

        public String getDescription() {
            return "PKCS#12 Arquivo com o certificado e chave privada (.PFX, .P12)";
        }
    }
	
}
