/**
 *  ecobeeSetZonesWithSchedule
 *
 *  Copyright 2015 Yves Racine
 *  LinkedIn profile: ca.linkedin.com/pub/yves-racine-m-sc-a/0/406/4b/
 *
 *  Developer retains all right, title, copyright, and interest, including all copyright, patent rights, trade secret 
 *  in the Background technology. May be subject to consulting fees under the Agreement between the Developer and the Customer. 
 *  Developer grants a non exclusive perpetual license to use the Background technology in the Software developed for and delivered 
 *  to Customer under this Agreement. However, the Customer shall make no commercial use of the Background technology without
 *  Developer's written consent.
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *
 *  Software Distribution is restricted and shall be done only with Developer's written approval.
 */
definition(
	name: "${getAppName()}",
	namespace: "BAXsoft",
	author: "David Burrell",
	description: "Automates the ecobee along with keen home vents",
	category: "My Apps",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Partner/ecobee.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Partner/ecobee@2x.png"
)

private String getAppName() {
	return  "EccobeeWithVents"
}

private String getAppVersion(){
	return "0.1"
}

preferences {

	page(name: "pageOne", title: "Setup", nextPage: "page1", hideWhenEmpty: true, uninstall: true){
		section("ecobee thermostat:"){
			input "thermostat", "capability.thermostat", title: "Select ecobee thermostat:"	
			app(name: "zoneApps", appName: "Zone Automation", namespace: "BAXsoft", title: "New Zone", multiple: true)
		}
	}
	
}

def currentThermostat(){
	return thermostat
}
	