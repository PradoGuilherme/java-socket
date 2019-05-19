package unicesumar.aep02_01;

import javax.swing.*;

import org.json.JSONObject;

import java.awt.Font;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client extends JFrame implements ActionListener {

	private JMenuBar barramenu = new JMenuBar();
	private JMenu menuconectar = new JMenu("Conectar");
	private JMenu menulistar = new JMenu("Listar");
	private JMenuItem menucliente = new JMenuItem("Conectar localhost...");
	private JMenuItem menudiretorio = new JMenuItem("listar diretorio...");
	private JTextArea caixa = new JTextArea(30, 50);
	private Socket conexao;
	private static final int PORT = 9092;

	public static void main(String[] args) {
		Client principal = new Client();
	}

	Client() {
		super("Cliente");
		Font font = caixa.getFont();
		float size = font.getSize() + 5.0f;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		menucliente.addActionListener(this);
		menudiretorio.addActionListener(this);
		menuconectar.add(menucliente);
		menulistar.add(menudiretorio);
		barramenu.add(menuconectar);
		barramenu.add(menulistar);
		setJMenuBar(barramenu);
		caixa.setBorder(
				BorderFactory.createCompoundBorder(caixa.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		add(caixa);
		caixa.setEditable(false);
		pack();
		setVisible(true);
	}

	public void enviaMensagem(JSONObject obj) {
		try {
			conexao = new Socket("localhost", PORT);
			caixa.append("Conectado a " + conexao.getRemoteSocketAddress() + "\n\n");
			OutputStream os = conexao.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			pw.println(obj);
			pw.close();
			conexao.close();
		} catch (IOException e) {
			caixa.append("[ERRO] " + e.getMessage() + "\n\n");
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();
		if (source == menucliente) {
				JSONObject obj = new JSONObject();
				String mensagemenviar = JOptionPane.showInputDialog(null, "Digite a mensagem a enviar:", "teste");
				obj.put("msg", mensagemenviar);
				enviaMensagem(obj);
				caixa.append("[Enviado] " + obj + "\n\n");
		} else if(source == menudiretorio) {
			 	JSONObject obj = new JSONObject();
				String mensagemenviar = JOptionPane.showInputDialog(null, "Digite o diretorio a pesquisar:", "D:\\");
				obj.put("list", mensagemenviar);
				enviaMensagem(obj);
				caixa.append("[Enviado] " + obj + "\n\n");
		}
	}
}
