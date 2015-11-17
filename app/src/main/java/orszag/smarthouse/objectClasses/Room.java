package orszag.smarthouse.objectClasses;

import java.util.List;

/**
 * Created by Patrik Glendell on 02/10/15.
 *
 * Implemented by David Munro & Juraj Orszag
 */
public class Room {
    private int id;
    private String name;
    private List<Device> devices;
    private int temperature;
    private int waterConsumption;
    private int energyConsumption;

    public Room(){

    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Device> getDevices() {
        return devices;
    }
    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }
    public int getTemperature() {
        return temperature;
    }
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
    public int getWaterConsumption() {
        return waterConsumption;
    }
    public void setWaterConsumption(int waterConsumption) {
        this.waterConsumption = waterConsumption;
    }
    public int getEnergyConsumption() {
        return energyConsumption;
    }
    public void setEnergyConsumption(int energyConsumption) {
        this.energyConsumption = energyConsumption;
    }
}