package robotlib;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.Highlighter;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.TreeMap;

public class Dashboard {
    private static Dashboard instance;

    public static Dashboard getInstance() {
        if (instance == null)
            instance = new Dashboard();
        return instance;
    }

    private final Color kWhiteColor = new Color(255, 255, 255);
    private final Color kDarkGreenColor = new Color(0, 50, 0);
    private final Color kDarkRedColor = new Color(55, 5, 0);
    private final Color kDisconnectedColor = new Color(255, 0, 0);
    private final Color kConnectedColor = new Color(100, 255, 0);
    private final Color kDisselectedColor = new Color(75, 75, 75);
    private final Color kSelectedColor = new Color(45, 45, 45);
    private final Color kBrightBackgroundColor = new Color(70, 70, 70);
    private final Color kDimBackgroundColor = new Color(42, 42, 42);

    private MyKeyListener keyListener;
    private JFrame jframe;
    private JPanel jpanel;
    private JPanel panelDriverStation, panelSmartDashboard;
    private JButton buttonEnable, buttonDisable;
    private JButton buttonTeleop, buttonAuto, buttonPractice, buttonTest;
    private JLabel labelComm, labelCode, labelJoysticks;
    private JLabel labelCommDot, labelCodeDot, labelJoysticksDot;
    private JLabel labelTime, labelStatus;
    private JTextArea textAreaLog;
    private JTextPane textPaneSmartDashboard;
    private DecimalFormat timeFormatter = new DecimalFormat("#.#");
    private Highlighter highlighterSmartDashboard;

    private boolean smartDashboardAutoExpand = true;
    private int modeSelected = 0;
    private boolean robotEnabled = false;
    private boolean robotConnected = false;

    public static void main(String[] args) {
        new Dashboard();
        // PrintManager m = new PrintManager(d::addNewLog);
        // for (int i = 0; i < 20; i++) {
        // System.out.println("String number " + i + " here");
        // }
    }

    private Dashboard() {
        new PrintManager(this::addNewLog);

        createWindow();

        panelDriverStation = createDriverStationPanel();
        panelSmartDashboard = createSmartDashboard();

        panelDriverStation.setPreferredSize(new Dimension(800, 200));
        panelDriverStation.setMinimumSize(new Dimension(800, 200));
        panelDriverStation.setMaximumSize(new Dimension(5000, 200));

        panelSmartDashboard.setPreferredSize(new Dimension(800, 300));
        panelSmartDashboard.setMinimumSize(new Dimension(800, 25));
        panelSmartDashboard.setMaximumSize(new Dimension(5000, 5000));

        jpanel.add(panelDriverStation);
        jpanel.add(createSmartDashboardLabel());
        jpanel.add(panelSmartDashboard);
        jpanel.add(Box.createRigidArea(new Dimension(15, 15)));
        jframe.pack();
        jframe.setVisible(true);
    }

    private void createWindow() {
        keyListener = new MyKeyListener(this);
        jframe = new JFrame("RobotSim Driver Station");
        jframe.addKeyListener(keyListener);
        jframe.setFocusable(true);
        jframe.setFocusTraversalKeysEnabled(false);

        jpanel = new JPanel();
        jpanel.setLayout(new BoxLayout(jpanel, BoxLayout.Y_AXIS));
        jpanel.setPreferredSize(new Dimension(800, 200));
        jpanel.setMinimumSize(new Dimension(200, 25));
        jpanel.setMaximumSize(new Dimension(5000, 5000));
        jframe.add(jpanel);
        jframe.setSize(800, 200);
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JPanel createDriverStationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(kDimBackgroundColor);
        panel.add(createRobotModeButtons());
        panel.add(createStatsLabels());
        panel.add(createLogViewer());
        return panel;
    }

