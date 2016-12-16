#/bin/bash

# modifica Manifest.MF dos arquivos .jar
echo "*** modificando os arquivos .jar ..."

# jar ufm jar-file manifest-addition.txt

jar ufm signature-signer-2.0.0-BETA2.jar manifest-addition-signer.txt
jar ufm ca-icpbrasil-homologacao-2.0.0-BETA2.jar manifest-addition-signer.txt
jar ufm ca-icpbrasil-2.0.0-BETA2.jar manifest-addition-signer.txt
jar ufm signature-timestamp-2.0.0-BETA2.jar manifest-addition-signer.txt
jar ufm signature-criptography-2.0.0-BETA2.jar manifest-addition-signer.txt
jar ufm signature-applet-2.0.0-BETA2.jar manifest-addition-signer.txt
jar ufm signature-policy-engine-2.0.0-BETA2.jar manifest-addition-signer.txt
jar ufm signature-core-2.0.0-BETA2.jar manifest-addition-signer.txt

read -s -n 1 -p "Press any key to continue…"

echo "*** Fim!"





