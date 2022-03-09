package it.unibo.disi.harvesteradapter.entity;

import java.io.File;
import java.util.concurrent.Callable;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unibo.disi.harvesteradapter.entity.json_model.HarvesterOutput;

public class SimulationJob implements Callable<HarvesterOutput>{

    private static Logger logger = LoggerFactory.getLogger(SimulationJob.class);

    private String executablePath;
    private String workingDirPath;

    public SimulationJob(String simulationExecutablePath, String workingDirPath){
        this.executablePath = simulationExecutablePath;
        this.workingDirPath = workingDirPath;
    }

    @Override
    public HarvesterOutput call() {
        //Execute simulation
        HarvesterOutput result = null;
        Process p = null;

        try{

            logger.info("[SIMULATION JOB] - Starting job " + "cmd /c " + executablePath);
            p = Runtime.getRuntime().exec("cmd /c " + executablePath, null, new File(this.workingDirPath));
            p.waitFor();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(Include.ALWAYS);
            result = objectMapper.readValue(new File(this.workingDirPath + "\\AHT_DrHarvester_OUTPUT.json"), HarvesterOutput.class);

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