    private JPanel createRobotModeButtons() {
        JPanel panel = new JPanel();
        panel.setBackground(kBrightBackgroundColor);
        panel.setPreferredSize(new Dimension(200, 200));
        panel.setMinimumSize(new Dimension(200, 200));
        panel.setMaximumSize(new Dimension(200, 200));
        panel.setLayout(null);
        buttonEnable = createSimpleButton("Enable");
        buttonDisable = createSimpleButton("Disable");
        buttonEnable.setBounds(20, 140, 80, 50);
        buttonDisable.setBounds(100, 140, 80, 50);
        buttonEnable.setForeground(kDarkGreenColor);
        buttonDisable.setForeground(kDisconnectedColor);
        buttonEnable.setBackground(kDisselectedColor);
        buttonDisable.setBackground(kSelectedColor);

        buttonTeleop = createSimpleButton("TeleOperated");
        buttonAuto = createSimpleButton("Autonomous");
        buttonPractice = createSimpleButton("Practice");
        buttonTest = createSimpleButton("Test");
        buttonTeleop.setBounds(20, 20, 160, 25);
        buttonAuto.setBounds(20, 45, 160, 25);
        buttonPractice.setBounds(20, 70, 160, 25);
        buttonTest.setBounds(20, 95, 160, 25);
        buttonTeleop.setHorizontalAlignment(SwingConstants.LEFT);
        buttonAuto.setHorizontalAlignment(SwingConstants.LEFT);
        buttonPractice.setHorizontalAlignment(SwingConstants.LEFT);
        buttonTest.setHorizontalAlignment(SwingConstants.LEFT);
        buttonTeleop.setForeground(kWhiteColor);
        buttonAuto.setForeground(kWhiteColor);
        buttonPractice.setForeground(kWhiteColor);
        buttonTest.setForeground(kWhiteColor);
        buttonTeleop.setBackground(kSelectedColor);
        buttonAuto.setBackground(kDisselectedColor);
        buttonPractice.setBackground(kDisselectedColor);
        buttonTest.setBackground(kDisselectedColor);
        buttonTeleop.setFocusPainted(false);
        buttonAuto.setFocusPainted(false);
        buttonPractice.setFocusPainted(false);
        buttonTest.setFocusPainted(false);
        buttonEnable.setFocusPainted(false);
        buttonDisable.setFocusPainted(false);

        buttonTeleop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modeButtonPressed(0, buttonTeleop);
            }
        });
        buttonAuto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modeButtonPressed(1, buttonAuto);
            }
        });
        buttonPractice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modeButtonPressed(2, buttonPractice);
            }
        });
        buttonTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modeButtonPressed(3, buttonTest);
            }
        });

        buttonEnable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableButtonPressed(true);
            }
        });
        buttonDisable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableButtonPressed(false);
            }
        });

        panel.add(buttonEnable);
        panel.add(buttonDisable);
        panel.add(buttonTeleop);
        panel.add(buttonAuto);
        panel.add(buttonPractice);
        panel.add(buttonTest);

        return panel;
    }

    private JPanel createStatsLabels() {
        JPanel panel = new JPanel();
        panel.setBackground(kDimBackgroundColor);
        panel.setPreferredSize(new Dimension(180, 200));
        panel.setMinimumSize(new Dimension(180, 200));
        panel.setMaximumSize(new Dimension(180, 200));
        panel.setLayout(null);
        labelTime = new JLabel("Elapsed Time: 0:00.0");
        labelTime.setBounds(20, 15, 160, 25);
        useLargestFittingFont(labelTime);
        labelTime.setText("Elapsed Time: 0");

        labelComm = new JLabel("Communications");
        labelCode = new JLabel("Robot Code");
        labelJoysticks = new JLabel("Joysticks");
        labelCommDot = new JLabel(" ");
        labelCodeDot = new JLabel(" ");
        labelJoysticksDot = new JLabel(" ");
        labelComm.setBounds(50, 60, 130, 15);
        labelCode.setBounds(50, 80, 130, 15);
        labelJoysticks.setBounds(50, 100, 130, 15);
        labelCommDot.setBounds(25, 63, 15, 10);
        labelCodeDot.setBounds(25, 83, 15, 10);
        labelJoysticksDot.setBounds(25, 103, 15, 10);
        labelCommDot.setBackground(kDisconnectedColor);
        labelCodeDot.setBackground(kDisconnectedColor);
        labelJoysticksDot.setBackground(kDisconnectedColor);
        labelCommDot.setOpaque(true);
        labelCodeDot.setOpaque(true);
        labelJoysticksDot.setOpaque(true);
        labelStatus = new JLabel(
                "<html><center style='font-size:14px'>No Robot</center><center style='font-size:14px'>Communication</center></html>");
        labelStatus.setBounds(20, 130, 140, 60);
        labelStatus.setHorizontalAlignment(SwingConstants.CENTER);
        labelTime.setForeground(kWhiteColor);
        labelComm.setForeground(kWhiteColor);
        labelCode.setForeground(kWhiteColor);
        labelJoysticks.setForeground(kWhiteColor);
        labelStatus.setForeground(kWhiteColor);

        panel.add(labelTime);
        panel.add(labelComm);
        panel.add(labelCode);
        panel.add(labelJoysticks);
        panel.add(labelCommDot);
        panel.add(labelCodeDot);
        panel.add(labelJoysticksDot);
        panel.add(labelStatus);
        return panel;
    }

    private JPanel createLogViewer() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(kBrightBackgroundColor);
        panel.setPreferredSize(new Dimension(400, 200));
        panel.setMinimumSize(new Dimension(200, 200));
        panel.setMaximumSize(new Dimension(5000, 200));

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        panel1.setBackground(kBrightBackgroundColor);
        textAreaLog = new JTextArea("System.out.println(\"will appear here!\");\n");
        textAreaLog.addKeyListener(keyListener);
        JScrollPane scrollPaneLog = new JScrollPane(textAreaLog); // JTextArea is placed in a JScrollPane.

        textAreaLog.setEditable(false);
        textAreaLog.setLineWrap(true);
        textAreaLog.setForeground(kWhiteColor);
        textAreaLog.setBackground(kSelectedColor);

        panel1.add(Box.createRigidArea(new Dimension(15, 15)));
        panel1.add(scrollPaneLog);
        panel1.add(Box.createRigidArea(new Dimension(15, 15)));

        panel.add(Box.createRigidArea(new Dimension(15, 15)));
        panel.add(panel1);
        panel.add(Box.createRigidArea(new Dimension(15, 15)));
        return panel;
    }

    private JPanel createSmartDashboard() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        String html = "<html><table style='font-size:14px'><tr><td><b>Key</b></td><td><b>Value</b></td></tr></table></html>";

        textPaneSmartDashboard = new JTextPane();
        textPaneSmartDashboard.setContentType("text/html");
        textPaneSmartDashboard.addKeyListener(keyListener);
        textPaneSmartDashboard.setText(html);
        textPaneSmartDashboard.setPreferredSize(new Dimension(800, 200));
        textPaneSmartDashboard.setMinimumSize(new Dimension(200, 0));
        textPaneSmartDashboard.setMaximumSize(new Dimension(5000, 5000));
        textPaneSmartDashboard.setEditable(false);
        // textPaneSmartDashboard.setEnabled(false);
        highlighterSmartDashboard = textPaneSmartDashboard.getHighlighter();
        panel.add(Box.createRigidArea(new Dimension(15, 15)));
        panel.add(new JScrollPane(textPaneSmartDashboard));
        panel.add(Box.createRigidArea(new Dimension(15, 15)));

        return panel;
    }

    private JPanel createSmartDashboardLabel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(800, 40));
        panel.setMinimumSize(new Dimension(200, 40));
        panel.setMaximumSize(new Dimension(5000, 40));
        JLabel labelSmartDashboardTitle = new JLabel(
                "<html><center style='font-size:18px'>SmartDashboard</center></html>");
        panel.add(labelSmartDashboardTitle);
        return panel;
    }

    private JButton createSimpleButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isPressed()) {
                    Color bg = getBackground();
                    setBackground(kSelectedColor);
                    super.paintComponent(g);
                    setBackground(bg);
                } else {
                    super.paintComponent(g);
                }
            }
        };
        button.addKeyListener(keyListener);
        button.setContentAreaFilled(false);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setOpaque(true);
        Border line = new LineBorder(Color.BLACK);
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);
        button.setBorder(compound);
        return button;
    }

    private void useLargestFittingFont(JLabel label) {
        Font labelFont = label.getFont();
        String labelText = label.getText();

        int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
        int componentWidth = label.getWidth();

        // Find out how much the font can grow in width.
        double widthRatio = (double) componentWidth / (double) stringWidth;

        int newFontSize = (int) (labelFont.getSize() * widthRatio);
        int componentHeight = label.getHeight();

        // Pick a new font size so it will not be larger than the height of label.
        int fontSizeToUse = Math.min(newFontSize, componentHeight);

        // Set the label's font size to the newly determined size.
        label.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
    }

    public void enableButtonPressed(boolean enable) {
        if (!robotConnected) {
            labelStatus.setText(
                    "<html><center style='font-size:14px'>No Robot</center><center style='font-size:14px'>Communication</center></html>");
            return;
        }

        String modeName = "Teleoperated";
        if (modeSelected == 1)
            modeName = "Autonomous";
        else if (modeSelected == 2)
            modeName = "Practice";
        else if (modeSelected == 3)
            modeName = "Test";

        robotEnabled = enable;
        if (enable) {
            buttonEnable.setBackground(kSelectedColor);
            buttonDisable.setBackground(kDisselectedColor);
            buttonEnable.setForeground(kConnectedColor);
            buttonDisable.setForeground(kDarkRedColor);
            labelStatus.setText("<html><center style='font-size:14px'>" + modeName
                    + "</center><center style='font-size:14px'>Enabled</center></html>");
            textPaneSmartDashboard.setHighlighter(null);
        } else {
            buttonEnable.setBackground(kDisselectedColor);
            buttonDisable.setBackground(kSelectedColor);
            buttonEnable.setForeground(kDarkGreenColor);
            buttonDisable.setForeground(kDisconnectedColor);
            labelStatus.setText("<html><center style='font-size:14px'>" + modeName
                    + "</center><center style='font-size:14px'>Disabled</center></html>");
            setTimestamp(0);
            textPaneSmartDashboard.setHighlighter(highlighterSmartDashboard);
        }
    }

    private void modeButtonPressed(int mode, JButton button) {
        if (mode == modeSelected)
            return;

        modeSelected = mode;
        enableButtonPressed(false);
        buttonTeleop.setBackground(kDisselectedColor);
        buttonAuto.setBackground(kDisselectedColor);
        buttonPractice.setBackground(kDisselectedColor);
        buttonTest.setBackground(kDisselectedColor);
        button.setBackground(kSelectedColor);
    }

    public void setStatusLabel(String str) {
        labelStatus.setText(str);
    }

    public void addNewLog(String s) {
        // jframe.setTitle(s);
        textAreaLog.append(s + "\n");
        textAreaLog.setCaretPosition(textAreaLog.getText().length());
    }

    public RobotMode getMode() {
        if (!robotEnabled)
            return RobotMode.Disabled;

        if (modeSelected == 0)
            return RobotMode.Teleop;
        else if (modeSelected == 1)
            return RobotMode.Auto;
        else if (modeSelected == 2)
            return RobotMode.Practice;
        else if (modeSelected == 3)
            return RobotMode.Test;

        return RobotMode.Disabled;
    }

    public void setTimestamp(double seconds) {
        labelTime.setText("Elapsed Time: " + timeFormatter.format(seconds));
    }

    public void setConnectionStatus(boolean connected) {
        robotConnected = connected;
        labelCommDot.setBackground(connected ? kConnectedColor : kDisconnectedColor);
        labelCodeDot.setBackground(connected ? kConnectedColor : kDisconnectedColor);
        labelJoysticksDot.setBackground(connected ? kConnectedColor : kDisconnectedColor);
    }

    public void updateSmartDashboard(TreeMap<String, String> data, int longestStrLen) {
        if (smartDashboardAutoExpand) {
            smartDashboardAutoExpand = false;
            jpanel.setPreferredSize(new Dimension(800, 600));
            jframe.pack();
        }
        StringBuilder sb = new StringBuilder("<html><pre><span style='font-size:16px'>");

        for (Map.Entry<String, String> entry : data.entrySet()) {
            var s = String.format("  %-" + longestStrLen + "s  |  %s", entry.getKey(), entry.getValue());
            sb.append(s);
            sb.append("\n");
        }

        sb.append("</span></pre></html>");
        textPaneSmartDashboard.setText(sb.toString());
    }

    @Deprecated
    public void updateSmartDashboardHTMLTable(TreeMap<String, String> data) {
        if (smartDashboardAutoExpand) {
            smartDashboardAutoExpand = false;
            jpanel.setPreferredSize(new Dimension(800, 600));
            jframe.pack();
        }
        StringBuilder sb = new StringBuilder(
                "<html><table style='font-size:14px'><tr><td><b>Key</b></td><td><b>Value</b></td></tr>");

        for (Map.Entry<String, String> entry : data.entrySet()) {
            sb.append("<tr><td>");
            sb.append(entry.getKey());
            sb.append("</td><td>");
            sb.append(entry.getValue());
            sb.append("</td></tr>");
        }

        sb.append("</table></html>");
        textPaneSmartDashboard.setText(sb.toString());
    }
}

class MyKeyListener implements KeyListener {
    private final Dashboard dashboard;

    public MyKeyListener(Dashboard dashboard) {
        this.dashboard = dashboard;

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // System.out.println("keytyped" + e.getKeyCode());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // System.out.println("keypressed" + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            dashboard.enableButtonPressed(false);
        } else {
            System.out.println("Please use keyboard or joystick in the simulator window");
            // SmartDashboard.putString("_Note", "Please use keyboard or joystick in the simulator window");
            // dashboard.updateSmartDashboard(SmartDashboard.getTreeMap(), SmartDashboard.getLongestStrLen());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // System.out.println("keyreleased" + e.getKeyCode());
    }

}