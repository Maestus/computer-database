package com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.cdb.model.Model;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public interface ModelDAO {

	boolean create(Model model);

	Model find(long id);

	Model map(ResultSet resultSet) throws SQLException;

	public static PreparedStatement initialisationRequetePreparee(Connection connexion, String sql,
			boolean returnGeneratedKeys, Object... objets) throws SQLException {

		PreparedStatement preparedStatement = (PreparedStatement) connexion.prepareStatement(sql,
				returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);

		for (int i = 0; i < objets.length; i++) {

			preparedStatement.setObject(i + 1, objets[i]);

		}

		return preparedStatement;
	}

}
