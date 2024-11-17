package emma.galzio.fido2server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter @Setter
public class UserEntity {

    @Id
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;

}
