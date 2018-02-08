package io.vertx.up.exception;

public class _400PagerInvalidException extends WebException {

    public _400PagerInvalidException(final Class<?> clazz,
                                     final String key) {
        super(clazz, key);
    }

    @Override
    public int getCode() {
        return -60023;
    }
}
