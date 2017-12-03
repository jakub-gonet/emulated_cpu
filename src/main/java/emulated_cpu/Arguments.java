package emulated_cpu;

public class Arguments {
    private Integer arg1, arg2;

    Arguments(Integer arg1, Integer arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    Arguments(Integer arg1) {
        this(arg1, null);
    }

    public int getArgumentsCount() {
        int argumentCount = 0;
        if (arg1 != null) argumentCount++;
        if (arg2 != null) argumentCount++;

        return argumentCount;
    }

    public Integer getArg1() {
        return arg1;
    }

    public Integer getArg2() {
        return arg2;
    }
}
