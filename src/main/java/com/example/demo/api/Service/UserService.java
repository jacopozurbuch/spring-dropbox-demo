package com.example.demo.api.Service;
import java.util.List;

import com.example.demo.api.models.User;

public interface UserService {

    // int count();

    User save(User user, String passwd);

    //int update(User user);

    List<User> findAll();

    User findById(int id);

    User deleteById(int id);

}
