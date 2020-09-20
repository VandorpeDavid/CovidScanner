package be.fkgent.election.domain.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Association {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    @NotEmpty
    private String abbreviation;

    @Column(unique = true)
    @NotEmpty
    private String name;

    @ManyToMany
    private Set<User> admins = new HashSet<>();

    @OneToMany(mappedBy = "association")
    @OrderBy("created")
    private List<Event> events = new ArrayList<>();

    public String toString() {
        return getAbbreviation();
    }
}
