#!/bin/bash
# 
# Start the OSGI framework runtime (Felix Main)
# 
# config: -Dfelix.config.properties=file:/path/to/config.properties (default ./conf/bundle)
# bundle: -b /path/to/dir (default ./bundle)
# 
# @author Tim Lauv

cd ./runtime
#java -jar ./bin/felix.jar
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar ./bin/felix.jar