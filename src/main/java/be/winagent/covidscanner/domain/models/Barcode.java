package be.winagent.covidscanner.domain.models;

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

    public static String NormalizeScannedCode(String scanned) {
        if(scanned.length() == 0) {
            return "";
        }
        return "0" + scanned.substring(0, scanned.length() - 1);
    }
}
