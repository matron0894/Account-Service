package account.service;

import account.model.RoleGroup;
import account.repos.RoleGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final RoleGroupRepository groupRepository;

    @Autowired
    public DataLoader(RoleGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
        createRoles();
    }

    private void createRoles() {
        try {
            groupRepository.save(new RoleGroup("ROLE_ADMINISTRATOR", "Administrator role"));
            groupRepository.save(new RoleGroup("ROLE_ACCOUNTANT", "Could update, and create new payments"));
            groupRepository.save(new RoleGroup("ROLE_USER", "Standard user role"));
            groupRepository.save(new RoleGroup("ROLE_AUDITOR", "The employee of the security department"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}