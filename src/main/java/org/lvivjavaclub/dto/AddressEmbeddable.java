package org.lvivjavaclub.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressEmbeddable {

    @NotBlank
    @Size(min = 2, max = 80)
    @Column(length = 80)
    private String country;

    @NotBlank
    @Size(min = 2, max = 80)
    @Column(length = 80)
    private String city;

    @NotBlank
    @Size(min = 2, max = 120)
    @Column(length = 120)
    private String street;

    @NotBlank
    @Size(min = 2, max = 20)
    @Column(length = 20)
    private String postalCode;
}
