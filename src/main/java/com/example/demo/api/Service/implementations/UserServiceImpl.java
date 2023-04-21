package com.example.demo.api.Service.implementations;

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
import com.example.demo.api.repository.UserRepository;
import com.example.demo.exception.UserExistentException;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${sns.topic.arn}")
    private String snsTopicArn;

    @Autowired
    private AmazonSNS amazonSNS;

    @Autowired
    public UserRepository userRepository;

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public void save(User user){
      if(findByName(user.getUser()) != null){
            throw new UserExistentException("name=" + user.getUser());
        }
      userRepository.save(user);

      try {
          PublishRequest request = new PublishRequest()
                                       .withMessage(String.format("user %s created", user.getName()))
                                       .withTopicArn(snsTopicArn);
          amazonSNS.publish(request); 
      } catch (AmazonServiceException e) {
          LOG.error("Error {} occurred while publishing to sns", e.getLocalizedMessage());
    }
    return; 
    }

    public int deleteByName(String userName, String authString){
        int num = userRepository.delete(userName); 
        return num;
    }

    public LoginResponse checkAuthCredentials(String authString) {
        return UserAuthentificationServiceImpl.isAuthorized(authString);
    }

    public User findByName(String userName){ 
        Optional<User> user = userRepository.findByName(userName);
        if(user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public int update(User user, String userName) {
        int num = userRepository.update(user, userName);
        return num;
    }

}
