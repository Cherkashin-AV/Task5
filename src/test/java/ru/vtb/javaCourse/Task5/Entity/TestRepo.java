package ru.vtb.javaCourse.Task5.Entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.repository.Repository;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.javaCourse.Task5.Repository.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:test;NON_KEYWORDS=KEY,LEVEL,VALUE",
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.datasource.schema=",
        "spring.jpa.show-sql=true",
        "spring.jpa.properties.hibernate.default_schema="})
@Import({InstantiationTracingBeanPostProcessor.class})
@DisplayName("Тестирование БД (связи между объектами и пользовательские функций в репозиториях)")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:test;NON_KEYWORDS=KEY,LEVEL,VALUE"
})
public class TestRepo {
    @Autowired
    AccountTypeRepo accountTypeRepo;
    @Autowired
    ProductClassRepo productClassRepo;
    @Autowired
    ProductRegisterTypeRepo productRegisterTypeRepo;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    ProductRegisterRepo productRegistrRepo;
    @Autowired
    AccountPoolRepo accountPoolRepo;


    @Test
    @DisplayName("Тестирование репозитория AccountTypeRepo")
    void testAccountType() {
        AccountType accType = accountTypeRepo.findById(Long.valueOf(1)).orElse(null);
        if (accType == null) {
            accountTypeRepo.save(accType = new AccountType("Клиентский"));
        }
        Assertions.assertTrue(accType.getValue().equals("Клиентский") && accType.getInternalId() != null, "Не найдена запись с internal_id=1 в таблице tpp_ref_account_type");
    }

    @Test
    @DisplayName("Тестирование пользовательских методов репозитория AccountTypeRepo")
    void testAccountTypeClassMethods() {
        checkUncheckedMethods(AccountTypeRepo.class, null);
    }

    @Test
    @DisplayName("Тестирование репозитория RefProductClass")
    void testPoductClass() {
        RefProductClass productClass = productClassRepo.findById(Long.valueOf(1)).orElse(null);
        if (productClass == null) {
            productClass = new RefProductClass();
            productClass.setValue("03.012.002");
            productClass.setGblCode("ABC");
            productClass.setGblName("ABC");
            productClass.setProductRowCode("ABC");
            productClass.setProductRowName("ABC");
            productClass.setSubclassCode("ABC");
            productClass.setSubclassName("ABC");
            productClassRepo.save(productClass);
        }
        Assertions.assertTrue(productClass.getValue().equals("03.012.002") && productClass.getInternalId() == 1, "Не найдена запись с internal_id=1 в таблице tpp_ref_product_class");
    }

    @Test
    @DisplayName("Тестирование пользовательских методов репозитория RefProductClass")
    void testPoductClassMethods() {
        RefProductClass refProductClass = productClassRepo.findByValue("02.001.005").orElseThrow(() -> new RuntimeException("findByValue: Не найдена запись с value = '02.001.005' "));
        Assertions.assertNotNull(refProductClass.getInternalId());
        checkUncheckedMethods(ProductClassRepo.class, List.of("findByValue"));
    }

    @Test
    @DisplayName("Тестирование репозитория ProductRegisterType")
    void testPoductRegisterType() {
        ProductRegisterType productRegisterType = productRegisterTypeRepo.findById(Long.valueOf(1)).orElse(null);
        if (productRegisterType == null) {
            productRegisterType = new ProductRegisterType();
            productRegisterType.setValue("ABC");
            productRegisterType.setRegisterTypeName("ABC");
            productRegisterType.setProductClass(productClassRepo.findById(Long.valueOf(1)).orElse(null));
            productRegisterType.setAccountType(accountTypeRepo.findById(Long.valueOf(1)).orElse(null));
            productRegisterTypeRepo.save(productRegisterType);
        }
        Assertions.assertTrue(productRegisterType.getValue().equals("03.012.002_47533_ComSoLd") && productRegisterType.getInternalId() != null, "Не найдена запись с internal_id=1 в таблице tpp_ref_product_register_type");
        Assertions.assertNotNull(productRegisterType.getProductClass(), "Не заполнена ссылка на RefProductClass");
        Assertions.assertEquals(1L, productRegisterType.getProductClass().getInternalId(), "В поле ProductClass не правильный объект");
        Assertions.assertNotNull(productRegisterType.getAccountType(), "Не заполнена ссылка на AccountType");
        Assertions.assertEquals(1L, productRegisterType.getAccountType().getInternalId(), "В поле AccountType не правильный объект");
        Set<ProductRegisterType> productRegisterTypes = productRegisterTypeRepo.findByProductClassAndAccountType("03.012.002", "Клиентский");
        Assertions.assertEquals(1, productRegisterTypes.size(), "Не найден продукт в каталоге");
    }

