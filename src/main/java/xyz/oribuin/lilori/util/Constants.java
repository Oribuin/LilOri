package xyz.oribuin.lilori.util;

// make each entry return their result

public enum Constants {
    DEFAULT_COLOR("#a6b2fc"),
    ERROR_COLOR("#f75454"),

    SUPPORT_SERVER("731659405958971413");

    private final String value;

    Constants(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}
