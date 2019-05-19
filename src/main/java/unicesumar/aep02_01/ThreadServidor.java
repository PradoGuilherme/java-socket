package unicesumar.aep02_01;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JTextArea;

public class ThreadServidor extends Thread {

	private Socket socket;
	private JTextArea caixa;

	public ThreadServidor(Socket socket, JTextArea caixa) {
		this.socket = socket;
		this.caixa = caixa;
	}

	@Override
	public void run() {
		String mensagem = "";
		try {
			BufferedReader leitor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			mensagem = leitor.readLine();
			caixa.append("[Recebido] " + mensagem + "\n");
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}