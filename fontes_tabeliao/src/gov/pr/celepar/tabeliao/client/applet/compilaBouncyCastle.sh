#/bin/bash

echo "***********************************************"

# Caso necessário defina as variáveis:
# JAVA_HOME=/usr/lib/jvm/java-6-sun-1.6.0.03
# SRC_HOME=/home/usuario/workspace/gtf-tabeliao/context/WEB-INF/src
# BC_SRC=/home/usuario/workspace/gtf-tabeliao/context/WEB-INF/bc_src
# LIBS_HOME=/home/usuario/workspace/gtf-tabeliao/context/WEB-INF/lib
SRC_HOME=/home/esaito/work_tab_cli/gtf-tabeliao/context/WEB-INF/src
BC_SRC=/home/esaito/work_tab_cli/gtf-tabeliao/context/WEB-INF/bc_src


#Compila as classes
echo "*** Compilando as classes..."
rm -rf classes
mkdir classes

# opções -Xlint:deprecation -Xlint:unchecked
javac -sourcepath "$BC_SRC":"$SRC_HOME" -classpath .:"$JAVA_HOME"/jre/lib/plugin.jar:"$JAVA_HOME"/jre/lib/rt.jar TabeliaoApplet.java -d classes

#Gera o arquivo .jar
echo "*** Gerando o arquivo .jar ..."
rm bouncycastleLite.jar
rm -rf classes/gov
jar -cvf bouncycastleLite.jar -C classes/ .


#Assina o arquivo .jar
echo "*** Assinando o arquivo .jar ..."
# keystore de desenvolvimento = localhost
# Gerar com:
# nsCertType = server, client, email, objsign
# keyUsage = critical, digitalSignature, keyEncipherment, dataEncipherment

jarsigner -keystore wwwtabeliao.jks -storepass "pinhaoprod" -keypass "pinhaoprod" bouncycastleLite.jar www.tabeliao.eparana.parana

#jarsigner -keystore tabeliao_icp.jks -storepass "pinhaoprod" -keypass "pinhaoprod" bouncycastleLite.jar www.tabeliao.eparana.parana

rm -rf classes

echo "*** Fim da compilacao!"
