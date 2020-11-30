import java.io.IOException;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class ChatServer {
    
    public static final int PORT = 7777;
    private ServerSocket serverSocket;
    private final List<ClienteSocket> clients = new LinkedList<>();
    
    public void start() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Server iniciado na porta: " + PORT);
        clientConnectionLoop();
    }
    
    private void clientConnectionLoop() throws IOException{
        while(true) {
            ClienteSocket clientSocket = new ClienteSocket(serverSocket.accept()); 
            clients.add(clientSocket);  
            new Thread(() -> clientMessageLoop(clientSocket)).start();
        }
    }
    
    private void clientMessageLoop(ClienteSocket clientSocket) {
        String msg;
        try {
            while((msg = clientSocket.getMessage()) != null){
                if("sair".equalsIgnoreCase(msg))
                    return;
                System.out.printf("Msg recebida do cliente %s: %s ", clientSocket.getRemoteSocketAddress(), msg);
                sendMsgToAll(clientSocket, msg);
            }
        } finally {
            clientSocket.close();
        }
    }
    
    private void sendMsgToAll(ClienteSocket sender, String msg) {
        Iterator<ClienteSocket> iterator = clients.iterator();
        while(iterator.hasNext()){
            ClienteSocket clienteSocket = iterator.next();
            if(!sender.equals(clienteSocket)){
                if(!clienteSocket.sendMsg("Cliente " + sender.getRemoteSocketAddress() + ": " + msg)){
                    iterator.remove();
                }
            }
               
        }
    }

    public static void main(String[] args){
        try {
            ChatServer server = new ChatServer();
            server.start();
        } catch (IOException ex) {
            System.out.println("Erro ao iniciar o servidor: "+ex.getMessage());
        }
        
        System.out.println("Servidor finalizado");
   }
}
