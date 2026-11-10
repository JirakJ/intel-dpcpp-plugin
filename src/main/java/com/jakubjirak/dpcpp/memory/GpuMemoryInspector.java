package com.jakubjirak.dpcpp.memory;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GpuMemoryInspector extends DialogWrapper {
    private final Project project;
    private JTable memoryTable;
    private DefaultTableModel tableModel;

    public GpuMemoryInspector(Project project) {
        super(project);
        this.project = project;
        setTitle("GPU Memory Inspector");
        init();
        loadMemoryInfo();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setPreferredSize(new Dimension(800, 600));

        String[] columnNames = {"Address", "Size (MB)", "Type", "Device", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        memoryTable = new JTable(tableModel);
        mainPanel.add(new JScrollPane(memoryTable), BorderLayout.CENTER);

        return mainPanel;
    }

    private void loadMemoryInfo() {
        tableModel.addRow(new Object[]{"0x7f8a4c000000", "512", "Device", "GPU 0", "Active"});
        tableModel.addRow(new Object[]{"0x7f8a4c200000", "256", "Shared", "GPU 0", "Active"});
    }
}
