package org.pantry.shopping.cases.output;

public record DelFromCartResponse(Integer status) {
    public static final DelFromCartResponse OK_ALL = new DelFromCartResponse(0);
    public static final DelFromCartResponse OK_SOME = new DelFromCartResponse(1);
    public static final DelFromCartResponse NOT_FOUND = new DelFromCartResponse(2);
    public static final DelFromCartResponse INVALID = new DelFromCartResponse(3);
    public static final DelFromCartResponse TOO_MANY = new DelFromCartResponse(4);
    public static final DelFromCartResponse ERROR = new DelFromCartResponse(-1);
}
