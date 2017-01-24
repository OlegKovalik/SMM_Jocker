package com.smmjocker.services;

import com.smmjocker.dao.UserRepository;
import com.smmjocker.db.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Users getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    @Transactional
    public void addUser(Users users) {
        userRepository.save(users);
    }

    @Override
    @Transactional
    public void updateUser(Users users) {


        userRepository.save(users);
    }
}
