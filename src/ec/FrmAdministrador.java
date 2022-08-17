package ec;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.awt.Insets;
import java.awt.Toolkit;

public class FrmAdministrador  extends JFrame
{
	ClsBBDD conexionBase = new ClsBBDD();
	JMenu mnAdminBase = new JMenu("AdminBase");
	JMenu mnOperador = new JMenu("Operador");
	JMenu mnReporte = new JMenu("Reportería");
	JMenu mnUsuario = new JMenu("Usuario");
	JMenu mnCinta = new JMenu("Cinta");
	JMenu mnActivarCinta = new JMenu("Activar Cinta");
	private JPanel contentPane;
	ClsParametros parametros=new ClsParametros();

	public FrmAdministrador(String perfil)
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage("imagen\\ICONO.png"));
		setResizable(false);
		setTitle("F\u00C1BRICA DE TARJETAS Y PASAPORTES");
		setDefaultCloseOperation(3);
		setBounds(100, 100, 574, 422);
		contentPane = new JPanel();
		contentPane.setToolTipText("");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JMenuBar barra = new JMenuBar();
		barra.setBackground(new Color(119, 136, 153));
		barra.setFont(new Font("Calibri", Font.BOLD | Font.ITALIC, 10));
		barra.setBounds(0, 0, 568, 33);
		contentPane.add(barra);
		mnUsuario.setForeground(new Color(245, 255, 250));


		mnUsuario.setEnabled(false);
		mnUsuario.setFont(new Font("Segoe UI", Font.ITALIC, 15));
		barra.add(mnUsuario);

		JMenuItem mniRegistroUsuario = new JMenuItem("- Crear Usuario");
		mniRegistroUsuario.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 12));
		mniRegistroUsuario.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				FrmUsuario frmUsuario = new FrmUsuario();
				frmUsuario.setVisible(true);
			}
		});
		mnUsuario.add(mniRegistroUsuario);
		mnCinta.setForeground(new Color(245, 255, 250));


		mnCinta.setEnabled(false);
		mnCinta.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				FrmCinta frmCinta = new FrmCinta();
				frmCinta.setVisible(true);
			}
		});
		mnCinta.setFont(new Font("Segoe UI", Font.ITALIC, 15));
		barra.add(mnCinta);

		JMenuItem mniRegistroCinta = new JMenuItem("- Crear Cinta");
		mniRegistroCinta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmCinta frmCinta = new FrmCinta();
				frmCinta.setVisible(true);
			}
		});
		mniRegistroCinta.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 12));
		mnCinta.add(mniRegistroCinta);
		mnReporte.setForeground(new Color(245, 255, 250));


		mnReporte.setEnabled(false);
		mnReporte.setFont(new Font("Segoe UI", Font.ITALIC, 15));
		barra.add(mnReporte);

		JMenuItem mniReportes = new JMenuItem("- Reportes.");
		mniReportes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmReporte frmReporte= new FrmReporte();
				frmReporte.setVisible(true);
			}
		});
		mniReportes.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 12));
		mnReporte.add(mniReportes);
		barra.add(mnActivarCinta);

		mnActivarCinta.setEnabled(false);
		mnActivarCinta.setFont(new Font("Segoe UI", Font.ITALIC, 15));
		//barra.add(mnActivarCinta);

		JMenuItem mniActivar = new JMenuItem("- Activar Cinta");
		mnActivarCinta.add(mniActivar);
		mniActivar.setForeground(new Color(0, 0, 0));
		mniActivar.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 12));				
		mnActivarCinta.setForeground(new Color(245, 245, 245));


		mnActivarCinta.setEnabled(false);
		mnActivarCinta.setFont(new Font("Segoe UI", Font.ITALIC, 15));

		//JMenuItem mniActivar = new JMenuItem("- Activar Cinta");
		mniActivar.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 12));
		mniActivar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				FrmActivarCinta frmActivarCinta = new FrmActivarCinta();
				frmActivarCinta.setVisible(true);
			}
		});
		mnActivarCinta.add(mniActivar);
		mnOperador.setForeground(new Color(245, 255, 250));


		mnOperador.setEnabled(false);
		mnOperador.setFont(new Font("Segoe UI", Font.ITALIC, 15));
		barra.add(mnOperador);

		JMenuItem mniOperadorRegistro = new JMenuItem("- Producci\u00F3n");
		mniOperadorRegistro.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 12));
		mniOperadorRegistro.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				FrmOperador frmOperador = new FrmOperador();
				frmOperador.setVisible(true);
			}
		});
		mnOperador.add(mniOperadorRegistro);
		mnAdminBase.setForeground(new Color(245, 255, 250));


		mnAdminBase.setEnabled(false);
		mnAdminBase.setFont(new Font("Segoe UI", Font.ITALIC, 15));
		barra.add(mnAdminBase);

		JMenuItem mniBorrarBase = new JMenuItem("- Borrar Base Datos");
		mniBorrarBase.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 12));
		mniBorrarBase.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				String borrarRegistro = "delete from tbregistro";
				String borrarActivaCinta = "delete from tbactivacinta";
				String borrarUsuario = "delete from tbusuario";
				String borrarCinta = "delete from tbcinta";
				//modifica los secuenciales
				String alterRegistro = "alter table tbregistro auto_increment=1";
				String alterUsuario = "alter table tbUsuario auto_increment=1";
				String alterCinta = "alter table tbcinta auto_increment=1";
				String alterActiva = "alter table tbactivacinta auto_increment=1";
				String creaAdmin="INSERT INTO tbusuario (`Nombre`, `clave`, `Identificacion`, `Estado`, `perfil`,`fecha_registro`) VALUES ('Pablo Sinchiguano', 'pablo123', '1713023073', 'Inactivo', 'Administrador',now())";

				conexionBase.Delete(borrarRegistro);
				conexionBase.Delete(borrarActivaCinta);
				conexionBase.Delete(borrarUsuario);
				conexionBase.Delete(borrarCinta);				
				conexionBase.Insertar(alterRegistro);
				conexionBase.Insertar(alterUsuario);
				conexionBase.Insertar(alterCinta);
				conexionBase.Insertar(alterActiva);
				conexionBase.Insertar(creaAdmin);

			}
		});
		mnAdminBase.add(mniBorrarBase);

		JMenuItem mniBuscarSerial = new JMenuItem("- Buscar por Serie");
		mniBuscarSerial.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 12));
		mniBuscarSerial.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) {
				FrmBuscaSerial buscarSerie = new FrmBuscaSerial();
				buscarSerie.setVisible(true);
			}
		});
		mnAdminBase.add(mniBuscarSerial);

		JLabel lblNewLabel = new JLabel("FÁBRICA DE TARJETAS Y PASAPORTES");
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setFont(new Font("Tahoma", 3, 16));
		lblNewLabel.setHorizontalAlignment(0);
		lblNewLabel.setBounds(88, 55, 428, 30);
		contentPane.add(lblNewLabel);

		JLabel lblPrincipal = new JLabel("");
		lblPrincipal.setBounds(0, 33, 568, 361);
		contentPane.add(lblPrincipal);
		//SetImagenLabel(lblPrincipal, "imagen\\principal.png");
		parametros.SetImagenLabel(lblPrincipal,"C:\\maquinaBT\\utilitarios\\imagen\\principal.png");
		activarPermisos(perfil);
	}
	public void activarPermisos(String perfil)
	{
		System.out.println("perfil recibido " + perfil);
		if (perfil.equals("Administrador"))
		{
			mnActivarCinta.setEnabled(true);
			mnAdminBase.setEnabled(true);
			mnCinta.setEnabled(true);
			mnUsuario.setEnabled(true);
			mnReporte.setEnabled(true);
		}
		else
		{
			mnOperador.setEnabled(true);
		}
	}
	}
