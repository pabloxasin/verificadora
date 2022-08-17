package ec;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.smartcardio.CardException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.beans.PropertyChangeEvent;
import java.awt.Font;
import javax.swing.JTextArea;
import java.awt.Color;

public class FrmBuscaSerial extends JFrame {

	private JPanel contentPane;
	ClsBBDD conexionBase=new ClsBBDD();
	ClsLector conexionLector= new ClsLector();
	private JRadioButton rdbSi;
	private JRadioButton rdbNo;
	private JLabel lblConoceElSerial;
	private JTextField txtSiSerial;
	private JTextField txtNoSerial;
	private JButton btnBuscar;
	JTextArea txaResultado = new JTextArea();
	ButtonGroup grupoRadio;
	private JLabel lblResultadoDeLa;
	ClsParametros parametros=new ClsParametros(); 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmBuscaSerial frame = new FrmBuscaSerial();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FrmBuscaSerial() {
		setResizable(false);
		setTitle("BUSQUEDA DE SERIAL");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 401, 423);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnLeerSerial = new JButton("Leer Serial");
		btnLeerSerial.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnLeerSerial.setEnabled(false);
		btnLeerSerial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//System.out.println("serial "+conexionLector.getSerial());
					String serial=conexionLector.getSerial();
					if (serial.length()==16)
					{
						txtNoSerial.setText(serial);
					}
					else 				
					{
						JOptionPane.showMessageDialog(null,"No existe tarjeta en el lector \n   o el chip no responde ");
					}
				} catch (CardException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,"Conecte un Lector RFID"); 
					//e1.printStackTrace();
				}
			}
		});
		btnLeerSerial.setBounds(107, 148, 109, 23);
		contentPane.add(btnLeerSerial);

		rdbSi = new JRadioButton("Si");		
		rdbSi.setFont(new Font("Tahoma", Font.BOLD, 11));
		rdbSi.setOpaque(false);
		rdbSi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txaResultado.setText("");
				txtNoSerial.setText("");
				txtSiSerial.setEnabled(true);
				txtNoSerial.setEnabled(false);
				btnLeerSerial.setEnabled(false);
			}
		});
		rdbSi.setBounds(52, 114, 44, 23);
		contentPane.add(rdbSi);

		rdbNo = new JRadioButton("No");
		rdbNo.setFont(new Font("Tahoma", Font.BOLD, 11));
		rdbNo.setOpaque(false);
		rdbNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txaResultado.setText("");
				txtSiSerial.setText("");
				txtSiSerial.setEnabled(false);
				btnLeerSerial.setEnabled(true);
			}
		});
		rdbNo.setBounds(52, 158, 49, 23);
		contentPane.add(rdbNo);

		grupoRadio = new ButtonGroup();
		grupoRadio.add(rdbSi);
		grupoRadio.add(rdbNo);

		lblConoceElSerial = new JLabel("\u00BFConoce el serial de la tarjeta?\r\n");
		lblConoceElSerial.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblConoceElSerial.setBounds(52, 84, 209, 14);
		contentPane.add(lblConoceElSerial);

		txtSiSerial = new JTextField();
		txtSiSerial.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtSiSerial.setEnabled(false);
		txtSiSerial.setBounds(106, 115, 134, 20);
		contentPane.add(txtSiSerial);
		txtSiSerial.setColumns(10);

		txtNoSerial = new JTextField();
		txtNoSerial.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtNoSerial.setEnabled(false);
		txtNoSerial.setColumns(10);
		txtNoSerial.setBounds(107, 179, 134, 20);
		contentPane.add(txtNoSerial);

		btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String consultaSerial="select r.lamina, r.fecha_registro, r.temperatura, r.humedad, r.maquina,r.estado, c.num_cinta, u.Nombre"
						+ " from tbregistro r join tbactivacinta ac on r.TbActivaCinta_idTbActivaCinta=r.TbActivaCinta_idTbActivaCinta"
						+ " join tbcinta c on c.idtbCinta=ac.tbcinta_idtbCinta"
						+ " join tbusuario u on ac.tbusuario_idtbUsuario=u.idtbUsuario and serial='"+numeroSerial()+"'";
				ResultSet rsDatoSerial=conexionBase.muestraTabla(consultaSerial);
				try {
					if (rsDatoSerial.next())
					{
						if (rsDatoSerial.getString(1)!=null)
						{
							ArrayList<String> resultado= new ArrayList<>();
							resultado.add("  Lamina: "+rsDatoSerial.getString(1));
							resultado.add("  Fecha Registro: "+rsDatoSerial.getString(2));
							resultado.add("  Cinta: "+rsDatoSerial.getString(7));
							resultado.add("  Operador: "+rsDatoSerial.getString(8));
							resultado.add("  Máquina: "+rsDatoSerial.getString(5));
							resultado.add("  Estado: "+rsDatoSerial.getString(6));
							resultado.add("  Temperatura: "+rsDatoSerial.getString(3));
							resultado.add("  Humedad: "+rsDatoSerial.getString(4));	
							for (int i = 0; i < resultado.size(); i++)
							{
								txaResultado.append(resultado.get(i)+"\n");
								//System.out.println(resultado.get(i));
							}
						}
						else 
						{
							JOptionPane.showMessageDialog(null,"serial no encontrado");
						}
					}
					else JOptionPane.showMessageDialog(null,"Serial erroneo");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				

			}
		});
		btnBuscar.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnBuscar.setBounds(270, 148, 89, 23);
		contentPane.add(btnBuscar);
		txaResultado.setForeground(new Color(0, 0, 205));
		txaResultado.setOpaque(false);
		txaResultado.setEditable(false);
		txaResultado.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		
		txaResultado.setBounds(52, 247, 260, 141);
		contentPane.add(txaResultado);
		
		lblResultadoDeLa = new JLabel("Resultado de la b\u00FAsqueda:");
		lblResultadoDeLa.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblResultadoDeLa.setBounds(50, 222, 166, 14);
		contentPane.add(lblResultadoDeLa);
		
		JLabel lblFondo = new JLabel("");
		lblFondo.setBounds(-3, 0, 400, 396);
		parametros.SetImagenLabel(lblFondo, "C:\\maquinaBT\\utilitarios\\imagen\\BuscarSerial.png");
		contentPane.add(lblFondo);
	}
	public String numeroSerial()
	{
		String numSerial="";
		if (rdbSi.isSelected() && txtSiSerial.getText().length()==16 )
		{
			numSerial=txtSiSerial.getText();
			System.out.println("buscar serial en bbdd");
		} 
		else if(rdbNo.isSelected() && txtNoSerial.getText().length()==16 )
		{
			numSerial=txtNoSerial.getText();
		}
		return numSerial;
	}
}
