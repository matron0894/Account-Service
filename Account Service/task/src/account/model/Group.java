package account.model;

import account.serializers.GroupSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Entity
@JsonSerialize(using = GroupSerializer.class)
@Table(name = "principle_groups")
@NoArgsConstructor //defines all the groups available in the system (e.g. customer, admin etc.)
public class Group implements Serializable, Comparable<Group> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;
    private String name;

    public Group(String code, String name) {
        this.code = code;
        this.name = name;
    }

//    @JsonIgnore
//    @ManyToMany(mappedBy = "roles")
//    private Set<User> users;


    public int compareTo(@NotNull Group o) {
        return this.getId().compareTo(o.getId());
    }
}