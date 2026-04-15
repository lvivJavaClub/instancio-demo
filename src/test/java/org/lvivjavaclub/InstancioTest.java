package org.lvivjavaclub;

import org.instancio.Instancio;
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

import static org.instancio.Select.all;
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
    }

    @Test
    void example03_setStaticValues() {
    }

    @Test
    void example04_setAllStringsToOneValue() {
    }

    @Test
    void example05_generateStringLengths() {
    }

    @Test
    void example06_generateNumbersAndBigDecimals() {
    }

    @Test
    void example07_generateTemporalValues() {
    }

    @Test
    void example08_generateEnumExcludingCertainValues() {
    }

    @Test
    void example09_supplyWithSupplier_newValueEachTime() {
    }

    @Test
    void example10_supplyCustomObjectsUsingRandom() {
    }

    @Test
    void example11_nullableFields() {
    }

    @Test
    void example12_ignoreField() {
    }

    @Test
    void example13_subtypeCollectionImplementation() {
    }

    @Test
    void example14_subtypeSetToTreeSet() {
    }

    @Test
    void example15_createListOfEntities() {
    }

    @Test
    void example16_createSetWithUniqueEmails() {
    }

    @Test
    void example17_createMap() {
    }

    @Test
    void example18_typeTokenForGenericDto() {
    }

    @Test
    void example19_withTypeParametersForGenericDto() {
    }

    @Test
    void example20_modelReuse() {
    }

    @Test
    void example21_modelPlusOverride() {
    }

    @Test
    void example22_withSeedForReproducibility() {
    }

    @Test
    void example23_onCompleteForDerivedValues() {
    }

    @Test
    void example24_assignCopyValueBetweenFields() {
    }

    @Test
    void example25_assignValueDirectly() {
    }

    @Test
    void example26_jpaAwareGeneration() {
    }

    @Test
    void example27_beanValidationAwareGeneration() {
    }

    @Test
    void example28_jpaAndBeanValidationTogether() {
    }

    @Test
    void example29_setBackReferencesAutomatically() {
    }

    @Test
    void example30_streamObjects() {
    }

    @Test
    void example31_createRecordWithOverrides() {
    }

    @Test
    void example32_nestedRecordItemCustomisation() {
    }

    @Test
    void example33_customUrisEverywhere() {
    }

    @Test
    void example34_businessSpecificCustomer() {
    }

    @Test
    void example35_usingSelectFieldByName() {
    }

    @Test
    void example36_listFromModel() {
    }

    @Test
    void example37_uniqueProductsByName() {
    }

    @Test
    void example38_overrideAllInstants() {
    }
}