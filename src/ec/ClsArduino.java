package ec;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.awt.Color;
import java.awt.Font;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

public class ClsArduino implements SerialPortEventListener
{
	CommPortIdentifier portId;//guarda el Id del Puerto
	Enumeration listaPuertos;//guarda la lista de los puertos Disponibles
	InputStream input=null;//medio por donde llegaran los datos que envie el arduino    
	OutputStream output=null;//Escribir los datos en el puero SERIAL
	HashMap portMap=new HashMap();//Guarda los bytes que llegarán desde arduino
	SerialPort serialPort=null;//el puerto que se escoje 
	private JButton botonLeer;
	private JButton botonGuardar;
	boolean conectado = false;
	private String cadena = "";
	private String dato = "";
	private ArrayList<JLabel> recibeLabel;
	private ArrayList<String> serialesNuevos = new ArrayList();
	private ArrayList<String> serialReutilizadosList = new ArrayList();
	ClsBBDD conexionBaseArduino = new ClsBBDD();
	ClsParametros parametro = new ClsParametros();
	int baudio;
	int valorBarra=0;
	String baudios; 
	public String getCadena()
	{
		return cadena;
	}

	public void setCadena(String cadena)
	{
		cadena = cadena;
	}

	public String getDato()
	{
		return dato;
	}

	public void setDato(String dato)
	{
		dato = dato;
	}

	public void setConectado(boolean estado)
	{
		conectado = estado;
	}

	public boolean getConectado()
	{
		return conectado;
	}
	
	public void initListener()//implementación de un mépuertosEncontrados que permite escuchar  
	{
		try
		{
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		}
		catch (Exception e)
		{
			System.out.println("Error al leer");
		}
	}

	public void serialEvent(SerialPortEvent spe)
	{
		Thread inicioBarra=new Thread(){
			public void run(){

				try
				{
					byte datoSimple = (byte) input.read();// si existe datos, los lee !=10
					if (datoSimple !=64)
					{
						//System.out.println("binario:"+datoSimple);	
						String texto = new String(new byte[] {datoSimple});
						cadena += texto;
						valorBarra++;
						//System.out.println("valorBarra: "+valorBarra);
						FrmOperador.pgbLeer.setMaximum(400);
						FrmOperador.pgbLeer.setValue(valorBarra);	
						//System.out.println("cadena: "+cadena);
					}
					else
					{
						dato = cadena;	
						//getTramaSerial(dato);
						getTramaSerial(getDato());						
						valorBarra=0;
					}					
				}
				catch (Exception e)
				{
					System.out.println("ERROR lectura: " + e.getMessage());
					e.printStackTrace();
				}
			}	
		};
		//inicioBarra.start();
		if (spe.getEventType() ==SerialPortEvent.DATA_AVAILABLE)// se encarga de ver si hay bytes en el canal
		{
			inicioBarra.start();
		}
	}


	public void getTramaSerial(String datoCadena)
	{
		serialesNuevos.clear();
		serialReutilizadosList.clear();    
		//System.out.println(datoCadena);
		///////para usar con la máquina BT
		//datoCadena="0/1309093826090476/1309093823090476/1309092456090476/1309093825090476/1309093822090476/1309092464090476/1309093824090476/1309093821090476/1309092465090476/1309093827090476/1309093818090476/1309092466090476/1309094244090476/1309093819090476/1309093814090476/1309094245090476/1309093820090476/1309092468090476/1309094248090476/1309093817090476/1309092467090476/1309094247090476/1309093816090476/1309093815090476/@/";
		/*String datoCadenaCortada=datoCadena.substring(2, (datoCadena.length()-3));
		String cadenaConsulta = datoCadenaCortada.replace("/", "','");
		String cadenaSql = "select serial from tbregistro where serial in ('" + cadenaConsulta + "')";*/
		///////para usar con la placa Arduino
		String datoCadenaCortada=datoCadena.substring(2, (datoCadena.length()-1));
		String cadenaConsulta = datoCadenaCortada.replace("/", "','");
		String cadenaSql = "select serial from tbregistro where serial in ('" + cadenaConsulta + "')";
		/*System.out.println("dato cadena "+datoCadena);
		System.out.println("dato datoCadenaCortada "+datoCadenaCortada);
		System.out.println("cadena consulta "+cadenaConsulta);		
		System.out.println(cadenaSql);*/

		ArrayList<String> serialrepetidasList = buscarSerie(cadenaSql);

		int count = 0;
		//for (String serie : datoCadena.split("/")) {
		for (String serie : datoCadenaCortada.split("/")) {
			//if (!serie.equals("0")) 
			{
				if (serie.length() == 16)
				{
					if (serialrepetidasList.contains(serie))
					{
						validarSerial(serie, count, 1);
						serialReutilizadosList.add(serie);
					}
					else
					{
						validarSerial(serie, count, 2);
						serialesNuevos.add(serie);
					}
					count++;
				}
				else
				{
					validarSerial(serie, count, 3);
					count++;
				}
			}
		}
		botonGuardar.setEnabled(true);
	}

