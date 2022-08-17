package ec;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import net.sf.jasperreports.charts.util.CategoryChartHyperlinkProvider;
import net.sf.jasperreports.engine.xml.JRExpressionFactory.IntegerExpressionFactory;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class FrmOperador extends JFrame
{
	UIManager UI = null;
	private ClsParametros parametro = new ClsParametros();
	private ClsBBDD conexionBase = new ClsBBDD();
	private ClsArduino conexionArduino = new ClsArduino();
	static public JProgressBar pgbLeer = new JProgressBar();
	JComboBox cmbPuertos = new JComboBox();
	JComboBox<String> cmbOperador = new JComboBox();
	JButton btnGuardar = new JButton("Guardar");
	JButton btnLeer = new JButton("Leer lámina");
	JTextField txtCodigo = new JTextField();
	public boolean bandera = false;
	int nuevos;
	int repetidos;
	int malos;
	int[] respuesta;
	ArrayList<String> serialesNuevos = new ArrayList();
	ArrayList<String> serialesRepetidos = new ArrayList();
	Thread barra = new Thread()
	{
		public void run()
		{
			try
			{
				for (int segundo = 1; segundo <= 100; segundo++)
				{
					pgbLeer.setValue(segundo);
					Thread.sleep(70L);
				}
				pgbLeer.setValue(0);
			}
			catch (Exception localException) {}
		}
	};
	private JPanel contentPane;
	JLabel lblSerial1 = new JLabel("1");
	JLabel lblSerial2 = new JLabel("2");
	JLabel lblSerial3 = new JLabel("3");
	JLabel lblSerial4 = new JLabel("4");
	JLabel lblSerial5 = new JLabel("5");
	JLabel lblSerial6 = new JLabel("6");
	JLabel lblSerial7 = new JLabel("7");
	JLabel lblSerial8 = new JLabel("8");
	JLabel lblSerial9 = new JLabel("9");
	JLabel lblSerial10 = new JLabel("10");
	JLabel lblSerial11 = new JLabel("11");
	JLabel lblSerial12 = new JLabel("12");
	JLabel lblSerial13 = new JLabel("13");
	JLabel lblSerial14 = new JLabel("14");
	JLabel lblSerial15 = new JLabel("15");
	JLabel lblSerial16 = new JLabel("16");
	JLabel lblSerial17 = new JLabel("17");
	JLabel lblSerial18 = new JLabel("18");
	JLabel lblSerial19 = new JLabel("19");
	JLabel lblSerial20 = new JLabel("20");
	JLabel lblSerial21 = new JLabel("21");
	JLabel lblSerial22 = new JLabel("22");
	JLabel lblSerial23 = new JLabel("23");
	JLabel lblSerial24 = new JLabel("24");
	String puertoConexion = null;
	String caracterInicio = null;
	int baudios;
	String[] maquinas = { "MTT NEW", "IA 1600", "MTT OLD", "CANCELAR" };
	ArrayList<JLabel> labelList = crearlabels();
	private final JLabel lblRegistroDeInlayers = new JLabel("REGISTRO DE INLAYERS");
	private final JLabel lblTemperatura = new JLabel("Temperatura:");
	private final JLabel lblHumedad = new JLabel("Humedad:");
	private JTextField txtTemperatura;
	private JTextField txtHumedad;

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					FrmOperador frame = new FrmOperador();
					frame.setVisible(true);
					frame.getSize();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public FrmOperador()	
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\maquinaBT\\utilitarios\\imagen\\ICONO.png"));
		setResizable(false);
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent arg0)
			{
				conexionArduino.desconectar();
			}
		});
		cargaPuertos();
		leeParametros();

		setTitle("VERIFICACIÓN E INICIALIZACIÓN DE CHIPS ");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 694, 637);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(143, 188, 143));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		cmbOperador.setFont(new Font("Tahoma", 0, 10));


		btnLeer.setFont(new Font("Tahoma", 0, 11));
		btnLeer.setEnabled(false);
		btnLeer.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				pgbLeer.setValue(0);
				conexionArduino.resetLabel(labelList);
				btnLeer.setText("Leyendo");
				conexionArduino.setLabels(labelList);
				conexionArduino.escribir(caracterInicio);
			}
		});
		btnLeer.setBounds(499, 281, 89, 23);
		contentPane.add(btnLeer);

		final JButton btnConectar = new JButton("Conectar");
		btnConectar.setFont(new Font("Tahoma", 0, 11));
		btnConectar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (btnConectar.getText().equals("Conectar"))
				{
					conexionArduino.conectar(cmbPuertos.getSelectedItem().toString(), btnLeer, btnGuardar,baudios);
					conexionArduino.setArrays(serialesRepetidos, serialesNuevos);
					conexionArduino.iniciarIO();
					conexionArduino.initListener();
					if (conexionArduino.getConectado())
					{
						btnConectar.setText("Conectado");
						btnConectar.setEnabled(false);
						leeParametros();
					}
					else
					{
						System.out.println("desconectado");
					}
				}
				else
				{
					btnConectar.setText("Conectar");
					conexionArduino.desconectar();
				}
				btnConectar.setEnabled(false);
			}
		});
		btnConectar.setBounds(499, 247, 89, 23);
		contentPane.add(btnConectar);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(211, 211, 211));
		panel.setBounds(32, 116, 334, 468);
		contentPane.add(panel);
		panel.setLayout(null);

		lblSerial1.setHorizontalAlignment(0);
		lblSerial1.setFont(new Font("Tahoma", 0, 10));
		lblSerial1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial1.setBounds(29, 33, 87, 42);
		panel.add(lblSerial1);

		lblSerial2.setHorizontalAlignment(0);
		lblSerial2.setFont(new Font("Tahoma", 0, 10));
		lblSerial2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial2.setBounds(126, 33, 86, 42);
		panel.add(lblSerial2);

		lblSerial4.setHorizontalAlignment(0);
		lblSerial4.setFont(new Font("Tahoma", 0, 10));
		lblSerial4.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial4.setBounds(29, 86, 87, 42);
		panel.add(lblSerial4);

		lblSerial5.setHorizontalAlignment(0);
		lblSerial5.setFont(new Font("Tahoma", 0, 10));
		lblSerial5.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial5.setBounds(126, 86, 86, 42);
		panel.add(lblSerial5);

		lblSerial3.setHorizontalAlignment(0);
		lblSerial3.setFont(new Font("Tahoma", 0, 10));
		lblSerial3.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial3.setBounds(222, 33, 87, 42);
		panel.add(lblSerial3);

		lblSerial6.setHorizontalAlignment(0);
		lblSerial6.setFont(new Font("Tahoma", 0, 10));
		lblSerial6.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial6.setBounds(222, 86, 87, 42);
		panel.add(lblSerial6);

		lblSerial7.setHorizontalAlignment(0);
		lblSerial7.setFont(new Font("Tahoma", 0, 10));
		lblSerial7.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial7.setBounds(29, 139, 87, 42);
		panel.add(lblSerial7);

		lblSerial8.setHorizontalAlignment(0);
		lblSerial8.setFont(new Font("Tahoma", 0, 10));
		lblSerial8.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial8.setBounds(126, 139, 86, 42);
		panel.add(lblSerial8);

		lblSerial9.setHorizontalAlignment(0);
		lblSerial9.setFont(new Font("Tahoma", 0, 10));
		lblSerial9.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial9.setBounds(222, 139, 87, 42);
		panel.add(lblSerial9);

		lblSerial12.setHorizontalAlignment(0);
		lblSerial12.setFont(new Font("Tahoma", 0, 10));
		lblSerial12.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial12.setBounds(222, 192, 87, 42);
		panel.add(lblSerial12);

		lblSerial11.setHorizontalAlignment(0);
		lblSerial11.setFont(new Font("Tahoma", 0, 10));
		lblSerial11.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial11.setBounds(126, 192, 86, 42);
		panel.add(lblSerial11);

		lblSerial10.setHorizontalAlignment(0);
		lblSerial10.setFont(new Font("Tahoma", 0, 10));
		lblSerial10.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial10.setBounds(29, 192, 87, 42);
		panel.add(lblSerial10);

		lblSerial16.setHorizontalAlignment(0);
		lblSerial16.setFont(new Font("Tahoma", 0, 10));
		lblSerial16.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial16.setBounds(29, 298, 87, 42);
		panel.add(lblSerial16);

		lblSerial17.setHorizontalAlignment(0);
		lblSerial17.setFont(new Font("Tahoma", 0, 10));
		lblSerial17.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial17.setBounds(126, 298, 86, 42);
		panel.add(lblSerial17);

		lblSerial19.setHorizontalAlignment(0);
		lblSerial19.setFont(new Font("Tahoma", 0, 10));
		lblSerial19.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial19.setBounds(29, 348, 87, 42);
		panel.add(lblSerial19);

		lblSerial20.setHorizontalAlignment(0);
		lblSerial20.setFont(new Font("Tahoma", 0, 10));
		lblSerial20.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial20.setBounds(126, 348, 86, 42);
		panel.add(lblSerial20);

		lblSerial24.setHorizontalAlignment(0);
		lblSerial24.setFont(new Font("Tahoma", 0, 10));
		lblSerial24.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial24.setBounds(222, 401, 87, 42);
		panel.add(lblSerial24);

		lblSerial23.setHorizontalAlignment(0);
		lblSerial23.setFont(new Font("Tahoma", 0, 10));
		lblSerial23.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial23.setBounds(126, 401, 86, 42);
		panel.add(lblSerial23);

		lblSerial22.setHorizontalAlignment(0);
		lblSerial22.setFont(new Font("Tahoma", 0, 10));
		lblSerial22.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial22.setBounds(29, 401, 87, 42);
		panel.add(lblSerial22);

		lblSerial21.setHorizontalAlignment(0);
		lblSerial21.setFont(new Font("Tahoma", 0, 10));
		lblSerial21.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial21.setBounds(222, 348, 87, 42);
		panel.add(lblSerial21);

		lblSerial18.setHorizontalAlignment(0);
		lblSerial18.setFont(new Font("Tahoma", 0, 10));
		lblSerial18.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial18.setBounds(222, 298, 87, 42);
		panel.add(lblSerial18);

		lblSerial15.setHorizontalAlignment(0);
		lblSerial15.setFont(new Font("Tahoma", 0, 10));
		lblSerial15.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial15.setBounds(222, 245, 87, 42);
		panel.add(lblSerial15);

		lblSerial14.setHorizontalAlignment(0);
		lblSerial14.setFont(new Font("Tahoma", 0, 10));
		lblSerial14.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial14.setBounds(126, 245, 86, 42);
		panel.add(lblSerial14);

		lblSerial13.setHorizontalAlignment(0);
		lblSerial13.setFont(new Font("Tahoma", 0, 10));
		lblSerial13.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblSerial13.setBounds(29, 245, 87, 42);
		panel.add(lblSerial13);

		JLabel lblIngresarElCdigo = new JLabel("Código del Núcleo:");
		lblIngresarElCdigo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblIngresarElCdigo.setBounds(387, 355, 107, 14);
		contentPane.add(lblIngresarElCdigo);


		txtCodigo.setBounds(501, 354, 145, 20);
		contentPane.add(txtCodigo);
		txtCodigo.setColumns(10);


		JLabel lblPuertoDeComunicacin = new JLabel("Puerto de comunicación:");
		lblPuertoDeComunicacin.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPuertoDeComunicacin.setBounds(387, 217, 144, 14);
		contentPane.add(lblPuertoDeComunicacin);

		JTextField txtPuerto = new JTextField();
		txtPuerto.setEnabled(false);
		txtPuerto.setHorizontalAlignment(0);
		txtPuerto.setBounds(531, 216, 86, 20);
		contentPane.add(txtPuerto);
		txtPuerto.setColumns(10);
		txtPuerto.setText(puertoConexion);

		System.out.println(cargaOperador());


		//tomate(255, 125, 12), cian(3, 169, 244),verde(76,175,80), tomate(255,193,80),morado(102,51,102)
		pgbLeer.setForeground(new Color(218,165,32));
		pgbLeer.setBounds(408, 315, 238, 14);
		contentPane.add(pgbLeer);
		btnGuardar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{

				if (cargaOperador())
				{
					if (!txtCodigo.getText().isEmpty()|| !txtHumedad.getText().isEmpty()||!txtHumedad.getText().isEmpty()) 
					{
						if (validarTempHumedad())
						{
							btnLeer.setText("Leer lámina");
							btnLeer.setEnabled(false);
							guardarLamina(serialesNuevos, serialesRepetidos);
						}
					} 
					else
					{
						JOptionPane.showMessageDialog(null, "   Revice los datos solicitados \n  Código, Temperatura y Humedad");
					}
				}
				else 
				{
					JOptionPane.showMessageDialog(null,"    No existe Operadores activos !\n    Solo podrá leer los seriales");
					btnGuardar.setEnabled(false);
					btnLeer.setText("Leer lámina");
					conexionArduino.resetLabel(labelList);
				}
			}
		});
		btnGuardar.setEnabled(false);
		btnGuardar.setFont(new Font("Tahoma", 0, 11));
		btnGuardar.setBounds(408, 533, 89, 23);
		contentPane.add(btnGuardar);


		cmbPuertos.setBounds(531, 177, 86, 20);
		contentPane.add(cmbPuertos);

		JLabel lblPuertosHabilitados = new JLabel("Puertos habilitados:");
		lblPuertosHabilitados.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPuertosHabilitados.setBounds(411, 180, 118, 14);
		contentPane.add(lblPuertosHabilitados);

		JButton btnForzarCierre = new JButton("Forzar Cierre");
		btnForzarCierre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cuadreCinta();			
			}
		});
		btnForzarCierre.setFont(new Font("Tahoma", 0, 11));
		btnForzarCierre.setBounds(533, 533, 113, 23);
		contentPane.add(btnForzarCierre);
		lblRegistroDeInlayers.setBounds(364, 118, 324, 23);
		contentPane.add(lblRegistroDeInlayers);
		lblRegistroDeInlayers.setHorizontalAlignment(0);
		lblRegistroDeInlayers.setForeground(new Color(0, 0, 0));
		lblRegistroDeInlayers.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblTemperatura.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTemperatura.setBounds(407, 391, 124, 14);

		contentPane.add(lblTemperatura);
		lblHumedad.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblHumedad.setBounds(424, 424, 107, 14);

		contentPane.add(lblHumedad);

		txtTemperatura = new JTextField();   
		txtTemperatura.addKeyListener(new KeyAdapter() {
			@Override   
			public void keyTyped(KeyEvent arg0)
			{
				conexionBase.ValidarNumero(arg0);
			}

		});
		txtTemperatura.setColumns(10);
		txtTemperatura.setBounds(500, 390, 55, 20);
		contentPane.add(txtTemperatura);

		txtHumedad = new JTextField();
		txtHumedad.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				conexionBase.ValidarNumero(arg0);
			}
		});
		txtHumedad.setColumns(10);
		txtHumedad.setBounds(501, 423, 55, 20);
		contentPane.add(txtHumedad);

		JLabel lblc = new JLabel("\u00B0C.  [10 , 40]");
		lblc.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblc.setBounds(565, 393, 71, 14);
		contentPane.add(lblc);

		JLabel label_1 = new JLabel("%.  [50, 70]");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		label_1.setBounds(566, 426, 70, 14);
		contentPane.add(label_1);

		JLabel lblRegistro = new JLabel("");
		lblRegistro.setFont(new Font("Tahoma", 0, 11));
		lblRegistro.setBounds(0, 0, 688, 608);
		contentPane.add(lblRegistro);

		parametro.SetImagenLabel(lblRegistro, "C:\\maquinaBT\\utilitarios\\imagen\\registro1.png");
	}

	public void paint(Graphics g)
	{
		super.paint(g);
		g.setColor(Color.BLACK);
		g.drawRect(203, 155, 2, 10);

		g.setColor(Color.BLACK);

		g.drawRect(203, 590, 2, 10);

		int distancia = 0;
		for (int i = 0; i < 8; i++)
		{
			distancia += 50;
			g.drawRect(50, 175, 10, 2);
			g.drawRect(50, 175 + distancia + i, 10, 2);
		}
	}

	public ArrayList<JLabel> crearlabels()
	{
		ArrayList<JLabel> labels = new ArrayList();
		labels.add(lblSerial1);
		labels.add(lblSerial2);
		labels.add(lblSerial3);
		labels.add(lblSerial4);
		labels.add(lblSerial5);
		labels.add(lblSerial6);
		labels.add(lblSerial7);
		labels.add(lblSerial8);
		labels.add(lblSerial9);
		labels.add(lblSerial10);
		labels.add(lblSerial11);
		labels.add(lblSerial12);
		labels.add(lblSerial13);
		labels.add(lblSerial14);
		labels.add(lblSerial15);
		labels.add(lblSerial16);
		labels.add(lblSerial17);
		labels.add(lblSerial18);
		labels.add(lblSerial19);
		labels.add(lblSerial20);
		labels.add(lblSerial21);
		labels.add(lblSerial22);
		labels.add(lblSerial23);
		labels.add(lblSerial24);
		return labels;
	}

	public void cargaPuertos()
	{
		String[] puertosHabilitados =conexionArduino.obtenerLista().split(",");
		DefaultComboBoxModel modeloPuertos = new DefaultComboBoxModel();
		for (int i = 0; i < puertosHabilitados.length; i++) {
			modeloPuertos.addElement(puertosHabilitados[i]);
		}
		cmbPuertos.setModel(modeloPuertos);
	}

	public void leeParametros()
	{
		ArrayList<String> puerto =parametro.parametrosInicio();
		puertoConexion = ((String)puerto.get(0)).substring(7, 11);
		caracterInicio = ((String)puerto.get(1)).substring(15, 16);
		baudios = Integer.parseInt(((String)puerto.get(4)).substring(5, 9));
	}

	public void guardarLamina(ArrayList<String> nuevoList, ArrayList<String> repetidosList)
	{
		JPanel panelMesaje = new JPanel();
		panelMesaje.setBackground(new Color(240, 240, 240));
		cargaOperador();
		panelMesaje.add(new JLabel("<html><p style=\"color:blue; font:9px;\">¿Escoja el operador?<br></p></html>"));
		panelMesaje.add(cmbOperador);

		String maquinaElegida = null;

		int variable = JOptionPane.showOptionDialog(null, panelMesaje, "El Inlayer tiene " + repetidosList.size() + " chips almacenados, " + nuevoList.size() + " nuevos;", -1, 1, null,maquinas,maquinas[0]);
		switch (variable)
		{
		case 0: 
			insertarLamina(maquinas[0].toString(),cmbOperador.getSelectedItem().toString(), nuevoList.size(), repetidosList.size());
			break;
		case 1: 
			insertarLamina(maquinas[1].toString(),cmbOperador.getSelectedItem().toString(), nuevoList.size(), repetidosList.size());
			break;
		case 2: 
			insertarLamina(maquinas[2].toString(),cmbOperador.getSelectedItem().toString(), nuevoList.size(), repetidosList.size());
			break;
		case 3: 
			conexionArduino.resetLabel(labelList);
			btnGuardar.setEnabled(false);
			btnLeer.setEnabled(true);
			conexionArduino.resetLabel(labelList);
			break;	
		}
		conexionArduino.resetLabel(labelList);
	}

	public Boolean cargaOperador()
	{
		boolean operador=false;
		ResultSet rsOperadoresActivos =conexionBase.muestraTabla("select nombre from tbactivacinta ac join tbusuario u on ac.tbusuario_idtbUsuario=u.idtbUsuario and ac.fechacierre is null");
		try
		{
			cmbOperador.removeAllItems();
			while(rsOperadoresActivos.next())
			{
				cmbOperador.addItem(rsOperadoresActivos.getString(1));
				operador=true;
			};

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return operador;
	}

	public void insertarLamina(String maquina, String operador, int cantidadNuevos, int cantidadRepetidos)
	{
		int ultima = ultimaLamina();
		String datosOperador = "SELECT distinct (ac.idTbActivaCinta), ac.stock, ac.tbusuario_idtbUsuario"
				+ "  FROM tbactivacinta ac  join tbusuario u on u.idtbusuario = ac.tbusuario_idtbUsuario"
				+ " join tbcinta c on c.idtbCinta=ac.tbcinta_idtbCinta and u.Nombre='" + operador + "'" +" and c.estado like 'En proceso'";
		ResultSet rsDatosCinta =conexionBase.muestraTabla(datosOperador);
		try
		{
			if (rsDatosCinta.next())
			{
				int idcinta = Integer.parseInt(rsDatosCinta.getString(1));
				int stock = Integer.parseInt(rsDatosCinta.getString(2));
				if (stock >= cantidadNuevos)
				{
					if (cantidadNuevos > 0)
					{
						String cadenaBuenos = "";
						for (String cadenaNuevos :serialesNuevos) {
							cadenaBuenos = cadenaBuenos + "(now(),'" +txtCodigo.getText() + "'," + (ultima + 1) + ",'" + cadenaNuevos + "','" + maquina + "',"+txtTemperatura.getText()+","+txtHumedad.getText()+"," + idcinta + "),";
						}
						String cadenaInsertNuevos = "INSERT INTO tbregistro (`fecha_registro`, `cod_material`, `lamina`, `serial`, `maquina`, `Temperatura`, `humedad`, `TbActivaCinta_idTbActivaCinta`) VALUES " + 
								cadenaBuenos;

						conexionBase.Insertar(cadenaInsertNuevos.substring(0, cadenaInsertNuevos.length() - 1));
						conexionBase.Modificar("update tbactivacinta set stock= stock-" + cantidadNuevos + " where idtbactivacinta=" + idcinta);
					}
					if (cantidadRepetidos > 0)
					{
						String cadenaAlmacenados = "";
						for (String cadenaRepetidos :serialesRepetidos) {
							cadenaAlmacenados = cadenaAlmacenados + "'" + cadenaRepetidos + "',";
						}
						String cadenaUpdateSerial = "update tbregistro set fecha_registro=now(), lamina=" + (ultima + 1) + ", estado='Modificado' where serial in (" + cadenaAlmacenados + ")";

						conexionBase.Modificar(cadenaUpdateSerial.substring(0, cadenaUpdateSerial.length() - 2) + ")");
					}
					btnLeer.setEnabled(true);
					btnGuardar.setEnabled(false);
					conexionArduino.resetLabel(labelList);
				}
				else
				{
					cuadreCinta();
				}
				System.out.println("Realizada por  " + operador);
				System.out.println("hecha en  " + maquina);
				System.out.println("chips nuevos " + cantidadNuevos);
				System.out.println("chips modificados " + cantidadRepetidos);
				System.out.println("cinta " + idcinta);
				System.out.println("lámina" + (ultima + 1));
				System.out.println("stock " + stock);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public int ultimaLamina()
	{
		int maxLamina = 0;
		String maximaLamina = "select (case when (max(lamina) is null) then 0 else max(lamina) end) lamina from tbregistro";
		ResultSet rsMaximaLamina =conexionBase.muestraTabla(maximaLamina);
		try
		{
			if (rsMaximaLamina.next()) {
				maxLamina = Integer.parseInt(rsMaximaLamina.getString(1));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return maxLamina;
	}
	public void cuadreCinta()
	{
		ResultSet rsOperadoresActivos =conexionBase.muestraTabla("select nombre from tbactivacinta ac join tbusuario u on ac.tbusuario_idtbUsuario=u.idtbUsuario and ac.fechacierre is null;");
		try
		{
			if (rsOperadoresActivos.next())
			{
				if (rsOperadoresActivos.toString().length()>0)
				{
					FrmCierreCinta frmCierre= new FrmCierreCinta();
					frmCierre.setVisible(true);
				}
				else
				{
					JOptionPane.showMessageDialog(null,"No existe Operadores habilitados");
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null,"No existe Operadores habilitados");
			}
		}				

		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	public boolean validarTempHumedad()
	{
		boolean tempeHume;
		if ((Integer.parseInt(txtHumedad.getText())>=50 && Integer.parseInt(txtHumedad.getText())<=70 ) && (Integer.parseInt(txtTemperatura.getText())>=10 && Integer.parseInt(txtTemperatura.getText())<=40 ))
		{
			tempeHume = true;
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Revice el Termómetro y Hidrómetro \n   Parámetros fuera de intervalo");			
			tempeHume = false;
		}
		return tempeHume;

	}
}
