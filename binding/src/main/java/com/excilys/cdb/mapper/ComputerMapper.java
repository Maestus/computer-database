package main.java.com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import main.java.com.excilys.cdb.model.Computer;

public class ComputerMapper implements RowMapper<Computer> {

	@Override
	public Computer mapRow(ResultSet rs, int rowNum) throws SQLException {
		Computer computer = new Computer();
		computer.setId(rs.getLong("id"));
		computer.setName(rs.getString("name"));

		if (rs.getTimestamp("discontinued") != null) {
			computer.setDiscontinued(rs.getTimestamp("discontinued").toLocalDateTime().toLocalDate());
		}

		if (rs.getTimestamp("introduced") != null) {
			computer.setIntroduced(rs.getTimestamp("introduced").toLocalDateTime().toLocalDate());
		}

		if ((Long) rs.getLong("company_id") != null && (Long) rs.getLong("company_id") != 0L) {
			computer.setCompanyId(rs.getLong("company_id"));
		}
		return computer;
	}
}
