package pet.eshop.shoppingcart;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import pet.eshop.common.entity.CartItem;
import pet.eshop.common.entity.Customer;
import pet.eshop.common.entity.Product;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CartItemRepositoryTests {
    @Autowired
    private CartItemRepository repo;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testSaveItem(){
        Integer customerId = 1;
        Integer productId = 1;

        Customer customer = entityManager.find(Customer.class, customerId);
        Product product = entityManager.find(Product.class, productId);

        CartItem newItem = new CartItem();
        newItem.setProduct(product);
        newItem.setCustomer(customer);
        newItem.setQuantity(1);

        CartItem savedItem = repo.save(newItem);

        assertThat(savedItem.getId()).isGreaterThan(0);
    }

    @Test
    public void testSaveThreeItems(){
        Integer customerOneId = 10;
        Integer customerTwoId = 1;
        Integer productId = 10;

        Customer customerOne = entityManager.find(Customer.class, customerOneId);
        Customer customerTwo = entityManager.find(Customer.class, customerTwoId);
        Product product = entityManager.find(Product.class, productId);

        CartItem newItemOne = new CartItem();
        newItemOne.setProduct(product);
        newItemOne.setCustomer(customerOne);
        newItemOne.setQuantity(2);


        CartItem newItemTwo = new CartItem();
        newItemTwo.setProduct(product);
        newItemTwo.setCustomer(customerTwo);
        newItemTwo.setQuantity(3);


        CartItem newItemThree = new CartItem();
        newItemThree.setProduct(new Product(2));
        newItemThree.setCustomer(new Customer(3));
        newItemThree.setQuantity(2);
        Iterable<CartItem> items = repo.saveAll(List.of(newItemOne, newItemTwo, newItemThree));

        assertThat(items).size().isGreaterThan(0);
    }

    @Test
    public void testFindByCustomer(){
        Integer customerId = 1;
        List<CartItem> itemsList = repo.findByCustomer(new Customer(customerId));
        itemsList.forEach(System.out::println);
        assertThat(itemsList).size().isGreaterThan(0);
    }

    @Test
    public void testFindByCustomerAndProduct(){
        Integer customerId = 10;
        Integer productId = 10;
        CartItem cartItem = repo.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
        System.out.println(cartItem);
        assertThat(cartItem).isNotNull();
    }

    @Test
    public void testUpdateQuantity(){
        Integer customerId = 1;
        Integer productId = 1;
        Integer quantity = 3;

        repo.updateQuantity(quantity, new Customer(customerId), new Product(productId));

        CartItem item = repo.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
        System.out.println(item);
        assertThat(item.getQuantity()).isEqualTo(3);
    }

    @Test
    public void testDeleteByCustomerAndProduct(){
        Integer customerId = 1;
        Integer productId = 1;
        repo.deleteByCustomerAndProduct(new Customer(customerId), new Product(productId));

        CartItem item = repo.findByCustomerAndProduct(new Customer(1), new Product(1));
        assertThat(item).isNull();
    }
}
