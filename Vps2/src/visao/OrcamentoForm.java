package visao;

import java.awt.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controle.OrcamentoProcess;
import modelo.Orcamento;

public class OrcamentoForm extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel painel;
	private JLabel id, fornecedor, produto, preco, rotulos;
	private JTextField tfId, tfFornecedor, tfproduto, tfpreco;

	private JScrollPane rolagem;
	private JTextArea verResultados;
	private JButton create, read, update, delete;
	private int autoId = OrcamentoProcess.orcamentos.size() + 1;
	private String texto = "";

	private final Locale BRASIL = new Locale("pt", "BR");
	private DecimalFormat df = new DecimalFormat("#.00");

	OrcamentoForm() {
		setTitle("Registro de or�amento");
		setBounds(100, 100, 500, 500);
		setBackground(new Color(0, 191, 255));
		painel = new JPanel();
		painel.setBackground(new Color(105, 255, 100));
		setContentPane(painel);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);

		id = new JLabel("Id:");
		id.setBounds(20, 20, 120, 30);
		painel.add(id);
		fornecedor = new JLabel("Fornecedor:");
		fornecedor.setBounds(20, 55, 120, 30);
		painel.add(fornecedor);
		produto = new JLabel("Produto:");
		produto.setBounds(20, 90, 120, 30);
		painel.add(produto);
		preco = new JLabel("Pre�o:");
		preco.setBounds(20, 125, 120, 30);
		painel.add(preco);
		rotulos = new JLabel("Id | Fornecedor | Produto | Pre�o");
		rotulos.setBounds(20, 230, 500, 30);
		painel.add(rotulos);

		tfId = new JTextField(String.format("%d", autoId));
		tfId.setEditable(false);
		tfId.setBounds(140, 25, 140, 30);
		painel.add(tfId);
		
		tfFornecedor = new JTextField();
		tfFornecedor.setBounds(140, 60, 140, 30);
		painel.add(tfFornecedor);
		
		tfproduto = new JTextField();
		tfproduto.setBounds(140, 95, 140, 30);
		painel.add(tfproduto);
		
		tfpreco = new JTextField();
		tfpreco.setBounds(140, 130, 140, 30);
		painel.add(tfpreco);
		
		verResultados = new JTextArea();
		verResultados.setEditable(false);
		verResultados.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		preencherAreaDeTexto();
		rolagem = new JScrollPane(verResultados);
		rolagem.setBounds(20, 250, 440, 200);
		painel.add(rolagem);

		create = new JButton("Cadastrar");
		read = new JButton("Buscar");
		update = new JButton("Atualizar");
		delete = new JButton("Excluir");
		
		create.setBounds(330, 25, 110, 40);
		create.setBackground(new Color(0, 191, 255));
		create.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		read.setBounds(330, 75, 110, 40);
		read.setBackground(new Color(0, 191, 255));
		read.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		update.setBounds(330, 125, 110, 40);
		update.setBackground(new Color(0, 191, 255));
		delete.setBounds(330, 175, 110, 40);
		delete.setBackground(new Color(0, 191, 255));
		
		update.setEnabled(false);
		delete.setEnabled(false);
		painel.add(create);
		painel.add(read);
		painel.add(update);
		painel.add(delete);

		tfFornecedor.addActionListener(this);
		create.addActionListener(this);
		read.addActionListener(this);
		update.addActionListener(this);
		delete.addActionListener(this);

	}

	private void cadastrar() {
		if (tfproduto.getText().length() != 0 && tfpreco.getText().length() != 0) {

			df.setCurrency(Currency.getInstance(BRASIL));

			OrcamentoProcess.orcamentos.add(
					new Orcamento(autoId, tfFornecedor.getText(), tfproduto.getText(), tfpreco.getText()));
			autoId++;
			preencherAreaDeTexto();
			limparCampos();
			OrcamentoProcess.salvar();
		} else {
			JOptionPane.showMessageDialog(this, "Favor preencher todos os campos.");
		}
	}

	private void limparCampos() {
		tfFornecedor.setText(null);
		tfproduto.setText(null);
		tfpreco.setText(null);
		tfId.setText(String.format("%d", autoId));
	}

	private void preencherAreaDeTexto() {
		texto = "";
		for (Orcamento orcamento : OrcamentoProcess.orcamentos) {
			OrcamentoProcess.compararPrecos(orcamento.getproduto());
		}
		for (Orcamento p : OrcamentoProcess.orcamentos) {
			texto += p.toString();
		}
		verResultados.setText(texto);
	}

	private void buscar() {
		String entrada = JOptionPane.showInputDialog(this, "Id do Or�amento:");

		boolean isNumeric = true;
		if (entrada != null) {
			for (int i = 0; i < entrada.length(); i++) {
				if (!Character.isDigit(entrada.charAt(i))) {
					isNumeric = false;
				}
			}
		} else {
			isNumeric = false;
		}
		if (isNumeric) {
			int id = Integer.parseInt(entrada);
			Orcamento ID = new Orcamento(id);
			if (OrcamentoProcess.orcamentos.contains(ID)) {
				int indice = OrcamentoProcess.orcamentos.indexOf(ID);
				tfId.setText(OrcamentoProcess.orcamentos.get(indice).getId(""));
				tfFornecedor.setText(OrcamentoProcess.orcamentos.get(indice).getfornecedor());
				tfproduto.setText(OrcamentoProcess.orcamentos.get(indice).getproduto());
				tfpreco.setText(String.valueOf(OrcamentoProcess.orcamentos.get(indice).getpreco()));

				create.setEnabled(false);
				update.setEnabled(true);
				delete.setEnabled(true);
				OrcamentoProcess.salvar();
			} else {
				JOptionPane.showMessageDialog(this, "Or�amento n�o encontrado");
			}
		}
		preencherAreaDeTexto();

	}

	private void alterar() {
		int id = Integer.parseInt(tfId.getText());
		Orcamento ID = new Orcamento(id);
		int indice = OrcamentoProcess.orcamentos.indexOf(ID);
		if (tfproduto.getText().length() != 0 && tfpreco.getText().length() != 0) {

			df.setCurrency(Currency.getInstance(BRASIL));

			OrcamentoProcess.orcamentos.set(indice,
					new Orcamento(id, tfFornecedor.getText().toString(), tfproduto.getText(), tfpreco.getText()));
			preencherAreaDeTexto();
			limparCampos();
		} else {
			JOptionPane.showMessageDialog(this, "Favor preencher todos os campos.");
		}

		create.setEnabled(true);
		update.setEnabled(false);
		delete.setEnabled(false);
		tfId.setText(String.format("%d", autoId));
		OrcamentoProcess.salvar();
		preencherAreaDeTexto();
	}

	private void excluir() {
		int id = Integer.parseInt(tfId.getText());
		Orcamento m = new Orcamento(id);
		int indice = OrcamentoProcess.orcamentos.indexOf(m);
		OrcamentoProcess.orcamentos.remove(indice);
		preencherAreaDeTexto();
		limparCampos();
		create.setEnabled(true);
		update.setEnabled(false);
		delete.setEnabled(false);
		tfId.setText(String.format("%d", autoId));
		OrcamentoProcess.salvar();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == create) {
			cadastrar();
		}
		if (e.getSource() == read) {
			buscar();
		}
		if (e.getSource() == update) {
			alterar();
		}
		if (e.getSource() == delete) {
			excluir();
		}

	}

	public static void main(String[] agrs) throws ParseException {

		OrcamentoProcess.abrir();
		new OrcamentoForm().setVisible(true);
	}

}