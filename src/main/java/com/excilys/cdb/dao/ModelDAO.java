package main.java.com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import main.java.com.excilys.cdb.model.Model;
import main.java.com.excilys.cdb.utils.Page;

public interface ModelDAO {

    /**
     * Insertion d'un objet dans la base de donnée.
     * @param model Un objet représentant un tuple d'une table.
     * @throws Exception Exception du à une mauvaise connection à la base de données.
     * @return Retourne l'identifiant de l'objet crée.
     */
    long create(Model model) throws Exception;

    /**
     * Trouver un objet depuis l'identifiant donné en argument. Leve une exception
     * si aucun element de la table implementant cette méthode n'a pour identifiant
     * la valeur du champs id donnée en argument.
     * @param id un entier
     * @throws DAOException Exception du à une mauvaise connection à la base de données.
     * @return L'objet trouvé
     */
    Model findById(long id) throws DAOException;

    /**
     * Met à jour un objet de la base de données. param m est un model.
     * @param m un object à mettre à jour dans la base de données.
     */
    void update(Model m);

    /**
     * Trouve tout les elements d'une table.
     * @param offset Determine à partir de quel element en commence à stocker ce qui sera retourné
     * @param nbElem Nombre d'element à retourner
     * @throws DAOException Exception du à une mauvaise connection à la base de données.
     * @return Un ensemble d'element de la base données.
     */
    Page<? extends Model> findAll(int offset, int nbElem) throws DAOException;

    /**
     * Map un resultat retourné par une requete SQL vers un objet représentant un
     * tuple d'une table.
     * @param resultSet Element à mapper.
     * @throws SQLException Exception du à une mauvaise connection à la base de données.
     * @return un nouvel objet qui est crée à partir du resultSet.
     */
    Model map(ResultSet resultSet) throws SQLException;

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

        System.out.println(connexion);

        PreparedStatement preparedStatement = (PreparedStatement) connexion.prepareStatement(sql,
                returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);

        for (int i = 0; i < objets.length; i++) {
            preparedStatement.setObject(i + 1, objets[i]);
        }

        return preparedStatement;
    }

}