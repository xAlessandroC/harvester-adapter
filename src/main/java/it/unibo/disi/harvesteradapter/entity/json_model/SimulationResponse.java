package it.unibo.disi.harvesteradapter.entity.json_model;

public class SimulationResponse {

    private boolean terminated;
    private HarvesterOutput result;

    public SimulationResponse(){}

    public SimulationResponse(boolean terminated, HarvesterOutput result){
        this.terminated = terminated;
        this.result = result;
    }

    public boolean isTerminated(){
        return this.terminated;
    }

    public void setTerminated(boolean terminated){
        this.terminated = terminated;
    }

    public HarvesterOutput getResult(){
        return this.result;
    }

    public void setResult(HarvesterOutput result){
        this.result = result;
    }
    
}
