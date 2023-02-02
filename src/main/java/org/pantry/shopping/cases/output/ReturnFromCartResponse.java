package org.pantry.shopping.cases.output;

public record ReturnFromCartResponse(Integer status) {
    public static final ReturnFromCartResponse OK_ALL = new ReturnFromCartResponse(0);
    public static final ReturnFromCartResponse OK_SOME = new ReturnFromCartResponse(1);
    public static final ReturnFromCartResponse TOO_MANY = new ReturnFromCartResponse(2);
    public static final ReturnFromCartResponse NOT_FOUND = new ReturnFromCartResponse(3);
    public static final ReturnFromCartResponse INVALID = new ReturnFromCartResponse(4);
    public static final ReturnFromCartResponse ERROR = new ReturnFromCartResponse(-1);
}