    @Test
    @DisplayName("Тестирование пользовательских методов репозитория ProductRegisterType")
    void testProductRegisterTypeMethods() {
        ProductRegisterType productRegisterType = productRegisterTypeRepo.findByValue("02.001.005_45343_CoDowFF")
                .orElseThrow(() -> new RuntimeException("findByValue: Не найдена запись с value = '02.001.005_45343_CoDowFF' "));
        Assertions.assertNotNull(productRegisterType.getInternalId());

        Set<ProductRegisterType> productRegisterTypes = productRegisterTypeRepo.findByProductClassAndAccountType("02.001.005", "Клиентский");
        Assertions.assertEquals(1, productRegisterTypes.size(), "Не найдена запись в ProductRegisterType");

        ProductRegisterType productRegisterType2 = productRegisterTypeRepo.findByProductClassAndValue("02.001.005", "02.001.005_45343_CoDowFF")
                .orElseThrow(() -> new RuntimeException("findByProductClassAndValue: Не найдена запись с productClassValue = '02.001.005', value = '02.001.005_45343_CoDowFF'"));
        Assertions.assertNotNull(productRegisterType2.getInternalId());
        checkUncheckedMethods(ProductRegisterTypeRepo.class, List.of("findByValue", "findByProductClassAndAccountType", "findByProductClassAndValue" ));
    }

    private Product setProducts(){
        Product product1 = new Product();
        product1.setType("PARENT");
        product1.setNumber("123321");
        product1.setProductClass(productClassRepo.findByValue("03.012.002").orElse(null));
        //productRepo.save(product1);

        Product product2 = new Product();
        product2.setType("CHILD");
        product2.setNumber("1233211");

        product1.addAgreement(product2);
        productRepo.save(product1);
        return product1;
    }

    @Test
    @Transactional
    @DisplayName("Тестирование репозитория Product")
    void testProduct() {
        setProducts();
        Product product1 = productRepo.findByNumber("123321").orElse(null);
        Assertions.assertNotNull(product1, "Не найден сохраненный ранее продукт");
        Assertions.assertEquals(1, product1.getAgreements().size(), "Не корректное количество доп.соглашений");
        Assertions.assertEquals("03.012.002", product1.getProductClass().getValue(), "Не корректное значение поля productClass");
        //Тестирование поиска доп. соглашения
        product1 = productRepo.findArrangementsByNumber(product1.getId(), "1233211").orElseThrow(()-> new RuntimeException("Не найдено дополнительное соглашение"));
        Assertions.assertEquals("1233211", product1.getNumber());
    }

    @Test
    @DisplayName("Тестирование пользовательских методов репозитория Product")
    void testProductMethods() {
        //Тестирование findByNumber в testProduct, исключаем
        checkUncheckedMethods(ProductRepo.class, List.of("findByNumber", "findArrangementsByNumber"));
    }

    @Test
    @Transactional
    @DisplayName("Тестирование репозитория ProductRegister")
    void testProductRegisterRepo() {
        Product product = setProducts();
        System.out.println(product);
        Assertions.assertNotNull(product.getId());

        ProductRegister productRegister = new ProductRegister();
        productRegister.setProduct(product);
        productRegister.setType(productRegisterTypeRepo.findByValue("03.012.002_47533_ComSoLd").orElse(null));
        productRegister.setCurrencyCode("EUR");
        productRegister.setState(State.OPEN);
        productRegister.setAccount(accountPoolRepo.findById(1L).orElse(null));
        productRegistrRepo.save(productRegister);
        Long id = productRegister.getId();

        productRegister = productRegistrRepo.findById(id).orElse(null);
        Assertions.assertEquals("03.012.002_47533_ComSoLd", productRegister.getType().getValue(), "Не корректное значени поля type");
        productRegister = productRegistrRepo.findByProduct_idAndType_value(product.getId(), "03.012.002_47533_ComSoLd").orElse(null);
        Assertions.assertNotNull(productRegister, "Не найдена сохраненная ранее запись");
        Assertions.assertEquals(product.getId(), productRegister.getProduct().getId(), "Не корректное значени поля product");
        Assertions.assertEquals(1, productRegister.getAccount().getId(), "Не корректное значени поля account");
    }

    @Test
    @DisplayName("Тестирование пользовательских методов репозитория ProductRegister")
    void testProductRegisterMethods() {
        checkUncheckedMethods(ProductRegisterRepo.class, List.of("findById", "findByProduct_idAndType_value", "findByProduct_id"));
    }

    @Test
    @DisplayName("Тестирование репозитория AccountsPool")
    void testAccountsPoolRepo() {
        AccountsPool pool = accountPoolRepo.findByAccountPoolKey(new AccountPoolKey("0022", "800", "15", "00", "03.012.002_47533_ComSoLd")).orElse(null);
        Assertions.assertEquals("475335516415314841861", pool.getFirstAccount());
    }

    private void checkUncheckedMethods(Class<? extends Repository> repositoryClass, List<String> exludeMethods){
        List<String> uncheckedMethods = getMethodsWithExlude(repositoryClass, exludeMethods);
        if (uncheckedMethods.size()!=0){
            System.out.println(uncheckedMethods);
            Assertions.assertEquals(0, uncheckedMethods.size(), "Обнаружены не тестированные метода в репозитории " + repositoryClass.getName());
        }
    }
    private List<String> getMethodsWithExlude(Class<? extends Repository> repositoryClass, List<String> exludeMethods){
        return Arrays
                .stream(repositoryClass.getDeclaredMethods())
                .map(m->m.getName())
                .filter(n->exludeMethods == null || !exludeMethods.contains(n))
                .toList();
    }

}
