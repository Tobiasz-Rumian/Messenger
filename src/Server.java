/*
 *  Klasa Server
 *  Tworzy serwer obsługujący informację o użytkownikach
 *
 *  @author Tobiasz Rumian
 *  @version 4.2
 *   Data: 06 Styczeń 2017 r.
 *   Indeks: 226131
 *   Grupa: śr 13:15 TN
 */
import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;


public class Server extends JFrame implements Runnable {
    private ArrayList<UserThread> users = new ArrayList<>();
    private static final int SERVER_PORT = 15000;
    private String host;
    private ServerSocket serverSocket;

    Server(){
        super("GLaDOS");
        setSize(256,300);
        URL url = null;
        try {
            url = new URL("https://pbs.twimg.com/profile_images/599972610244485121/cC9awA_s.jpg");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        Icon icon = new ImageIcon(url);
        JLabel label = new JLabel(icon);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(label);
        setVisible(true);
        (new Thread(this)).start();

    }

    @Override
    public void run() {
        Socket s;
        try {
            host = InetAddress.getLocalHost().getHostName();
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Gniazdko dla serwera nie mo�e by� utworzone");
            System.exit(0);
        }
        System.out.println("Serwer został uruchomiony na hoscie " + host);

        while (true) {
            try {
                s = serverSocket.accept();
                if (s != null) {
                    new UserThread(this, s);
                }
            } catch (IOException e) {
                System.out.println("BLAD SERWERA: Nie mozna polaczyc sie z klientem ");
            }
        }
    }
    class UserThread implements Runnable {
        private Socket socket;
        private ObjectOutputStream output;
        private ObjectInputStream input;
        private boolean kill = false;
        private String nick;
        private Server server;
        private int port;
        private String host;
        UserThread(Server server, Socket socket) throws IOException {
            this.server = server;
            this.socket = socket;
            (new Thread(this)).start();
        }

        ObjectOutputStream getOutput() {
            return output;
        }

        public String toString() {
            return nick;
        }

        void useCommand() {
            int userIndex=-1;
            String s="error";
            try {
                s = (String) input.readObject();
                if (s.contains("$")) {
                    userIndex=Integer.parseInt(s.substring(1, s.length()));
                    if(userIndex!=-1){
                        output.writeObject("$CONNECTION$");
                        output.writeObject(users.get(userIndex).nick);
                        output.writeObject(users.get(userIndex).host);
                        output.writeObject(Integer.toString(users.get(userIndex).port));
                    }
                    else System.err.println("Błędny numer indeksu");
                } else {
                    if(s.equals("LIST")){
                        getOutput().writeObject("$LIST$");
                        for (UserThread u:users) output.writeObject(u.toString());
                        output.writeObject("$!LIST$");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                server.users.add(this);
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());
                nick = (String) input.readObject();
                host = (String) input.readObject();
                port = Integer.parseInt((String) input.readObject());
                while (!kill) useCommand();
                server.users.remove(this);
                input.close();
                output.close();
                socket.close();
                socket = null;
            } catch (Exception ignored) {}
        }
    }
}
