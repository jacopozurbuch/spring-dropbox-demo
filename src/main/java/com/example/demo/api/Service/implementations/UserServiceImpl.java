package com.example.demo.api.Service.implementations;

import java.util.ArrayList;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.example.demo.api.Service.UserService;
import com.example.demo.api.models.LoginResponse;
import com.example.demo.api.models.User;


import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static List<User> users = new ArrayList<>();
    private static int userCount = 0;

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${sns.topic.arn}")
    private String snsTopicArn;

    @Autowired
    private AmazonSNS amazonSNS;

    public List<User> findAll(){
        return users;
    }

    public User save(User user, String passwd){
      if ( user.getId() == null ){
          user.setId(++userCount);
      }
      UserAuthentificationServiceImpl.saveUserPasswd(user, passwd);
      users.add(user);
      try {
          PublishRequest request = new PublishRequest()
                                       .withMessage(String.format("user %s created", user.getName()))
                                       .withTopicArn(snsTopicArn);
          amazonSNS.publish(request); 
      } catch (AmazonServiceException e) {
          LOG.error("Error {} occurred while publishing to sns", e.getLocalizedMessage());
    } 
      return user;
    }

    public LoginResponse checkAuthCredentials(String authString) {
        return UserAuthentificationServiceImpl.isAuthorized(authString);
    }


    public User findById(int id){ 
        for (User user:users) {
            if (user.getId()==id){
                 return user;
            }
        }
        return null;
    }

    public User findByName(String userName){ 
        for (User user:users) {
            if (user.getUser().equals(userName)){
                 return user;
            }
        }
        return null;
    }

    public User deleteById(int id){ 
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if ( user.getId() == id ) {
                iterator.remove();
                return user;
            }
        }
        return null;
    }

}
