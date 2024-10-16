import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Client {
    private static final int SERVER_PORT = 6666;

    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", SERVER_PORT);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            Thread.currentThread().setName("Client thread" + socket.getLocalPort());

            System.out.println("Connected to the server!");

            int n = 2_000_000;
            int[] originalArray = new int[n];
            Random random = new Random();
            for (int i = 0; i < n; i++) {
                 originalArray[i] = random.nextInt(10_000_001);
             }

            while(true) {
                System.out.println("Enter number of threads or type 0 to exit: ");

                int numberOfThreads = scanner.nextInt();
                if(numberOfThreads == 0) {
                    break;
                }

                System.out.println("Sending number of threads<" + numberOfThreads + "> to the server...");
                writer.println(numberOfThreads);

                System.out.println("Do you want to enter the array(length and coordinates) or want to generate? (enter/generate):");
                scanner.nextLine();
                String answer = scanner.nextLine();
                System.out.println("Sending answer to the server...");
                writer.println(answer);

                int[] array;

                if(answer.equals("enter")) {
                    System.out.println("Enter the length of the Array: ");
                    n = scanner.nextInt();
                    array = new int[n];

                    System.out.println("Enter the coordinates of the array: ");
                    for (int i = 0; i < n; i++) {
                        array[i] = scanner.nextInt();
                    }
                }
                else {
                    array = Arrays.copyOf(originalArray,originalArray.length);
                }

                System.out.println("Sending the length of the array <" + n + "> to the server...");
                writer.println(n);

                System.out.println("Sending the coordinates of the array to the server...");
                for (int value : array) {
                    writer.println(value);
                }

                int[] sortedData = new int[n];
                for (int i = 0; i < n; i++) {
                    sortedData[i] = Integer.parseInt(reader.readLine());
                }

                if(answer.equals("enter")) {
                    System.out.println("Sorted coordinates: ");
                    for (int value : sortedData) {
                        System.out.print(value + " ");
                    }
                }
                else {
                    System.out.println("Sorted coordinates:");
                    for (int i = 0; i < 200; i++) {
                        System.out.print(sortedData[i] + " ");
                    }
                }

                String executionTime = reader.readLine();
                System.out.println("\n" + executionTime);
            }

        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }
}

