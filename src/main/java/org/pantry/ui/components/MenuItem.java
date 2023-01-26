package org.pantry.ui.components;

import java.util.function.Function;

public abstract class MenuItem {
    private String label;

    protected void setLabel(String label) {
        this.label = label;
    }

    protected String getLabel() {
        return this.label;
    }

    @Override
    public String toString() {
        return this.getLabel();
    }

    public abstract void select();
}
