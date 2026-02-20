package com.petwise;

import com.petwise.infrastructure.tutor.persistence.TutorRepository;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * JUnit 5 extension that truncates all repository data before each test method.
 *
 * <p>Used by {@link PostgresGatewayTest} and {@link IntegrationTest} to guarantee that every test
 * starts against a clean database, regardless of what previous tests may have persisted.
 *
 * <p>Add new repositories to the list inside {@code beforeEach} as new
 * aggregates are introduced to the project.
 */
class PostgresCleanUpExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(final ExtensionContext context) {
        final var appContext = SpringExtension.getApplicationContext(context);

        cleanUp(List.of(appContext.getBean(TutorRepository.class)));
    }

    @SuppressWarnings("rawtypes")
    private void cleanUp(final Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}
