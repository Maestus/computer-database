package main.java.com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.com.excilys.cdb.model.Computer;

public class ComputerMapper extends Mapper {

    @Override
    public Computer map(ResultSet resultSet) throws SQLException {
        Computer computer = new Computer();
        computer.setId(resultSet.getLong("id")).setNom(resultSet.getString("name"))
                .setIntroduced(resultSet.getTimestamp("introduced") == null ? null : resultSet.getTimestamp("introduced").toLocalDateTime().toLocalDate())
                .setDiscontinued(resultSet.getTimestamp("discontinued") == null ? null : resultSet.getTimestamp("discontinued").toLocalDateTime().toLocalDate())
                .setCompanyId(resultSet.getObject("company_id"));
        return computer;
    }
}
