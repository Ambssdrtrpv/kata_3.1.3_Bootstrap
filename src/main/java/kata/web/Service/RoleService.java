package kata.web.Service;

import kata.web.model.Role;
import kata.web.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void addRole(Role role) {
        roleRepository.save(role);
    }

    @Transactional(readOnly = true)
    public Role findRole(Integer id) {
        return roleRepository.getReferenceById(id);
    }

    public void editRoleById(Role role) {
        roleRepository.save(role);
    }

    public void removeRoleById(Integer id) {
        roleRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Role> getListOfRoles() {
        return roleRepository.findAll();
    }
    public Role getRoleById(Integer id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        return roleOptional.orElse(null);
    }
}
