#!/bin/bash

source ./setenv.sh

echo "-------------------- Reset the VPN --------------------"

echo "[VPN Client] Kill vpn-daemon"
pgrep vpn-daemon | xargs -rt kill -9

echo "[VPN Client] Kill the main"
ps aux | grep main | grep -v grep | awk '{print $2}' | xargs -rt kill -9

echo "[VPN Client] Off the vpn"
sudo poff vpnpptp | sed 's/^/[VPN Client] &/g'

echo "[VPN Client] Kill the pptp"
ps aux | grep pptp | grep -v grep | awk '{print $2}' | xargs -rt sudo kill -9

default_route=$(ip route list | grep default | awk {'print $1'}) 
if [ ! -n "$default_route" ]; then
    echo "[VPN Client] Add the default route"
    sudo ip route add default via $DEFAULT_GATEWAY dev $NETWORK_INTERFACE proto static | sed 's/^/[VPN Client] &/g'
else 
    echo "[VPN Client] Change the default route"
    sudo ip route change default via $DEFAULT_GATEWAY dev $NETWORK_INTERFACE proto static | sed 's/^/[VPN Client] &/g'
fi

echo "-------------------- Done --------------------"