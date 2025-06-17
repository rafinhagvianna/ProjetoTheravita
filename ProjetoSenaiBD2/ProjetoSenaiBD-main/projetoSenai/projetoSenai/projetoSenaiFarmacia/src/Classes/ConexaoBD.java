package Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {
	public static Connection getConexao() throws SQLException {

		String url = "jdbc:mysql://localhost:3306/projetosenai";
		String usuario = "root";
		String senha = "farmacia123";

		return DriverManager.getConnection(url, usuario, senha);
	}
}
