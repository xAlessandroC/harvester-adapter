package it.unibo.disi.harvesteradapter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import it.unibo.disi.harvesteradapter.entity.SimulationJob;
import it.unibo.disi.harvesteradapter.entity.json_model.HarvesterInput;
import it.unibo.disi.harvesteradapter.entity.json_model.HarvesterOutput;
import it.unibo.disi.harvesteradapter.entity.json_model.SimulationResponse;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.PreDestroy;

@RestController
@RequestMapping("/harvester")
public class HarvesterController {
    
    private static Logger logger = LoggerFactory.getLogger(HarvesterController.class);

    private ExecutorService executor;
    private Map<String, Future<HarvesterOutput>> jobMap;

    @Value("${harvester_env}")
    private Resource harvesterEnv;

    @Value("${harvester_executable}")
    private Resource harvesterExecutable;

    @Value("${capacity}")
    private int capacity;

    /*
    *   Initialization
    */
    public HarvesterController() {
        executor = Executors.newFixedThreadPool(10);
        jobMap = new ConcurrentHashMap<>();
    }

    /*
    * Termination
    */
    @PreDestroy
    public void destroy(){
        logger.info("[HARVESTER CONTROLLER]: Stopping all jobs");

        for (String keyString: jobMap.keySet()) {
            Future<HarvesterOutput> future = jobMap.get(keyString);
        
            if(!future.isDone())
                future.cancel(true);
        }
        this.executor.shutdownNow();
    }

    /*
    *   Routes
    */

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getHarvesterName(){
        logger.info("[HARVESTER CONTROLLER] [GET] [/harvester]: Request received!");
        
        return "{\"response\":\"ATH Harvester Adapter v.01\"}";
    }

    @PostMapping(path = {"/simulation"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> startSimulation(@RequestBody final HarvesterInput input){
        logger.info("[HARVESTER CONTROLLER] [POST] [/harvester/simulation]: Simulation request received");

        String id = "";

        try{

            if(getRunningSimulations() == capacity){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"response\":\"Max capacity reached!\"}");
            }

            synchronized(this){
                id = UUID.randomUUID().toString();

                // Create simulation folder *harvesterEnv/simulation/uuid/*
                String simulation_foldname = this.harvesterEnv.getFile().getAbsolutePath() + File.separator + "simulations" + File.separator + id;
                boolean created = new File(simulation_foldname).mkdirs();

                if(!created){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"response\":\"Error during simulation setting\"}");
                }
                logger.info("[HARVESTER CONTROLLER] [POST] [/harvester/simulation]: Simulation folder created " + simulation_foldname);

                Future<HarvesterOutput> future = executor.submit(new SimulationJob(this.harvesterExecutable.getFile().getAbsolutePath(), this.harvesterEnv.getFile().getAbsolutePath(), simulation_foldname, input));
                logger.info("[HARVESTER CONTROLLER] [POST] [/harvester/simulation]: Simulation submitted with ID " + id + " - " + future);
            
                jobMap.put(id, future);
            }
        
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.OK).body("{\"jobId\":\"" + id + "\"}");
    }

    @GetMapping(path = {"/simulation/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SimulationResponse getSimulationResult(@PathVariable final String id){
        logger.info("[HARVESTER CONTROLLER] [GET] [/harvester/simulation/id]: Simulation result request received");

        boolean terminated = false;
        HarvesterOutput result  = null;

        if(!jobMap.containsKey(id)){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        try{

            Future<HarvesterOutput> future = jobMap.get(id);
    
            if(future.isDone()){
                terminated = true;
                result = future.get();
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }

        SimulationResponse response = new SimulationResponse();
        response.setResult(result);
        response.setTerminated(terminated);

        return response;
    }

    private int getRunningSimulations(){

        int count = 0;
        for (String keyString: jobMap.keySet()) {
            Future<HarvesterOutput> future = jobMap.get(keyString);
        
            if(!future.isDone())
                count ++;
        }

        return count;
    }
    
}
