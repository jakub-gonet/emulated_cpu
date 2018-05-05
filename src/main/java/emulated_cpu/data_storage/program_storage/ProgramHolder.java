package emulated_cpu.data_storage.program_storage;

import emulated_cpu.data_storage.Addressable;

import java.util.List;

public interface ProgramHolder extends Addressable {
    public void setProgram(List<Integer> program);

    public List<Integer> getProgram();
}
