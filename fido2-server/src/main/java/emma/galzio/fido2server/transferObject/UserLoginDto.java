package emma.galzio.fido2server.transferObject;

import lombok.Data;

@Data
public class UserLoginDto {

    private String id;
    private String username;
    private String displayName;
    private String icon;
    private String email;
    private String firstName;
    private String lastName;

}