	public String obtenerLista()
	{
		String puertosEncontrados = "";
		listaPuertos = CommPortIdentifier.getPortIdentifiers();//nos trae la cantidad de puertos disponibles 	
		while (listaPuertos.hasMoreElements())
		{
			CommPortIdentifier puertoEscogido = (CommPortIdentifier)listaPuertos.nextElement();
			if (puertoEscogido.getPortType() ==CommPortIdentifier.PORT_SERIAL)
			{
				portMap.put(puertoEscogido.getName(), puertoEscogido);
				puertosEncontrados += puertoEscogido.getName() + ",";//com1,com2,com3,	
				System.out.println("Puerto encontrado: " + puertoEscogido.getName());
			}
		}
		return puertosEncontrados;
	}

	public void conectar(String puertoHabilitado, JButton botonLeer, JButton botonGuardar, int baudios)
	{
		this.botonLeer = botonLeer;
		this.botonGuardar = botonGuardar;
		this.baudio =baudios;
		portId = (CommPortIdentifier) portMap.get(puertoHabilitado);
		CommPort commport = null;
		try
		{
			commport = portId.open("TigerControlPanel", 1000);
			serialPort = (SerialPort) commport; //cast   baud=115200, baudArduino=9600
			serialPort.setSerialPortParams(baudio, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);	
			setConectado(true);
			botonLeer.setEnabled(true);
			System.out.println("COMUNICACIÓN EXITOSA");
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Verifique que la Máquina esté conectada");
			System.exit(1);
		}
	}

	public void desconectar()
	{
		try
		{
			serialPort.removeEventListener();
			serialPort.close();
			input.close();
			output.close();
			System.out.println("DESCONECTADO");
			setConectado(false);
		}
		catch (Exception e)
		{
			System.out.println("ERROR: " + e.getMessage());
		}
	}

	public boolean iniciarIO()
	{
		boolean exito = false;
		try
		{
			input = serialPort.getInputStream();
			output = serialPort.getOutputStream();
			exito = true;
		}
		catch (Exception e)
		{
			System.out.println("ERROR AL INICIAR IO " + e.toString());
			exito = false;
			e.printStackTrace();
		}
		return exito;
	}

	public void escribir(String caracterInicio)
	{
		try
		{
			cadena = "";
			dato = "";
			output.write(caracterInicio.getBytes());
			output.flush();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage() + "Caracter de  inicio incorrecto");
			e.printStackTrace();
		}
	}

	public void setLabels(ArrayList<JLabel> reciboLabels)
	{
		recibeLabel = reciboLabels;
	}

	public void setArrays(ArrayList<String> repetidosList, ArrayList<String> nuevosList)
	{
		serialesNuevos.clear();
		serialReutilizadosList.clear();
		serialesNuevos = nuevosList;
		serialReutilizadosList = repetidosList;
	}

	public void validarSerial(String serialValidar, int count, int color)
	{
		if (count <= 23)
		{
			JLabel etiqueta = (JLabel)recibeLabel.get(count);
			switch (color)
			{
			case 1: 
				etiqueta.setText("<html><center>Serie:  " + serialValidar + "<br/> Serial Repetido</center> <html/>");
				etiqueta.setOpaque(true);
				etiqueta.setBackground(new Color(255, 255, 0));
				break;
			case 2: 
				etiqueta.setText("<html><center>Serie:  " + serialValidar + "<br/> Serial Nuevo</center> <html/>");
				etiqueta.setOpaque(true);
				etiqueta.setBackground(new Color(0, 255, 0));
				break;
			case 3: 
				etiqueta.setText("<html><center> Chip no existe <br/> ó Sin soldar </center> <html/>");
				//D:\\universidad\\ProyectoFinal\\prueba\\pruebaCadena
				etiqueta.setIcon(new ImageIcon("imagen\\chipError.jpg"));
			}
		}
	}

	public ArrayList<String> buscarSerie(String consulta)
	{
		serialReutilizadosList.clear();
		ResultSet rsBuscarSerial = conexionBaseArduino.muestraTabla(consulta);
		ArrayList<String> serialesRepetidos = new ArrayList();
		try
		{
			while (rsBuscarSerial.next()) {
				serialesRepetidos.add(rsBuscarSerial.getString(1));
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return serialesRepetidos;
	}
	public void resetLabel(ArrayList<JLabel> recibeLabel)
	{
		int count=1;
		for (JLabel etiqueta :recibeLabel)
		{
			etiqueta.setText("<html><center>"+count+"<html/>");
			etiqueta.setFont(new Font("Tahoma", 0, 10));
			etiqueta.setBackground(null);
			etiqueta.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			etiqueta.setIcon(null);
			count++;
		}	
	}
	}