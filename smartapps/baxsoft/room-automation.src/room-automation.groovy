definition(
		name: "Room Automation",
		namespace: "BAXsoft",
		author: "David Burrell",
		description: "Automates a rooms",
		category: "My Apps",

		// the parent option allows you to specify the parent app in the form <namespace>/<app name>
		parent: "BAXsoft:EccobeeWithVents",
		iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
		iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
		iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	page(name: "page1", title:"Room", install: true, uninstall: true)
	
}

def page1() {
	def ecobeePrograms=[]
	// try to get the thermostat programs list (ecobee)
	try {
		ecobeePrograms = parent?.currentThermostat()?.currentClimateList.toString().minus('[').minus(']').tokenize(',')
		ecobeePrograms.sort()
	} catch (e) {
		traceEvent(settings.logFilter,"Not able to get the list of climates (ecobee), exception $e",settings.detailedNotif, get_LOG_ERROR())
	}
	
	dynamicPage(name: "page1") {
		section("Room name") {
			label title: "Enter room name:", defaultValue: app.label, required: true
		}
		section ("ecobee thermostat: ${parent.currentThermostat()}"){
			input "vents", "capability.switch", title: "Which vents are in this room?", multiple: true, submitOnChange: true
			input (name:"unconditionalClimates", type:"enum", title: "Which ecobee climate/program(unconditional)? ", options: ecobeePrograms.findAll{ !settings.conditionalClimates?.contains(it) }, multiple: true, defaultValue: settings.unconditionalClimates, required: false, submitOnChange: true)
			input (name:"conditionalClimates", type:"enum", title: "Which ecobee climate/program(conditional)? ", options: ecobeePrograms.findAll{ !settings.unconditionalClimates?.contains(it) }, multiple: true, defaultValue: settings.conditionalClimates, required: false, submitOnChange: true)
		}
	}
}


def installed() {
	log.debug "Installed with settings: ${settings}"
	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	initialize()
}

def initialize() {
	// if the user did not override the label, set the label to the default
	/*if (!overrideLabel) {
	 app.updateLabel(defaultLabel())
	 }*/

}

def updateClimateMode(name){
	log.debug "Updating Climate Mode -> $name"
	
	
}

def updateOperatingMode(name){
	log.debug "Updating operating mode -> $name"
	
}



