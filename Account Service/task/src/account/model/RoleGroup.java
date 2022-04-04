package account.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "role_groups")
@NoArgsConstructor //defines all the groups available in the system (e.g. customer, admin etc.)
public class RoleGroup {

    @Id
    private String code;
    @Column(unique = true, nullable = false)
    private String name;

    public RoleGroup(String code, String name) {
        this.code = code;
        this.name = name;
    }

//    @JsonIgnore
//    @ManyToMany(mappedBy = "roles")
//    private Set<User> users;


}