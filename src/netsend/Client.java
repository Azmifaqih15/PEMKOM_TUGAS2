/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package netsend;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
/**
 *
 * @author acer
 */
public class Client  extends JFrame{
    private JButton selectButton, sendButton;
    private JTextArea statusArea;
    private File selectedFile;
    
    public Client() {
        setTitle("Client File Sender");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Status area
        statusArea = new JTextArea();
        statusArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(statusArea);
        add(scrollPane, BorderLayout.CENTER);

        // Panel untuk tombol
        JPanel panel = new JPanel();
        selectButton = new JButton("Select File");
        sendButton = new JButton("Send File");
        sendButton.setEnabled(false);

        panel.add(selectButton);
        panel.add(sendButton);
        add(panel, BorderLayout.SOUTH);

        // Aksi untuk tombol Select File
        selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(Client.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    statusArea.append("File selected: " + selectedFile.getName() + "\n");
                    sendButton.setEnabled(true);
                }
            }
        });

        // Aksi untuk tombol Send File
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendFile(selectedFile);
            }
        });
    }

    // Method untuk mengirim file ke server
    private void sendFile(File file) {
        String serverAddress = "localhost";
        int port = 5000;
        
        try (Socket socket = new Socket(serverAddress, port);
             FileInputStream fileInputStream = new FileInputStream(file);
             DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())) {

            statusArea.append("Connecting to server...\n");

            dataOutputStream.writeUTF(file.getName());
            dataOutputStream.writeLong(file.length());

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                dataOutputStream.write(buffer, 0, bytesRead);
            }

            statusArea.append("File sent successfully.\n");
        } catch (IOException ex) {
            statusArea.append("Error sending file: " + ex.getMessage() + "\n");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Client().setVisible(true);
            }
        });
    }
    
}
