package unicesumar.aep02_01;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JTextArea;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;

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

			JSONObject obj = new JSONObject(mensagem);
			caixa.append("[Recebido] " + mensagem + "\n\n");
			if (obj.has("list")) {

				String value = obj.getString("list");

				if (value != null) {
					File raiz = new File(value);
					caixa.append("[Retorno] Arquivos do diretório " + value + "\n\n");
					for (File f : raiz.listFiles()) {
						if (f.isFile()) {
							caixa.append("[Retorno] " + f.getName() + "\n\n");
						}
					}
				}
			}
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}