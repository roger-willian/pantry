package org.pantry.shopping.entities;

public record ListItem(Long id, Double quantity, String unit, String name) {
    public boolean isValid() {
        if (quantity <= 0) return false;
        else if (unit.trim().isEmpty()) return false;
        else if (name.trim().isEmpty()) return false;
        else return true;
    }

    public boolean isSimilar(ListItem other) {
        if (other == null) return false;
        if (!other.name().equals(name)) return false;
        if (!other.unit().equals(unit)) return false;

        return true;
    }
}
