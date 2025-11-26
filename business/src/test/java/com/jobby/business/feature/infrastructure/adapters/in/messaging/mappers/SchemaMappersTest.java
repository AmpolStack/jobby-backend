package com.jobby.business.feature.infrastructure.adapters.in.messaging.mappers;

import com.jobby.business.feature.address.infrastructure.adapters.in.messaging.SchemaAddressMapper;
import com.jobby.business.feature.address.infrastructure.persistence.jpa.JpaAddressEntity;
import com.jobby.business.feature.business.infrastructure.adapters.in.messaging.mappers.CustomMappers;
import com.jobby.business.feature.business.infrastructure.adapters.in.messaging.mappers.SchemaBusinessMapper;
import com.jobby.business.feature.city.infrastructure.adapters.in.messaging.SchemaCityMapper;
import com.jobby.business.feature.city.infrastructure.persistence.jpa.JpaCityEntity;
import com.jobby.business.feature.country.infrastructure.adapter.in.messaging.SchemaCountryMapper;
import com.jobby.business.feature.country.infrastructure.persistence.jpa.JpaCountryEntity;
import com.jobby.infraestructure.security.SecuredProperty;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class SchemaMappersTest {

    private final CustomMappers customMappers = new CustomMappers();
    private final SchemaCountryMapper countryMapper = Mappers.getMapper(SchemaCountryMapper.class);
    // We need to manually link mappers if we are not using Spring context in this
    // simple test,
    // or we can use Mappers.getMapper if they don't have complex dependencies.
    // However, they use 'uses = ...'. MapStruct generates implementations that
    // instantiate the 'uses' classes if they are not injected.
    // But here we are using 'componentModel = "spring"'.
    // So we might need to mock or manually instantiate the generated classes if we
    // want to test them in isolation without Spring.
    // A better approach for a quick verification test without spinning up Spring
    // context is to rely on the fact that MapStruct generates code.
    // But we can't easily instantiate the generated classes if we don't know their
    // names (usually Impl).

    // Let's try to use the generated implementations directly if possible, or just
    // rely on the fact that we want to test the logic.
    // Since I cannot run the compiler here to generate the Impl classes, I cannot
    // run this test *unless* I run 'mvn compile' first.
    // But I can write the test and ask the user to run it, or I can try to run 'mvn
    // test' which will generate sources.

    @Test
    void testCustomMappers() {
        Instant now = Instant.now();
        long millis = customMappers.map(now);
        assertEquals(now.toEpochMilli(), millis);
        assertEquals(now.toEpochMilli(), customMappers.map(millis).toEpochMilli());

        String text = "hello";
        byte[] bytes = customMappers.map(text);
        assertArrayEquals(text.getBytes(StandardCharsets.UTF_8), bytes);
        assertEquals(text, customMappers.map(bytes));
    }
}
