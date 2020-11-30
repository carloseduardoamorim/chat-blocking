
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class ChatClient implements Runnable {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private ClienteSocket clientSocket;
    private Scanner scanner;

    public ChatClient() {
        scanner = new Scanner(System.in);
    }
    
    public void start() throws IOException {
        try {
            clientSocket = new ClienteSocket(new Socket(SERVER_ADDRESS, ChatServer.PORT));
            System.out.println("Clente conectado ao servidor em " + SERVER_ADDRESS + ":" + ChatServer.PORT);
            new Thread(this).start();
            menssageLoop();
        } finally {
            clientSocket.close();
        }
    }
    
    @Override
    public void run() {
        String msg;
        while ((msg = clientSocket.getMessage()) != null){
            System.out.printf("Mensagem recebida do servidor: %s\n", msg);
        }
        
    }
    
    private void menssageLoop() throws IOException {
        String msg;
        do {
            System.out.print("digite uma mensagem (ou sair para finalizar): ");
            msg = scanner.nextLine();
            clientSocket.sendMsg(msg);
        } while(!msg.equalsIgnoreCase("sair"));
    }
    
    public static void main(String[] args) {
        
        try {
            ChatClient client = new ChatClient();
            client.start();
        } catch (IOException ex) {
            System.out.println("Erro ao iniciar cliente: " + ex.getMessage());
        }
        System.out.println("Cliente finalizado");
    }
    
}
