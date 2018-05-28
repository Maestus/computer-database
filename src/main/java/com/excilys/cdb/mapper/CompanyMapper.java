package main.java.com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

//import org.springframework.jdbc.core.RowMapper;

import main.java.com.excilys.cdb.model.Company;

public class CompanyMapper implements RowMapper<Company> {

	@Override
	public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
		Company company = new Company();
		company.setId(rs.getLong("id"));
		company.setName(rs.getString("name"));
		
		return company;
	}
}
