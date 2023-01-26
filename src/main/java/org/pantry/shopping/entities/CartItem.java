package org.pantry.shopping.entities;

public record CartItem(Double quantity, String unit, String name, Integer pricePerUnit, Integer expiration) {

    public boolean isSimilar(CartItem other) {
        if (other == null) return false;
        if (!other.name().equals(name)) return false;
        if (!other.unit().equals(unit)) return false;
        if (!other.pricePerUnit().equals(pricePerUnit)) return false;
        if (!other.expiration().equals(expiration)) return false;

        return true;
    }
}
