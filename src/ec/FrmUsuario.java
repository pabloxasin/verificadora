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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ActionMapUIResource;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class FrmUsuario extends JFrame
{
	private JPanel contentPane;
	private JTextField txtIdentificacion;
	private JTextField txtNombre;
	private JTextField txtClave;
	ClsBBDD conexion = new ClsBBDD();
	private JTable tbUsuario;
	final JButton btnGuardar = new JButton("Guardar");
	ClsParametros parametros=new ClsParametros();
	DefaultTableModel modelo = new DefaultTableModel(){
		@Override
		public boolean isCellEditable(int fila, int columna){
			if(columna==4)
			{
				return true;
			}	
			else
			{
				return false; 
			}  		  
		}
	};

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					FrmUsuario frame = new FrmUsuario();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public FrmUsuario()
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\maquinaBT\\utilitarios\\imagen\\ICONO.png"));
		setResizable(false);
		setTitle("Ingreso de datos de los operadores");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 585, 385);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("REGISTRO DE USUARIOS");
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setHorizontalAlignment(0);
		lblNewLabel.setBounds(10, 11, 379, 23);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Identificación:");
		lblNewLabel_1.setForeground(new Color(0, 0, 0));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1.setBounds(20, 57, 81, 14);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Nombres:");
		lblNewLabel_2.setForeground(new Color(0, 0, 0));
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_2.setBounds(44, 88, 67, 14);
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("Clave:");
		lblNewLabel_3.setForeground(new Color(0, 0, 0));
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_3.setBounds(66, 114, 45, 14);
		contentPane.add(lblNewLabel_3);

		JLabel lblPerfil = new JLabel("Perfil:");
		lblPerfil.setForeground(new Color(0, 0, 0));
		lblPerfil.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPerfil.setBounds(66, 139, 45, 14);
		contentPane.add(lblPerfil);

		txtIdentificacion = new JTextField();
		txtIdentificacion.addFocusListener(new FocusAdapter()
		{
			public void focusLost(FocusEvent arg0)
			{
				conexion.BuscarRegistro("select * from tbUsuario where identificacion='" + txtIdentificacion.getText() + "'", txtIdentificacion);
			}
		});
		txtIdentificacion.addKeyListener(new KeyAdapter()
		{
			public void keyTyped(KeyEvent arg0)
			{
				conexion.ValidarNumero(arg0);
			}
		});
		txtIdentificacion.setBounds(111, 54, 81, 20);
		contentPane.add(txtIdentificacion);
		txtIdentificacion.setColumns(10);

		txtNombre = new JTextField();
		txtNombre.setBounds(110, 85, 164, 20);
		contentPane.add(txtNombre);
		txtNombre.setColumns(10);

		txtClave = new JTextField();
		txtClave.setColumns(10);
		txtClave.setBounds(110, 111, 164, 20);
		contentPane.add(txtClave);

		final JComboBox cmbPerfil = new JComboBox();
		cmbPerfil.setFont(new Font("Tahoma", 0, 11));
		cmbPerfil.setModel(new DefaultComboBoxModel(new String[] { "Administrador", "Operador" }));
		cmbPerfil.setBounds(111, 136, 135, 20);
		contentPane.add(cmbPerfil);


		btnGuardar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if ((txtClave.getText().isEmpty()) || (txtNombre.getText().isEmpty()) || (txtIdentificacion.getText().isEmpty()))
				{
					JOptionPane.showMessageDialog(null, "Revice los datos ingresados");
					encerar();
				}
				else
				{
					String insertarUsuario = "insert into tbUsuario (`Nombre`,`Clave`,`Identificacion`,`estado`,`fecha_registro`,`perfil`) VALUES('" + 
							txtNombre.getText() + "','" + txtClave.getText() + "','" +txtIdentificacion.getText() + "','Inactivo',now(),'" + cmbPerfil.getSelectedItem() + "')";
					conexion.Insertar(insertarUsuario);
					conexion.blanquearTabla(tbUsuario,modelo);
					conexion.cargaTabla(tbUsuario, "select identificacion,nombre,clave,estado from tbUsuario", modelo);
					encerar();
				}
			}
		});
		btnGuardar.setFont(new Font("Tahoma", 0, 11));
		btnGuardar.setBounds(299, 53, 89, 23);
		contentPane.add(btnGuardar);

		JButton btnModificar = new JButton("Modificar");
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String modificaUsuario="update tbusuario set nombre= '"+txtNombre.getText()+"',"
						+ " clave='"+txtClave.getText()+"' where identificacion='"+txtIdentificacion.getText()+"'";
				conexion.Modificar(modificaUsuario);
				encerar();
				conexion.blanquearTabla(tbUsuario,modelo);
				conexion.cargaTabla(tbUsuario, "select identificacion,nombre,clave,estado from tbUsuario", modelo);
			}
		});
		btnModificar.setFont(new Font("Tahoma", 0, 11));
		btnModificar.setBounds(299, 85, 89, 23);
		contentPane.add(btnModificar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtIdentificacion.setEnabled(true); 
				encerar();
				btnGuardar.setEnabled(true);
			}
		});
		btnCancelar.setFont(new Font("Tahoma", 0, 11));
		btnCancelar.setBounds(299, 118, 89, 23);
		contentPane.add(btnCancelar);

		JScrollPane scpUsuario = new JScrollPane();
		scpUsuario.setBounds(20, 222, 368, 90);
		contentPane.add(scpUsuario);

		tbUsuario = new JTable();
		tbUsuario.setCellSelectionEnabled(true);
		tbUsuario.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				txtIdentificacion.setEnabled(false);
				cargarTxtTabla(tbUsuario);
				btnGuardar.setEnabled(false);
			}
		});
		tbUsuario.setFont(new Font("Tahoma", 0, 10));
		scpUsuario.setViewportView(tbUsuario);


		modelo.setColumnIdentifiers(new Object[] { "<html><p style=\"color:blue; font:9px;\">Id</p></html>", 
				"<html><p style=\"color:blue; font:9px;\">Nombre</p></html>", 
				"<html><p style=\"color:blue; font:9px;\">Clave</p></html>", 
		"<html><p style=\"color:blue; font:9px;\">estado</p></html>" });
		conexion.cargaTabla(tbUsuario, "select identificacion,nombre,clave,estado from tbUsuario", modelo);

		JLabel lblParaModificarUn = new JLabel("Para modificar un registro seleccionelo con un clic.");
		lblParaModificarUn.setForeground(new Color(0, 0, 0));
		lblParaModificarUn.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblParaModificarUn.setBounds(20, 188, 369, 14);
		contentPane.add(lblParaModificarUn);
		
		JLabel lblUsuario = new JLabel("");
		lblUsuario.setFont(new Font("Dialog", Font.BOLD, 11));
		lblUsuario.setBounds(0, 4, 579, 352);
		parametros.SetImagenLabel(lblUsuario, "C:\\maquinaBT\\utilitarios\\imagen\\operador.png");
		contentPane.add(lblUsuario);
	}

	public String toString()
	{
		return "FrmUsuario [contentPane=" + contentPane + ", txtIdentificacion=" + txtIdentificacion + ", txtNombre=" + txtNombre + ", txtClave=" + txtClave + ", tbUsuario=" + tbUsuario + "]";
	}

	public JTextField getTxtIdentificacion()
	{
		return txtIdentificacion;
	}

	public void setTxtIdentificacion(JTextField txtIdentificacion)
	{
		txtIdentificacion = txtIdentificacion;
	}

	public JTextField getTxtNombre()
	{
		return txtNombre;
	}

	public void setTxtNombre(JTextField txtNombre)
	{
		txtNombre = txtNombre;
	}

	public JTextField getTxtClave()
	{
		return txtClave;
	}

	public void setTxtClave(JTextField txtClave)
	{
		txtClave = txtClave;
	}

	public void encerar()
	{
		txtClave.setText("");
		txtIdentificacion.setText("");
		txtNombre.setText("");
		btnGuardar.setEnabled(true);
	}
	public void cargarTxtTabla(JTable celdaSeleccionada)
	{
		int fila = tbUsuario.getSelectedRow();
		String identificacion = String.valueOf(celdaSeleccionada.getValueAt(fila,0));
		String nombre = String.valueOf(celdaSeleccionada.getValueAt(fila,1));
		String clave = String.valueOf(celdaSeleccionada.getValueAt(fila,2));// por ultimo, obtengo el valor de la celda
		txtIdentificacion.setText(identificacion);
		txtNombre.setText(nombre);
		txtClave.setText(clave);	  	  
	}
}
