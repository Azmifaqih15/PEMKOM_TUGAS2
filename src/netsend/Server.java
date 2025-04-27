/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package netsend;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
/**
 *
 * @author acer
 */
public class Server extends JFrame{
    private JTextArea statusArea;
    
    public Server() {
        setTitle("Server File Receiver");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Status area
        statusArea = new JTextArea();
        statusArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(statusArea);
        add(scrollPane, BorderLayout.CENTER);
        
        // Menampilkan status pada saat server berjalan
        statusArea.append("Server is listening for incoming connections...\n");
    }

    // Method untuk menjalankan server
    public void startServer() {
        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Socket socket = serverSocket.accept();
            statusArea.append("Client connected: " + socket.getInetAddress().getHostAddress() + "\n");

            try (DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())) {
                String fileName = dataInputStream.readUTF();
                long fileSize = dataInputStream.readLong();
                statusArea.append("Receiving file: " + fileName + " (" + fileSize + " bytes)\n");

                // Menyimpan file yang diterima
                try (FileOutputStream fileOutputStream = new FileOutputStream("received_" + fileName)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    long totalBytesRead = 0;

                    while (totalBytesRead < fileSize) {
                        bytesRead = dataInputStream.read(buffer);
                        totalBytesRead += bytesRead;
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }

                    statusArea.append("File received successfully.\n");
                }
            }
        } catch (IOException e) {
            statusArea.append("Error: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Server server = new Server();
                server.setVisible(true);
                server.startServer();
            }
        });
    }
    
}
