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
		log.debug "Not able to get the list of climates (ecobee), exception $e"
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
	unsubscribe()
	initialize()
}

def initialize() {
	// if the user did not override the label, set the label to the default
	/*if (!overrideLabel) {
	 app.updateLabel(defaultLabel())
	 }*/

	subscribe(parent?.currentThermostat(), "climateName", setClimateHandler)
	subscribe(parent?.currentThermostat(), "thermostatOperatingState", thermostatOperatingHandler)

	setupInitialVentState()

}

private def setupInitialVentState(){
	def initialClimateName = parent?.currentThermostat().currentClimateName

	log.debug "Initial Climate Mode -> $initialClimateName"

	if(initialClimateName){
		updateClimateMode(initialClimateName)
	}
}

private def updateClimateMode(name){
	log.debug "Updating Climate Mode -> $name"

	def ventRoutine

	/* unconditional or is conditional meeting the conditions*/	
	if(isAnUnconditionalClimate(name) || isAMetConditionClimate(name)){
		// open vents
		log.debug "Open Vents in room"
		ventRoutine = { vent -> openVent(vent)}
	}
	else {
		log.debug "close Vents in room"
		ventRoutine = { vent -> closeVent(vent)}
	}

	settings?.vents.each { ventRoutine(it) }
}

def roomVents(){
	return settings?.vents
}

private def isAnUnconditionalClimate(name){
	def isA = false

	isA = settings.unconditionalClimates?.contains(name)

	if(isA) {
		log.debug "Is an unconditional climate"
	}

	return isA
}

private def isAMetConditionClimate(name){
	def met = false

	if(isAnUnconditionalClimate(name)){
		// evaluate conditions

		met = true

		log.debug "Is a met conditional climate"
	}

	return met
}

private def updateOperatingMode(name){
	log.debug "Updating operating mode -> $name"

	def ventRoutine

	// open all vents while fan only
	if(name == "fan only"){
		// open vents
		log.debug "Open Vents in room fan is on"

		settings?.vents.each { openVent(it) }
	}
	else if(name == "cooling" || name == "heating"){
		// open vents
		log.debug "system changed to ${name} so setup vents"

		setupInitialVentState()
	}
}

def setClimateHandler(evt){
	log.debug "climate change: ${evt.value}"

	log.debug "notify child of climate change -> $label: $evt.value"
	updateClimateMode(evt.value)

}

def thermostatOperatingHandler(evt){
	log.debug "thermostat operating mode change: ${evt.value}"


	log.debug "notify child of operating mode change -> $label: $evt.value"
	updateOperatingMode(evt.value)

}

def openVent(vent){
	log.debug "open vent: ${vent.id}"

	try{
		if(vent.currentValue("switch") == "off") {
			log.debug "open vent: ${vent.id} was off setting to level 100 and on"

			vent.setLevel(100)
			vent.on()
		}
		else {
			if(vent.currentValue("level") != 100){
				log.debug "open vent: ${vent.id} was on but level was not 100"

				vent.setLevel(100)
				vent.on()
			}
		}
	}
	catch(ex){
		log.error("openVent->${vent.id}", ex)

		ventSwitch.clearObstruction()

		vent.setLevel(100)
		vent.on()
	}
}

def closeVent(vent){
	try{
		if(vent.currentValue("switch") != "off"){
			vent.off()
		}
	}
	catch(ex){
		vent.clearObstruction()
		vent.off()
	}
}