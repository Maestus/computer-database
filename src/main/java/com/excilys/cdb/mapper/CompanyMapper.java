package main.java.com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.com.excilys.cdb.model.Company;

public class CompanyMapper extends Mapper {

    @Override
    public Company map(ResultSet resultSet) throws SQLException {
        Company company = new Company();
        company.setId(resultSet.getLong("id")).setNom(resultSet.getString("name"));
        return company;
    }
}
