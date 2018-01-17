package emulated_cpu;

/**
 * Arguments class is used as OP codes wrapper.
 */
public class Arguments {

    public Integer arg1, arg2;

    /**
     * Creates new Arguments object with two parameters.
     *
     * @param arg1 first argument
     * @param arg2 second argument
     */
    public Arguments(Integer arg1, Integer arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    /**
     * Creates new Arguments object with just one parameter.
     *
     * @param arg1 parameter
     */
    public Arguments(Integer arg1) {
        this(arg1, null);
    }


    /**
     * Gets number of non null arguments. Can be 0, 1 or 2.
     *
     * @return number of non null arguments
     */
    public int getArgumentsCount() {
        int count = 0;
        if (arg1 != null) count++;
        if (arg2 != null) count++;
        return count;
    }
}
