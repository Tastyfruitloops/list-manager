package services;

import entities.User;
import services.interfaces.IUserService;

import java.util.List;

public class UserService implements IUserService {
    @Override
    public List <User> getAllUsers() {
        return null;
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return null;
    }

    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public User updateUser(Long id, String attributes) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }
}
