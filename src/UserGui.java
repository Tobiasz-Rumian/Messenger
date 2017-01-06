/*
 *  Klasa UserGui
 *  Obsługuje interfejs graficzny wyboru kontaktów.
 *
 *  @author Tobiasz Rumian
 *  @version 1.1
 *   Data: 06 Styczeń 2017 r.
 *   Indeks: 226131
 *   Grupa: śr 13:15 TN
 */
import javax.swing.*;


class UserGui extends JFrame {
    private User user;
    private JMenuBar menuBar = new JMenuBar();
    private JComboBox<String> users = new JComboBox<>();
    private JButton buttonConnect = new JButton("Połącz");
    private JButton buttonList = new JButton("Pokaż aktywnych");
    private JButton buttonAbout = new JButton("Autor");

    {
        menuBar.add(buttonConnect);
        menuBar.add(buttonList);
        menuBar.add(buttonAbout);

    }

    UserGui( User user) {
        super(user.getNick());
        this.user = user;
        setSize(300, 100);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(users);
        setJMenuBar(menuBar);
        setVisible(true);
        buttonConnect.addActionListener(e -> user.connect(users.getSelectedIndex()));
        buttonList.addActionListener(e -> user.getList());
        buttonAbout.addActionListener(e -> user.about());
    }

    void kill() {
        setVisible(false);
        dispose();
    }

    void addUser(String nick) {
        users.addItem(nick);
    }

    void clearUsers() {
        users.removeAll();
    }
}
