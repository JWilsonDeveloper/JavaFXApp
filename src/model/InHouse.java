package model;

/** This class extends the Part class to define an Outsourced Part object.
 * @author James Wilson
 */

public class InHouse extends Part {
    private int machineId;
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * @return the machine id
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * @param machineId the machine id to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

}
