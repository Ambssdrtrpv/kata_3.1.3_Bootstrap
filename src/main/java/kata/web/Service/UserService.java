package kata.web.Service;

import kata.web.model.Role;
import kata.web.model.User;
import kata.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    UserRepository userRepository;
    private BCryptPasswordEncoder bCrypt;
    private RoleService roleService;

    @Autowired
    public UserService(UserRepository userRepository,BCryptPasswordEncoder bCrypt, RoleService roleService) {
        this.bCrypt = bCrypt;
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(s);
        if(user == null) {
            throw new UsernameNotFoundException("Пользователь " + s + " не найден");
        }
        return User.fromUser(user);
    }

    //@Transactional(readOnly = true)
    public User findUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Transactional
    public List<User> getUsersWithRoles() {
        return userRepository.findAllWithRoles();
    }


    public void save(User user) {
        user.setPassword(bCrypt.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User update(User user) {
        user.setPassword(bCrypt.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public User findUserById(Integer id) {
        return userRepository.findUserById(id);
    }
    public List<Role> getList_Roles(List<String> values) {
        List<Role> roles = new ArrayList<>();
        for (String val:values){
            roles.add(roleService.getRoleById(Integer.parseInt(val)));
        }
        return roles;
    }
}
