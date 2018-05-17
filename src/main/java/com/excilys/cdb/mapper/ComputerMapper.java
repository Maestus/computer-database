package main.java.com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

//import org.springframework.jdbc.core.RowMapper;

import main.java.com.excilys.cdb.model.Computer;

public class ComputerMapper extends Mapper implements RowMapper<Computer> {

	@Override
	public Computer map(ResultSet resultSet) throws SQLException {
		Computer computer = new Computer();
		computer.setId(resultSet.getLong("id")).setNom(resultSet.getString("name"))
				.setIntroduced(resultSet.getTimestamp("introduced") == null ? null
						: resultSet.getTimestamp("introduced").toLocalDateTime().toLocalDate())
				.setDiscontinued(resultSet.getTimestamp("discontinued") == null ? null
						: resultSet.getTimestamp("discontinued").toLocalDateTime().toLocalDate())
				.setCompanyId(resultSet.getObject("company_id"));
		return computer;
	}

	@Override
	public Computer mapRow(ResultSet rs, int rowNum) throws SQLException {
		Computer computer = new Computer();
		computer.setId(rs.getInt("id"));
		computer.setNom(rs.getString("name"));
		if (rs.getTimestamp("discontinued") != null) {
			computer.setDiscontinued(rs.getTimestamp("discontinued").toLocalDateTime().toLocalDate());
		}

		if (rs.getTimestamp("introduced") != null) {
			computer.setIntroduced(rs.getTimestamp("introduced").toLocalDateTime().toLocalDate());
		}
		return computer;
	}
}
