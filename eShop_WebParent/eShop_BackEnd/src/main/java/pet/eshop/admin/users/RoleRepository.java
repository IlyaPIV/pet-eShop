package pet.eshop.admin.users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pet.eshop.common.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    // Integer - так как индекс у класса Role этого типа
}
