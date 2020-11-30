
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cadua
 */
public class ClienteSocket {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    
    public ClienteSocket(Socket socket) throws IOException{
        this.socket = socket;
        
        System.out.println("Cliente " + socket.getRemoteSocketAddress() + " conectou");
        
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }
    
    public SocketAddress getRemoteSocketAddress(){
        return socket.getRemoteSocketAddress();
    }
    
    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException ex) {
            System.out.println("Erro ao fechar o socket"+ ex.getMessage());
        }
        
    }
    
    public String getMessage() {
        try {
            return in.readLine();
        } catch (IOException ex) {
            return null;
        }
    }
    
    public boolean sendMsg(String msg) {
        out.println(msg);
        return !out.checkError();
    }
}
