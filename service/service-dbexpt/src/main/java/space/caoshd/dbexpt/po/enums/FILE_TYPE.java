package space.caoshd.dbexpt.po.enums;

public enum FILE_TYPE {

    TEXT(1, "fileExportExecutorTypeText"), ZIP(2, "fileExportExecutorTypeZip"), EXCEL(3, "fileExportExecutorTypeExcel");
    private final int code;
    private final String value;

    private FILE_TYPE(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int code() {
        return code;
    }

    public String value() {
        return value;
    }

    public static FILE_TYPE byCode(int code) {
        for (FILE_TYPE lineSeparator : FILE_TYPE.values()) {
            if (lineSeparator.code == code) {
                return lineSeparator;
            }
        }
        throw new RuntimeException("文件类型错误.");
    }

    public static FILE_TYPE byValue(String value) {
        for (FILE_TYPE lineSeparator : FILE_TYPE.values()) {
            if (lineSeparator.value.equals(value)) {
                return lineSeparator;
            }
        }
        throw new RuntimeException("文件类型错误.");
    }
}
