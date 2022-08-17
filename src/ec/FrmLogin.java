package ec;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.Toolkit;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class FrmLogin extends JFrame
{
	private JPanel contentPane;
	private JTextField txtOperador;
	private JPasswordField txpClave;

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					FrmLogin frame = new FrmLogin();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public FrmLogin()
	{
		
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\maquinaBT\\utilitarios\\imagen\\ICONO.png"));
		setResizable(false);
		setTitle("AUTENTICACIÓN");
		setDefaultCloseOperation(3);
		setBounds(100, 100, 500, 177);
		this.contentPane = new JPanel();
		//contentPane.setBackground(new Color(119, 136, 153));
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(null);

		JLabel lblFbricaDeTarjetas = new JLabel("FÁBRICA DE TARJETAS Y PASAPORTES ");
		lblFbricaDeTarjetas.setHorizontalAlignment(SwingConstants.CENTER);
		lblFbricaDeTarjetas.setForeground(new Color(245, 255, 250));
		lblFbricaDeTarjetas.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblFbricaDeTarjetas.setBounds(10, 11, 474, 14);
		this.contentPane.add(lblFbricaDeTarjetas);

		JLabel lblOperador = new JLabel("Operador:");
		lblOperador.setForeground(new Color(245, 255, 250));
		lblOperador.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblOperador.setBounds(68, 48, 68, 14);
		this.contentPane.add(lblOperador);

		JLabel lblClave = new JLabel("Clave:");
		lblClave.setForeground(new Color(245, 255, 250));
		lblClave.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblClave.setBounds(87, 77, 37, 14);
		this.contentPane.add(lblClave);

		this.txtOperador = new JTextField();
		this.txtOperador.setFont(new Font("Tahoma", 0, 11));
		this.txtOperador.setBounds(146, 43, 135, 20);
		this.contentPane.add(this.txtOperador);
		this.txtOperador.setColumns(10);

		JTextField txtValida = new JTextField();

		JButton btnIngresar = new JButton("Ingresar");
		btnIngresar.setFont(new Font("Tahoma", 0, 11));
		btnIngresar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ClsBBDD conexionBase = new ClsBBDD();
				String consulta = "select nombre, perfil, estado from tbusuario where nombre='" +txtOperador.getText() + "' and clave='" + txpClave.getText() + "'";
				ResultSet rsConsulta = conexionBase.muestraTabla(consulta);
				try
				{
					if (rsConsulta.next())
					{
						String perfil=rsConsulta.getString(2).toString();
						String estado=rsConsulta.getString(3).toString();
						switch (perfil)
						{
						case "Operador":               
							if (estado.equals("Activo"))
							{
								FrmAdministrador administrador = new FrmAdministrador("Operador");
								administrador.setVisible(true); 
								dispose();
								break;
							} 
							else
							{
								JOptionPane.showMessageDialog(null, "    No tiene Cinta designada \nConsulte con el Administrador");
								break;
							}
						default:
						{
							FrmAdministrador operador = new FrmAdministrador("Administrador");
							operador.setVisible(true);
							dispose();
							break;
						}
						}												
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Usuario no encontrado");
					}
				}
				catch (SQLException e1)
				{
					JOptionPane.showInternalMessageDialog(null, "Usuario no encontrado");
					e1.printStackTrace();
				}
			}
		});
		btnIngresar.setBounds(62, 114, 101, 23);
		this.contentPane.add(btnIngresar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setFont(new Font("Tahoma", 0, 11));
		btnCancelar.setBounds(216, 114, 101, 23);
		this.contentPane.add(btnCancelar);

		this.txpClave = new JPasswordField();
		this.txpClave.setFont(new Font("Tahoma", 0, 11));
		this.txpClave.setBounds(148, 71, 133, 20);
		this.contentPane.add(this.txpClave);
		
		JLabel lblLogin = new JLabel("");
		lblLogin.setBounds(0, 0, 494, 148);
		contentPane.add(lblLogin);
		ClsParametros parametros= new ClsParametros();
		parametros.SetImagenLabel(lblLogin, "C:\\maquinaBT\\utilitarios\\imagen\\login.png");
	}
	
}
