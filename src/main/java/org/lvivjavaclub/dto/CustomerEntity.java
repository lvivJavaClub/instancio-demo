package org.lvivjavaclub.dto;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.net.URI;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {

    @Id
    private UUID id;

    @NotBlank
    @Size(min = 2, max = 80)
    @Column(length = 80, nullable = false)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 80)
    @Column(length = 80, nullable = false)
    private String lastName;

    @Email
    @Column(length = 120, nullable = false, unique = true)
    private String email;

    @Past
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private CustomerStatus status;

    @Column(length = 500)
    private URI avatarUri;

    @Embedded
    private AddressEmbeddable address;

    @Embedded
    private AuditMetadata audit;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "customer_tags", joinColumns = @JoinColumn(name = "customer_id"))
    @Column(name = "tag", length = 40)
    @Builder.Default
    private Set<String> tags = new LinkedHashSet<>();
}
