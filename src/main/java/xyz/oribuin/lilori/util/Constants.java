package xyz.oribuin.lilori.util;

// make each entry return their result

public enum Constants {
    DEFAULT_COLOR("#a6b2fc"),
    ERROR_COLOR("#f75454"),

    SUPPORT_SERVER("731659405958971413"),
    OWNER_ID("345406020450779149"),
    SUPPORT_ROLE("733059033405063298"),
    MEMBER_ROLE(731661458680578069L),
    TICKETS_CATEGORY(733086484470694018L),

    ;

    private final Object value;

    Constants(final Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public String getString() {
        return (String) value;
    }

    public Long getLong() {
        return (Long) value;
    }

    public Integer getInteger() {
        return (Integer) value;
    }

    public Boolean getBoolean() {
        return (Boolean) value;
    }

    public Double getDouble() {
        return (Double) value;
    }

    public Float getFloat() {
        return (Float) value;
    }

    public Short getShort() {
        return (Short) value;
    }

    public Byte getByte() {
        return (Byte) value;
    }

    public Character getCharacter() {
        return (Character) value;
    }

    public Object[] getArray() {
        return (Object[]) value;
    }

    public int[] getIntArray() {
        return (int[]) value;
    }

    public long[] getLongArray() {
        return (long[]) value;
    }

    public double[] getDoubleArray() {
        return (double[]) value;
    }

    public float[] getFloatArray() {
        return (float[]) value;
    }

    public short[] getShortArray() {
        return (short[]) value;
    }

    public byte[] getByteArray() {
        return (byte[]) value;
    }

    public boolean[] getBooleanArray() {
        return (boolean[]) value;
    }

}
