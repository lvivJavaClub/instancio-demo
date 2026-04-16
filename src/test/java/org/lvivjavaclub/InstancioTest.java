package org.lvivjavaclub;

import org.instancio.Assign;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.instancio.TypeToken;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.lvivjavaclub.dto.AddressEmbeddable;
import org.lvivjavaclub.dto.CreateOrderRequestDto;
import org.lvivjavaclub.dto.CustomerEntity;
import org.lvivjavaclub.dto.CustomerProfileDto;
import org.lvivjavaclub.dto.CustomerStatus;
import org.lvivjavaclub.dto.NotificationCommand;
import org.lvivjavaclub.dto.OrderStatus;
import org.lvivjavaclub.dto.PageResponse;
import org.lvivjavaclub.dto.ProductEntity;
import org.lvivjavaclub.dto.PurchaseOrderEntity;
import org.lvivjavaclub.dto.PurchaseOrderLineEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.all;
import static org.instancio.Select.allStrings;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

class InstancioTest {

    @Test
    void example01_simpleCreateDto() {
        CustomerProfileDto dto = Instancio.create(CustomerProfileDto.class);

        assertNotNull(dto);
        assertNotNull(dto.getCustomerId());
    }

    @Test
    void example02_simpleCreateEntity() {
        ProductEntity entity = Instancio.create(ProductEntity.class);

        assertNotNull(entity);
        assertNotNull(entity.getId());
        assertNotNull(entity.getName());
    }

    @Test
    void example03_setStaticValues() {
        CustomerEntity customerEntity = Instancio.of(CustomerEntity.class)
                .set(field(CustomerEntity::getStatus), CustomerStatus.ACTIVE)
                .set(field(CustomerEntity::getEmail), "lviv@javaclub.com").create();

        assertNotNull(customerEntity);
        assertThat(customerEntity.getStatus()).isEqualTo(CustomerStatus.ACTIVE);
        assertThat(customerEntity.getEmail()).isEqualTo("lviv@javaclub.com");
    }

    @Test
    void example04_setAllStringsToOneValue() {
        AddressEmbeddable addressEmbeddable = Instancio.of(AddressEmbeddable.class)
                .set(allStrings(), "lviv-java-club")
                .create();

        assertThat(addressEmbeddable.getCity()).isEqualTo("lviv-java-club");
        assertThat(addressEmbeddable.getCountry()).isEqualTo("lviv-java-club");
        assertThat(addressEmbeddable.getPostalCode()).isEqualTo("lviv-java-club");
        assertThat(addressEmbeddable.getStreet()).isEqualTo("lviv-java-club");
    }

    @Test
    void example05_generateStringLengths() {
        CustomerEntity customerEntity = Instancio.of(CustomerEntity.class)
                .generate(field(CustomerEntity::getFirstName), gen -> gen.string().length(5))
                .generate(field(CustomerEntity::getLastName), gen -> gen.string().minLength(5).maxLength(10))
                .create();

        assertThat(customerEntity.getFirstName()).hasSize(5);
        assertThat(customerEntity.getLastName()).hasSizeBetween(5, 10);
    }

    @Test
    void example06_generateNumbersAndBigDecimals() {
        ProductEntity entity = Instancio.of(ProductEntity.class)
                .generate(field(ProductEntity::getPrice), gen -> gen.math().bigDecimal().range(new BigDecimal("10.00"),
                        new BigDecimal("99.99")))
                .generate(field(ProductEntity::getWeightKg), gen -> gen.math().bigDecimal().range(new BigDecimal("0.100"),
                        new BigDecimal("5.000")))
                .create();

        assertThat(entity.getPrice()).isBetween(new BigDecimal("10.00"), new BigDecimal("99.99"));
        assertThat(entity.getWeightKg()).isBetween(new BigDecimal("0.100"), new BigDecimal("5.000"));
    }

    @Test
    void example07_generateTemporalValues() {
        NotificationCommand notificationCommand = Instancio.of(NotificationCommand.class)
                .generate(field(NotificationCommand.class, "scheduledFor"), gen -> gen.temporal().offsetDateTime().future())
                .generate(field(NotificationCommand.class, "retryDelay"),
                        gen -> gen.temporal().duration().min(5, ChronoUnit.MINUTES).max(5, ChronoUnit.HOURS))
                .create();

        assertThat(notificationCommand.scheduledFor()).isAfter(OffsetDateTime.now());
        assertThat(notificationCommand.retryDelay()).isBetween(Duration.ofMinutes(5), Duration.ofHours( 4));
    }

