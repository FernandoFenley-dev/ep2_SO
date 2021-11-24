public class Leitor extends Thread {

    public static int leitores = 0;
    private final int number;
    private String palavraLida;

    // --
    private Database database;

    public Leitor(Database database) {
        this.database = database;
        this.number = Leitor.leitores++;
    }

    @Override
    public void run() {
        try {
            this.database.read(this.number);
        } catch (InterruptedException ex) {
            System.err.println("Erro na execução do leitor");
        }
    }

    public String getPalavraLida() {
        return palavraLida;
    }

    public void setPalavraLida(String palavraLida) {
        this.palavraLida = palavraLida;
    }

}
