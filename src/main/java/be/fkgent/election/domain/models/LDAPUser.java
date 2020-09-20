package be.fkgent.election.domain.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;
import javax.persistence.Column;
import java.util.List;

/**
 * User loaded from UGent LDAP
 * @author davdorpe
 * @see User
 * @since 0.0.1
 */
@Entry(
        base = "ou=people,dc=UGent,dc=be",
        objectClasses = { "ugentPerson" })
@Getter
@Setter
public final class LDAPUser {
    @Id
    @Column(columnDefinition = "raw(32)")
    private Name id;

    @Attribute(name="uid")
    private String username;

    @Attribute(name="ugentBarcode")
    private List<String> barcode;

    @Attribute(name="ugentStudentID")
    private String studentID;

    @Attribute(name="ugentID")
    private String ugentID;

    @Attribute(name="mail")
    private String email;

    @Attribute(name = "givenName")
    private String firstName;

    @Attribute(name = "sn")
    private String lastName;
}