    @Test
    void example08_generateEnumExcludingCertainValues() {
        PurchaseOrderEntity purchaseOrderEntity = Instancio.of(PurchaseOrderEntity.class)
                .generate(field(PurchaseOrderEntity::getStatus), gen -> gen.enumOf(OrderStatus.class).excluding(OrderStatus.DRAFT))
                .create();

        assertThat(purchaseOrderEntity.getStatus()).isNotEqualTo(OrderStatus.DRAFT);
    }

    @Test
    void example09_supplyWithSupplier_newValueEachTime() {
        CustomerEntity customerEntity = Instancio.of(CustomerEntity.class)
                .supply(field(CustomerEntity::getId), UUID::randomUUID)
                .supply(field(CustomerEntity::getAvatarUri), () -> URI.create("https://lvivjavaclub.com/" + UUID.randomUUID()))
                .create();

        assertNotNull(customerEntity.getId());
        assertNotNull(customerEntity.getAvatarUri());
    }

    @Test
    void example10_supplyCustomObjectsUsingRandom() {
        PurchaseOrderLineEntity purchaseOrderLineEntity = Instancio.of(PurchaseOrderLineEntity.class)
                .supply(field(PurchaseOrderLineEntity::getQuantity), random -> random.intRange(1, 10))
                .supply(field(PurchaseOrderLineEntity::getUnitPrice), random -> new BigDecimal(random.intRange(10, 50)))
                .create();

        assertThat(purchaseOrderLineEntity.getQuantity()).isBetween(1, 10);
        assertThat(purchaseOrderLineEntity.getUnitPrice()).isBetween(new BigDecimal(10), new BigDecimal(50));
    }

    @Test
    void example11_nullableFields() {
        CustomerProfileDto dto = Instancio.of(CustomerProfileDto.class)
                .withNullable(field(CustomerProfileDto::getWebsite))
                .withNullable(field(CustomerProfileDto::getNickname))
                .create();

        assertThat(dto).isNotNull();
    }

    @Test
    void example12_ignoreField() {
        CustomerEntity customer = Instancio.of(CustomerEntity.class)
                .ignore(field(CustomerEntity::getAvatarUri))
                .create();

        assertNull(customer.getAvatarUri());
    }

    @Test
    void example13_subtypeCollectionImplementation() {
        ProductEntity product = Instancio.of(ProductEntity.class)
                .subtype(field(ProductEntity::getImageUris), LinkedList.class)
                .generate(field(ProductEntity::getImageUris), gen -> gen.collection().size(3))
                .create();

        assertInstanceOf(LinkedList.class, product.getImageUris());
        assertEquals(3, product.getImageUris().size());
    }

    @Test
    void example14_subtypeSetToTreeSet() {
    }

    @Test
    void example15_createListOfEntities() {
        List<ProductEntity> productEntities = Instancio.ofList(ProductEntity.class).size(5).create();
        assertThat(productEntities).hasSize(5);
    }

    @Test
    void example16_createSetWithUniqueEmails() {
        Set<CustomerEntity> customers = Instancio.ofSet(CustomerEntity.class)
                .size(10)
                .withUnique(field(CustomerEntity::getEmail))
                .create();

        assertThat(customers).hasSize(10);
    }

    @Test
    void example17_createMap() {
        Map<UUID, ProductEntity> productMap = Instancio.ofMap(UUID.class, ProductEntity.class)
                .size(4)
                .create();

        assertThat(productMap).hasSize(4);
    }

    @Test
    void example18_typeTokenForGenericDto() {
        PageResponse<CustomerProfileDto> page = Instancio.create(new TypeToken<>() {});
        assertNotNull(page);
        assertNotNull(page.getContent());
    }

    @Test
    void example19_withTypeParametersForGenericDto() {
        @SuppressWarnings("unchecked")
        PageResponse<CustomerProfileDto> page = Instancio.of(PageResponse.class)
                .withTypeParameters(CustomerProfileDto.class)
                .create();

        assertNotNull(page);
    }

