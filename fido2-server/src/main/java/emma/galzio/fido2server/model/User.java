package emma.galzio.fido2server.model;

import lombok.Data;

@Data
public class User {

    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;

}
