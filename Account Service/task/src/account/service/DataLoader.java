package account.service;

import account.model.Group;
import account.repos.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;

//@Component
public class DataLoader {

    private final GroupRepository groupRepository;

    @Autowired
    public DataLoader(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
        createRoles();
    }

    private void createRoles() {
        try {
            groupRepository.save(new Group("ADMINISTRATOR", "Administrator role"));
            groupRepository.save(new Group("ACCOUNTANT", "Could update, and create new payments"));
            groupRepository.save(new Group("USER", "Standard user role"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}