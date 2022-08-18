package pet.eshop.admin.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import pet.eshop.common.entity.Role;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository repository;

    @Test
    public void testCreateSingleRole(){
        Role role = new Role("Admin", "manage everything");
        Role savedRole = repository.save(role);
        assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateMultiRoles(){
        Role roleSalesperson = new Role("Salesperson", "manage product price, customers," +
                " shipping, orders and sales reports");
        Role roleEditor = new Role("Editor", "manage categories, brands, products, " +
                " articles and menus");
        Role roleShipper = new Role("Shipper", "view products, view orders and update order status");
        Role roleAssistant = new Role("Assistant", "manage questions and reviews");

        repository.saveAll(List.of(roleAssistant, roleSalesperson, roleEditor, roleShipper));
    }
}
