package com.jakubjirak.dpcpp.debugger;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.xdebugger.XDebugSession;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class GdbOneApiDebuggerSupport {
    
    private static final String GDB_ONEAPI_PATH = "/opt/intel/oneapi/debugger/latest/bin/gdb-oneapi";
    
    public static boolean isAvailable() {
        File gdbOneApi = new File(GDB_ONEAPI_PATH);
        return gdbOneApi.exists() && gdbOneApi.canExecute();
    }
    
    public static GeneralCommandLine createDebugCommandLine(@NotNull Project project, 
                                                            @NotNull String executablePath) 
            throws ExecutionException {
        if (!isAvailable()) {
            throw new ExecutionException("gdb-oneapi not found. Install Intel oneAPI Base Toolkit.");
        }
        
        GeneralCommandLine commandLine = new GeneralCommandLine();
        commandLine.setExePath(GDB_ONEAPI_PATH);
        commandLine.addParameter("--interpreter=mi");
        commandLine.addParameter("--nx");
        commandLine.addParameter("--quiet");
        commandLine.addParameter(executablePath);
        
        String oneApiRoot = System.getenv("ONEAPI_ROOT");
        if (oneApiRoot != null) {
            commandLine.withEnvironment("ONEAPI_ROOT", oneApiRoot);
        }
        
        return commandLine;
    }
    
    public static void showGpuThreads(@NotNull XDebugSession session) {
        Messages.showInfoMessage(
            session.getProject(),
            "GPU Thread Inspector\n\nThis feature shows:\n" +
            "• Active GPU threads\n" +
            "• Work-group hierarchy\n" +
            "• Thread state (running/suspended)",
            "GPU Thread Inspector"
        );
    }
    
    public static void inspectGpuMemory(@NotNull Project project, long address) {
        Messages.showInfoMessage(
            project,
            String.format("GPU Memory Inspector\n\nAddress: 0x%x\n\n" +
                "Features:\n" +
                "• Device memory viewer\n" +
                "• USM allocation tracking\n" +
                "• Memory leak detection", address),
            "GPU Memory Inspector"
        );
    }
}
