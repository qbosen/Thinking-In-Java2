#!/usr/bin/env bash

# 测试 apt 例子
# apt 在1.8中被删除了，所以必须是 1.7, 然后导入 tools.jar

# 项目根目录
PROJECT_ROOT=/Users/qiubaisen/Documents/gitproject/thinking-In-Java
# 包目录路径
PACKAGE_PATH=annotations
# JDK 目录
JAVA_7_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_80.jdk/Contents/Home
# 源文件所在目录路径
SRC_ROOT=${PROJECT_ROOT}/src/main/java
# 运行根路径
RUN_ROOT=${PROJECT_ROOT}/target
# 设置Classpath
TEMP_CLASSPATH="${JAVA_7_HOME};${JAVA_7_HOME}/lib/tools.jar;${RUN_ROOT}/annotations"

apt=${JAVA_7_HOME}/bin/apt


echo ${apt} -classpath ${TEMP_CLASSPATH} -nocompile -factory annotations.InterfaceExtractorProcessorFactory  Multiplier.java -s ../annotations

cd ${SRC_ROOT}/${PACKAGE_PATH}
${apt} -classpath ${TEMP_CLASSPATH} -nocompile -factory annotations.InterfaceExtractorProcessorFactory  Multiplier.java -s ../annotations

