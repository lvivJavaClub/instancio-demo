package org.lvivjavaclub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileDto {

    private UUID customerId;

    @NotBlank
    @Size(min = 2, max = 120)
    private String fullName;

    @Email
    private String email;

    private URI website;

    private Optional<String> nickname;

    @NotEmpty
    private List<String> interests;

    private Set<URI> favouriteLinks;

    private Map<String, Integer> loyaltyPointsByCategory;

    private OffsetDateTime registeredAt;
}