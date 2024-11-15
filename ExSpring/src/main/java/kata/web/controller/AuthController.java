package kata.web.controller;

import kata.web.Service.RoleService;
import kata.web.Service.UserService;
import kata.web.model.Role;
import kata.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/admin")
public class AuthController {

    private final UserService userService;
    private final RoleService roleService;
    private final Logger log = Logger.getLogger(AuthController.class.getName());

    @Autowired
    public AuthController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String showUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "show";
    }

    @GetMapping("/show")
    public String show(@RequestParam(value = "id") Integer id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        log.info("user с айди" + id + ":" + user);
        return "user";
    }

    @GetMapping("/new")
    public String save(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getListOfRoles());
        return "save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("user") @Valid User user,
                       BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getListOfRoles());
            return "save";
        }//мб set роли
        userService.save(user);
        return "redirect:/admin/show";
    }

    @GetMapping("/edit/{id}")
    public String update(Model model, @PathVariable Integer id) {
        model.addAttribute("user", userService.findUserById(id));
        List<Role> roles = roleService.getListOfRoles();
        model.addAttribute("roles", roles);
        log.info("запроса с id = " + id + " берет с формы следующие роли: " + roles);
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getListOfRoles());
            return "update";
        }
        userService.update(user);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Integer id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

}
