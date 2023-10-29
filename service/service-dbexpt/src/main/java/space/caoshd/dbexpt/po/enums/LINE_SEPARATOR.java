package space.caoshd.dbexpt.po.enums;

public enum LINE_SEPARATOR {

    LF(1, "\n"), CRLF(2, "\r\n"), CR(3, "\r");
    private final int code;
    private final String value;

    private LINE_SEPARATOR(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int code() {
        return code;
    }

    public String value() {
        return value;
    }

    public static LINE_SEPARATOR byCode(int code) {
        for (LINE_SEPARATOR lineSeparator : LINE_SEPARATOR.values()) {
            if (lineSeparator.code == code) {
                return lineSeparator;
            }
        }
        throw new RuntimeException("行分割符错误.");
    }

    public static LINE_SEPARATOR byValue(String value) {
        for (LINE_SEPARATOR lineSeparator : LINE_SEPARATOR.values()) {
            if (lineSeparator.value.equals(value)) {
                return lineSeparator;
            }
        }
        throw new RuntimeException("行分割符错误.");
    }

}
