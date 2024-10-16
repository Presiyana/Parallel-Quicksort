import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ForkJoinPool;

public class ClientRequestHandler implements Runnable {
    private Socket socket;

    public ClientRequestHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        Thread.currentThread().setName("Client Request Handler for " + socket.getRemoteSocketAddress());

        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String inputLine;
            while((inputLine = in.readLine()) != null) {

                int numberOfThreads = Integer.parseInt(inputLine);
                System.out.println("Number of threads received from client: " + numberOfThreads);

                String answer = in.readLine();
                System.out.println("Answer received from client.");

                int n = Integer.parseInt(in.readLine());
                System.out.println("Number of coordinates received from client: " + n);

                int[] array = new int[n];
                for(int i = 0; i < n; i++) {
                    array[i] = Integer.parseInt(in.readLine());
                }

                if(answer.equals("enter")) {
                    System.out.println("Array received from client: ");
                    for (int value : array) {
                        System.out.print(value + " ");
                    }
                }
                else {
                    System.out.println("Array received from client: ");
                    for (int i = 0; i < 200; i++) {
                        System.out.print(array[i] + " ");
                    }
                }

                long startTime = System.currentTimeMillis();

                ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);
                forkJoinPool.invoke(new QuickSort(array, 0, array.length - 1));

                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;

                for(int value: array) {
                    out.println(value);
                }

                out.println("Execution time: " + executionTime + "ms");

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
