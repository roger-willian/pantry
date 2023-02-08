package org.pantry.shopping.cases.output;

public record DelFromListInternalResponse(StatusCode status) {

    public enum StatusCode {
        OK,
        NOT_FOUND,
        ERROR
    }

    public static DelFromListInternalResponse ok() {
        return new DelFromListInternalResponse(StatusCode.OK);
    }

    public static DelFromListInternalResponse notFound() {
        return new DelFromListInternalResponse(StatusCode.NOT_FOUND);
    }

    public static DelFromListInternalResponse error() {
        return new DelFromListInternalResponse(StatusCode.ERROR);
    }
}