    @Test
    void example20_modelReuse() {
        Model<ProductEntity> model = Instancio.of(ProductEntity.class)
                .generate(field(ProductEntity::getPrice), gen -> gen.math().bigDecimal().range(new BigDecimal("1.00"),
                        new BigDecimal("20.00")))
                .generate(field(ProductEntity::getWeightKg), gen -> gen.math().bigDecimal().range(new BigDecimal("0.100"),
                        new BigDecimal("1.000")))
                .toModel();

        ProductEntity entity1 = Instancio.create(model);
        ProductEntity entity2 = Instancio.create(model);
    }

    @Test
    void example21_modelPlusOverride() {
        Model<ProductEntity> model = Instancio.of(ProductEntity.class)
                .generate(field(ProductEntity::getPrice), gen -> gen.math().bigDecimal().range(new BigDecimal("1.00"),
                        new BigDecimal("20.00")))
                .generate(field(ProductEntity::getWeightKg), gen -> gen.math().bigDecimal().range(new BigDecimal("0.100"),
                        new BigDecimal("1.000")))
                .toModel();

        ProductEntity entity = Instancio.of(model)
                .set(field(ProductEntity::getProductPage), URI.create("lvivjavaclub.com"))
                .create();

        assertThat(entity.getProductPage()).isNotNull();
    }

    @Test
    void example22_withSeedForReproducibility() {
        long seed = 12345L;

        ProductEntity p1 = Instancio.of(ProductEntity.class)
                .withSeed(seed)
                .create();

        ProductEntity p2 = Instancio.of(ProductEntity.class)
                .withSeed(seed)
                .create();

        assertEquals(p1, p2);
    }

    @Test
    void example23_onCompleteForDerivedValues() {
        PurchaseOrderLineEntity line = Instancio.of(PurchaseOrderLineEntity.class)
                .set(field(PurchaseOrderLineEntity::getQuantity), 3)
                .set(field(PurchaseOrderLineEntity::getUnitPrice), new BigDecimal("19.99"))
                .<PurchaseOrderLineEntity>onComplete(all(PurchaseOrderLineEntity.class), it ->
                        it.setLineTotal(it.getUnitPrice().multiply(BigDecimal.valueOf(it.getQuantity()))))
                .create();

        assertEquals(new BigDecimal("59.97"), line.getLineTotal());
    }

    @Test
    void example24_assignCopyValueBetweenFields() {
        CustomerProfileDto dto = Instancio.of(CustomerProfileDto.class)
                .assign(Assign.valueOf(field(CustomerProfileDto::getEmail))
                        .to(field(CustomerProfileDto::getFullName)))
                .create();

        assertEquals(dto.getEmail(), dto.getFullName());
    }

    @Test
    void example25_assignValueDirectly() {
        CustomerEntity customer = Instancio.of(CustomerEntity.class)
                .assign(
                        Assign.valueOf(field(CustomerEntity::getFirstName)).set("John"),
                        Assign.valueOf(field(CustomerEntity::getLastName)).set("Doe")
                )
                .create();

        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
    }

    @Test
    void example26_jpaAwareGeneration() {
        Settings settings = Settings.create().set(Keys.JPA_ENABLED, true);

        ProductEntity entity = Instancio.of(ProductEntity.class).withSettings(settings).create();

        assertThat(entity.getName()).hasSizeBetween(3, 120);
    }

    @Test
    void example27_beanValidationAwareGeneration() {
        Settings settings = Settings.create().set(Keys.BEAN_VALIDATION_ENABLED, true);

        CustomerEntity entity = Instancio.of(CustomerEntity.class).withSettings(settings).create();

        assertNotNull(entity);
        assertNotNull(entity.getFirstName());
        assertTrue(entity.getFirstName().length() >= 2);
        assertTrue(entity.getFirstName().length() <= 80);
    }

    @Test
    void example28_jpaAndBeanValidationTogether() {
        Settings settings = Settings.create()
                .set(Keys.JPA_ENABLED, true)
                .set(Keys.BEAN_VALIDATION_ENABLED, true);

        CustomerEntity customer = Instancio.of(CustomerEntity.class)
                .withSettings(settings)
                .create();

        assertNotNull(customer);
        assertNotNull(customer.getEmail());
        assertTrue(customer.getFirstName().length() <= 80);
    }

