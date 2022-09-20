package pet.eshop.admin.categories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pet.eshop.common.entity.Category;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

    @MockBean   //получаем фейковый объект
    private CategoryRepository repository;

    @InjectMocks    //инжектим фейковый объект
    private CategoryService service;

    @Test
    public void testCheckUniqueInNewModeReturnDuplicateName(){
        Integer id = null;
        String name = "Computers";
        String alias = "comps";

        Category category = new Category(id, name, alias);

        Mockito.when(repository.findByName(name)).thenReturn(category);         // ответы фейкового репозитория
        Mockito.when(repository.findByAlias(alias)).thenReturn(null);     // ответы фейкового репозитория

        String result = service.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("DuplicateName");
    }

    @Test
    public void testCheckUniqueInNewModeReturnDuplicateAlias(){
        Integer id = null;
        String name = "lenses";
        String alias = "lenses";

        Category category = new Category(id, name, alias);

        Mockito.when(repository.findByName(name)).thenReturn(null);      // ответы фейкового репозитория
        Mockito.when(repository.findByAlias(alias)).thenReturn(category);     // ответы фейкового репозитория

        String result = service.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("DuplicateAlias");
    }

    @Test
    public void testCheckUniqueInNewModeReturnOK(){
        Integer id = null;
        String name = "GPU";
        String alias = "gpu";

        Mockito.when(repository.findByName(name)).thenReturn(null);      // ответы фейкового репозитория
        Mockito.when(repository.findByAlias(alias)).thenReturn(null);     // ответы фейкового репозитория

        String result = service.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("OK");
    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicateName(){
        Integer id = 1;
        String name = "Computers";
        String alias = "comps";

        Category category = new Category(2, name, alias);

        Mockito.when(repository.findByName(name)).thenReturn(category);         // ответы фейкового репозитория
        Mockito.when(repository.findByAlias(alias)).thenReturn(null);     // ответы фейкового репозитория

        String result = service.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("DuplicateName");
    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicateAlias(){
        Integer id = 2;
        String name = "lenses";
        String alias = "lenses";

        Category category = new Category(3, name, alias);

        Mockito.when(repository.findByName(name)).thenReturn(null);      // ответы фейкового репозитория
        Mockito.when(repository.findByAlias(alias)).thenReturn(category);     // ответы фейкового репозитория

        String result = service.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("DuplicateAlias");
    }

    @Test
    public void testCheckUniqueInEditModeReturnOK(){
        Integer id = 3;
        String name = "GPU";
        String alias = "gpu";

        Category category = new Category(id, name, alias);

        Mockito.when(repository.findByName(name)).thenReturn(null);      // ответы фейкового репозитория
        Mockito.when(repository.findByAlias(alias)).thenReturn(category);     // ответы фейкового репозитория

        String result = service.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("OK");
    }
}
