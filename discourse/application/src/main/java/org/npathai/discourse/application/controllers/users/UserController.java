package org.npathai.discourse.application.controllers.users;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import org.npathai.discourse.application.domain.users.RegistrationData;
import org.npathai.discourse.application.domain.users.User;
import org.npathai.discourse.application.domain.users.UserService;
import org.npathai.discourse.application.domain.users.UsernameAlreadyExistsException;

@Controller("/users")
public class UserController {

    private UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @Post
    public HttpResponse<User> create(RegistrationData registrationData) {
        try {
            return HttpResponse.ok(userService.create(registrationData));
        } catch (UsernameAlreadyExistsException e) {
            return HttpResponse.badRequest();
        }
    }
}
