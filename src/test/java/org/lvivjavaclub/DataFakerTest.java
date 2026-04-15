package org.lvivjavaclub;

import org.lvivjavaclub.dto.AddressEmbeddable;
import org.lvivjavaclub.dto.AuditMetadata;
import org.lvivjavaclub.dto.CreateOrderRequestDto;
import org.lvivjavaclub.dto.CustomerEntity;
import org.lvivjavaclub.dto.CustomerProfileDto;
import org.lvivjavaclub.dto.CustomerStatus;
import org.lvivjavaclub.dto.ProductEntity;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class DataFakerTest {

    private final Faker faker = new Faker(new Locale("en"));

    @Test
    void generateManyCustomersWithRichFields() {
        List<CustomerEntity> customers = IntStream.range(0, 50)
                .mapToObj(i -> CustomerEntity.builder()
                        .id(UUID.randomUUID())
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .email(faker.internet().emailAddress())
                        .birthDate(LocalDate.now().minusYears(faker.number().numberBetween(18, 75)))
                        .status(faker.options().option(CustomerStatus.values()))
                        .avatarUri(URI.create(faker.internet().url()))
                        .address(AddressEmbeddable.builder()
                                .country(faker.address().country())
                                .city(faker.address().city())
                                .street(faker.address().streetAddress())
                                .postalCode(faker.address().zipCode())
                                .build())
                        .audit(AuditMetadata.builder()
                                .createdAt(Instant.now().minusSeconds(faker.number().numberBetween(10_000, 1_000_000)))
                                .lastModifiedAt(OffsetDateTime.now(ZoneOffset.UTC)
                                        .minusDays(faker.number().numberBetween(0, 90)))
                                .createdBy(faker.name().username())
                                .lastModifiedBy(faker.bool().bool() ? faker.name().username() : null)
                                .build())
                        .tags(randomCustomerTags())
                        .build())
                .toList();

        assertEquals(50, customers.size());
        assertTrue(customers.stream().allMatch(c -> c.getEmail() != null && !c.getEmail().isBlank()));
        assertTrue(customers.stream().allMatch(c -> c.getAddress() != null));
        assertTrue(customers.stream().allMatch(c -> c.getTags() != null && !c.getTags().isEmpty()));
    }

    @Test
    void generateManyProductsForCatalogSeed() {
        List<ProductEntity> products = IntStream.range(0, 100)
                .mapToObj(i -> ProductEntity.builder()
                        .id(UUID.randomUUID())
                        .name(faker.commerce().productName())
                        .description(faker.lorem().paragraph(2))
                        .price(BigDecimal.valueOf(faker.number().randomDouble(2, 10, 5000)))
                        .weightKg(BigDecimal.valueOf(faker.number().randomDouble(3, 1, 25)))
                        .productPage(URI.create(faker.internet().url()))
                        .warrantyPeriod(Duration.ofDays(faker.number().numberBetween(30, 730)))
                        .imageUris(List.of(
                                URI.create(faker.internet().image()),
                                URI.create(faker.internet().image()),
                                URI.create(faker.internet().image())
                        ))
                        .build())
                .toList();

        assertEquals(100, products.size());
        assertTrue(products.stream().allMatch(p -> p.getPrice() != null && p.getPrice().compareTo(BigDecimal.ZERO) > 0));
        assertTrue(products.stream().allMatch(p -> p.getImageUris() != null && p.getImageUris().size() == 3));
    }

    @Test
    void generateLargeCreateOrderRequestWithManyItemsAndMetadata() {
        List<CreateOrderRequestDto.OrderItemRequestDto> items = IntStream.range(0, 12)
                .mapToObj(i -> new CreateOrderRequestDto.OrderItemRequestDto(
                        UUID.randomUUID(),
                        faker.number().numberBetween(1, 6)
                ))
                .toList();

        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put("source", "lviv-java-club-demo");
        metadata.put("campaign", faker.marketing().buzzwords());
        metadata.put("customerSegment", faker.options().option("new", "active", "vip", "b2b"));
        metadata.put("traceId", UUID.randomUUID().toString());
        metadata.put("requestedBy", faker.name().username());
        metadata.put("note", faker.lorem().sentence());

        CreateOrderRequestDto request = new CreateOrderRequestDto(
                UUID.randomUUID(),
                items,
                URI.create("https://example.org/callback/" + UUID.randomUUID()),
                metadata
        );

        assertNotNull(request.customerId());
        assertEquals(12, request.items().size());
        assertEquals("lviv-java-club-demo", request.metadata().get("source"));
        assertNotNull(request.callbackUri());
    }

    @Test
    void generateManyCustomerProfilesWithNestedCollectionsAndMaps() {
        List<CustomerProfileDto> profiles = IntStream.range(0, 30)
                .mapToObj(i -> CustomerProfileDto.builder()
                        .customerId(UUID.randomUUID())
                        .fullName(faker.name().fullName())
                        .email(faker.internet().emailAddress())
                        .website(URI.create(faker.internet().url()))
                        .nickname(faker.bool().bool()
                                ? Optional.of(faker.funnyName().name())
                                : Optional.empty())
                        .interests(List.of(
                                faker.hobby().activity(),
                                faker.hobby().activity(),
                                faker.esports().game()
                        ))
                        .favouriteLinks(Set.of(
                                URI.create(faker.internet().url()),
                                URI.create(faker.internet().url()),
                                URI.create(faker.internet().url())
                        ))
                        .loyaltyPointsByCategory(Map.of(
                                "books", faker.number().numberBetween(0, 5000),
                                "electronics", faker.number().numberBetween(0, 5000),
                                "home", faker.number().numberBetween(0, 5000)
                        ))
                        .registeredAt(OffsetDateTime.now(ZoneOffset.UTC)
                                .minusDays(faker.number().numberBetween(1, 1000)))
                        .build())
                .toList();

        assertEquals(30, profiles.size());
        assertTrue(profiles.stream().allMatch(p -> p.getInterests().size() == 3));
        assertTrue(profiles.stream().allMatch(p -> p.getFavouriteLinks().size() == 3));
        assertTrue(profiles.stream().allMatch(p -> p.getLoyaltyPointsByCategory().size() == 3));
    }

    private Set<String> randomCustomerTags() {
        Set<String> tags = new LinkedHashSet<>();
        tags.add(faker.options().option("newsletter", "beta-user", "vip", "wholesale", "returning"));
        tags.add(faker.options().option("football", "java", "spring", "cloud", "testing"));
        if (faker.bool().bool()) {
            tags.add(faker.options().option("promo-2026", "black-friday", "loyalty", "referral"));
        }
        return tags;
    }
}