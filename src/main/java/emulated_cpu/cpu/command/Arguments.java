package emulated_cpu.cpu.command;

import emulated_cpu.data_storage.Addressable;

/**
 * Arguments class is used as OP codes wrapper.
 */
public class Arguments {

    private Addressable addressable1, addressable2;
    private Integer addr1;
    private Integer addr2;


    public Arguments(Addressable addressable1, Integer addr1, Addressable addressable2, Integer addr2) {
        this.addressable1 = addressable1;
        this.addressable2 = addressable2;
        this.addr1 = addr1;
        this.addr2 = addr2;
    }


    /**
     * Gets number of non null arguments. Can be 0, 1 or 2.
     *
     * @return number of non null arguments
     */
    public int getArgumentsCount() {
        int count = 0;
        if (getAddr1() != null) count++;
        if (getAddr2() != null) count++;
        return count;
    }

    public Integer getArg1() {
        return addressable1.read(getAddr1());
    }

    public Integer getArg2() {
        return addressable2.read(getAddr2());
    }

    public Integer getAddr1() {
        return addr1;
    }

    public Integer getAddr2() {
        return addr2;
    }
}
