package com.example.leetcodetracker.ordering;

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
        newPosition = normalizeTarget(newPosition, items.size());

        items.remove(oldPosition);
        if (newPosition > oldPosition) {
            newPosition--;
        }

        items.add(newPosition, item);

        int start = Math.min(oldPosition, newPosition);
        int end = Math.max(oldPosition, newPosition);
        for (int i = start; i <= end; i++) {
            items.get(i).setPosition(i);
        }
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
}
