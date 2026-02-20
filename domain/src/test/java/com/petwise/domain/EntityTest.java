package com.petwise.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.petwise.domain.events.DomainEvent;
import com.petwise.domain.utils.IDUtils;
import com.petwise.domain.validation.ValidationHandler;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Entity")
public class EntityTest extends UnitTest {

    @Test
    @DisplayName("should initialise with an empty event list when null is supplied as events")
    public void shouldInitialiseWithEmptyEventList_whenNullEventsAreSupplied() {
        final var anEntity = new DummyEntity(new DummyID(), null);

        assertNotNull(anEntity.getDomainEvents());
        assertTrue(anEntity.getDomainEvents().isEmpty());
    }

    @Test
    @DisplayName("should create a defensive copy of the events list supplied in the constructor")
    public void shouldCreateDefensiveCopyOfEventList_whenEventsArePassedToConstructor() {
        final List<DomainEvent> events = new ArrayList<>();
        final var anEntity = new DummyEntity(new DummyID(), events);

        assertNotNull(anEntity.getDomainEvents());
        assertTrue(anEntity.getDomainEvents().isEmpty());
        assertNotSame(anEntity.getDomainEvents(), events);
        assertThrows(RuntimeException.class, () -> anEntity.getDomainEvents().add(Instant::now));
    }

    @Test
    @DisplayName("should add the event to the internal list when registerEvent is called")
    public void shouldAddEvent_whenRegisterEventIsCalled() {
        final var anEntity = new DummyEntity(new DummyID(), new ArrayList<>());

        anEntity.registerEvent(Instant::now);

        assertNotNull(anEntity.getDomainEvents());
        assertEquals(1, anEntity.getDomainEvents().size());
        assertThrows(RuntimeException.class, () -> anEntity.getDomainEvents().add(Instant::now));
    }

    @Test
    @DisplayName("should dispatch all events and clear the list when publishDomainEvents is called")
    public void shouldDispatchAllEventsAndClearList_whenPublishDomainEventsIsCalled() {
        final var expectedRemainingEvents = 0;
        final var expectedPublishedEvents = 2;
        final var publishedCount = new AtomicInteger(0);
        final var anEntity = new DummyEntity(new DummyID(), new ArrayList<>());

        anEntity.registerEvent(Instant::now);
        anEntity.registerEvent(Instant::now);

        assertEquals(2, anEntity.getDomainEvents().size());

        anEntity.publishDomainEvents(event -> publishedCount.incrementAndGet());

        assertNotNull(anEntity.getDomainEvents());
        assertEquals(expectedRemainingEvents, anEntity.getDomainEvents().size());
        assertEquals(expectedPublishedEvents, publishedCount.get());
        assertThrows(RuntimeException.class, () -> anEntity.getDomainEvents().add(Instant::now));
    }

    // -------------------------------------------------------------------------
    // Test doubles
    // -------------------------------------------------------------------------

    public static class DummyID extends Identifier<String> {

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
