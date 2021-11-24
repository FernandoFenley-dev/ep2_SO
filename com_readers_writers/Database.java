import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Database {

    private List<String> list;
    private int readers; // number of active readers

    public Database() {
        list = new ArrayList<String>();
        this.readers = 0;
    }

    public void carregaEstruturaRAM() throws IOException {
        File arq = new File("./dados/bd.txt"); // arquivo alvo

        if (arq.isFile()) {
            try (BufferedReader br = new BufferedReader(new FileReader(arq))) {
                String linhaArq;

                while ((linhaArq = br.readLine()) != null) {
                    list.add(linhaArq);
                }
            } catch (Exception e) {
                System.out.println("Erro na leitura dos arquivos");
            }
        }
    }

    public List<String> getList() {
        return list;
    }

    void read(int number) throws InterruptedException {
        synchronized (this) {
            this.readers++;
            //System.out.println("Reader " + number + " starts reading.");
        }

        ThreadLocalRandom generator = ThreadLocalRandom.current();
        String palavraLida;

        for (int i = 0; i < 100; i++) {
            int pos = generator.nextInt(100);

            palavraLida = this.getList().get(pos);
        }

        Thread.sleep(1);

        synchronized (this) {
            //System.out.println("Reader " + number + " stops reading.");
            this.readers--;
            if (this.readers == 0) {
                this.notifyAll();
            }
        }

    }

    public synchronized void write(int number) throws InterruptedException {
        while (this.readers != 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
        }
        //System.out.println("Writer " + number + " starts writing.");

        ThreadLocalRandom generator = ThreadLocalRandom.current();

        for (int i = 0; i < 100; i++) {
            int pos = generator.nextInt(100);
            this.getList().set(pos, "MODIFICADO");
        }
        Thread.sleep(1);

        //System.out.println("Writer " + number + " stops writing.");
        this.notifyAll();
    }
}
