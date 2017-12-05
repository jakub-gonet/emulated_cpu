package emulated_cpu;

public class Arguments {

    public Integer arg1, arg2;

    public Arguments(Integer arg1, Integer arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public Arguments(Integer arg1) {
        this(arg1, null);
    }


    public int getArgumentsCount() {
        int count = 0;
        if (arg1 != null) count++;
        if (arg2 != null) count++;
        return count;
    }
}
