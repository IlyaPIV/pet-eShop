package pet.eshop.admin.users;

import org.springframework.data.repository.CrudRepository;
import pet.eshop.common.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {
}
