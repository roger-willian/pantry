package org.pantry.ui.components;

import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Prompt {
    public static <R> R ask(String question, Converter<String, R> convert, Predicate<R> validate) {
        R response = null;
        boolean valid = false;
        do {
            try {
                System.out.println(question);
                String raw = System.console().readLine();
                response = convert.convert(raw);
                valid = validate.test(response);
                if (!valid) System.out.println("Invalid");
            } catch (Exception e) {
                // Do nothing
            }
        } while (!valid);

        return response;
    }
    public static String ask(String question, Predicate<String> validate) {
        return ask(question, identity -> identity, validate);
    }

    public static <R> R ask(String question, Converter<String, R> convert) {
        return ask(question, convert, alwaysValid -> true);
    }

    public static String ask(String question) {
        return ask(question, identity->identity, alwaysValid -> true);
    }

    public interface Converter<T, R> {
        public R convert(T raw) throws Exception;
    }
}
