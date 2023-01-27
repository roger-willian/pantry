package org.pantry.shopping.cases.output;

public record DelFromListResponse(Integer status) {
    public static final DelFromListResponse OK = new DelFromListResponse(0);
    public static final DelFromListResponse NOT_FOUND = new DelFromListResponse(1);
    public static final DelFromListResponse ERROR = new DelFromListResponse(-1);
}
