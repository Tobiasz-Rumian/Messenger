/*
 *  Klasa Test
 *  Uruchamia serwer oraz dwóch użytkowników
 *
 *  @author Tobiasz Rumian
 *  @version 1.0
 *   Data: 06 Styczeń 2017 r.
 *   Indeks: 226131
 *   Grupa: śr 13:15 TN
 */


class Tester {

    public static void main(String[] args) {
        new Server();

        try {
            Thread.sleep(1000);
        } catch (Exception ignored) {}

        new User("Ewa");

        new User("Adam");
    }

}

