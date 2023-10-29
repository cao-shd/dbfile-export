package space.caoshd.dbexpt.po.enums;

public enum TASK_STATUS {

    UNASSIGNED(0, "未分配"),
    ASSIGNED(1, "已分配"),
    RUNNING(2, "运行中"),
    DONE(3, "已完成"),
    ERROR(-1, "错误"),
    CANCEL(-2,"取消执行");
    private final int code;

    private final String value;

    private TASK_STATUS(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int code() {
        return code;
    }

    public String value() {
        return value;
    }
    

}
