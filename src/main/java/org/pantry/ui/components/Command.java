package org.pantry.ui.components;

public class Command extends MenuItem {
    private final Action command;
    public Command(String label, Action command) {
        this.command = command;
        this.setLabel(label);
    }
    @Override
    public void select() {
        command.execute();
    }

    public interface Action {
        void execute();
    }

    public final static Action NoAction = new Action() {
        @Override
        public void execute() {
            // Does nothing
        }
    };
}
