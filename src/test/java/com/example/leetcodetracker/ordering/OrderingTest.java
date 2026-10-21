package com.example.leetcodetracker.ordering;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Ordering}. Plain JUnit, no Spring context: the class is
 * pure positional logic over a list of {@link Positionable}.
 *
 * <p>The invariant under test throughout is that positions stay contiguous and
 * zero-based ({@code 0..n-1}) after every operation, since that is what
 * {@code normalize()} exists to restore and what {@code @OrderBy("position ASC")}
 * relies on at the persistence layer.
 */
class OrderingTest {

    private final Ordering<Item> ordering = new Ordering<>();

    @Nested
    @DisplayName("insert")
    class Insert {

        @Test
        void appendsWhenPositionIsMinusOne() {
            List<Item> items = items("a", "b");

            ordering.insert(items, new Item("c"), -1);

            assertThat(inPositionOrder(items)).containsExactly("a", "b", "c");
            assertThat(positionsOf(items)).containsExactly(0, 1, 2);
        }

        @Test
        void prependsAndShiftsTailWhenPositionIsZero() {
            List<Item> items = items("a", "b");

            ordering.insert(items, new Item("c"), 0);

            assertThat(inPositionOrder(items)).containsExactly("c", "a", "b");
            assertThat(positionsOf(items)).containsExactlyInAnyOrder(0, 1, 2);
        }

        @Test
        void shiftsTailWhenInsertingIntoTheMiddle() {
            List<Item> items = items("a", "b", "c");

            ordering.insert(items, new Item("d"), 1);

            assertThat(inPositionOrder(items)).containsExactly("a", "d", "b", "c");
            assertThat(positionsOf(items)).containsExactlyInAnyOrder(0, 1, 2, 3);
        }

        @Test
        void appendsWhenPositionIsBeyondTheEnd() {
            List<Item> items = items("a", "b");

            ordering.insert(items, new Item("c"), 99);

            assertThat(inPositionOrder(items)).containsExactly("a", "b", "c");
            assertThat(positionsOf(items)).containsExactly(0, 1, 2);
        }

        @Test
        void insertsIntoAnEmptyCollection() {
            List<Item> items = items();

            ordering.insert(items, new Item("a"), -1);

            assertThat(positionsOf(items)).containsExactly(0);
        }

