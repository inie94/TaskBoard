package ru.inie.taskboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.inie.taskboard.entity.User;
import ru.inie.taskboard.repositories.UserRepository;

import java.text.SimpleDateFormat;

@Service
public class UserService {

    private final UserRepository repository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public void create(UserRepresentation userRepresentation) {
        User user = new User();
        user.setFirstname(userRepresentation.getFirstname());
        user.setLastname(userRepresentation.getLastname());
        user.setEmail(userRepresentation.getEmail());
        user.setGender(userRepresentation.getGender());

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        java.util.Date utilDate;
        java.sql.Date sqlDate;
        try {
            utilDate = format.parse(userRepresentation.getDateOfBirth());
            sqlDate = new java.sql.Date(utilDate.getTime());
            user.setDateOfBirth(sqlDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        user.setPassword(passwordEncoder.encode(userRepresentation.getPassword()));
        System.out.println(user);
        repository.save(user);
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email).get();
    }
}
