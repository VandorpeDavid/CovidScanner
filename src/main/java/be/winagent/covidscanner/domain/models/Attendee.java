package be.fkgent.election.domain.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public abstract class Attendee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Event event;

    @CreationTimestamp
    private LocalDateTime entered;

    public abstract String getFirstName();
    public abstract String getLastName();
    public abstract String getEmail();
    public abstract String getPhone();
    public abstract String getCasName();

    public abstract String getContactDetails();

}
