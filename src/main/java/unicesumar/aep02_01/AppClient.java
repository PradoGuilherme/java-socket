package unicesumar.aep02_01;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class AppClient {

	public static void main(String[] args) {
		AppClient client = new AppClient();
		try {
			client.executar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void executar() throws Exception {
		Socket socket = null;
		Scanner leitorTeclado = new Scanner(System.in);
		PrintWriter toServer = null;
		Scanner fromServer = null;
		String comando = null;
		String resposta = null;
		do {
			System.out.print(">>");
			comando = leitorTeclado.nextLine();
			if (comando.startsWith("connect")) {
				String[] partesDoComando = comando.split(" ");
				String endereco = partesDoComando[1];
				int porta = Integer.parseInt(partesDoComando[2]);
				socket = new Socket(endereco, porta);
				toServer = new PrintWriter(socket.getOutputStream());
				fromServer = new Scanner(socket.getInputStream());
				System.out.println(fromServer.nextLine() + "\n");
			} else if (comando.equals("disconnect")) {
				if (socket != null && socket.isConnected()) {
					toServer.println(comando);
					toServer.flush();
					socket.close();
				}
			} else if (comando.startsWith("ls")) {
				toServer.println(comando);
				toServer.flush();
				resposta = fromServer.nextLine();
				System.out.println(resposta + "\n");
			} else if (comando.startsWith("exclude")) {
				toServer.println(comando);
				toServer.flush();
				resposta = fromServer.nextLine();
				System.out.println(resposta + "\n");
			} else if (comando.startsWith("mkdir")) {
				toServer.println(comando);
				toServer.flush();
				resposta = fromServer.nextLine();
				System.out.println(resposta + "\n");
			} else if (comando.startsWith("sendFile")) {
				toServer.println(comando);
				toServer.flush();
				resposta = fromServer.nextLine();
				System.out.println(resposta + "\n");
			} else {
				System.out.println("Comando não encontrado!" + "\n");
			}
		} while (!comando.equals("sair"));
		System.out.println("Tau!" + "\n");
	}

}
