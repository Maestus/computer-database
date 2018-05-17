package main.java.com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

//import org.springframework.jdbc.core.RowMapper;

import main.java.com.excilys.cdb.model.Company;

public class CompanyMapper extends Mapper implements RowMapper<Company> {

    @Override
    public Company map(ResultSet resultSet) throws SQLException {
        Company company = new Company();
        company.setId(resultSet.getLong("id")).setNom(resultSet.getString("name"));
        return company;
    }

	@Override
	public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
		Company company = new Company();
		company.setId(rs.getInt("id"));
		company.setNom(rs.getString("name"));
		
		return company;
	}
}
