package com.thombs.ChessWeb.Models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;


        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty", "Username must not be empty!");
        if (user.getUsername().length() < 2 || user.getUsername().length() > 12) {
            errors.rejectValue("username", "Size.userForm.username", "Username must be ");
        }
        
        if (userService.getUser(user.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username", "Username already taken!");
        }
        
        //ValidationUtils.rejectIfEmpty(errors, "email", "NotEmpty", "Email cannot be empty!");
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Password is required!", "Password is required!");
        if (user.getPassword().length() < 5 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password", "Password must be between 5 and 32 characters!");
        }

        if (!user.getPasswordConfirmation().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirmation", "Diff.userForm.passwordConfirm", "Passwords did not match!");
        }
    }
}
