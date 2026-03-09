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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link Entity}. */
@DisplayName("Entity")
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.LongVariable",
    "PMD.AvoidDuplicateLiterals",
    "PMD.ShortVariable",
    "PMD.AbstractClassWithoutAbstractMethod"
})
class EntityTest extends UnitTest {
    EntityTest() {}

    /** Tests for entity construction and initialisation. */
    @Nested
    @DisplayName("Construction")
    class ConstructionTest {
        ConstructionTest() {}

        @Test
        @DisplayName("should initialise with an empty event list when null is supplied as events")
        void shouldInitialiseWithEmptyEventList_whenNullEventsAreSupplied() {
            final var anEntity = new DummyEntity(new DummyID(), null);

            assertNotNull(anEntity.getDomainEvents());
            assertTrue(anEntity.getDomainEvents().isEmpty());
        }

        @Test
        @DisplayName(
                "should create a defensive copy of the events list supplied in the constructor")
        void shouldCreateDefensiveCopyOfEventList_whenEventsArePassedToConstructor() {
            final List<DomainEvent> events = new ArrayList<>();
            final var anEntity = new DummyEntity(new DummyID(), events);

            assertNotNull(anEntity.getDomainEvents());
            assertTrue(anEntity.getDomainEvents().isEmpty());
            assertNotSame(anEntity.getDomainEvents(), events);
            assertThrows(
                    RuntimeException.class, () -> anEntity.getDomainEvents().add(Instant::now));
        }
    }

    /** Tests for domain event registration and publishing. */
    @Nested
    @DisplayName("Domain Events")
    class DomainEventsTest {
        DomainEventsTest() {}

        @Test
        @DisplayName("should add the event to the internal list when registerEvent is called")
        void shouldAddEvent_whenRegisterEventIsCalled() {
            final var anEntity = new DummyEntity(new DummyID(), new ArrayList<>());

            anEntity.registerEvent(Instant::now);

            assertNotNull(anEntity.getDomainEvents());
            assertEquals(1, anEntity.getDomainEvents().size());
            assertThrows(
                    RuntimeException.class, () -> anEntity.getDomainEvents().add(Instant::now));
        }

        @Test
        @DisplayName(
                "should dispatch all events and clear the list when "
                        + "publishDomainEvents is called")
        void shouldDispatchAllEventsAndClearList_whenPublishDomainEventsIsCalled() {
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
            assertThrows(
                    RuntimeException.class, () -> anEntity.getDomainEvents().add(Instant::now));
        }

        @Test
        @DisplayName("should be a no-op when publishDomainEvents is called with null publisher")
        void shouldBeNoOp_whenPublishDomainEventsCalledWithNullPublisher() {
            final var anEntity = new DummyEntity(new DummyID(), new ArrayList<>());
            anEntity.registerEvent(Instant::now);
            anEntity.publishDomainEvents(null);
            assertEquals(1, anEntity.getDomainEvents().size());
        }

        @Test
        @DisplayName("should silently ignore null events passed to registerEvent")
        void shouldIgnoreNullEvent_whenRegisterEventCalledWithNull() {
            final var anEntity = new DummyEntity(new DummyID(), new ArrayList<>());
            anEntity.registerEvent(null);
            assertTrue(anEntity.getDomainEvents().isEmpty());
        }
    }

    /** Tests for the equals / hashCode contract. */
    @Nested
    @DisplayName("Equality")
    class EqualityTest {
        EqualityTest() {}

        @Test
        @DisplayName("should be equal to itself")
        void shouldBeEqualToItself() {
            final var anEntity = new DummyEntity(new DummyID(), new ArrayList<>());
            assertEquals(anEntity, anEntity);
        }

        @Test
        @DisplayName("should not be equal to null")
        void shouldNotBeEqualToNull() {
            final var anEntity = new DummyEntity(new DummyID(), new ArrayList<>());
            assertNotEquals(null, anEntity);
        }

        @Test
        @DisplayName("should not be equal to a different class instance")
        void shouldNotBeEqualToDifferentClassInstance() {
            final var anEntity = new DummyEntity(new DummyID(), new ArrayList<>());
            assertNotEquals("not-an-entity", anEntity);
        }

        @Test
        @DisplayName("two entities with the same ID should be equal")
        void shouldBeEqual_whenSameId() {
            final var id = new DummyID();
            final var entity1 = new DummyEntity(id, new ArrayList<>());
            final var entity2 = new DummyEntity(id, new ArrayList<>());
            assertEquals(entity1, entity2);
            assertEquals(entity1.hashCode(), entity2.hashCode());
        }

        @Test
        @DisplayName("two entities with different IDs should not be equal")
        void shouldNotBeEqual_whenDifferentId() {
            final var entity1 = new DummyEntity(new DummyID(), new ArrayList<>());
            final var entity2 = new DummyEntity(new DummyID(), new ArrayList<>());
            assertNotEquals(entity1, entity2);
        }
    }

    /** Dummy identifier for testing. */
    @SuppressWarnings({"PMD.ShortVariable", "PMD.CallSuperInConstructor"})
    static class DummyID extends Identifier<String> {
        private final String id;

        /** Creates a new DummyID with a random UUID. */
        DummyID() {
            this.id = IDUtils.uuid();
        }

        @Override
        public String getValue() {
            return this.id;
        }
    }

    /** Dummy entity for testing. */
    @SuppressWarnings("PMD.UncommentedEmptyMethodBody")
    static class DummyEntity extends Entity<DummyID> {

        /**
         * Creates a DummyEntity.
         *
         * @param dummyID the entity ID
         * @param domainEvents initial events
         */
        DummyEntity(final DummyID dummyID, final List<DomainEvent> domainEvents) {
            super(dummyID, domainEvents);
        }

        @Override
        public void validate(final ValidationHandler handler) {}
    }
}
