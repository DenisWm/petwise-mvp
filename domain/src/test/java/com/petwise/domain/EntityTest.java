package com.petwise.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.petwise.domain.events.DomainEvent;
import com.petwise.domain.utils.IDUtils;
import com.petwise.domain.validation.ValidationHandler;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

public class EntityTest extends UnitTest {

    @Test
    public void givenNullAsEvents_whenInstantiate_shouldBeOk() {
        final List<DomainEvent> events = null;
        final var anEntity = new DummyEntity(new DummyID(), events);

        assertNotNull(anEntity.getDomainEvents());
        assertTrue(anEntity.getDomainEvents().isEmpty());
    }

    @Test
    public void givenDomainEvents_whenPassInConstructor_shouldCreateADefensiveClone() {
        final List<DomainEvent> events = new ArrayList<>();
        final var anEntity = new DummyEntity(new DummyID(), events);

        assertNotNull(anEntity.getDomainEvents());
        assertTrue(anEntity.getDomainEvents().isEmpty());
        assertNotSame(anEntity.getDomainEvents(), events);
        assertThrows(
                RuntimeException.class,
                () -> {
                    final var actualEvents = anEntity.getDomainEvents();
                    actualEvents.add(Instant::now);
                });
    }

    @Test
    public void givenDomainEvents_whenCallsRegisterEvent_shouldAddEventToList() {
        final List<DomainEvent> events = new ArrayList<>();
        final var anEntity = new DummyEntity(new DummyID(), events);
        int expectedEvents = 1;

        anEntity.registerEvent(Instant::now);

        assertNotNull(anEntity.getDomainEvents());
        assertEquals(expectedEvents, anEntity.getDomainEvents().size());
        assertNotSame(anEntity.getDomainEvents(), events);
        assertThrows(
                RuntimeException.class,
                () -> {
                    final var actualEvents = anEntity.getDomainEvents();
                    actualEvents.add(Instant::now);
                });
    }

    @Test
    public void
            givenDomainEvents_whenCallsPublishDomainEvents_shouldCallPublisherClearEventsList() {
        final List<DomainEvent> events = new ArrayList<>();
        final var expectedEvents = 0;
        final var expectedSentEvents = 2;
        final var counter = new AtomicInteger(0);
        final var anEntity = new DummyEntity(new DummyID(), events);
        anEntity.registerEvent(Instant::now);

        anEntity.registerEvent(Instant::now);

        assertEquals(2, anEntity.getDomainEvents().size());

        anEntity.publishDomainEvents(
                event -> {
                    counter.incrementAndGet();
                });

        assertNotNull(anEntity.getDomainEvents());
        assertEquals(expectedEvents, anEntity.getDomainEvents().size());
        assertEquals(expectedSentEvents, counter.get());
        assertNotSame(anEntity.getDomainEvents(), events);
        assertThrows(
                RuntimeException.class,
                () -> {
                    final var actualEvents = anEntity.getDomainEvents();
                    actualEvents.add(Instant::now);
                });
    }

    public static class DummyID extends Identifier {

        private final String id;

        public DummyID() {
            this.id = IDUtils.uuid();
        }

        @Override
        public String getValue() {
            return this.id;
        }
    }

    public static class DummyEntity extends Entity<DummyID> {

        public DummyEntity(final DummyID dummyID, final List<DomainEvent> domainEvents) {
            super(dummyID, domainEvents);
        }

        @Override
        public void validate(ValidationHandler handler) {}
    }
}
