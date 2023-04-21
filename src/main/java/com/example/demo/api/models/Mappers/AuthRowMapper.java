package com.example.demo.api.models.Mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import com.example.demo.api.models.Auth;

public class AuthRowMapper implements RowMapper<Auth> {

    @Override
    public Auth mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Auth(
                rs.getBytes("hash"),
                rs.getBytes("salt")
        );
    }    
}
