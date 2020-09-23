package be.winagent.wish.domain.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    @NotEmpty
    private String username;

    @NotEmpty
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Barcode> barcodes = new HashSet<>();

    private boolean admin;

    @Email
    private String email;

    private String firstName;
    private String lastName;

    public void setEmail(String email) {
        this.email = email.strip();
    }

    public String toString() {
        return String.format("%s (%s)", username, getEmail());
    }
}
