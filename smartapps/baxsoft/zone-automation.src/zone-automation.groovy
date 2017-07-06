definition(
		name: "Zone Automation",
		namespace: "BAXsoft",
		author: "David Burrell",
		description: "Automates a virtual zone",
		category: "My Apps",

		// the parent option allows you to specify the parent app in the form <namespace>/<app name>
		parent: "BAXsoft:EccobeeWithVents",
		iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
		iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
		iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	page(name: "page1", title:"Zone", nextPage:"namePage", uninstall: true)
	page(name: "namePage", title:"Zone Name", install: true)
}

def page1() {
	dynamicPage(name: "page1") {
		section ("ecobee thermostat: ${parent.currentThermostat()}"){
			input "numberOfRooms", "number", title: "Number of rooms:", range: "1..20"
		}
	}
}

// page for allowing the user to give the automation a custom name
def namePage() {
	dynamicPage(name: "namePage") {
		section("Zone name") {
			label title: "Enter zone name:", defaultValue: app.label, required: true
		}
	}
}

// a method that will set the default label of the automation.
// It uses the lights selected and action to create the automation label
//def defaultLabel() {
//	def lightsLabel = settings.lights.size() == 1 ? lights[0].displayName : lights[0].displayName + ", etc..."
//
//			if (action == "color") {
//				"Turn on and set color of $lightsLabel"
//			} else if (action == "level") {
//				"Turn on and set level of $lightsLabel"
//			} else {
//				"Turn $action $lightsLabel"
//			}
//}

def installed() {
	log.debug "Installed with settings: ${settings}"
	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unschedule()
	initialize()
}

def initialize() {
	// if the user did not override the label, set the label to the default
	/*if (!overrideLabel) {
	 app.updateLabel(defaultLabel())
	 }*/

}






// main page to select lights, the action, and turn on/off times
//def mainPage() {
//	dynamicPage(name: "mainPage") {
//		section {
//			lightInputs()
//			actionInputs()
//		}
//		timeInputs()
//	}
//}


// inputs to select the lights
//def lightInputs() {
//	input "lights", "capability.switch", title: "Which lights do you want to control?", multiple: true, submitOnChange: true
//}
//
//// inputs to control what to do with the lights (turn on, turn on and set color, turn on
//// and set level)
//def actionInputs() {
//	if (lights) {
//		input "action", "enum", title: "What do you want to do?", options: actionOptions(), required: true, submitOnChange: true
//		if (action == "color") {
//			input "color", "enum", title: "Color", required: true, multiple:false, options: [
//				["Soft White":"Soft White - Default"],
//				["White":"White - Concentrate"],
//				["Daylight":"Daylight - Energize"],
//				["Warm White":"Warm White - Relax"],
//				"Red","Green","Blue","Yellow","Orange","Purple","Pink"]
//
//		}
//		if (action == "level" || action == "color") {
//			input "level", "enum", title: "Dimmer Level", options: [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]], defaultValue: "80"
//		}
//	}
//}
//
//// utility method to get a map of available actions for the selected switches
//def actionMap() {
//	def map = [on: "Turn On", off: "Turn Off"]
//	if (lights.find{it.hasCommand('setLevel')} != null) {
//		map.level = "Turn On & Set Level"
//	}
//	if (lights.find{it.hasCommand('setColor')} != null) {
//		map.color = "Turn On & Set Color"
//	}
//	map
//}
//
//// utility method to collect the action map entries into maps for the input
//def actionOptions() {
//	actionMap().collect{[(it.key): it.value]}
//}
//
//// inputs for selecting on and off time
//def timeInputs() {
//	if (settings.action) {
//		section {
//			input "turnOnTime", "time", title: "Time to turn lights on", required: true
//			input "turnOffTime", "time", title: "Time to turn lights off", required: true
//		}
//	}
//}


