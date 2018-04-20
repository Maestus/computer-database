package main.java.com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import main.java.com.excilys.cdb.model.Model;
import main.java.com.excilys.cdb.utils.Page;

public interface ModelDAO {

	/*
	 * Insertion d'un objet dans la base de donnée.
	 * 
	 * @param model Un objet représentant un tuple d'une table
	 */
	void create(Model model) throws DAOException;

	/*
	 * Trouver un objet depuis l'identifiant donné en argument. Leve une exception
	 * si aucun element de la table implementant cette méthode n'a pour identifiant
	 * la valeur du champs id donnée en argument.
	 * 
	 * @param id un entier
	 * @throws DAOException
	 */
	Model findById(long id) throws DAOException;

	/*
	 * Met à jour un objet de la base de données.
	 * param m est un model
	 */
	void update(Model m);
	
	/*
	 * Trouve tout les elements d'une table.
	 * 
	 * @throws DAOException
	 */
	Page<? extends Model> findAll(int offset, int nbElem) throws DAOException;

	/*
	 * Map un resultat retourné par une requete SQL vers un objet représentant un
	 * tuple d'une table.
	 * 
	 * @param
	 */
	Model map(ResultSet resultSet) throws SQLException;

	/*
	 * Construit la requete.
	 */
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
