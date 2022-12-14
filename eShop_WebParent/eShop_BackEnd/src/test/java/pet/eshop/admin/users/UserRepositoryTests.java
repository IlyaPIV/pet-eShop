package pet.eshop.admin.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import pet.eshop.common.entity.Role;
import pet.eshop.common.entity.User;


import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateTableUser(){
        //creating DB in MySQL
    }

    @Test
    public void testSingleUserWithSingleRole(){
        Role roleAdmin = entityManager.find(Role.class, 1);
        User newUser = new User("petermoloch@mail.ru", "7940588", "Peter", "Moloch");
        newUser.addRole(roleAdmin);

        User savedUser = repo.save(newUser);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }


    @Test
    public void testSingleUserWithTwoRoles(){
        Role roleEditor = new Role(4);      // чтобы это работало - добавляем конструктор + hashCode + equals в Role
        Role roleAssistant = new Role(2);   // чтобы это работало - добавляем конструктор + hashCode + equals в Role

        User newUser = new User("rave@gmail.com", "ravi2020", "Ravi", "Kumar");
        newUser.addRole(roleEditor);
        newUser.addRole(roleAssistant);

        User savedUser = repo.save(newUser);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }


    @Test
    public void testListAllUsers(){
        Iterable<User> userList = repo.findAll();
        userList.forEach(user -> System.out.println(user));
    }

    @Test
    public void testGetUserById(){
        User userTwo = repo.findById(3).get();
        System.out.println(userTwo);
        assertThat(userTwo).isNotNull();
    }

    @Test
    public void testUpdateUserDetails(){
        User userOne = repo.findById(1).get();
        userOne.setEnabled(true);

        repo.save(userOne);
    }

    @Test
    public void testUpdateUserRoles(){
        User userTwo = repo.findById(3).get();

        userTwo.getRoles().remove(new Role(2)); // role Assistant
        userTwo.addRole(new Role(3));           // role Salesperson

        repo.save(userTwo);
    }

    @Test
    public void testDeleteUser(){
        Integer userId = 3;
        repo.deleteById(userId);
    }

    @Test
    public void testGetUserByEmail(){
        String email = "petermoloch@mail.ru";
        User userByEmail = repo.getUserByEmail(email);

        assertThat(userByEmail).isNotNull();
    }

    @Test
    public void testCountById(){
        Integer id = 2;
        Long countById = repo.countById(id);

        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testDisableUser(){
        Integer id = 1;
        repo.updateEnabledStatus(id, false);
    }

    @Test
    public void testEnableUser(){
        Integer id = 1;
        repo.updateEnabledStatus(id, true);
    }

    @Test
    public void testListFirstPage(){
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(pageable);

        List<User> listUsers = page.getContent();
        listUsers.forEach(System.out::println);

        assertThat(listUsers.size()).isEqualTo(pageSize);
    }

    @Test
    public void testSearchUsers(){
        String keyword = "bruce";
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(keyword, pageable);

        List<User> listUsers = page.getContent();
        listUsers.forEach(System.out::println);

        assertThat(listUsers.size()).isGreaterThan(0);
    }
}