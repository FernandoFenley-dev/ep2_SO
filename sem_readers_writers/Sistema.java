import java.util.concurrent.ThreadLocalRandom;

public class Sistema extends Thread {

    private final static int TAM_MAX_ARRANJO = 100;
    private final static int TAM_REPETICAO_PROP = 50;

    private static int numeroLeitores = 100;
    private static int numeroEscritores = 0;

    static float mediaTempo;

    private Thread[] threads;
    private Database database;

    public Sistema(Database database) {
        this.database = database;
        threads = new Thread[100];
    }

    private int buscaPosicao(ThreadLocalRandom generator) {
        int pos = generator.nextInt(100);
        while (threads[pos] != null) { // verifica se a posição no arranjo de threads já está ocupada
            pos = generator.nextInt(100);
        }
        return pos;
    }

    private void populaObjetoThreads(int qntdLeitores, int qntdEscritores) {
        int totalObjetos = qntdLeitores + qntdEscritores;
        if (totalObjetos != 100) {
            System.out.println("Quantidade de objetos inválida, o total de objetos deve ser igual a 100");
            return;
        }

        ThreadLocalRandom generator = ThreadLocalRandom.current();

        for (int i = 0; i < qntdLeitores; i++) {
            int pos = buscaPosicao(generator);
            threads[pos] = new Leitor(database);
        }

        for (int i = qntdLeitores; i < totalObjetos; i++) {
            int pos = buscaPosicao(generator);
            threads[pos] = new Escritor(database);
        }
    }

    private void executaThreads() {
        for (int i = 0; i < threads.length; i++) {
            if (threads[i] != null) {
                threads[i].start();
            } else {
                System.out.println("Thread não existe");
            }
        }
        for (int i = 0; i < threads.length; i++) {
            if (threads[i] != null) {
                try {
                    threads[i].join();
                } catch (InterruptedException ex) {
                    System.err.println("Erro join");
                }
            } else {
                System.out.println("Thread não existe");
            }
        }
    }

    @Override
    public void run() {
        long horaInicioPrograma = System.currentTimeMillis();

        for (int i = 0; i <= TAM_MAX_ARRANJO; i++) {

            for (int j = 0; j < TAM_REPETICAO_PROP; j++) {

                Sistema sis = new Sistema(database);
                sis.populaObjetoThreads(numeroLeitores, numeroEscritores);
                long horaInicioThread = System.currentTimeMillis();
                sis.executaThreads();
                long horaFim = System.currentTimeMillis();

                mediaTempo += horaFim - horaInicioThread;
                //System.out.println("\n" + j + "\n");
            }

            mediaTempo /= TAM_REPETICAO_PROP;
            System.out.println("leitores: " + numeroLeitores + " escritores " + numeroEscritores + " Média: " + mediaTempo);

            numeroEscritores++;
            numeroLeitores--;
            mediaTempo = 0;
        }

        long fimPrograma = System.currentTimeMillis();
        System.out.println("Tempo de execução: " + ((fimPrograma - horaInicioPrograma)/60000) + " minutos");

    }
}
