package ec;

import java.awt.event.KeyEvent;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class ClsBBDD
{
	String _url = "jdbc:mysql://localhost:3306/verificacion";
	Connection conexion = null;

	public Connection conectar()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			conexion = DriverManager.getConnection(this._url, "root", "root");
			if (conexion != null) {
				System.out.println("Conexion a base de datos " + _url + " . . . Ok");
			} else {
				System.out.println("Hubo un problema al intentar conectarse a la base de datos" + _url);
			}
		}
		catch (SQLException ex)
		{
			System.out.println(ex);
		}
		catch (ClassNotFoundException ex)
		{
			System.out.println(ex);
		}
		return this.conexion;
	}

	public ResultSet muestraTabla(String consulta)
	{
		conectar();
		Statement state = null;
		ResultSet resultado = null;
		try
		{
			state = conexion.createStatement();
			resultado = state.executeQuery(consulta);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			try
			{
				conexion.close();
				state.close();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
		}
		return resultado;
	}

	public void Insertar(String sQL)
	{
		conectar();
		Statement sentencia = null;
		try
		{
			sentencia = conexion.createStatement();
			boolean respuesta = sentencia.execute(sQL);
			if (respuesta = true)
					{	
						JOptionPane.showMessageDialog(null, "Registro ingresado");
					} else {
						JOptionPane.showMessageDialog(null, "Registro no ingresado");
					}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	public void Delete(String sQL)
	{
		conectar();
		Statement sentencia = null;
		try
		{
			sentencia = conexion.createStatement();
			boolean respuesta = sentencia.execute(sQL);
			if (respuesta = true)
					{	
						JOptionPane.showMessageDialog(null, "Registro borrado");
					} else {
						JOptionPane.showMessageDialog(null, "Registro no borrado");
					}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void Modificar(String sQL)
	{
		conectar();
		Statement sentencia = null;
		try
		{
			sentencia = conexion.createStatement();
			int respuesta = sentencia.executeUpdate(sQL);
			if (respuesta != 0) {
				JOptionPane.showMessageDialog(null, "Registro Actualizado con éxito");
			} else {
				JOptionPane.showMessageDialog(null, "Registro  no Actualizado ");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void cargaTabla(JTable tbCarga, String consulta, DefaultTableModel modelo)
	{
		ResultSet respuesta = muestraTabla(consulta);

		tbCarga.getAutoResizeMode();
		DefaultTableCellRenderer centrar = new DefaultTableCellRenderer();
		centrar.setHorizontalAlignment(0);
		try
		{
			while (respuesta.next())
			{
				Object[] fila = new Object[modelo.getColumnCount()];
				for (int i = 0; i < modelo.getColumnCount(); i++) {
					fila[i] = respuesta.getString(i + 1);
				}
				modelo.addRow(fila);
			}
			tbCarga.setModel(modelo);
			for (int i = 0; i < modelo.getColumnCount(); i++) {
				tbCarga.getColumnModel().getColumn(i).setCellRenderer(centrar);
			}
		}
		catch (Exception localException) {}
	}

	public void blanquearTabla(JTable tablaBlanquear, DefaultTableModel modelo)
	{
		for (int i = 0; i < tablaBlanquear.getRowCount(); i++)
		{
			modelo.removeRow(i);
			i--;
		}
	}

	public void BuscarRegistro(String consulta, JTextField txtfield)
	{
		ResultSet busca = muestraTabla(consulta);
		try
		{
			if (busca.next())
			{
				JOptionPane.showMessageDialog(null, "Registro existente");
				txtfield.setText("");
			}
		}
		catch (Exception localException) {}
	}

	public void ValidarNumero(KeyEvent evt)
	{
		if ((!Character.isDigit(evt.getKeyChar())) && (!Character.isISOControl(evt.getKeyChar()))) {
			evt.consume();
		}
	}
	public void ValidarLetra(KeyEvent evt)
	{
		if ((Character.isDigit(evt.getKeyChar())) ) {
			evt.consume();
		}
	}
}
