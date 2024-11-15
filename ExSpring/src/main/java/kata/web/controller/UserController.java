package kata.web.controller;

import kata.web.Service.UserService;
import kata.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import kata.web.model.User;
import javax.validation.Valid;
import java.security.Principal;
import java.sql.SQLException;
import java.util.Optional;


@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/user")
    public String showUser(Principal principal, Model model) throws SQLException {
        User user = userService.findUserByLogin(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }
}