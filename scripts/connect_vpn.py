from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
import time
import random
import sys

package = 'com.jrzheng.supervpnfree'
activity = 'com.jrzheng.supervpn.view.MainActivity'
runComponent = package + '/' + activity

print "Wait For connection..."
device = MonkeyRunner.waitForConnection(300, sys.argv[1])

print "Connected."
device.startActivity(component=runComponent)

print "start activity."
MonkeyRunner.sleep(10)

print "Click connect."
device.touch(160, 160, 'DOWN_AND_UP')

MonkeyRunner.sleep(10)

print "Click Trust"
device.touch(50, 345, 'DOWN_AND_UP')

MonkeyRunner.sleep(10)

print "Click OK"
device.touch(230, 400, 'DOWN_AND_UP')

MonkeyRunner.sleep(30)
print "Monkey success."
