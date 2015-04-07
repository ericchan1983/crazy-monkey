#!/bin/bash

# clean the evn
source ./setenv.sh
cd $CRAZY_MONKEY_HOME && /bin/bash ./reset_vpn.sh

# Git update and build
echo "[Crazy Monkey] Update the crazy monkey code..."
cd $CRAZY_MONKEY_HOME && git pull

echo "[Crazy Monkey] Build the project..."
cd $CRAZY_MONKEY_HOME && $ANT_HOME/bin/ant

# Clean the env
cd $CRAZY_MONKEY_HOME && /bin/bash ./stop.sh

# Run the vpn client
cd $VPN_CLINET_HOME && ./autorun

# Run the testing
$JAVA_HOME/bin/java -jar $CRAZY_MONKEY_HOME/crazy-monkey-0.1.jar

