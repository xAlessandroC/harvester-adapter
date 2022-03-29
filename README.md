# Harvester Adapter

The harvester adapter enables the registration of the Harvester tool functionalities in the Arrowhead Framework. This is done by mean of a Java Spring applicatiton which exposes a REST API.

## Content
* [Requirements](#requirements)
* [Harvester Environment](#harvester-environment)
* [Application setup](#application-set-up)
* [Build and start](#build-and-start-the-adapter)
* [API](#apis)
    * [Submit Simulation](#submit-simulation)
    * [Get Simulation Result](#get-simulation-result)

---

## Requirements
* Java 11
* Maven 3.8.4
* Matlab 2020a
* LTSpice 17.0.33 (Windows version for both deployment)
* Harvester Environment
* Wine (only for Linux deployment to run the LTSpice.exe)

The tool has been tested for Windows 10 and Debian.

## Harvester Environment
The adapter needs a proper configured harvester environment in order to work correctly. All the files needed can be download from [here](https://drive.google.com/file/d/1wmrZuRre_0miivuB7s8DrcTT8ciiM4GJ/view?usp=sharing).

Download the whole folder and put it under the *src/main/resources* folder

The next step is to customize the environment according to you deployment (Linux or Windows). Here all the changes to make:
#### File AHT_DrHarvesterRunTime_v12.m
1.  **Line 32** - Change the path (windows or linux) with your LTSpice one
2.  **Line 35/36** - Change the paths(windows or linux) with you LTSpice libraries ones
3.  **Line 40** - Change the(windows or linux) path with your working environment one (the folder you have just downloaded)
4.  **Line 564/567** - Comment or uncomment accoring to your deployment
5. (OPTIONAL) **Line 17/18** - Change if you want to rename input/output file.

#### File DrHarvester.bat (only for **Windows deployment**)
1.  Change the path of matlab file according to you installation

#### File DrHarvester.sh (only for **Linux deployment**)
1.  Change the path of matlab file according to you installation

#### File executeSimulation.sh (only for **Linux deployment**)
1.  Add the path of your harvester environment folder
2.  Add the path of your LTSpice executable (it should be something like XVIIx64.exe)

## Application set up
All the configuration for the java application are contained inside the [application.properties](src/main/resources/application.properties)

* **server.address** - IP Address where the service runs
* **server.port** - Port of the service
* **sr_address** - IP Address of the AF service registry instance
* **sr_port** - Port of the AF service registry
* **harvester_env** - Path of the folder which contains harvester environment
* **harvester_exeutable** - Path of the *DrHarvester.bat* or **DrHarvester.sh* script
* **capacity** - Number of maximum concurrent simulation
* **cleaner.delay** - Delay fo the cleaning procedure in ms

## Build and start the adapter
Build the project with maven

```java
mvn clean install
```

and run the jar

```java
java -jar .\target\harvester-adapter-0.0.1-SNAPSHOT.jar
```

## APIs
### Submit Simulation
| Function | URL subpath | Method | Input | Output |
| -------- | ----------- | ------ | ----- | ------ |
| Submit Simulation     | /harvester/simulation       | POST    | [HarvesterInput](#harvester-input)     | [UUID](#uuid)     |

"SolarHeavyLoad" | "SolarLightLoad" | "TEG" | "Piezo"
#### Harvester Input
```json
{
 "devId": "string",
 "harvId": "string",

 "lowpwrI": 0.0,
 "activeI": 0.0,
 "duty": 0,
 "Vload": 0.0,
 "devAvgI": 0.0,
 "batSOC": 0.0,
 "batV": 0.0,

 "phIrr": 0.0,
 "thThot": 0,
 "thTcold": 0,
 "thGrad": 0,
 "vibAcc": 0,
 "vibFreq": 0
}
```
| Field | Description | Unit | Mandatory |
| ----- | ----------- | --------- | --------- |
| `devId` | Name of the device  | - | yes |
| `harvId` | Name od the energy harvesting model one of "SolarHeavyLoad", "SolarLightLoad", "TEG", "Piezo" | - | yes |
| `lowpwrI` | Device current consumption in low power (idle) state | *mA* | yes - Alternative to devAvgI |
| `activeI` | Device current consumption in active state | *mA* | yes - Alternative to devAvgI |
| `duty` | Duty cycle expressed as percentage | *%* | yes - Alternative to devAvgI |
| `devAvgI` | Average current consumption of the device | *mA* | yes - Alternative to the previous three fields |
| `Vload` | Nominal power supply voltage of the load | *V* | yes |
| `batSOC` | Actual state of charge | *%* | yes - Alternative to batV |
| `batV` | Actual battery voltage | *V* | yes - Alternative to batSOC |
| `phIrr` | Irradiance | *W/m<sup>2</sup>* | it depends on the harvId type |
| `thThot` | Hot side TEG's temperature | *°C* | it depends on the harvId type |
| `thTcold` | Cold side TEG's temperature | *°C* | it depends on the harvId type |
| `thGrad` | Thermal gradient | *°C* | it depends on the harvId type |
| `vibAcc` | Amount of acceleration vibrations | *mG* | it depends on the harvId type |
| `vibFreq` | Fundamental frequency | *Hz* | it depends on the harvId type |

</br>

#### UUID
```json
{
 "jobId": "string"
}
```
| Field | Description |
| ----- | ----------- |
| `jobId` | ID of the submitted simulation |
</br>

### Get Simulation Result

| Function | URL subpath | Method | Input | Output |
| -------- | ----------- | ------ | ----- | ------ |
| Get Result     | /harvester/simulation/{id}       | GET    | [jobId](#uuid)     | [HarvesterOutput](#harvester-output)     |

#### Harvester Output
```json
{
 "terminated": true,
 "result": {
    "devId": "string",
    "harvId": "string",
    "batState": 0,
    "batlifeh": 0.0,
    "tChargeh": 0,
    "dSOCrate": 0.0,
    "date": "string",
    "simStatus": 0
 }
}
```
| Field | Description |
| ----- | ----------- |
| `terminated` | Check if simulation is finished. If false the result will be null |
| `devId` | Name of the device |
| `harvId` | Name od the energy harvesting model |
| `batState` | Battery State Flag. 1->Charging, 0-> Discharging |
| `batlifeh` | Estimated remaining battery life | 
| `tChargeh` | Estimated time to fully charge the battery |
| `dSOCrate` | Estimated rate of variation of state of charge |
| `date` | date captured at the end of the simulation |
| `simStatus` | Reutrn flag with the status of the requested simulation. 1->Simulation valid, 0-> Simulation not valid |