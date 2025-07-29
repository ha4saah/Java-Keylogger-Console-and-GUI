import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class KeyloggerGUI extends JFrame {
    private JTextArea logArea;
    private JTextArea inputArea;
    private JButton startButton;
    private JButton stopButton;
    private JButton clearButton;
    private JButton saveButton;
    private JLabel statusLabel;
    private PrintWriter writer;
    private SimpleDateFormat dateFormat;
    private boolean isRunning = false;
    private KeyListener keyListener;

    // Simplified uniform button class
    class UniformButton extends JButton {
        public UniformButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setForeground(new Color(0, 255, 180));
            setBackground(new Color(30, 30, 40));
            setFont(new Font("Consolas", Font.BOLD, 14));
            setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0, 255, 180), 1),
                new EmptyBorder(5, 15, 5, 15)
            ));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(120, 35));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Button background
            if (getModel().isPressed()) {
                g2.setColor(new Color(50, 50, 60));
            } else {
                g2.setColor(getBackground());
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 255, 180));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
        }
    }

    public KeyloggerGUI() {
        setTitle("Keylogger-Ha4saah");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        // Main panel with dark background
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBackground(new Color(20, 20, 30));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Add components to main panel
        mainPanel.add(createControlPanel(), BorderLayout.NORTH);
        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);
        mainPanel.add(createStatusPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
        initializeKeyListener();
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 0));
        panel.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(new Color(0, 255, 180), 2),
            "Controls",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Consolas", Font.BOLD, 14),
            new Color(0, 255, 180)
        ));
        panel.setBackground(new Color(20, 30, 40));
        
        // Create uniform buttons
        startButton = new UniformButton("Start");
        stopButton = new UniformButton("Stop");
        clearButton = new UniformButton("Clear");
        saveButton = new UniformButton("Save");
        
        stopButton.setEnabled(false);
        
        startButton.addActionListener(e -> startLogging());
        stopButton.addActionListener(e -> stopLogging());
        clearButton.addActionListener(e -> clearLog());
        saveButton.addActionListener(e -> saveToFile());
        
        panel.add(startButton);
        panel.add(stopButton);
        panel.add(clearButton);
        panel.add(saveButton);
        
        return panel;
    }

    // ... [keep all your other methods exactly the same: createCenterPanel(), 
    // createStatusPanel(), initializeKeyListener(), startLogging(), stopLogging(), 
    // clearLog(), saveToFile(), and main()] ...
    
    // The rest of your existing methods remain unchanged
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(15, 25, 35), getWidth(), getHeight(), new Color(5, 5, 15));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        // Input area (for capturing keystrokes)
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(new Color(0,255,180),2,true), "Input Area (Click here to capture keystrokes)", 0, 0, new Font("Consolas", Font.BOLD, 14), new Color(0,255,180)));
        inputPanel.setBackground(new Color(15, 25, 35));
        
        inputArea = new JTextArea();
        inputArea.setEditable(true);
        inputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        inputArea.setBackground(new Color(20, 20, 30));
        inputArea.setForeground(new Color(0,255,180));
        inputArea.setCaretColor(new Color(0,255,180));
        inputArea.setBorder(new LineBorder(new Color(0,255,180), 1, true));
        JScrollPane inputScrollPane = new JScrollPane(inputArea);
        inputScrollPane.setBorder(null);
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);
        
        // Log area (for displaying captured keystrokes)
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(new Color(0,255,180),2,true), "Keystroke Log", 0, 0, new Font("Consolas", Font.BOLD, 14), new Color(0,255,180)));
        logPanel.setBackground(new Color(15, 25, 35));
        
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        logArea.setBackground(new Color(20, 20, 30));
        logArea.setForeground(new Color(0,255,180));
        logArea.setCaretColor(new Color(0,255,180));
        logArea.setBorder(new LineBorder(new Color(0,255,180), 1, true));
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(null);
        logPanel.add(logScrollPane, BorderLayout.CENTER);
        
        panel.add(inputPanel);
        panel.add(logPanel);
        
        return panel;
    }
    
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder(new LineBorder(new Color(0,255,180),2,true), "Status", 0, 0, new Font("Consolas", Font.BOLD, 14), new Color(0,255,180)));
        panel.setBackground(new Color(10, 20, 30));
        
        statusLabel = new JLabel("Ready - Click 'Start Logging' to begin");
        statusLabel.setForeground(new Color(0,255,180));
        statusLabel.setFont(new Font("Consolas", Font.BOLD, 14));
        panel.add(statusLabel);
        
        return panel;
    }
    
    private void initializeKeyListener() {
        keyListener = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!isRunning) return;
                
                String keyText = KeyEvent.getKeyText(e.getKeyCode());
                String timestamp = dateFormat.format(new Date());
                String logEntry = timestamp + " - Key Pressed: " + keyText + " (Code: " + e.getKeyCode() + ")";
                
                // Add to GUI log area
                SwingUtilities.invokeLater(() -> {
                    logArea.append(logEntry + "\n");
                    logArea.setCaretPosition(logArea.getDocument().getLength());
                });
                
                // Write to file
                if (writer != null) {
                    writer.println(logEntry);
                    writer.flush();
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                // Optional: log key release events
            }
            
            @Override
            public void keyTyped(KeyEvent e) {
                if (!isRunning) return;
                
                if (e.getKeyChar() != KeyEvent.CHAR_UNDEFINED) {
                    String timestamp = dateFormat.format(new Date());
                    String logEntry = timestamp + " - Character Typed: '" + e.getKeyChar() + "'";
                    
                    // Add to GUI log area
                    SwingUtilities.invokeLater(() -> {
                        logArea.append(logEntry + "\n");
                        logArea.setCaretPosition(logArea.getDocument().getLength());
                    });
                    
                    // Write to file
                    if (writer != null) {
                        writer.println(logEntry);
                        writer.flush();
                    }
                }
            }
        };
        
        // Add key listener to input area
        inputArea.addKeyListener(keyListener);
    }
    
    private void startLogging() {
        try {
            // Create log file
            writer = new PrintWriter(new FileWriter("keylog_gui.txt", true));
            String startMsg = "=== Keylogger GUI Started: " + dateFormat.format(new Date()) + " ===";
            writer.println(startMsg);
            writer.flush();
            
            // Update GUI
            logArea.append(startMsg + "\n");
            statusLabel.setText("Logging Active - Keystrokes are being captured");
            
            // Update button states
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            
            isRunning = true;
            
            // Focus on input area
            inputArea.requestFocus();
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error starting logger: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void stopLogging() {
        if (writer != null) {
            String stopMsg = "=== Keylogger GUI Stopped: " + dateFormat.format(new Date()) + " ===";
            writer.println(stopMsg);
            writer.close();
            writer = null;
        }
        
        // Update GUI
        logArea.append("=== Logging Stopped ===\n");
        statusLabel.setText("Logging Stopped");
        
        // Update button states
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        
        isRunning = false;
    }
    
    private void clearLog() {
        logArea.setText("");
        statusLabel.setText("Log Cleared");
    }
    
    private void saveToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Log to File");
        fileChooser.setSelectedFile(new File("keylogger_log.txt"));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                PrintWriter fileWriter = new PrintWriter(new FileWriter(file));
                fileWriter.print(logArea.getText());
                fileWriter.close();
                
                JOptionPane.showMessageDialog(this, "Log saved to: " + file.getAbsolutePath(), 
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(), 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KeyloggerGUI gui = new KeyloggerGUI();
            gui.setVisible(true);
        });
    }
}