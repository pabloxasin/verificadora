package ec;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class FrmCinta extends JFrame
{
	private JPanel contentPane;
	private JTextField txtCodigo;
	private JTextField txtChips;
	private JTextField txtChipsOk;
	ClsBBDD conexion = new ClsBBDD();
	JScrollPane scpCinta;
	private JTable tbCintas;
	private JTextField txtNumCinta;
	ClsParametros parametros=new ClsParametros();
	DefaultTableModel modelo = new DefaultTableModel(){
		@Override
		public boolean isCellEditable(int fila, int columna){
			if(columna==5)
			{
				return true;
			}	
			else
			{
				return false; 
			}  		  
		}
	};;


	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					FrmCinta frame = new FrmCinta();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public FrmCinta()
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\maquinaBT\\utilitarios\\imagen\\ICONO.png"));
		setResizable(false);
		setTitle("Ingreso de datos de Cintas");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 629, 428);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblCdigo = new JLabel("Código:");
		lblCdigo.setForeground(new Color(0, 0, 0));
		lblCdigo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCdigo.setBounds(48, 94, 46, 14);
		contentPane.add(lblCdigo);

		JLabel lblTotalChips = new JLabel("Total Chips:");
		lblTotalChips.setForeground(new Color(0, 0, 0));
		lblTotalChips.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTotalChips.setBounds(28, 122, 67, 14);
		contentPane.add(lblTotalChips);

		txtCodigo = new JTextField();
		txtCodigo.addFocusListener(new FocusAdapter()
		{
			public void focusLost(FocusEvent arg0)
			{
				conexion.BuscarRegistro("select * from tbcinta where codigo like '" + txtCodigo.getText() + "' or num_cinta like '"+txtNumCinta.getText()+"'", txtCodigo);
			}
		});
		txtCodigo.setColumns(10);
		txtCodigo.setBounds(97, 91, 199, 20);
		contentPane.add(txtCodigo);

		txtChips = new JTextField();
		txtChips.addKeyListener(new KeyAdapter()
		{
			public void keyTyped(KeyEvent arg0)
			{
				conexion.ValidarNumero(arg0);
			}
		});
		txtChips.setColumns(10);
		txtChips.setBounds(98, 119, 198, 20);
		contentPane.add(txtChips);

		txtChipsOk = new JTextField();
		txtChipsOk.addKeyListener(new KeyAdapter()
		{
			public void keyTyped(KeyEvent arg0)
			{
				conexion.ValidarNumero(arg0);
			}
		});
		txtChipsOk.setColumns(10);
		txtChipsOk.setBounds(98, 147, 198, 20);
		contentPane.add(txtChipsOk);


		final JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if ((txtNumCinta.getText().isEmpty()) || (txtChips.getText().isEmpty()) || (txtChipsOk.getText().isEmpty()) || (txtCodigo.getText().isEmpty()) || (Integer.parseInt(txtChips.getText()) < Integer.parseInt(txtChipsOk.getText())))
				{
					JOptionPane.showMessageDialog(null, "revice los datos ingresados");
					encerar();
				}
				else
				{
					String insertarCinta = "insert into tbcinta (`codigo`,`total_chips`,`total_buenos`,`estado`,`fecha_registro`,`Num_cinta`) VALUES('" + 
							txtCodigo.getText() + "','" + txtChips.getText() + "','" + txtChipsOk.getText() + "','Abierta',now()," + txtNumCinta.getText() + ")";
					conexion.Insertar(insertarCinta);
					conexion.blanquearTabla(tbCintas, modelo);
					conexion.cargaTabla(tbCintas, "select Num_cinta,codigo,Total_chips,Total_buenos,Estado from tbcinta", modelo);
					encerar();
				}
			}
		});
		btnGuardar.setFont(new Font("Tahoma", 0, 11));
		btnGuardar.setBounds(329, 88, 89, 23);
		contentPane.add(btnGuardar);

		JButton btnModificar = new JButton("Modificar");
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if ((txtNumCinta.getText().isEmpty()) || (txtChips.getText().isEmpty()) || (txtChipsOk.getText().isEmpty()) || (txtCodigo.getText().isEmpty()) || (Integer.parseInt(txtChips.getText()) < Integer.parseInt(txtChipsOk.getText())))
				{
					JOptionPane.showMessageDialog(null, "revice los datos ingresados");
					encerar();
				}
				else
				{
					String modificaCinta="update tbcinta set codigo= '"+txtCodigo.getText()+"',"
							+ " total_chips='"+txtChips.getText()+"',total_buenos='"+txtChipsOk.getText()+"' where num_cinta='"+txtNumCinta.getText()+"' and estado='Abierta' ";
					System.out.println(modificaCinta);
					conexion.Modificar(modificaCinta);
					conexion.blanquearTabla(tbCintas, modelo);
					conexion.cargaTabla(tbCintas, "select Num_cinta,codigo,Total_chips,Total_buenos,Estado from tbcinta", modelo);
					encerar();
				}
				
			}
		});
		btnModificar.setFont(new Font("Tahoma", 0, 11));
		btnModificar.setBounds(329, 120, 89, 23);
		contentPane.add(btnModificar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtNumCinta.setEnabled(true);
				btnGuardar.setEnabled(true);
				encerar();
			}
		});
		btnCancelar.setFont(new Font("Tahoma", 0, 11));
		btnCancelar.setBounds(329, 153, 89, 23);
		contentPane.add(btnCancelar);

		JLabel lblDatosDeCintas = new JLabel("REGISTRO DE CINTAS");
		lblDatosDeCintas.setForeground(new Color(0, 0, 0));
		lblDatosDeCintas.setHorizontalAlignment(0);
		lblDatosDeCintas.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblDatosDeCintas.setBounds(4, 22, 414, 23);
		contentPane.add(lblDatosDeCintas);

		JLabel lblTotalChipsOk = new JLabel("Total Chips Ok:");
		lblTotalChipsOk.setForeground(new Color(0, 0, 0));
		lblTotalChipsOk.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTotalChipsOk.setBounds(11, 151, 84, 14);
		contentPane.add(lblTotalChipsOk);

		JScrollPane scpCinta = new JScrollPane();
		scpCinta.setBounds(28, 242, 395, 126);
		contentPane.add(scpCinta);

		tbCintas = new JTable();
		tbCintas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				txtNumCinta.setEnabled(false);
				cargarTxtTabla(tbCintas);
				btnGuardar.setEnabled(false);
				
			}
		});
		tbCintas.setFont(new Font("Tahoma", 0, 10));
		scpCinta.setViewportView(tbCintas);


		modelo.setColumnIdentifiers(new Object[] { "<html><p style=\"color:blue; font:9px;\">Num_cinta</p></html>", 
				"<html><p style=\"color:blue; font:9px;\">Código</p></html>", 
				"<html><p style=\"color:blue; font:9px;\">Total Chips</p></html>", 
				"<html><p style=\"color:blue; font:9px;\">Total Buenos</p></html>", 
		"<html><p style=\"color:blue; font:9px;\">Estado</p></html>" });
		conexion.cargaTabla(tbCintas, "select Num_cinta,codigo,Total_chips,Total_buenos,Estado from tbcinta", modelo);

		JLabel lblNmeroDeCinta = new JLabel("Num cinta:");
		lblNmeroDeCinta.setForeground(new Color(0, 0, 0));
		lblNmeroDeCinta.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNmeroDeCinta.setBounds(33, 66, 59, 14);
		contentPane.add(lblNmeroDeCinta);

		txtNumCinta = new JTextField();
		txtNumCinta.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent arg0)
				{
					conexion.BuscarRegistro("select * from tbcinta where num_cinta like '"+txtNumCinta.getText()+"'", txtNumCinta);
				}			
		});
		txtNumCinta.addKeyListener(new KeyAdapter()
		{
			public void keyTyped(KeyEvent e)
			{
				conexion.ValidarNumero(e);
			}
		});
		txtNumCinta.setColumns(10);
		txtNumCinta.setBounds(97, 63, 76, 20);
		contentPane.add(txtNumCinta);
		
		JLabel lblParaModificarUna = new JLabel("Para modificar una cinta que este  Abierta, haga doble clic en la cinta:");
		lblParaModificarUna.setForeground(new Color(0, 0, 0));
		lblParaModificarUna.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblParaModificarUna.setBounds(28, 206, 390, 14);
		contentPane.add(lblParaModificarUna);
		
		JLabel lblCinta = new JLabel("");
		lblCinta.setForeground(new Color(245, 255, 250));
		lblCinta.setBounds(-1, 0, 627, 398);
		parametros.SetImagenLabel(lblCinta, "C:\\maquinaBT\\utilitarios\\imagen\\cinta.png");
		contentPane.add(lblCinta);
	}

	public String toString()
	{
		return 
				"FrmCinta [txtCodigo=" + txtCodigo + ", txtChips=" + txtChips + ", txtChipsOk=" + txtChipsOk + ", tbCintas=" + tbCintas + "]";
	}

	public JTextField getTxtCodigo()
	{
		return txtCodigo;
	}

	public void setTxtCodigo(JTextField txtCodigo)
	{
		txtCodigo = txtCodigo;
	}

	public JTextField getTxtChips()
	{
		return txtChips;
	}

	public void setTxtChips(JTextField txtChips)
	{
		txtChips = txtChips;
	}

	public JTextField getTxtChipsOk()
	{
		return txtChipsOk;
	}

	public void setTxtChipsOk(JTextField txtChipsOk)
	{
		txtChipsOk = txtChipsOk;
	}

	public void encerar()
	{
		txtChips.setText("");
		txtChipsOk.setText("");
		txtCodigo.setText("");
		txtNumCinta.setText("");
	}
	public void cargarTxtTabla(JTable celdaSeleccionada)
	{
		int fila = tbCintas.getSelectedRow();
		String num_cinta = String.valueOf(celdaSeleccionada.getValueAt(fila,0));
		String codigo = String.valueOf(celdaSeleccionada.getValueAt(fila,1));
		String chips = String.valueOf(celdaSeleccionada.getValueAt(fila,2));
		String chipsOk = String.valueOf(celdaSeleccionada.getValueAt(fila,3));// por ultimo, obtengo el valor de la celda
		txtNumCinta.setText(num_cinta);
		txtCodigo.setText(codigo);
		txtChips.setText(chips);
		txtChipsOk.setText(chipsOk);
	}
}
