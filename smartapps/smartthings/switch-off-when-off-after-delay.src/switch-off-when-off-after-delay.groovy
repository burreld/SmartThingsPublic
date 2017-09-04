/**
 *  Copyright 2015 SmartThings
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Lights Off, When Closed
 *
 *  Author: SmartThings
 */
definition(
		name: "Switch Off, When Off after delay",
		namespace: "smartthings",
		author: "SmartThings",
		description: "Turn your switch off when an switch is turned off.",
		category: "Convenience",
		iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/light_contact-outlet.png",
		iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/light_contact-outlet@2x.png"
		)

preferences {
	section ("When the switch turns off...") {
		input "triggerSwitch", "capability.switch", title: "What Switch?"
		input "minutes", "number", title: "Minutes until turn off?"
	}
	section ("Turn off a switch...") { input "switch1", "capability.switch" }
}

def installed() {
	subscribe(triggerSwitch, "switch.off", contactClosedHandler)
}

def updated() {
	unsubscribe()
	subscribe(triggerSwitch, "switch.off", contactClosedHandler)
	subscribe(switch1, "switch.on", fanOnHandler)
}

def fanOnHandler(evt){
	if(triggerSwitch.currentValue("switch") != "on"){
		scheduleFanTurnOff()
	}
}

def contactClosedHandler(evt) {	
	scheduleFanTurnOff()	
}

def scheduleFanTurnOff(){
	runIn(minutes * 60, turnOffHandler)
}

def turnOffHandler(){
	if(triggerSwitch.currentValue("switch") != "on"){
		switch1.off()
	}
}