package ec;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Toolkit;

public class FrmActivarCinta extends JFrame
{
	private JPanel contentPane;
	private JTextField txtCintaSecuencia;
	private JTable tbCintaActiva;	
	private int idCinta = 0;
	private int idUsuario = 0;
	private int stock = 0;
	JComboBox cmbOperador = new JComboBox();
	ClsBBDD conexion = new ClsBBDD();
	ClsParametros parametros=new ClsParametros();
	DefaultTableModel modelo = new DefaultTableModel();
	private final JLabel lblActivar = new JLabel("");
	JTextArea txaObservacion = new JTextArea();

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					FrmActivarCinta frame = new FrmActivarCinta();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public FrmActivarCinta()
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\maquinaBT\\utilitarios\\imagen\\ICONO.png"));
		setResizable(false);
		setTitle("Activación de cinta");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 583, 342);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNumCinta = new JLabel("Num Cinta:");
		lblNumCinta.setForeground(new Color(0, 0, 0));
		lblNumCinta.setBounds(23, 56, 64, 14);
		lblNumCinta.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPane.add(lblNumCinta);		

		JLabel lblOperador = new JLabel("Operador:");
		lblOperador.setForeground(new Color(0, 0, 0));
		lblOperador.setBounds(27, 89, 58, 14);
		lblOperador.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPane.add(lblOperador);

		txtCintaSecuencia = new JTextField();
		txtCintaSecuencia.setBounds(95, 53, 64, 20);
		txtCintaSecuencia.setEditable(false);
		txtCintaSecuencia.setFont(new Font("Tahoma", 0, 12));
		txtCintaSecuencia.setHorizontalAlignment(0);
		txtCintaSecuencia.setColumns(10);
		contentPane.add(txtCintaSecuencia);

		//carga campos num_cinta y cmboperador


		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.setBounds(294, 56, 77, 23);
		btnGuardar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					if(cmbOperador.getSelectedItem().toString().length()>0 && txtCintaSecuencia.getText().length()>0)
					{
						int respuesta = JOptionPane.showConfirmDialog(null, "Desea activar la cinta: " + txtCintaSecuencia.getText() + 
								" al operador: " + cmbOperador.getSelectedItem() + "?");
						if (respuesta == 0)
						{
							String activarCinta = "INSERT INTO tbactivacinta (`fechaActivacion`, `tbUsuario_idtbUsuario`, `tbCinta_idtbCinta`, `stock`) VALUES (now(),'" + cargaIdUsuario() + "', '" + idCinta + "', '" + stock + "')";
							conexion.Insertar(activarCinta);
							String modificarUsuario = "UPDATE tbusuario SET `Estado` = 'Activo' WHERE `idtbUsuario` = '" + cargaIdUsuario() + "'";
							//System.out.println(modificarUsuario);
							conexion.Modificar(modificarUsuario);
							String modificarCinta = "UPDATE tbcinta SET `estado` = 'En proceso' WHERE `idtbCinta` = '" + idCinta + "'";
							conexion.Modificar(modificarCinta);
							modelo.setRowCount(0);
							conexion.cargaTabla(tbCintaActiva, "Select num_cinta, codigo,total_buenos,total_chips,estado from tbcinta where estado!='Abierta'", modelo);
							txtCintaSecuencia.setText("");
							txaObservacion.setText("");
							cargaCinta();
							cmbOperador.removeAllItems();
							cargaUsuario();
						}
						else
						{
							JOptionPane.showMessageDialog(null,"Registro no almacenado");
							txtCintaSecuencia.setText("");
							txaObservacion.setText("");
						}
					}
					else 
					{
						JOptionPane.showMessageDialog(null,"No existe Cinta u Operador disponibles");
						dispose();
					}
				} catch (Exception e) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null,"No existe Cinta u Operador disponibles");
					dispose();
				}
			}
		});
		btnGuardar.setFont(new Font("Tahoma", 0, 11));
		contentPane.add(btnGuardar);

		JButton btnModificar = new JButton("Modificar");
		btnModificar.setEnabled(false);
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnModificar.setBounds(295, 94, 77, 23);
		btnModificar.setFont(new Font("Tahoma", 0, 11));
		contentPane.add(btnModificar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancelar.setBounds(296, 134, 77, 23);
		btnCancelar.setFont(new Font("Tahoma", 0, 11));
		contentPane.add(btnCancelar);

		JScrollPane scpActiva = new JScrollPane();
		scpActiva.setBounds(22, 219, 348, 79);
		contentPane.add(scpActiva);

		tbCintaActiva = new JTable();
		tbCintaActiva.setFont(new Font("Tahoma", 0, 11));
		tbCintaActiva.setBounds(141, 191, 1, 1);
		scpActiva.setViewportView(tbCintaActiva);

		JLabel lblObservacin = new JLabel("Observaci\u00F3n:");
		lblObservacin.setForeground(new Color(0, 0, 0));
		lblObservacin.setBounds(11, 124, 84, 14);
		lblObservacin.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPane.add(lblObservacin);

		JLabel lblActivadas = new JLabel("ACTIVACI\u00D3N DE CINTAS");
		lblActivadas.setForeground(new Color(0, 0, 0));
		lblActivadas.setBounds(10, 23, 369, 14);
		lblActivadas.setHorizontalAlignment(0);
		lblActivadas.setFont(new Font("Tahoma", Font.BOLD, 13));
		contentPane.add(lblActivadas);
		cmbOperador.setBounds(94, 85, 180, 20);
		cmbOperador.setFont(new Font("Tahoma", Font.PLAIN, 11));
		contentPane.add(cmbOperador);

		JTextArea txaObservacion = new JTextArea();
		txaObservacion.setBounds(96, 127, 180, 39);
		contentPane.add(txaObservacion);

		//SetImagenLabel(JLabel lblImagen, String ruta)

		modelo.setColumnIdentifiers(new Object[] { "<html><p style=\"color:blue; font:9px;\">Num_cinta</p></html>", 
				"<html><p style=\"color:blue; font:9px;\">Código</p></html>", 
				"<html><p style=\"color:blue; font:9px;\">Total Buenos</p></html>", 
				"<html><p style=\"color:blue; font:9px;\">Total Chips</p></html>", 
		"<html><p style=\"color:blue; font:9px;\">Estado</p></html>" });
		conexion.cargaTabla(tbCintaActiva, "Select num_cinta, codigo,total_buenos,total_chips,estado from tbcinta where estado!='Abierta'", modelo);

		JLabel lblDetalleDeLas = new JLabel("Detalle de las cintas activas:");
		lblDetalleDeLas.setForeground(new Color(0, 0, 0));
		lblDetalleDeLas.setBounds(25, 186, 285, 14);
		lblDetalleDeLas.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPane.add(lblDetalleDeLas);	
		lblActivar.setBounds(0, 0, 581, 317);
		contentPane.add(lblActivar);
		parametros.SetImagenLabel(lblActivar, "C:\\maquinaBT\\utilitarios\\imagen\\activar.png");

		if (cargaUsuario()==false || cargaCinta()==false)
		{
			JOptionPane.showMessageDialog(null,"NO PUEDE ACTIVAR CINTA");
		}
	}

	public boolean cargaCinta()
	{
		boolean cinta=false;
		String consulCinta = "select min(num_cinta), idtbCinta, total_buenos from tbcinta where estado='Abierta'";
		ResultSet rsNumCinta = conexion.muestraTabla(consulCinta);
		try
		{
			while (rsNumCinta.next())
			{
				if (rsNumCinta.getString(2)==null)
				{
					idCinta=0;
				}
				else
				{
					cinta=true;
					txtCintaSecuencia.setText(rsNumCinta.getString(1));
					idCinta = Integer.parseInt(rsNumCinta.getString(2));
					stock = Integer.parseInt(rsNumCinta.getString(3));
					System.out.println("Cinta "+cinta);
				}
				

			};						
		}
		catch (SQLException localSQLException)
		{
			System.out.println(localSQLException);
		}
		return cinta;
	}

	public boolean cargaUsuario()
	{
		boolean usuario=false;
		String consulUsuario="select Nombre from tbUsuario where estado='Inactivo' and perfil not like 'Administrador' order by nombre asc";
		ResultSet rsNumUsuario = conexion.muestraTabla(consulUsuario);
		try
		{
			cmbOperador.removeAll();
			while (rsNumUsuario.next())
			{
				usuario=true;
				cmbOperador.addItem(rsNumUsuario.getString(1));	
				System.out.println("Usuario "+usuario);

			};
		}

		catch (SQLException e)
		{
		}
		return usuario;
	}

	public int cargaIdUsuario()
	{
		ResultSet rsIdUsuario = conexion.muestraTabla("select idtbusuario from tbUsuario where nombre='" + cmbOperador.getSelectedItem() + "'");
		try
		{
			if (rsIdUsuario.next()) {
				idUsuario = Integer.parseInt(rsIdUsuario.getString(1));
			} else {
				JOptionPane.showMessageDialog(null, "No existe Usuario Activo");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return idUsuario;
	}	
}
