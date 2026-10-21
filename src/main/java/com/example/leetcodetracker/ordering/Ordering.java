package com.example.leetcodetracker.ordering;

import java.util.Comparator;
import java.util.List;

public class Ordering<T extends Positionable> {

    public void insert(List<T> items, T item, int position) {
        if (items.contains(item)) {
            throw new IllegalArgumentException("Item is already in the collection");
        }
        position = normalizeTarget(position, items.size());
        items.add(position, item);

        for (int i = position; i < items.size(); i++) {
            items.get(i).setPosition(i);
        }
    }

    public void move(List<T> items, T item, int newPosition) {
        if (!items.contains(item)) {
            throw new IllegalArgumentException("Item is not in collection.");
        }
        int oldPosition = item.getPosition();
        // A move does not grow the collection, so the last valid slot is size() - 1.
        // insert() does grow it, which is why it passes size() instead.
        newPosition = normalizeTarget(newPosition, items.size() - 1);

        if (oldPosition == newPosition) {
            return;
        }

        if (newPosition < oldPosition) {
            for (T other : items) {
                int position = other.getPosition();

                if (position >= newPosition && position < oldPosition) {
                    other.setPosition(position + 1);
                }
            }
        } else {
            for (T other : items) {
                int position = other.getPosition();

                if (position > oldPosition && position <= newPosition) {
                    other.setPosition(position - 1);
                }
            }
        }

        item.setPosition(newPosition);
    }

    public void remove(List<T> items, T item) {
        if (!items.contains(item)) {
            throw new IllegalArgumentException("Item is not in collection.");
        }

        int oldPosition = item.getPosition();
        items.remove((Object) item);

        for (int i = oldPosition; i < items.size(); i++) {
            items.get(i).setPosition(i);
        }
    }

    private int normalizeTarget(int requested, int size) {
        if (requested == -1 || requested >= size) {
            return size;
        }
        if (requested < 0) {
            throw new IllegalArgumentException("Position must be >= -1.");
        }
        return requested;
    }

    public void normalize(List<T> items) {
        items.sort(Comparator.comparingInt(Positionable::getPosition));

        for (int i = 0; i < items.size(); i++) {
            items.get(i).setPosition(i);
        }
    }
}
