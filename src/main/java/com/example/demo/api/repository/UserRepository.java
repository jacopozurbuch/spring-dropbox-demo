package com.example.demo.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.api.models.Auth;
import com.example.demo.api.models.User;
import com.example.demo.api.models.Mappers.AuthRowMapper;
import com.example.demo.api.models.Mappers.UserRowMapper;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(User user){
        String sql_user = "INSERT INTO public.users(name, username, birthday, email) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql_user,user.getName(),user.getUser(),user.getBirthday(), user.getEmail());

        String sql_password = "INSERT INTO public.password(username, salt, hash) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql_password, user.getUser(),user.getPassword().getSalt(), user.getPassword().getHash());
    }
    
    public Optional<User> findByName(String userName) {
        String sql = "SELECT * FROM public.users WHERE username = (?)";
        return jdbcTemplate.query(sql, new UserRowMapper(), userName).stream().findFirst();
    }   

    public List<User> findAll() {
        String sql = "SELECT * FROM public.users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    public int update(User user, String userName) {
        String sql = "UPDATE public.users SET name = ?, username = ?, email = ?, birthday = ? WHERE username = ?";
    return jdbcTemplate.update(sql,
            user.getName(),
            user.getUser(),
            user.getEmail(), 
            user.getBirthday(), userName);
    }

    public int delete(String userName) {
        String sql = "DELETE FROM public.users WHERE username = (?)";
        return jdbcTemplate.update(sql, userName);
     }

    public Optional<Auth> getPassword(String userName) {
        String sql = "SELECT * FROM public.password WHERE username = (?)";
        return jdbcTemplate.query(sql, new AuthRowMapper(), userName).stream().findFirst();
    } 


}
