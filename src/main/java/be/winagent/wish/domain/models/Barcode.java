package be.winagent.wish.domain.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@Table(indexes = @Index(columnList = "code")) // Allow fast searching
public class Barcode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty
    @Column(unique = true)
    private String code;

    @ManyToOne
    private User user;
}
