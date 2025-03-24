package kata.web.controller;

import kata.web.Service.RoleService;
import kata.web.Service.UserService;
import kata.web.model.Role;
import kata.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
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
        model.addAttribute("users", userService.getUsersWithRoles());
        return "show";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable Integer id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        log.info("user с айди" + id + ":" + user);
        return "user";
    }

    @GetMapping("/new")
    public String save(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        log.info("новый юзер"+user);
        List<Role> roles = roleService.getListOfRoles();
        model.addAttribute("roles", roles);
        //model.addAttribute("roles", roleService.getListOfRoles());
        return "save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("user") @Valid User user,
                       BindingResult bindingResult, Model model, @RequestParam("authorities") List<String> values) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getListOfRoles());
            return "save";
        }
        List<Role> roles = userService.getList_Roles(values);
        user.setRoles(roles);
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String update(Model model, @PathVariable Integer id) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        List<Role> roles = roleService.getListOfRoles();
        model.addAttribute("roles", roles);
        log.info("запроса с id = " + id + " берет с формы следующие роли: " + roles);
        for (Role role:roles){
            Role role1 = new Role(role.getName());
            log.info("id у roles = " + role1.getId());
        }

        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("user") User user,@RequestParam("authorities") List<String> values, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getListOfRoles());
            return "update";
        }
        List<Role> roles = userService.getList_Roles(values);
        user.setRoles(roles);
        userService.update(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Integer id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

}
