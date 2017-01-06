/*
 *  Klasa UserServer
 *  Obsługuje połączenia przychodzące użytkownika
 *
 *  @author Tobiasz Rumian
 *  @version 2.2
 *   Data: 06 Styczeń 2017 r.
 *   Indeks: 226131
 *   Grupa: śr 13:15 TN
 */
import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;


public class UserServer implements Runnable {
    private Random random = new Random();
    private int port = random.nextInt(15000);
    private String host;
    private ServerSocket serverSocket;
    private User user;
    private boolean kill = false;

    UserServer(User user) {
        this.user = user;
        (new Thread(this)).start();
    }

    @Override
    public void run() {
        Socket s;
        createServerSocket();
        while (!kill) {
            try {
                s = serverSocket.accept();
                if (s != null) {
                    try {
                        new Conversation(user.getNick(), s);
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
            } catch (IOException e) {
                System.out.println("BLAD SERWERA: Nie mozna polaczyc sie z klientem ");
            }
        }
    }

    String getHost() {
        return host;
    }

    int getPort() {
        return port;
    }

    void kill() {
        kill = true;
    }

    private void createServerSocket() {
        int tries = 0;
        while (true) {
            try {
                host = InetAddress.getLocalHost().getHostName();
                serverSocket = new ServerSocket(port);
                break;
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.err.println(port + " zajety");
                port = random.nextInt(15000);
            }
            tries++;
            if (tries > 100) {
                JOptionPane.showMessageDialog(null, "Gniazdko dla serwera nie może być utworzone");
                System.exit(0);
            }
        }
        System.out.println("Serwer został uruchomiony na hoscie " + host);
    }
}