    @Test
    void example29_setBackReferencesAutomatically() {
        Settings settings = Settings.create().set(Keys.SET_BACK_REFERENCES, true);

        PurchaseOrderEntity order = Instancio.of(PurchaseOrderEntity.class)
                .withSettings(settings)
                .generate(field(PurchaseOrderEntity::getLines), gen -> gen.collection().size(3))
                .create();

        assertNotNull(order.getLines());
        assertEquals(3, order.getLines().size());

        for (PurchaseOrderLineEntity line : order.getLines()) {
            assertSame(order, line.getOrder());
        }
    }

    @Test
    void example30_streamObjects() {
        List<CustomerEntity> list = Instancio.stream(CustomerEntity.class).limit(3).collect(Collectors.toList());
        assertThat(list).hasSize(3);
    }

    @Test
    void example31_createRecordWithOverrides() {
        CreateOrderRequestDto request = Instancio.of(CreateOrderRequestDto.class)
                .generate(field(CreateOrderRequestDto.class, "metadata"),
                        gen -> gen.map().size(2))
                .generate(field(CreateOrderRequestDto.class, "callbackUri"),
                        gen -> gen.net().uri())
                .create();

        assertNotNull(request.customerId());
        assertNotNull(request.items());
    }

    @Test
    void example32_nestedRecordItemCustomisation() {
        CreateOrderRequestDto request = Instancio.of(CreateOrderRequestDto.class)
                .generate(field(CreateOrderRequestDto.class, "items"),
                        gen -> gen.collection().size(4))
                .generate(field(CreateOrderRequestDto.OrderItemRequestDto.class, "quantity"),
                        gen -> gen.ints().range(1, 3))
                .create();

        assertEquals(4, request.items().size());
        assertTrue(request.items().stream().allMatch(it -> it.quantity() >= 1 && it.quantity() <= 3));
    }

    @Test
    void example33_customUrisEverywhere() {
        ProductEntity product = Instancio.of(ProductEntity.class)
                .generate(all(URI.class), gen -> gen.net().uri())
                .create();

        assertNotNull(product.getProductPage());
    }

    @Test
    void example34_businessSpecificCustomer() {
        CustomerEntity customer = Instancio.of(CustomerEntity.class)
                .set(field(CustomerEntity::getStatus), CustomerStatus.ACTIVE)
                .generate(field(CustomerEntity::getBirthDate), gen -> gen.temporal().localDate().past())
                .<CustomerEntity>onComplete(all(CustomerEntity.class), c -> {
                    if (c.getTags().isEmpty()) {
                        c.getTags().add("demo");
                        c.getTags().add("lviv-java-club");
                    }
                }).create();

        assertEquals(CustomerStatus.ACTIVE, customer.getStatus());
        assertFalse(customer.getTags().isEmpty());
        assertTrue(customer.getBirthDate().isBefore(LocalDate.now()));
    }

    @Test
    void example35_usingSelectFieldByName() {
        ProductEntity product = Instancio.of(ProductEntity.class)
                .set(Select.field(ProductEntity.class, "name"), "Demo Product")
                .set(Select.field(ProductEntity.class, "description"), "Generated for live demo")
                .create();

        assertEquals("Demo Product", product.getName());
        assertEquals("Generated for live demo", product.getDescription());
    }

    @Test
    void example36_listFromModel() {
        Model<CustomerProfileDto> profileModel = Instancio.of(CustomerProfileDto.class)
                .withNullable(field(CustomerProfileDto::getNickname))
                .generate(field(CustomerProfileDto::getInterests), gen -> gen.collection().size(3))
                .toModel();

        List<CustomerProfileDto> profiles = Instancio.ofList(profileModel)
                .size(5)
                .create();

        assertEquals(5, profiles.size());
        assertTrue(profiles.stream().allMatch(p -> p.getInterests().size() == 3));
    }

    @Test
    void example37_uniqueProductsByName() {
        List<ProductEntity> products = Instancio.ofList(ProductEntity.class)
                .size(8)
                .withUnique(field(ProductEntity::getName))
                .create();

        long uniqueCount = products.stream()
                .map(ProductEntity::getName)
                .distinct()
                .count();

        assertEquals(8, uniqueCount);
    }

    @Test
    void example38_overrideAllInstants() {
        Instant fixedInstant = Instant.parse("2026-01-01T00:00:00Z");

        PurchaseOrderEntity order = Instancio.of(PurchaseOrderEntity.class)
                .set(all(Instant.class), fixedInstant)
                .create();

        assertEquals(fixedInstant, order.getSubmittedAt());
    }
}