package be.fkgent.election.domain.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @ManyToOne
    private Association association;

    @NotEmpty
    private String name;

    private boolean deleted;

    @CreationTimestamp
    private LocalDateTime created;

    @OneToMany(mappedBy = "event")
    @OrderBy("entered")
    private List<Attendee> attendees = new ArrayList<>();
}
