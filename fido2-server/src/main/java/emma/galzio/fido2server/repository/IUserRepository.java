package emma.galzio.fido2server.repository;

import emma.galzio.fido2server.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserEntity, String> {
}
