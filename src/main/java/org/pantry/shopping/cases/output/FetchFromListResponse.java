package org.pantry.shopping.cases.output;

public record FetchFromListResponse(Integer status) {
    public static final FetchFromListResponse OK_ALL = new FetchFromListResponse(0);
    public static final FetchFromListResponse OK_SOME = new FetchFromListResponse(1);
    public static final FetchFromListResponse NOT_FOUND = new FetchFromListResponse(2);
    public static final FetchFromListResponse ERROR = new FetchFromListResponse(-1);
}
