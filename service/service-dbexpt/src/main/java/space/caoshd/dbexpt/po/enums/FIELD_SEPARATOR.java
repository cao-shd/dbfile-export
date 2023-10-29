package space.caoshd.dbexpt.po.enums;

public enum FIELD_SEPARATOR {

    COMMA(1, (char) 44), TAB(2, (char) 9), ESC(3, (char) 27), PIPE(4, (char) 124);
    private final int code;
    private final char value;

    private FIELD_SEPARATOR(int code, char value) {
        this.code = code;
        this.value = value;
    }

    public int code() {
        return code;
    }

    public char value() {
        return value;
    }

    public static FIELD_SEPARATOR byCode(int code) {
        for (FIELD_SEPARATOR fieldSeparator : FIELD_SEPARATOR.values()) {
            if (fieldSeparator.code == code) {
                return fieldSeparator;
            }
        }
        throw new RuntimeException("字段分割符错误.");
    }

    public static FIELD_SEPARATOR byValue(char value) {
        for (FIELD_SEPARATOR fieldSeparator : FIELD_SEPARATOR.values()) {
            if (fieldSeparator.value == value) {
                return fieldSeparator;
            }
        }
        throw new RuntimeException("字段分割符错误.");
    }

}
