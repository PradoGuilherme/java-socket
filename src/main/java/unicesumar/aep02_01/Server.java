package unicesumar.aep02_01;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;

public class Server extends JFrame {
	
	private JMenuBar barramenu = new JMenuBar();
	private JMenu menuconectar = new JMenu("Servidor");
	private JTextArea caixa = new JTextArea(30, 50);
	private Socket conexao;
	private static final int PORT = 9092;
	
	public static void main(String[] args) {
		Server servidor = new Server();
		servidor.start();
	}
	
	Server() {
		super("Servidor");
		Font font = caixa.getFont();
		float size = font.getSize() + 5.0f;
		caixa.setFont( font.deriveFont(size) );
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		caixa.setBorder(BorderFactory.createCompoundBorder(
				caixa.getBorder(), 
		        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		caixa.setEditable(false);
		add(caixa);
		pack();
		setVisible(true);
	}

	public void start() {
		System.out.println("Inicializando o servidor...");

		try {
			ServerSocket socket = new ServerSocket(PORT);
			socket.setReuseAddress(true);
			caixa.append("Servidor ouvindo na porta " + PORT + "\n\n");

			while (true) {
				Socket toClient = socket.accept();
	            caixa.append("Conectado a " + toClient.getRemoteSocketAddress() + "\n\n");
				new ThreadServidor(toClient, caixa).start();
			}
		} catch (IOException e) {
			caixa.append("[ERRO] " + e.getMessage() + "\n\n");
			e.printStackTrace();
		}
		System.out.println("Servidor finalizado.");

	}
}
