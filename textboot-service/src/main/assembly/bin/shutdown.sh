#!/bin/bash
cd `dirname $0`

#当前路径
BIN_DIR=`pwd`

#向上一层路径
cd ..
DEPLOY_DIR=`pwd`
echo $DEPLOY_DIR

#依赖jar包目录
LIB_DIR=$DEPLOY_DIR/lib
#配置文件路径
CONF_DIR=$DEPLOY_DIR/conf
#日志输出路径
LOGS_DIR=$DEPLOY_DIR/logs

#如果logs目录不存在，就创建一个
if [ ! -d $LOGS_DIR ]; then
    mkdir $LOGS_DIR
fi
echo "LOGS_DIR :$LOGS_DIR"

#控制台日志输出收集位置
STDOUT_FILE=$LOGS_DIR/stdout.log

# 如果JDK环境变量没有写到全局要添加如下几行
#JAVA_HOME=/usr/program/jdk1.8.0_181
#PATH=$JAVA_HOME/bin:$PATH
#export JAVA_HOME
#export PATH

#从dubbo.properties取得应用名、端口号，端口名
SERVER_NAME=`sed '/dubbo.application.name/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`
SERVER_PROTOCOL_NAME=`sed '/dubbo.protocol.name/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`
SERVER_PROTOCOL_PORT=`sed '/dubbo.protocol.port/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`

#应用名为空的话就取当前系统名
if [ -z "$SERVER_NAME" ]; then
    echo "SERVER_NAME is empty"
    SERVER_NAME=`hostname`
fi

#根据配置文件路径去查找当前是否已有dubbo应用启动起来
APP_PID=`ps -ef -ww | grep "java" | grep " -DappName=$SERVER_NAME " | awk '{print $2}'`
echo "SERVER_NAME: $SERVER_NAME"
echo "SERVER_PROTOCOL_NAME: $SERVER_PROTOCOL_NAME"
echo "SERVER_PROTOCOL_PORT: $SERVER_PROTOCOL_PORT"
echo "APP_PID: $APP_PID"

#APP_PID不为空，说明应用已启动，直接退出
if [ -z "$APP_PID" ]; then
    echo "ERROR: The $SERVER_NAME does not started!"
    exit 1
fi

#DUMP
#if ["$1" = "dump"]; then
#    $BIN_DIR/dump.sh
#fi

echo -e "Stopping the $SERVER_NAME ...\c"
kill $APP_PID 1>/dev/null 2>/dev/null

COUNT=1
while [ $COUNT -gt 0 ]; do
    echo -e ".\c"
    sleep 1
    if [ -n "$SERVER_PROTOCOL_PORT" ]; then
        COUNT=`netstat -tln | grep $SERVER_PROTOCOL_PORT | wc -l`
    else
        COUNT=`ps -f | grep java | grep "$DEPLOY_DIR" | awk '{print $2}' | wc -l`
    fi
    if [ $COUNT -lt 1 ]; then
        break
    fi
done
echo "OK!"
echo "STOP SUCCESSED APP_PID: $APP_PID"