package main.java.com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import java.sql.PreparedStatement;

import main.java.com.excilys.cdb.exception.DAOException;
import main.java.com.excilys.cdb.model.Model;
import main.java.com.excilys.cdb.persistance.Datasource;
import main.java.com.excilys.cdb.utils.Page;

public abstract class ModelDAO {

    protected Connection connection;
	
	/**
     * Constructeur par defaut.
     */
    public ModelDAO() {
        connection = Datasource.getConnexion();
    }
	
    /**
     * Insertion d'un objet dans la base de donnée.
     * @param model Un objet représentant un tuple d'une table.
     * @throws Exception Exception du à une mauvaise connection à la base de données.
     * @return Retourne l'identifiant de l'objet crée.
     */
    abstract Optional<Long> create(Model model) throws Exception;

    /**
     * Trouver un objet depuis l'identifiant donné en argument. Leve une exception
     * si aucun element de la table implementant cette méthode n'a pour identifiant
     * la valeur du champs id donnée en argument.
     * @param id un entier
     * @throws DAOException Exception du à une mauvaise connection à la base de données.
     * @return L'objet trouvé
     */
    abstract Optional<? extends Model> findById(long id) throws DAOException;

    /**
     * Met à jour un objet de la base de données. param m est un model.
     * @param m un object à mettre à jour dans la base de données.
     */
    abstract void update(Model m);

    /**
     * Trouve tout les elements d'une table.
     * @param offset Determine à partir de quel element en commence à stocker ce qui sera retourné
     * @param nbElem Nombre d'element à retourner
     * @throws DAOException Exception du à une mauvaise connection à la base de données.
     * @return Un ensemble d'element de la base données.
     */
    abstract Page<? extends Model> findAll(int offset, int nbElem) throws DAOException;

    /**
     * Construit la requete.
     * @param connexion Objet de connection à la base données.
     * @param sql Chaine de caractere detaillant la requete qui sera executé.
     * @param returnGeneratedKeys Indique si l'on souhaite obtenir la clé de l'objet nouvellement crée (par exemple)
     * @param objets Ensemble de variable qui permettent de personnaliser la requete sql
     * @return un PreparedStatement
     * @throws SQLException Exception du à une mauvaise connection à la base de données.
     */
    static PreparedStatement initialisationRequetePreparee(Connection connexion, String sql,
            boolean returnGeneratedKeys, Object... objets) throws SQLException {

        PreparedStatement preparedStatement = (PreparedStatement) connexion.prepareStatement(sql,
                returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);

        for (int i = 0; i < objets.length; i++) {
            preparedStatement.setObject(i + 1, objets[i]);
        }

        return preparedStatement;
    }

}
