package com.example.demo.api.Service;
import java.util.List;

import com.example.demo.api.models.User;

public interface UserService {

    void save(User user);

    int update(User user, String userName);

    List<User> findAll();

    User findByName(String userName);

    int deleteByName(String userName, String authString);

}