        @Test
        void rejectsAnItemAlreadyInTheCollection() {
            List<Item> items = items("a", "b");
            Item existing = items.get(0);

            assertThatThrownBy(() -> ordering.insert(items, existing, 1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("already in the collection");
        }

        @Test
        void rejectsPositionsBelowMinusOne() {
            List<Item> items = items("a", "b");

            assertThatThrownBy(() -> ordering.insert(items, new Item("c"), -2))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Position must be >= -1");
        }
    }

    @Nested
    @DisplayName("move")
    class Move {

        @Test
        void shiftsInterveningItemsBackWhenMovingForward() {
            List<Item> items = items("a", "b", "c", "d");

            ordering.move(items, items.get(0), 2);

            assertThat(inPositionOrder(items)).containsExactly("b", "c", "a", "d");
            assertThat(positionsOf(items)).containsExactlyInAnyOrder(0, 1, 2, 3);
        }

        @Test
        void shiftsInterveningItemsForwardWhenMovingBackward() {
            List<Item> items = items("a", "b", "c", "d");

            ordering.move(items, items.get(3), 1);

            assertThat(inPositionOrder(items)).containsExactly("a", "d", "b", "c");
            assertThat(positionsOf(items)).containsExactlyInAnyOrder(0, 1, 2, 3);
        }

        @Test
        void isANoOpWhenTargetMatchesCurrentPosition() {
            List<Item> items = items("a", "b", "c");

            ordering.move(items, items.get(1), 1);

            assertThat(inPositionOrder(items)).containsExactly("a", "b", "c");
            assertThat(positionsOf(items)).containsExactly(0, 1, 2);
        }

        @Test
        void movesToTheEndWhenPositionIsMinusOne() {
            List<Item> items = items("a", "b", "c");

            ordering.move(items, items.get(0), -1);

            assertThat(inPositionOrder(items)).containsExactly("b", "c", "a");
            // Must stay 0..n-1: a move does not grow the collection.
            assertThat(positionsOf(items)).containsExactlyInAnyOrder(0, 1, 2);
        }

        @Test
        void clampsToTheEndWhenPositionIsBeyondTheLastSlot() {
            List<Item> items = items("a", "b", "c");

            ordering.move(items, items.get(0), 99);

            assertThat(inPositionOrder(items)).containsExactly("b", "c", "a");
            assertThat(positionsOf(items)).containsExactlyInAnyOrder(0, 1, 2);
        }

        @Test
        void isANoOpOnASingleItemCollection() {
            List<Item> items = items("a");

            ordering.move(items, items.get(0), -1);

            assertThat(positionsOf(items)).containsExactly(0);
        }

        @Test
        void rejectsAnItemNotInTheCollection() {
            List<Item> items = items("a", "b");

            assertThatThrownBy(() -> ordering.move(items, new Item("z"), 0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("not in collection");
        }
    }

    @Nested
    @DisplayName("remove")
    class Remove {

        @Test
        void renumbersTheTailAfterRemovingFromTheMiddle() {
            List<Item> items = items("a", "b", "c", "d");

            ordering.remove(items, items.get(1));

            assertThat(inPositionOrder(items)).containsExactly("a", "c", "d");
            assertThat(positionsOf(items)).containsExactly(0, 1, 2);
        }

        @Test
        void renumbersEverythingAfterRemovingTheHead() {
            List<Item> items = items("a", "b", "c");

            ordering.remove(items, items.get(0));

            assertThat(inPositionOrder(items)).containsExactly("b", "c");
            assertThat(positionsOf(items)).containsExactly(0, 1);
        }

        @Test
        void leavesRemainingItemsUntouchedAfterRemovingTheTail() {
            List<Item> items = items("a", "b", "c");

            ordering.remove(items, items.get(2));

            assertThat(inPositionOrder(items)).containsExactly("a", "b");
            assertThat(positionsOf(items)).containsExactly(0, 1);
        }

        @Test
        void emptiesASingleItemCollection() {
            List<Item> items = items("a");

            ordering.remove(items, items.get(0));

            assertThat(items).isEmpty();
        }

        @Test
        void rejectsAnItemNotInTheCollection() {
            List<Item> items = items("a", "b");

            assertThatThrownBy(() -> ordering.remove(items, new Item("z")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("not in collection");
        }
    }

    @Nested
    @DisplayName("normalize")
    class Normalize {

        @Test
        void closesGapsBetweenPositions() {
            List<Item> items = new ArrayList<>(
                    List.of(new Item("a", 0), new Item("b", 5), new Item("c", 9)));

            ordering.normalize(items);

            assertThat(inPositionOrder(items)).containsExactly("a", "b", "c");
            assertThat(positionsOf(items)).containsExactly(0, 1, 2);
        }

        @Test
        void sortsTheListByPositionRatherThanExistingListOrder() {
            List<Item> items = new ArrayList<>(
                    List.of(new Item("a", 2), new Item("b", 0), new Item("c", 1)));

            ordering.normalize(items);

            assertThat(items).extracting(Item::name).containsExactly("b", "c", "a");
            assertThat(positionsOf(items)).containsExactly(0, 1, 2);
        }

        @Test
        void isANoOpOnAnEmptyCollection() {
            List<Item> items = items();

            ordering.normalize(items);

            assertThat(items).isEmpty();
        }
    }

    /** Builds a mutable list of items whose positions already match their indices. */
    private static List<Item> items(String... names) {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            items.add(new Item(names[i], i));
        }
        return items;
    }

    private static List<Integer> positionsOf(List<Item> items) {
        return items.stream().map(Positionable::getPosition).toList();
    }

    /** The user-visible ordering, which {@code move} expresses via positions only. */
    private static List<String> inPositionOrder(List<Item> items) {
        return items.stream()
                .sorted(Comparator.comparingInt(Positionable::getPosition))
                .map(Item::name)
                .toList();
    }

    /**
     * Stand-in for the persisted join entities. Deliberately relies on identity
     * equality, matching entities that do not override {@code equals}, since
     * {@code Ordering} uses {@code List.contains} and {@code List.remove}.
     */
    private static final class Item implements Positionable {

        private final String name;
        private int position;

        Item(String name) {
            this(name, 0);
        }

        Item(String name, int position) {
            this.name = name;
            this.position = position;
        }

        String name() {
            return name;
        }

        @Override
        public int getPosition() {
            return position;
        }

        @Override
        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public String toString() {
            return name + "@" + position;
        }
    }
}
