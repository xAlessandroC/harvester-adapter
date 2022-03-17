package it.unibo.disi.harvesteradapter.entity;

import java.io.File;
import java.util.concurrent.Callable;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unibo.disi.harvesteradapter.entity.json_model.HarvesterInput;
import it.unibo.disi.harvesteradapter.entity.json_model.HarvesterOutput;

public class SimulationJob implements Callable<HarvesterOutput>{

    private static Logger logger = LoggerFactory.getLogger(SimulationJob.class);

    private String executablePath;
    private String workingDirPath;
    private String simulation_foldname;
    private HarvesterInput input;

    public SimulationJob(String simulationExecutablePath, String workingDirPath, String simulation_foldname, HarvesterInput input){
        this.executablePath = simulationExecutablePath;
        this.workingDirPath = workingDirPath;
        this.simulation_foldname = simulation_foldname;
        this.input = input;
    }

    @Override
    public HarvesterOutput call() {
        //Execute simulation
        HarvesterOutput result = null;
        Process p = null;

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(Include.ALWAYS);

            // Write input inside the proper simulation folder
            objectMapper.writeValue(new File(this.simulation_foldname + File.separator + "AHT_DrHarvester_INPUT.json"), input);

            logger.info("[SIMULATION JOB] - Starting job " + "cmd /c " + executablePath + " " + this.simulation_foldname + File.separator + "AHT_DrHarvester_INPUT.json" + " " + this.simulation_foldname + File.separator + "AHT_DrHarvester_OUTPUT.json");
            p = Runtime.getRuntime().exec("cmd /c " + executablePath + " " + this.simulation_foldname + File.separator + "AHT_DrHarvester_INPUT.json" + " " + this.simulation_foldname + File.separator + "AHT_DrHarvester_OUTPUT.json", null, new File(this.workingDirPath));
            p.waitFor();

            result = objectMapper.readValue(new File(this.workingDirPath + File.separator + "AHT_DrHarvester_OUTPUT.json"), HarvesterOutput.class);

        }
        catch(InterruptedException e){
            p.destroy();
            return null;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }

        return result;
    }
    
}
