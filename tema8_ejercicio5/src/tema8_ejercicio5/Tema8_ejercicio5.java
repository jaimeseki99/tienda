package tema8_ejercicio5;

import java.awt.EventQueue;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

class ConnectionSingleton {
	private static Connection conection;
	
	public static Connection getConnection() throws SQLException {
		String url="jdbc:mysql://127.0.0.1:3307/tienda";
		String user="alumno";
		String password="alumno";
		
		if (conection==null || conection.isClosed()) {
			conection=DriverManager.getConnection(url, user, password);
		}
		return conection;
	}
}

public class Tema8_ejercicio5 {

	private JFrame frame;
	private JTable tableProducto;
	private JTextField textFieldCodigo;
	private JTextField textFieldNombre;
	private JTextField textFieldPrecio;
	private JTextField textFieldUnidades;
	private JTextField textFieldNuevoPrecio;
	private JTextField textFieldStock;
	private JTextField textFieldNumeroUds;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tema8_ejercicio5 window = new Tema8_ejercicio5();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Tema8_ejercicio5() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 764, 671);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Código");
		model.addColumn("Nombre");
		model.addColumn("Precio");
		model.addColumn("Unidades");
		
		JButton btnMostrar = new JButton("Mostrar");
		btnMostrar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Connection conexion = ConnectionSingleton.getConnection();
					Statement stmt = conexion.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM producto");
					while (rs.next()) {
						Object[] filas = new Object[4];
						filas[0]=rs.getInt("codigo");
						filas[1]=rs.getString("nombre");
						filas[2]=rs.getDouble("precio");
						filas[3]=rs.getInt("unidades");
						model.addRow(filas);
					}
				} catch(SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnMostrar.setBounds(57, 15, 117, 29);
		frame.getContentPane().add(btnMostrar);
		
		
		JTable tableProducto = new JTable(model);
		tableProducto.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		frame.getContentPane().add(tableProducto);
		
		JScrollPane scrollpane = new JScrollPane(tableProducto);
		scrollpane.setBounds(30, 56, 166, 79);
		frame.getContentPane().add(scrollpane);
		
		JLabel lblNuevoProducto = new JLabel("Nuevo Producto");
		lblNuevoProducto.setBounds(45, 147, 117, 16);
		frame.getContentPane().add(lblNuevoProducto);
		
		textFieldCodigo = new JTextField();
		textFieldCodigo.setBounds(57, 177, 66, 26);
		frame.getContentPane().add(textFieldCodigo);
		textFieldCodigo.setColumns(10);
		
		JLabel lblCodigo = new JLabel("Código");
		lblCodigo.setBounds(6, 181, 61, 16);
		frame.getContentPane().add(lblCodigo);
		
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(135, 181, 61, 16);
		frame.getContentPane().add(lblNombre);
		
		textFieldNombre = new JTextField();
		textFieldNombre.setColumns(10);
		textFieldNombre.setBounds(200, 177, 66, 26);
		frame.getContentPane().add(textFieldNombre);
		
		JLabel lblPrecio = new JLabel("Precio");
		lblPrecio.setBounds(6, 216, 61, 16);
		frame.getContentPane().add(lblPrecio);
		
		textFieldPrecio = new JTextField();
		textFieldPrecio.setColumns(10);
		textFieldPrecio.setBounds(57, 212, 66, 26);
		frame.getContentPane().add(textFieldPrecio);
		
		textFieldUnidades = new JTextField();
		textFieldUnidades.setColumns(10);
		textFieldUnidades.setBounds(200, 212, 66, 26);
		frame.getContentPane().add(textFieldUnidades);
		
		JLabel lblUnidades = new JLabel("Uds");
		lblUnidades.setBounds(145, 216, 61, 16);
		frame.getContentPane().add(lblUnidades);
		
		JButton btnAnyadir = new JButton("Añadir producto");
		btnAnyadir.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Connection conexion = ConnectionSingleton.getConnection();
					PreparedStatement insert_pstmt = conexion.prepareStatement("INSERT INTO producto (codigo, nombre, precio, unidades) VALUES (?, ?, ?, ?)");
					insert_pstmt.setInt(1, Integer.parseInt(textFieldCodigo.getText()));
					insert_pstmt.setString(2, textFieldNombre.getText());
					insert_pstmt.setDouble(3, Double.parseDouble(textFieldPrecio.getText()));
					insert_pstmt.setInt(4, Integer.parseInt(textFieldUnidades.getText()));
					int rows_inserted = insert_pstmt.executeUpdate();
					btnMostrar.doClick();
					textFieldCodigo.setText("");
					textFieldNombre.setText("");
					textFieldPrecio.setText("");
					textFieldUnidades.setText("");
					JOptionPane.showMessageDialog(null, "Producto añadido con éxito");
					insert_pstmt.close();
					conexion.close();
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnAnyadir.setBounds(45, 244, 181, 29);
		frame.getContentPane().add(btnAnyadir);
		
		JLabel lblBorrarProducto = new JLabel("Borrar producto");
		lblBorrarProducto.setBounds(20, 296, 142, 16);
		frame.getContentPane().add(lblBorrarProducto);
		
		JLabel lblEligeProducto = new JLabel("Elige Producto");
		lblEligeProducto.setBounds(20, 339, 142, 16);
		frame.getContentPane().add(lblEligeProducto);
		
		JComboBox comboBoxProd1 = new JComboBox();
		try {
			Connection conexion = ConnectionSingleton.getConnection();
			Statement stmt = conexion.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT codigo FROM producto");
			while (rs.next()) {
				comboBoxProd1.addItem(rs.getInt("codigo"));
			}
			rs.close();
			stmt.close();
			conexion.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		comboBoxProd1.setBounds(135, 334, 52, 27);
		frame.getContentPane().add(comboBoxProd1);
		
		JButton btnEliminarProducto = new JButton("Eliminar");
		btnEliminarProducto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Connection conexion = ConnectionSingleton.getConnection();
					PreparedStatement delete_pstmt = conexion.prepareStatement("DELETE FROM producto WHERE codigo=?");
					delete_pstmt.setInt(1, Integer.parseInt(comboBoxProd1.getSelectedItem().toString()));
					int rowsDeleted=delete_pstmt.executeUpdate();
					comboBoxProd1.setSelectedIndex(-1);
					JOptionPane.showMessageDialog(null, "Producto borrado satisfactoriamente");
					btnMostrar.doClick();
					delete_pstmt.close();
					conexion.close();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnEliminarProducto.setBounds(45, 381, 117, 29);
		frame.getContentPane().add(btnEliminarProducto);
		
		JLabel lblActuPrecio = new JLabel("Actualizar precio");
		lblActuPrecio.setBounds(440, 13, 181, 16);
		frame.getContentPane().add(lblActuPrecio);
		
		JLabel lblEligeProducto2 = new JLabel("Elige Producto");
		lblEligeProducto2.setBounds(417, 43, 117, 16);
		frame.getContentPane().add(lblEligeProducto2);
		
		JComboBox comboBoxProd2 = new JComboBox();
		try {
			Connection conexion = ConnectionSingleton.getConnection();
			Statement stmt = conexion.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT codigo FROM producto");
			while (rs.next()) {
				comboBoxProd2.addItem(rs.getInt("codigo"));
			}
			rs.close();
			stmt.close();
			conexion.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		comboBoxProd2.setBounds(542, 38, 66, 27);
		frame.getContentPane().add(comboBoxProd2);
		
		JLabel lblNuevoPrecio = new JLabel("Nuevo precio");
		lblNuevoPrecio.setBounds(406, 92, 94, 16);
		frame.getContentPane().add(lblNuevoPrecio);
		
		textFieldNuevoPrecio = new JTextField();
		textFieldNuevoPrecio.setBounds(518, 88, 66, 26);
		frame.getContentPane().add(textFieldNuevoPrecio);
		textFieldNuevoPrecio.setColumns(10);
		
		JButton btnActualizarPrecio = new JButton("Actualizar precio");
		btnActualizarPrecio.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Connection conexion = ConnectionSingleton.getConnection();
					PreparedStatement update_pstmt = conexion.prepareStatement("UPDATE producto SET precio=? WHERE codigo=?");
					update_pstmt.setDouble(1, Double.parseDouble(textFieldNuevoPrecio.getText()));
					update_pstmt.setInt(2, Integer.parseInt(comboBoxProd2.getSelectedItem().toString()));
					int rowsUpdated=update_pstmt.executeUpdate();
					comboBoxProd2.setSelectedIndex(-1);
					textFieldNuevoPrecio.setText("");
					JOptionPane.showMessageDialog(null, "Precio actualizado satisfactoriamente");
					btnMostrar.doClick();
					update_pstmt.close();
					conexion.close();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnActualizarPrecio.setBounds(406, 141, 194, 29);
		frame.getContentPane().add(btnActualizarPrecio);
		
		JLabel lblIncremento = new JLabel("Incrementar stock");
		lblIncremento.setBounds(368, 238, 132, 16);
		frame.getContentPane().add(lblIncremento);
		
		JLabel lblEligeProducto3 = new JLabel("Elige producto");
		lblEligeProducto3.setBounds(376, 196, 124, 16);
		frame.getContentPane().add(lblEligeProducto3);
		
		JComboBox comboBoxProd3 = new JComboBox();
		try {
			Connection conexion = ConnectionSingleton.getConnection();
			Statement stmt = conexion.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT codigo FROM producto");
			while (rs.next()) {
				comboBoxProd3.addItem(rs.getInt("codigo"));
			}
			rs.close();
			stmt.close();
			conexion.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		comboBoxProd3.setBounds(518, 191, 68, 27);
		frame.getContentPane().add(comboBoxProd3);
		
		JLabel lblStock = new JLabel("Nuevas uds");
		lblStock.setBounds(281, 197, 87, -16);
		frame.getContentPane().add(lblStock);
		
		textFieldStock = new JTextField();
		textFieldStock.setBounds(506, 234, 89, 26);
		frame.getContentPane().add(textFieldStock);
		textFieldStock.setColumns(10);
		
		JButton btnIncrStock = new JButton("Incrementar Stock");
		btnIncrStock.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Connection conexion = ConnectionSingleton.getConnection();
					PreparedStatement update_pstmt = conexion.prepareStatement("UPDATE producto SET unidades=unidades+? WHERE codigo = ?");
					update_pstmt.setInt(1, Integer.parseInt(textFieldStock.getText()));
					update_pstmt.setInt(2, Integer.parseInt(comboBoxProd3.getSelectedItem().toString()));
					int rowsUpdated=update_pstmt.executeUpdate();
					JOptionPane.showMessageDialog(null, "Stock ampliado con éxito");
					textFieldStock.setText("");
					comboBoxProd3.setSelectedIndex(-1);
					btnMostrar.doClick();
					update_pstmt.close();
					conexion.close();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnIncrStock.setBounds(387, 296, 223, 29);
		frame.getContentPane().add(btnIncrStock);
		
		JLabel lblVenta = new JLabel("Venta");
		lblVenta.setBounds(376, 358, 70, 15);
		frame.getContentPane().add(lblVenta);
		
		JLabel lblEligeProducto4 = new JLabel("Elige producto");
		lblEligeProducto4.setBounds(376, 388, 124, 15);
		frame.getContentPane().add(lblEligeProducto4);
		
		JComboBox comboBoxProd4 = new JComboBox();
		try {
			Connection conexion = ConnectionSingleton.getConnection();
			Statement stmt = conexion.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT codigo FROM producto");
			while (rs.next()) {
				comboBoxProd4.addItem(rs.getInt("codigo"));
			}
			rs.close();
			stmt.close();
			conexion.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		comboBoxProd4.setBounds(506, 381, 66, 24);
		frame.getContentPane().add(comboBoxProd4);
		
		JLabel lblUdsVendidas = new JLabel("Unidades a vender");
		lblUdsVendidas.setBounds(349, 422, 163, 15);
		frame.getContentPane().add(lblUdsVendidas);
		
		textFieldNumeroUds = new JTextField();
		textFieldNumeroUds.setBounds(506, 417, 114, 34);
		frame.getContentPane().add(textFieldNumeroUds);
		textFieldNumeroUds.setColumns(10);
		
		JButton btnVenderProducto = new JButton("Vender");
		btnVenderProducto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Connection conexion = ConnectionSingleton.getConnection();
					PreparedStatement update_stmt = conexion.prepareStatement("UPDATE producto SET unidades=unidades-? WHERE codigo=?");
					update_stmt.setInt(1, Integer.parseInt(textFieldNumeroUds.getText()));
					update_stmt.setInt(2, Integer.parseInt(comboBoxProd4.getSelectedItem().toString()));
					int rowsUpdated=update_stmt.executeUpdate();
					int dineroVentas;
					PreparedStatement select_stmt = conexion.prepareStatement("SELECT precio FROM producto WHERE codigo=?");
					select_stmt.setInt(1, Integer.parseInt(comboBoxProd4.getSelectedItem().toString()));
					

				} catch (SQLException e1) {
					
				}
			}
		});
		btnVenderProducto.setBounds(420, 455, 139, 25);
		frame.getContentPane().add(btnVenderProducto);
		
		JLabel lblGanancias = new JLabel("Ganancias totales");
		lblGanancias.setForeground(new Color(51, 209, 122));
		lblGanancias.setBounds(349, 504, 132, 15);
		frame.getContentPane().add(lblGanancias);
		
		JLabel lblEurosGanancias = new JLabel("");
		lblEurosGanancias.setForeground(new Color(51, 209, 122));
		lblEurosGanancias.setBounds(518, 492, 70, 15);
		frame.getContentPane().add(lblEurosGanancias);
		
	}

}
