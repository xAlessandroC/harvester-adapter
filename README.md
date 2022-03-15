# Harvester Adapter

The harvester adapter enables the registration of the Harvester tool functionalities in the Arrowhead Framework. This is done by mean of a Java Spring applicatiton which exposes a REST API.

## Requirements
* Java 11 + 
* Maven 3.8.4
* Matlab 2020a
* LTSpice
* Harvester Environment

## Harvester Environment
The adapter needs a proper configured harvester environment in order to work correctly. All the files needed can be download from [here](https://drive.google.com/file/d/1wmrZuRre_0miivuB7s8DrcTT8ciiM4GJ/view?usp=sharing).

Download the whole folder and put it under the *src/main/resources* folder

There you can find a README (README_DrHarvester.docx) with all the changes to make in order to customize the environment settings according to your own setup. Briefly:

1.  **File AHT_DrHarvesterRunTime_v12.m: line 24** - change the path with your LTSpice one
2.  **File AHT_DrHarvesterRunTime_v12.m: line 27/28** - change the paths with you LTSpice libraries ones
3. **File AHT_DrHarvesterRunTime_v12.m: line 32** - change the path with your working environment one (the folder you have just downloaded)
4. **DrHarvester.bat** - change the path of matlab file according to you installation
5. (OPTIONAL) **File AHT_DrHarvesterRunTime_v12.m: line 17/18** - change if you want to rename input/output file.

## Application set up
All the configuration for the java application are contained inside the [application.properties](src/main/resources/application.properties)

* **server.address** - IP Address where the service lies
* **server.port** - Port of the service
* **sr_address** - IP Address of the AF service registry instance
* **sr_port** - Port of the AF service registry
* **harvester_env** - Path of the folder which container harvester environment
* **harvester_exeutable** - Path of the *DrHarvester.bat* script
* **capacity** - number of maximum concurrent simulation (possible concurrency problems) 


## Build and start the adapter
Build the project with maven

```java
mvn clean install
```

and run the jar

```java
java -jar .\target\harvester-adapter-0.0.1-SNAPSHOT.jar
```

