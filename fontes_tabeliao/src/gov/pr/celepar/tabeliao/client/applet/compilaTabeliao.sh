#/bin/bash

# Caso necessário defina as variáveis:
# JAVA_HOME=/usr/lib/jvm/java-6-sun-1.6.0.03
# SRC_HOME=/home/usuario/workspace/gtf-tabeliao/context/WEB-INF/src
# BC_SRC=/home/usuario/workspace/gtf-tabeliao/context/WEB-INF/bc_src
# LIBS_HOME=/home/usuario/workspace/gtf-tabeliao/context/WEB-INF/lib
# LIBS_JBOSS=/usr/lib/jboss4/client/jbossall-client.jar

SRC_HOME=/home/esaito/work_novo/gtf-tabeliao/context/WEB-INF/src
BC_SRC=/home/esaito/work_novo/gtf-tabeliao/context/WEB-INF/bc_src
#LIBS_HOME=/home/esaito/work_novo/gtf-tabeliao/context/WEB-INF/lib
#:$LIBS_HOME/log4j-1.2.11.jar

echo "***********************************************"

#Compila as classes
echo "*** Compilando as classes..."
rm -rf classes
mkdir classes
echo $LIBS_HOME

javac -sourcepath "$BC_SRC":"$SRC_HOME" -classpath .:"$JAVA_HOME"/jre/lib/plugin.jar TabeliaoApplet.java -d classes
cp *.png classes/gov/pr/celepar/tabeliao/client/applet/.

#Gera o arquivo .jar
echo "*** Gerando o arquivo .jar ..."
rm TabeliaoApplet.jar
rm -rf classes/org
jar -cvfm TabeliaoApplet.jar MANIFEST.MF -C classes/ .

#Assina o arquivo .jar
echo "*** Assinando o arquivo .jar ..."
# keystore de desenvolvimento = localhost
# Gerar com:
# nsCertType = server, client, email, objsign
# keyUsage = critical, digitalSignature, keyEncipherment, dataEncipherment

# jarsigner -keystore localhost.jks -storepass "celepar" -keypass "celepar" TabeliaoApplet.jar #localhost
# Keystore de produção
# nsCertType = server, client, email, objsign
# keyUsage = critical, digitalSignature, keyEncipherment, dataEncipherment
jarsigner -keystore wwwtabeliao.jks -storepass "pinhaoprod" -keypass "pinhaoprod" TabeliaoApplet.jar www.tabeliao.eparana.parana

rm -rf classes

echo "*** Fim da compilacao!"
