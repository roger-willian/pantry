package org.pantry.shopping.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public record CartItem(Long id, Double quantity, String unit, String name, Integer pricePerUnit, Integer expiration) {

    public static final SimpleDateFormat dateFormat;

    static {
            dateFormat = new SimpleDateFormat("yyyyMMdd");
            dateFormat.setLenient(false);
    };

    public boolean isSimilar(CartItem other) {
        if (other == null) return false;
        if (!other.name().equals(name)) return false;
        if (!other.unit().equals(unit)) return false;
        if (!other.pricePerUnit().equals(pricePerUnit)) return false;
        if (!other.expiration().equals(expiration)) return false;

        return true;
    }

    public boolean isValid() {
        if (quantity <= 0D) return false;
        if (unit.trim().isEmpty()) return false;
        if (name.trim().isEmpty()) return false;
        if (pricePerUnit < 0D) return false;

        try {
            dateFormat.parse(expiration.toString());
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
