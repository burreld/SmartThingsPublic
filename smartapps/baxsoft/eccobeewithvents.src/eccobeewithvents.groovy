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
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Partner/ecobee@2x.png",
	singleInstance: true
)

private String getAppName() {
	return  "EccobeeWithVents"
}

private String getAppVersion(){
	return "0.1"
}


preferences {

	page(name: "pageOne", title: "Setup", install: true, uninstall: true)	
}

def pageOne(){
	dynamicPage(name: "pageOne", title: "Setup", install: true, uninstall: true){
		section("ecobee thermostat:"){
			input "thermostat", "capability.thermostat", title: "Select ecobee thermostat:", submitOnChange: true
			
			if(thermostat){
				app(name: "zoneApps", appName: "Zone Automation", namespace: "BAXsoft", title: "New Zone", multiple: true, hideable: true, hidden: true)
				
			}
		}
		
		if(thermostat){
			def atts = ["thermostatId",
				"thermostatName",
				"temperatureDisplay",
				"coolingSetpointDisplay",
				"heatingSetpointDisplay",
				"heatLevelUp",
				"heatLevelDown",
				"coolLevelUp",
				"coolLevelDown",
				"verboseTrace",
				"fanMinOnTime",
				"humidifierMode",
				"dehumidifierMode",
				"humidifierLevel",
				"dehumidifierLevel",
				"condensationAvoid",
				"groups",
				"equipmentStatus",
				"alerts",
				"alertText",
				"programScheduleName",
				"programFanMode",
				"programType",
				"programCoolTemp",
				"programHeatTemp",
				"programCoolTempDisplay",
				"programHeatTempDisplay",
				"programEndTimeMsg",
				"weatherDateTime",
				"weatherSymbol",
				"weatherStation",
				"weatherCondition",
				"weatherTemperatureDisplay",
				"weatherPressure",
				"weatherRelativeHumidity",
				"weatherWindSpeed",
				"weatherWindDirection",
				"weatherPop",
				"weatherTempHigh",
				"weatherTempLow",
				"weatherTempHighDisplay",
				"weatherTempLowDisplay",
				"plugName",
				"plugState",
				"plugSettings",
				"hasHumidifier",
				"hasDehumidifier",
				"hasErv",
				"hasHrv",
				"ventilatorMinOnTime",
				"ventilatorMode",
				"programNameForUI",
				"thermostatOperatingState",
				"climateList",
				"modelNumber",
				"followMeComfort",
				"autoAway",
				"intervalRevision",
				"runtimeRevision",
				"thermostatRevision",
				"heatStages",
				"coolStages",
				"climateName",
				"setClimate",
				"auxMaxOutdoorTemp",
				"stage1HeatingDifferentialTemp",
				"stage1CoolingDifferentialTemp",
				"stage1HeatingDissipationTime",
				"stage1CoolingDissipationTime",
			   
			   // Report Runtime events
			   
				"auxHeat1RuntimeInPeriod",
				"auxHeat2RuntimeInPeriod",
				"auxHeat3RuntimeInPeriod",
				"compCool1RuntimeInPeriod",
				"compCool2RuntimeInPeriod",
				"dehumidifierRuntimeInPeriod",
				"humidifierRuntimeInPeriod",
				"ventilatorRuntimeInPeriod",
				"fanRuntimeInPeriod",
	   
				"auxHeat1RuntimeDaily",
				"auxHeat2RuntimeDaily",
				"auxHeat3RuntimeDaily",
				"compCool1RuntimeDaily",
				"compCool2RuntimeDaily",
				"dehumidifierRuntimeDaily",
				"humidifierRuntimeDaily",
				"ventilatorRuntimeDaily",
				"fanRuntimeDaily",
				"reportData",
	   
				"auxHeat1RuntimeYesterday",
				"auxHeat2RuntimeYesterday",
				"auxHeat3RuntimeYesterday",
				"compCool1RuntimeYesterday",
				"compCool2RuntimeYesterday",
	   
				"auxHeat1RuntimeAvgWeekly",
				"auxHeat2RuntimeAvgWeekly",
				"auxHeat3RuntimeAvgWeekly",
				"compCool1RuntimeAvgWeekly",
				"compCool2RuntimeAvgWeekly",
	   
				"auxHeat1RuntimeAvgMonthly",
				"auxHeat2RuntimeAvgMonthly",
				"auxHeat3RuntimeAvgMonthly",
				"compCool1RuntimeAvgMonthly",
				"compCool2RuntimeAvgMonthly",
	   
	   
			   // Report Sensor Data & Stats
					   
				"reportSensorMetadata",
				"reportSensorData",
				"reportSensorAvgInPeriod",
				"reportSensorMinInPeriod",
				"reportSensorMaxInPeriod",
				"reportSensorTotalInPeriod",
			   
			   // Remote Sensor Data & Stats
	   
				"remoteSensorData",
				"remoteSensorTmpData",
				"remoteSensorHumData",
				"remoteSensorOccData",
				"remoteSensorAvgTemp",
				"remoteSensorAvgHumidity",
				"remoteSensorMinTemp",
				"remoteSensorMinHumidity",
				"remoteSensorMaxTemp",
				"remoteSensorMaxHumidity",
	   
	   
			   // Recommendations
			   
				"tip1Text",
				"tip1Level",
				"tip1Solution",
				"tip2Text",
				"tip2Level",
				"tip2Solution",
				"tip3Text",
				"tip3Level",
				"tip3Solution",
				"tip4Text",
				"tip4Level",
				"tip4Solution",
				"tip5Text",
				"tip5Level",
				"tip5Solution"
			   ]
	   
			
			def strP = "Attribute - Value\n"
				section("paragraph") {
					paragraph "Attribute - Value"
					paragraph "thermostatOperatingState - ${thermostat?.thermostatOperatingState}"
					paragraph "currentThermostatOperatingState - ${thermostat?.currentThermostatOperatingState}"
					for(attFound in atts){
						paragraph  "${attFound} - ${thermostat.currentValue(attFound)}"
					}
					
					
				}
			
		}
	}
}

def currentThermostat(){
	return thermostat
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
	// nothing needed here, since the child apps will handle preferences/subscriptions
	// this just logs some messages for demo/information purposes
	log.debug "there are ${childApps.size()} child smartapps"
	childApps.each {child ->
		log.debug "child app: ${child.label}"
	}
}
	