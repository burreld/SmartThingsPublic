package test
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
	page(name: "page2", title:"Zone", install: true, uninstall: true)
    
}

def page1() {
	dynamicPage(name: "page2") {
		section ("ecobee thermostat: ${parent.currentThermostat}"){
			input "numberOfRooms", "number", title: "Number of rooms:", range: "1..20"	
			input "rooms", "number", title: "Test", multiple: true		
		}
		

	}
}