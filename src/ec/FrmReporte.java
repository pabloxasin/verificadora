package ec;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JRadioButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.beans.PropertyChangeEvent;

public class FrmReporte extends JFrame {

	private JPanel contentPane;
	ClsParametros parametros= new ClsParametros();
	ButtonGroup radioButton;
	String fechaFin;
	String fechaInicio;
	ClsBBDD conexionBase= new ClsBBDD();
	SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
	JRadioButton rdbOperador = new JRadioButton("Por Operador");
	JRadioButton rdbProduccion = new JRadioButton("Por Producci\u00F3n");
	JRadioButton rdbPorCinta = new JRadioButton("Ingrese Cinta");
	JRadioButton rdbEnProceso = new JRadioButton("En Proceso");
	JRadioButton rdbCerradas = new JRadioButton("Cerradas");
	JRadioButton rdbAbierta = new JRadioButton("Abiertas");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmReporte frame = new FrmReporte();
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
	public FrmReporte() {
		setResizable(false);
		setTitle("Rango de fechas para reportes.");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 422, 339);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);		

		JDateChooser dtcFinal = new JDateChooser();
		dtcFinal.getCalendarButton().setFont(new Font("Tahoma", Font.BOLD, 12));
		dtcFinal.setBounds(282, 221, 103, 20);
		contentPane.add(dtcFinal);

		JDateChooser dtcInicio = new JDateChooser();
		dtcInicio.getCalendarButton().setFont(new Font("Tahoma", Font.BOLD, 12));
		dtcInicio.setBounds(88, 221, 103, 20);
		contentPane.add(dtcInicio);

		JLabel lblNewLabel = new JLabel("Inicio:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setBounds(37, 221, 52, 14);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Fin:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1.setBounds(240, 221, 28, 14);
		contentPane.add(lblNewLabel_1);

		JButton btnEjecutar = new JButton("Visualizar");
		btnEjecutar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (dtcInicio.getDate()!=null && dtcFinal.getDate()!=null)
				{
					Calendar inicio=dtcInicio.getCalendar();
					Calendar fin=dtcFinal.getCalendar();
					if (inicio.before(fin)||inicio.equals(fin)) 
					{
						fechaFin=sdf.format(dtcFinal.getDate());
						fechaInicio=sdf.format(dtcInicio.getDate());
						if (rdbOperador.isSelected())
						{
							llamarReporte(fechaFin,fechaInicio,"C:\\maquinaBT\\utilitarios\\jasper\\rptCintaOperador.jasper","Operador");
						}
						else if (rdbProduccion.isSelected())
						{
							llamarReporte(fechaFin,fechaInicio,"C:\\maquinaBT\\utilitarios\\jasper\\rptProduccion.jasper\\","Producción");
						}
						else if (rdbAbierta.isSelected())
						{
							llamarReporte(fechaFin,fechaInicio,"C:\\maquinaBT\\utilitarios\\jasper\\rptEstado.jasper","Abierta");
						}
						else if (rdbCerradas.isSelected())
						{
							llamarReporte(fechaFin,fechaInicio,"C:\\maquinaBT\\utilitarios\\jasper\\rptEstado.jasper","Cerrada");
						}
						else if (rdbEnProceso.isSelected())
						{
							llamarReporte(fechaFin,fechaInicio,"C:\\maquinaBT\\utilitarios\\jasper\\rptEstado.jasper","En Proceso");
						}
						else if (rdbPorCinta.isSelected())
						{
							//llamarReporte(fechaFin,fechaFin,"C:\\maquinaBT\\utilitarios\\jasper\\rptCintaOperador.jasper");
							String numCinta = JOptionPane.showInputDialog("Número de la cinta?");
					        //System.out.println("Número de cinta "+Integer.valueOf(numCinta));rptCuadreNumCinta
							llamarReporte(fechaFin,fechaInicio,"C:\\maquinaBT\\utilitarios\\jasper\\rptCuadreNumCinta.jasper",numCinta);
						}
					}
					else 
					{
						JOptionPane.showMessageDialog(null,"Rango de busqueda incorrecto!!");
					}
				}
				else
					JOptionPane.showMessageDialog(null,"Escoja un rango correcto!!");
			}
		});
		btnEjecutar.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnEjecutar.setBounds(128, 263, 135, 36);
		contentPane.add(btnEjecutar);

		JLabel lblNewLabel_2 = new JLabel("Escoja la fecha el rango de fechas:");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(0, 98, 403, 19);
		contentPane.add(lblNewLabel_2);

		
		rdbOperador.setOpaque(false);
		rdbOperador.setFont(new Font("Tahoma", Font.BOLD, 11));
		rdbOperador.setBounds(72, 130, 130, 23);
		contentPane.add(rdbOperador);

		rdbProduccion.setOpaque(false);
		rdbProduccion.setFont(new Font("Tahoma", Font.BOLD, 11));
		rdbProduccion.setBounds(222, 130, 136, 23);
		contentPane.add(rdbProduccion);
		
		
		rdbCerradas.setOpaque(false);
		rdbCerradas.setFont(new Font("Tahoma", Font.BOLD, 11));
		rdbCerradas.setBounds(72, 155, 130, 23);
		contentPane.add(rdbCerradas);
		
		
		rdbAbierta.setOpaque(false);
		rdbAbierta.setFont(new Font("Tahoma", Font.BOLD, 12));
		rdbAbierta.setBounds(221, 156, 130, 23);
		contentPane.add(rdbAbierta);
		
		
		rdbEnProceso.setOpaque(false);
		rdbEnProceso.setFont(new Font("Tahoma", Font.BOLD, 11));
		rdbEnProceso.setBounds(72, 180, 130, 23);
		contentPane.add(rdbEnProceso);
		rdbPorCinta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					dtcInicio.setDate(sdf.parse("2022-01-01"));
					dtcFinal.setDate(sdf.parse("2022-01-01"));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		
		rdbPorCinta.setOpaque(false);
		rdbPorCinta.setFont(new Font("Tahoma", Font.BOLD, 11));
		rdbPorCinta.setBounds(222, 181, 130, 23);
		contentPane.add(rdbPorCinta);

		JLabel lblReporte = new JLabel("");
		lblReporte.setBounds(0, 0, 416, 310);
		parametros.SetImagenLabel(lblReporte, "C:\\maquinaBT\\utilitarios\\imagen\\reporte.png");
		contentPane.add(lblReporte);

		radioButton = new ButtonGroup();
		radioButton.add(rdbProduccion);
		radioButton.add(rdbOperador);
		radioButton.add(rdbAbierta);
		radioButton.add(rdbCerradas);
		radioButton.add(rdbEnProceso);
		radioButton.add(rdbPorCinta);
	}
	
	public void llamarReporte(String fin,String inicio,String path,String estado)
	{
		try
		{
			Map parametros = new HashMap();
			parametros.put("inicio", inicio);
			parametros.put("fin", fin);
			parametros.put("estado", estado);
			JasperReport repor = (JasperReport)JRLoader.loadObjectFromFile(path);
			JasperPrint repoImpreso = JasperFillManager.fillReport(repor, parametros, conexionBase.conectar());
			JasperViewer visor = new JasperViewer(repoImpreso, false);
			visor.setVisible(true);
			JasperExportManager.exportReportToPdfFile(repoImpreso, "C:\\maquinaBT\\reporte\\reporte"+estado+"_" +LocalDate.now()+ ".pdf");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
