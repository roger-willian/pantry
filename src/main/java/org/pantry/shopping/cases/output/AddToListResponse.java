package org.pantry.shopping.cases.output;

public record AddToListResponse(Integer status) {
    public final static AddToListResponse OK_NEW = new AddToListResponse(0);
    public final static AddToListResponse OK_INCREASED = new AddToListResponse(1);
    public final static AddToListResponse INVALID = new AddToListResponse(3);
    public final static AddToListResponse ERROR = new AddToListResponse(-1);
}
