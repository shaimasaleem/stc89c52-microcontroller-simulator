import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class STC89C52SimulatorUI extends JFrame {

    private final Color BG = new Color(11, 8, 19);
    private final Color PANEL = new Color(18, 13, 36);
    private final Color PANEL2 = new Color(20, 14, 38);
    private final Color BORDER = new Color(88, 28, 135);
    private final Color PURPLE = new Color(192, 132, 252);
    private final Color GREEN = new Color(34, 197, 94);
    private final Color WHITE = Color.WHITE;
    private final Color MUTED = new Color(148, 163, 184);

    private JTable programTable;
    private JLabel statusLabel, currentInstruction, currentAddress;
    private JLabel pcValue, spValue, aValue, bValue, r0Value, r1Value, r2Value, r3Value;
    private JLabel r4Value, r5Value, r6Value, r7Value;
    private JLabel cyValue, acValue, ovValue, pValue;
    private JTextArea traceArea;
    private JTable memoryTable;

    private int currentIndex = 2;

    private final String[][] instructions = {
        {"0000", "MOV A,#05H", "Data Transfer"},
        {"0002", "MOV R1,#03H", "Data Transfer"},
        {"0004", "ADD A,R1", "Arithmetic"},
        {"0005", "INC A", "Increment"},
        {"0006", "MOV 20H,A", "Data Transfer"},
        {"0008", "SJMP 000CH", "Control Flow"},
        {"000A", "NOP", "No Operation"},
        {"000B", "NOP", "No Operation"},
        {"000C", "END", "Termination"}
    };

    public STC89C52SimulatorUI() {
        setTitle("STC89C52 Microcontroller Simulator - Week 2 UI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1250, 780);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(15, 18, 15, 18));

        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createMainContent(), BorderLayout.CENTER);
        root.add(createExecutionTrace(), BorderLayout.SOUTH);

        setContentPane(root);
        selectInstruction(currentIndex);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setBackground(BG);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("STC89C52 MICROCONTROLLER SIMULATOR");
        title.setForeground(PURPLE);
        title.setFont(new Font("Consolas", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel controls = new JPanel(new BorderLayout());
        controls.setBackground(PANEL2);
        controls.setBorder(panelBorder());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        buttons.setOpaque(false);

        JButton load = button("LOAD");
        JButton reset = button("RESET");
        JButton step = button("STEP");
        JButton run = button("RUN");

        load.addActionListener(e -> loadProgram());
        reset.addActionListener(e -> resetSimulator());
        step.addActionListener(e -> stepExecution());
        run.addActionListener(e -> runExecution());

        buttons.add(load);
        buttons.add(reset);
        buttons.add(step);
        buttons.add(run);

        JPanel status = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        status.setOpaque(false);
        JLabel statusText = label("STATUS :", WHITE, true, 13);
        statusLabel = label("READY", GREEN, true, 13);
        status.add(statusText);
        status.add(statusLabel);

        controls.add(buttons, BorderLayout.WEST);
        controls.add(status, BorderLayout.EAST);

        header.add(title);
        header.add(Box.createVerticalStrut(12));
        header.add(controls);

        return header;
    }

    private JPanel createMainContent() {
        JPanel main = new JPanel(new GridLayout(1, 3, 12, 0));
        main.setBackground(BG);

        main.add(createProgramColumn());
        main.add(createRegistersPanel());
        main.add(createMemoryColumn());

        return main;
    }

    private JPanel createProgramColumn() {
        JPanel column = new JPanel(new BorderLayout(0, 12));
        column.setBackground(BG);

        JPanel programPanel = panel("PROGRAM / INSTRUCTIONS");

        String[] cols = {"PTR", "ADDRESS", "INSTRUCTION", "CATEGORY"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        for (String[] row : instructions) {
            model.addRow(new Object[]{"", row[0], row[1], row[2]});
        }

        programTable = new JTable(model);
        styleTable(programTable);
        programTable.getColumnModel().getColumn(0).setPreferredWidth(35);
        programTable.getColumnModel().getColumn(1).setPreferredWidth(70);
        programTable.getColumnModel().getColumn(2).setPreferredWidth(125);
        programTable.getColumnModel().getColumn(3).setPreferredWidth(105);

        programTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = programTable.getSelectedRow();
                if (row >= 0) selectInstruction(row);
            }
        });

        programPanel.add(new JScrollPane(programTable), BorderLayout.CENTER);

        JPanel current = panel("CURRENT INSTRUCTION");
        currentInstruction = label("ADD A,R1", WHITE, true, 20);
        currentAddress = label("Address : 0004H", new Color(216, 180, 254), false, 13);
        current.add(currentInstruction, BorderLayout.CENTER);

        JPanel addressWrap = new JPanel(new BorderLayout());
        addressWrap.setOpaque(false);
        addressWrap.add(currentAddress, BorderLayout.WEST);
        current.add(addressWrap, BorderLayout.SOUTH);

        column.add(programPanel, BorderLayout.CENTER);
        column.add(current, BorderLayout.SOUTH);
        return column;
    }

    private JPanel createRegistersPanel() {
        JPanel panel = panel("REGISTERS");
        panel.setLayout(new BorderLayout());

        JPanel regs = new JPanel(new GridLayout(12, 1, 0, 3));
        regs.setBackground(PANEL);

        pcValue = addRegister(regs, "PC", "0004H");
        spValue = addRegister(regs, "SP", "07H");
        aValue = addRegister(regs, "A", "05H");
        bValue = addRegister(regs, "B", "00H");
        r0Value = addRegister(regs, "R0", "00H");
        r1Value = addRegister(regs, "R1", "03H");
        r2Value = addRegister(regs, "R2", "00H");
        r3Value = addRegister(regs, "R3", "00H");
        r4Value = addRegister(regs, "R4", "00H");
        r5Value = addRegister(regs, "R5", "00H");
        r6Value = addRegister(regs, "R6", "00H");
        r7Value = addRegister(regs, "R7", "00H");

        panel.add(regs, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createMemoryColumn() {
        JPanel column = new JPanel(new BorderLayout(0, 12));
        column.setBackground(BG);

        JPanel memory = panel("MEMORY (DATA MEMORY)");

        String[] cols = {"ADDRESS", "VALUE"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        String[][] values = {
            {"00", "00H"}, {"01", "00H"}, {"02", "05H"}, {"03", "03H"},
            {"04", "08H"}, {"05", "06H"}, {"06", "05H"}, {"07", "20H"},
            {"08", "00H"}, {"09", "00H"}, {"0A", "00H"}, {"0B", "00H"},
            {"20", "15H"}
        };
        for (String[] v : values) model.addRow(v);

        memoryTable = new JTable(model);
        styleTable(memoryTable);
        memory.add(new JScrollPane(memoryTable), BorderLayout.CENTER);

        JPanel flags = panel("FLAGS / STATUS");
        JPanel flagGrid = new JPanel(new GridLayout(2, 4, 4, 4));
        flagGrid.setBackground(PANEL);

        flagGrid.add(label("CY", PURPLE, true, 12));
        flagGrid.add(label("AC", PURPLE, true, 12));
        flagGrid.add(label("OV", PURPLE, true, 12));
        flagGrid.add(label("P", PURPLE, true, 12));

        cyValue = label("0", WHITE, true, 15);
        acValue = label("0", WHITE, true, 15);
        ovValue = label("0", WHITE, true, 15);
        pValue = label("1", GREEN, true, 15);

        flagGrid.add(cyValue);
        flagGrid.add(acValue);
        flagGrid.add(ovValue);
        flagGrid.add(pValue);

        flags.add(flagGrid, BorderLayout.CENTER);

        column.add(memory, BorderLayout.CENTER);
        column.add(flags, BorderLayout.SOUTH);
        return column;
    }

    private JPanel createExecutionTrace() {
        JPanel trace = panel("EXECUTION TRACE");
        trace.setPreferredSize(new Dimension(0, 145));
        trace.setLayout(new GridLayout(1, 2, 25, 0));

        JPanel stages = new JPanel(new GridLayout(4, 1, 0, 2));
        stages.setBackground(PANEL);
        stages.add(label("FETCH                         ✓", GREEN, true, 13));
        stages.add(label("DECODE                       ✓", GREEN, true, 13));
        stages.add(label("EXECUTE                      ✓", GREEN, true, 13));

        traceArea = new JTextArea();
        traceArea.setEditable(false);
        traceArea.setBackground(PANEL);
        traceArea.setForeground(WHITE);
        traceArea.setFont(new Font("Consolas", Font.BOLD, 13));
        traceArea.setBorder(new EmptyBorder(4, 10, 4, 4));

        stages.add(label("RESULT / CPU STATE CHANGES", PURPLE, true, 12));

        trace.add(stages);
        trace.add(new JScrollPane(traceArea));
        return trace;
    }

    private JLabel addRegister(JPanel parent, String name, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(PANEL);
        JLabel n = label(name, PURPLE, true, 14);
        JLabel v = label(value, WHITE, true, 14);
        row.add(n, BorderLayout.WEST);
        row.add(v, BorderLayout.EAST);
        parent.add(row);
        return v;
    }

    private void selectInstruction(int index) {
        if (index < 0 || index >= instructions.length) return;
        currentIndex = index;
        programTable.setRowSelectionInterval(index, index);

        for (int i = 0; i < programTable.getRowCount(); i++) {
            programTable.setValueAt(i == index ? "▶" : "", i, 0);
        }

        currentInstruction.setText(instructions[index][1]);
        currentAddress.setText("Address : " + instructions[index][0] + "H");

        // UI preview of CPU-state values; actual CPU integration can replace these values.
        String addr = instructions[index][0] + "H";
        pcValue.setText(addr);

        if (index == 0) {
            aValue.setText("05H");
            r1Value.setText("00H");
            traceArea.setText("A     00H  →  05H\nPC    0000H → 0002H");
        } else if (index == 1) {
            aValue.setText("05H");
            r1Value.setText("03H");
            traceArea.setText("R1    00H  →  03H\nPC    0002H → 0004H");
        } else if (index == 2) {
            aValue.setText("08H");
            r1Value.setText("03H");
            pValue.setText("1");
            pValue.setForeground(GREEN);
            traceArea.setText("A     05H  →  08H\nPC    0004H → 0005H");
        } else if (index == 3) {
            aValue.setText("09H");
            pValue.setText("0");
            pValue.setForeground(WHITE);
            traceArea.setText("A     08H  →  09H\nPC    0005H → 0006H");
        } else if (index == 4) {
            aValue.setText("09H");
            traceArea.setText("MEM[20H]  00H → 09H\nPC        0006H → 0008H");
        } else if (index == 5) {
            traceArea.setText("PC    0008H → 000CH\nControl flow instruction");
        } else if (index == 8) {
            traceArea.setText("STATUS    RUNNING → HALTED\nProgram execution terminated");
        } else {
            traceArea.setText("PC    updated to next instruction\nNo register data change");
        }
    }

    private void loadProgram() {
        selectInstruction(0);
        statusLabel.setText("READY");
        statusLabel.setForeground(GREEN);
    }

    private void resetSimulator() {
        selectInstruction(0);
        spValue.setText("07H");
        aValue.setText("00H");
        r1Value.setText("00H");
        statusLabel.setText("RESET");
        statusLabel.setForeground(PURPLE);
        traceArea.setText("CPU state reset\nPC → 0000H\nRegisters → default state");
    }

    private void stepExecution() {
        if (currentIndex < instructions.length - 1) {
            selectInstruction(currentIndex + 1);
            statusLabel.setText("STEPPED");
            statusLabel.setForeground(new Color(234, 179, 8));
        } else {
            statusLabel.setText("HALTED");
            statusLabel.setForeground(new Color(239, 68, 68));
        }
    }

    private void runExecution() {
        statusLabel.setText("RUNNING");
        statusLabel.setForeground(new Color(59, 130, 246));

        Timer timer = new Timer(250, null);
        timer.addActionListener(new ActionListener() {
            int i = currentIndex;
            public void actionPerformed(ActionEvent e) {
                if (i < instructions.length - 1) {
                    selectInstruction(++i);
                } else {
                    ((Timer)e.getSource()).stop();
                    statusLabel.setText("TERMINATED");
                    statusLabel.setForeground(new Color(239, 68, 68));
                }
            }
        });
        timer.start();
    }

    private JButton button(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Consolas", Font.BOLD, 13));
        b.setForeground(new Color(216, 180, 254));
        b.setBackground(new Color(26, 18, 51));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(new Color(107, 33, 168)));
        b.setPreferredSize(new Dimension(100, 35));
        return b;
    }

    private JPanel panel(String title) {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBackground(PANEL);
        p.setBorder(panelBorder());

        JLabel t = label(title, PURPLE, true, 13);
        p.add(t, BorderLayout.NORTH);
        return p;
    }

    private Border panelBorder() {
        return BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(10, 12, 10, 12)
        );
    }

    private JLabel label(String text, Color color, boolean bold, int size) {
        JLabel l = new JLabel(text);
        l.setForeground(color);
        l.setFont(new Font("Consolas", bold ? Font.BOLD : Font.PLAIN, size));
        return l;
    }

    private void styleTable(JTable table) {
        table.setBackground(PANEL);
        table.setForeground(WHITE);
        table.setGridColor(new Color(76, 29, 149));
        table.setFont(new Font("Consolas", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(76, 29, 149));
        table.setSelectionForeground(WHITE);
        table.getTableHeader().setBackground(PANEL2);
        table.getTableHeader().setForeground(PURPLE);
        table.getTableHeader().setFont(new Font("Consolas", Font.BOLD, 11));
        table.getTableHeader().setReorderingAllowed(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new STC89C52SimulatorUI().setVisible(true));
    }
}
