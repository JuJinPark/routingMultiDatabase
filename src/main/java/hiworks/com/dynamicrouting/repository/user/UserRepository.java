package hiworks.com.dynamicrouting.repository.user;

import hiworks.com.dynamicrouting.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User,Long> {
}
