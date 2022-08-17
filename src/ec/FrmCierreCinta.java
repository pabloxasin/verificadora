package ec;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Color;

public class FrmCierreCinta extends JFrame
{
	ClsBBDD conexionBase = new ClsBBDD();
	JComboBox cmbOperador = new JComboBox();
	DefaultTableModel modelo = new DefaultTableModel();
	private JPanel contentPane;
	private JTable tbDatosOperador;
	private JTextField txtDañados;
	private JTextField txtNokFabrica;
	Boolean carga=false;
	ClsParametros parametros = new ClsParametros();

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					FrmCierreCinta frame = new FrmCierreCinta();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public FrmCierreCinta()
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\maquinaBT\\utilitarios\\imagen\\ICONO.png"));
		setTitle("Datos de Cierre de cinta");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 508, 378);
		contentPane = new JPanel();
		
		cargaOperador();
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("CIERRE DE CINTA DE PRODUCCIÓN");
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel.setHorizontalAlignment(0);
		lblNewLabel.setBounds(10, 22, 323, 24);
		contentPane.add(lblNewLabel);

		JLabel lblOperador = new JLabel("Operador:");
		lblOperador.setForeground(new Color(0, 0, 0));
		lblOperador.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblOperador.setBounds(34, 69, 69, 14);
		contentPane.add(lblOperador);
		cmbOperador.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				cargaTabla();
			}
		});
		cmbOperador.setFont(new Font("Tahoma", 0, 11));

		cmbOperador.setBounds(102, 66, 185, 20);
		contentPane.add(cmbOperador);
		
				
		JLabel lblDatosDeLa = new JLabel("Datos de la cinta Activa");
		lblDatosDeLa.setForeground(new Color(0, 0, 0));
		lblDatosDeLa.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDatosDeLa.setHorizontalAlignment(0);
		lblDatosDeLa.setBounds(10, 108, 307, 14);
		contentPane.add(lblDatosDeLa);

		JScrollPane scpDatosCinta = new JScrollPane();
		scpDatosCinta.setBounds(20, 137, 297, 40);
		contentPane.add(scpDatosCinta);

		tbDatosOperador = new JTable();
		tbDatosOperador.setFont(new Font("Tahoma", Font.PLAIN, 11));
		tbDatosOperador.setEnabled(false);
		tbDatosOperador.setBounds(40, 182, 289, 14);
		scpDatosCinta.setViewportView(tbDatosOperador);

		JLabel lblChipsDaados = new JLabel("Chips Dañados:");
		lblChipsDaados.setForeground(new Color(0, 0, 0));
		lblChipsDaados.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblChipsDaados.setBounds(10, 198, 87, 14);
		contentPane.add(lblChipsDaados);

		txtDañados = new JTextField();
		txtDañados.setFont(new Font("Tahoma", 0, 11));
		txtDañados.setBounds(102, 195, 38, 20);
		contentPane.add(txtDañados);
		txtDañados.setColumns(10);
		
		JButton btnCerrar = new JButton("CERRAR CINTA");
		btnCerrar.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnCerrar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (carga) 
				{
					String operadorCierre = "select clave from tbusuario where nombre='" + cmbOperador.getSelectedItem().toString() + "'";
					ResultSet rsOperadorCierre = conexionBase.muestraTabla(operadorCierre);
					try
					{
						if(rsOperadorCierre.next())
						{
							String clave = JOptionPane.showInputDialog("Ingrese su Clave");
							if (rsOperadorCierre.getString(1).equals(clave))
							{
								if ((!txtDañados.getText().isEmpty()) && (!txtNokFabrica.getText().isEmpty()))
								{
									int chipsSobran = Integer.parseInt(tbDatosOperador.getModel().getValueAt(0, 3).toString());
									int chipsTotalBuenos = Integer.parseInt(tbDatosOperador.getModel().getValueAt(0, 2).toString());
									int chipsTotalCinta = Integer.parseInt(tbDatosOperador.getModel().getValueAt(0, 1).toString());
									int dañados = Integer.parseInt(txtDañados.getText().toString());
									int malosFabrica = Integer.parseInt(txtNokFabrica.getText().toString());
									int chipsBuenosSobran = chipsSobran - dañados;
									int laminasSobran = chipsBuenosSobran / 24;
									int chipsSueltosSobran = chipsBuenosSobran % 24;
									if ((chipsTotalCinta - chipsTotalBuenos == malosFabrica) && (chipsSobran >= dañados))
									{
										if (laminasSobran >= 1)
										{
											JOptionPane.showMessageDialog(null, "Usted puede elaborar:\n       Láminas " + 
													laminasSobran + "\n " + 
													"     Chips Sueltos " + chipsSueltosSobran + "\n " + 
													"!! No puede cerrar la cinta !!");
											txtDañados.setText("");
											txtNokFabrica.setText("");
										}
										else
										{
											int confirmado = JOptionPane.showConfirmDialog(null, "Chips sobrantes " + (chipsSobran - dañados), "Cinta cerrada", laminasSobran);
											if (confirmado == 0)
											{
												String actualizaCinta = "UPDATE tbcinta SET estado = 'Cerrada',fecha_cierre=now() WHERE num_cinta = '" +tbDatosOperador.getModel().getValueAt(0, 0) + "'";
												String actualizaUsuario = "UPDATE tbusuario SET `Estado` = 'Inactivo' WHERE nombre = '" + cmbOperador.getSelectedItem().toString() + "'";
												String actualizaActivaCinta = "UPDATE tbactivacinta SET `fechaCierre` = now(),nok_fabrica = '" + malosFabrica + "', nok_maquina = '" + dañados + "',stock=stock-"+dañados+ 
														" WHERE tbcinta_idtbcinta=(select idtbcinta from tbcinta where num_cinta='" + tbDatosOperador.getModel().getValueAt(0, 0) + "')";
												System.out.println(actualizaActivaCinta);

												conexionBase.Modificar(actualizaActivaCinta);
												conexionBase.Modificar(actualizaUsuario);
												llamarReporte(cmbOperador.getSelectedItem().toString());                      
												conexionBase.Modificar(actualizaCinta);
												dispose();
												//System.out.println(cmbOperador.getSelectedItem().toString());
											}
											else
											{
												JOptionPane.showMessageDialog(null, "Revise los datos ingresados");
												txtDañados.setText("");
												txtNokFabrica.setText("");
											}
										}
									}
									else
									{
										JOptionPane.showMessageDialog(null, "Verifique los datos ingresados");
										txtDañados.setText("");
										txtNokFabrica.setText("");
									}
								}
								else
								{
									JOptionPane.showMessageDialog(null, "Ingrese las cantidades");
								}
							}
							else 
							{
								JOptionPane.showMessageDialog(null, "Clave incorrecta");
							}
						}
						else
						{
							JOptionPane.showMessageDialog(null, "mal pesimo");
						}
					}
					catch (SQLException e1)
					{
						//e1.printStackTrace();
						
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "No hay cintas que cerrar");
					dispose();
				}
			}
		});
		btnCerrar.setBounds(19, 256, 110, 24);
		contentPane.add(btnCerrar);

		JButton btnCancelar = new JButton("CANCELAR");
		btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnCancelar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();        
			}
		});
		btnCancelar.setBounds(208, 257, 113, 23);
		contentPane.add(btnCancelar);

		JLabel lblChipsNokFbrica = new JLabel("Chips Nok Fábrica:");
		lblChipsNokFbrica.setForeground(new Color(0, 0, 0));
		lblChipsNokFbrica.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblChipsNokFbrica.setBounds(161, 198, 112, 14);
		contentPane.add(lblChipsNokFbrica);

		txtNokFabrica = new JTextField();
		txtNokFabrica.setFont(new Font("Tahoma", 0, 11));
		txtNokFabrica.setColumns(10);
		txtNokFabrica.setBounds(272, 195, 46, 20);
		contentPane.add(txtNokFabrica);

		JLabel lblCierre = new JLabel("");
		lblCierre.setBounds(0, -3, 492, 342);
		parametros.SetImagenLabel(lblCierre, "C:\\maquinaBT\\utilitarios\\imagen\\cierre.png");
		contentPane.add(lblCierre);

		//SetImagenLabel(lblFondo, "imagen\\cedCapas_6.jpg");		 

		modelo.setColumnIdentifiers(new Object[] { "<html><p style=\"color:blue; font:9px;\">Num_Cinta</p></html>", 
				"<html><p style=\"color:blue; font:9px;\">Total Chips</p></html>", 
				"<html><p style=\"color:blue; font:9px;\">Total Ok</p></html>", 
		"<html><p style=\"color:blue; font:9px;\">Sobrantes</p></html>" });
	}


	public void cargaOperador()
	{
		
		ResultSet rsOperadoresActivos =conexionBase.muestraTabla("select nombre from tbactivacinta ac join tbusuario u on ac.tbusuario_idtbUsuario=u.idtbUsuario and ac.fechacierre is null");

		try
		{
			while (rsOperadoresActivos.next())
			{
				carga=true;
				cmbOperador.addItem(rsOperadoresActivos.getString(1));
				
			}				
			//rsOperadoresActivos.close();				
		}
		catch (SQLException e)
		{
			System.out.println(e);
			JOptionPane.showMessageDialog(null, "No existe operador disponible");
		}
		

	}


	public void cargaTabla()
	{
		for (int i = 0; i < tbDatosOperador.getRowCount(); i++)
		{
			modelo.removeRow(i);
			i--;
		}
		String datosCinta = "select c.num_cinta, c.total_chips, c.total_buenos, ac.stock from tbcinta c "
				+ "join tbactivacinta ac on c.idtbCinta=ac.tbcinta_idtbCinta "
				+ "join tbusuario u on ac.tbusuario_idtbUsuario=u.idtbUsuario and c.estado='En proceso' and u.nombre like '" + cmbOperador.getSelectedItem() + "'";
		System.out.println(datosCinta);
		conexionBase.cargaTabla(tbDatosOperador, datosCinta, modelo);
	}

	public void llamarReporte(String operador)
	{
		String path = "reporte\\rptCuadre.jasper";
		try
		{
			Map parametros = new HashMap();
			parametros.put("nombre", operador);
			parametros.put("fabrica",txtNokFabrica.getText());
			parametros.put("dañados",txtDañados.getText());
			JasperReport repor = (JasperReport)JRLoader.loadObjectFromFile(path);
			JasperPrint repoImpreso = JasperFillManager.fillReport(repor, parametros, conexionBase.conectar());
			JasperViewer visor = new JasperViewer(repoImpreso, false);
			visor.setVisible(true);
			JasperExportManager.exportReportToPdfFile(repoImpreso, "C:\\maquinaBT\\reporte\\cuadreCinta" + tbDatosOperador.getModel().getValueAt(0, 0).toString() + ".pdf");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
