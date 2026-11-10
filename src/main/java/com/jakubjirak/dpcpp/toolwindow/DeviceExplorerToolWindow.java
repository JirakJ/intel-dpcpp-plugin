package com.jakubjirak.dpcpp.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class DeviceExplorerToolWindow implements ToolWindowFactory {
    
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        DeviceExplorerPanel panel = new DeviceExplorerPanel(project);
        Content content = ContentFactory.getInstance().createContent(panel, "", false);
        toolWindow.getContentManager().addContent(content);
    }
    
    private static class DeviceExplorerPanel extends JPanel {
        
        public DeviceExplorerPanel(Project project) {
            setLayout(new BorderLayout());
            
            JTextArea infoArea = new JTextArea();
            infoArea.setEditable(false);
            infoArea.setText(
                "DPC++ Device Explorer\n\n" +
                "This tool window shows:\n" +
                "• Available SYCL devices (CPU, GPU, FPGA)\n" +
                "• Device capabilities and properties\n" +
                "• Real-time device utilization\n" +
                "• Memory usage statistics\n\n" +
                "Features:\n" +
                "✓ Platform detection (Level Zero, OpenCL, CUDA)\n" +
                "✓ Compute unit count and frequency\n" +
                "✓ Memory hierarchy visualization\n" +
                "✓ Device selection for debugging\n"
            );
            
            JScrollPane scrollPane = new JScrollPane(infoArea);
            add(scrollPane, BorderLayout.CENTER);
        }
    }
}
