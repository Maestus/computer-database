package main.java.com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.com.excilys.cdb.model.Model;

public abstract class Mapper {
    /**
     * Map un resultat retourné par une requete SQL vers un objet représentant un
     * tuple d'une table.
     * @param resultSet Element à mapper.
     * @throws SQLException Exception du à une mauvaise connection à la base de données.
     * @return un nouvel objet qui est crée à partir du resultSet.
     */
    public abstract Model map(ResultSet resultSet) throws SQLException;
}
