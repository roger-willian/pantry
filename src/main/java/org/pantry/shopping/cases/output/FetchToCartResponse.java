package org.pantry.shopping.cases.output;

public record FetchToCartResponse(Integer status) {
    public static final FetchToCartResponse OK_NEW = new FetchToCartResponse(0);
    public static final FetchToCartResponse OK_INCREASED = new FetchToCartResponse(1);
    public static final FetchToCartResponse ERROR = new FetchToCartResponse(-1);
}
