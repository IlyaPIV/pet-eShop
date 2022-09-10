package pet.eshop.admin.users;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import pet.eshop.common.entity.User;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User getUserByEmail(@Param("email") String email);

    public Long countById(Integer id);

    @Modifying
    @Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
    public void updateEnabledStatus(Integer id, boolean enabled);
}
