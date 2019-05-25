package unicesumar.aep02_01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class AppServer {

	String arquivos = "";

	public static void main(String[] args) {
		AppServer server = new AppServer();
		try {
			server.executar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void executar() throws Exception {
		System.out.println("[server] listening...");
		ServerSocket serverSocket = new ServerSocket(8080);
		String comando = "";
		while (true) {
			Socket clientSocket = serverSocket.accept();
			PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream());
			Scanner fromClient = new Scanner(clientSocket.getInputStream());
			toClient.println("Olá client! Hoje é:" + (new Date().toString()));
			while (!comando.equals("disconnect")) {
				toClient.flush();
				comando = fromClient.nextLine();
				if (comando.startsWith("ls")) {
					arquivos = "";
					String[] partesDoComando = comando.split(" ");
					String nomeDoDiretorio = partesDoComando[1];
					String parametros = partesDoComando[2].replaceAll("-", "");
					File diretorio = new File(nomeDoDiretorio);
					boolean recursivo = parametros.equals("r");
					String listagem = listarArquivos(diretorio, recursivo);
					toClient.println(listagem);
					toClient.flush();
				}
				if (comando.startsWith("exclude")) {
					String[] partesDoComando = comando.split(" ");
					String nomeDoDiretorio = partesDoComando[1];
					File diretorio = new File(nomeDoDiretorio);
					String retornoExclusao = excludeDiretorio(diretorio);
					toClient.println(retornoExclusao);
					toClient.flush();
				}
				if (comando.startsWith("mkdir")) {
					String[] partesDoComando = comando.split(" ");
					String nomeDoDiretorio = partesDoComando[1];
					File diretorio = new File(nomeDoDiretorio);
					String retornoCreate = createDiretorio(diretorio);
					toClient.println(retornoCreate);
					toClient.flush();
				}
				if (comando.startsWith("sendFile")) {
					String[] partesDoComando = comando.split(" ");
					String nomeDoArquivo = partesDoComando[1];
					String nomeDoDiretorio = partesDoComando[2];
					File diretorio = new File(nomeDoDiretorio);
					File arquivo = new File(nomeDoArquivo);
					
					String retornoCreate = createArquivo(diretorio, arquivo);
					toClient.println(retornoCreate);
					toClient.flush();
				}

			}
		}
	}

	private String createArquivo(File diretorio, File arquivo) {
		FileOutputStream out;
		try {
			out = new FileOutputStream(arquivo);
			out.write(5);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String createDiretorio(File diretorio) {
		if (!diretorio.exists()) {
			System.out.println("creating directory: " + diretorio.getName());
			boolean created = false;

			try {
				diretorio.mkdir();
				created = true;
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (created)
				return "Diretorio criado com sucesso.";
		}
		return "Diretorio já existe.";
	}

	private String excludeDiretorio(File diretorio) {
		try {
			if ((diretorio.exists()) && (diretorio.isDirectory())) {
				File[] listofFiles = diretorio.listFiles();
				if (listofFiles.length == 0) {
					diretorio.delete();
					return "Pasta :: " + diretorio.getAbsolutePath() + " foi deletada.";
				} else {
					return "Pasta :: " + diretorio.getAbsolutePath() + " não foi deletada pois existe arquivos.";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ("Não foi possível deletar pasta. Erro: " + e.getLocalizedMessage());
		}
		return "";
	}

	private String listarArquivos(File diretorio, boolean recursivo) {
		for (final File fileEntry : diretorio.listFiles()) {
			if (fileEntry.isDirectory()) {
				arquivos += "DIRETORIO => " + fileEntry.getName().concat(" ");
				if (recursivo) {
					listarArquivos(new File(fileEntry.getPath()), recursivo);
				}
			} else {
				arquivos += "ARQUIVO => " + fileEntry.getName().concat(" ");
			}
		}
		return arquivos;
	}
}