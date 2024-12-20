package com.projet.projet.covoiturage.service;

import com.projet.projet.covoiturage.dao.UserDao;
import com.projet.projet.covoiturage.model.User;
import com.projet.projet.covoiturage.util.EmailUtil;

import java.sql.SQLException;
import java.util.Random;

public class UserService {
    private UserDao userDao;

    public UserService() throws SQLException, ClassNotFoundException {
        this.userDao = new UserDao();
    }

    public boolean registerUser(User user) throws Exception {
        if (!userDao.isEmailUnique(user.getEmail())) {
            throw new Exception("Email is already registered");
        }

        // Generate a verification code
        String verificationCode = generateVerificationCode();

        // Send verification email

        // Save user with `isVerified` as false
        user.setVerified(false);
        userDao.saveUser(user);

        return true;
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}
