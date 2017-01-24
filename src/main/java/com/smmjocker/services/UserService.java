package com.smmjocker.services;

import com.smmjocker.db.Users;

public interface UserService {
    Users getUserByLogin(String login);
    void addUser(Users users);
    void updateUser(Users users);
}
