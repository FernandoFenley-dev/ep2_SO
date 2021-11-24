public class Escritor extends Thread {

    public static int escritores = 0;
    private final int number;

    // --
    private Database database;

    public Escritor(Database database) {
        this.database = database;
        this.number = Escritor.escritores++;
    }

    @Override
    public void run() {
        try {
            this.database.write(this.number);
        } catch (InterruptedException ex) {
            System.err.println("Erro na execução do escritor");
        }
    }

}
