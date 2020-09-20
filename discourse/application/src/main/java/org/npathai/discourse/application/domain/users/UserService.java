package org.npathai.discourse.application.domain.users;

public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(RegistrationData registrationData) throws UsernameAlreadyExistsException {
        User user = new User();
        user.setUsername(registrationData.getUsername());
        user.setEmail(registrationData.getEmail());
        user.setName(registrationData.getName());
        user.setPassword(registrationData.getPassword());

        userRepository.save(user);

        return user;
    }
}
