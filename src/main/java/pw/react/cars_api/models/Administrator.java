package pw.react.cars_api.models;

import jakarta.persistence.*;

@Entity
@Table(name = "administrators", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
})
public class Administrator {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String username;

    @Column
    private String passwordHash;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
