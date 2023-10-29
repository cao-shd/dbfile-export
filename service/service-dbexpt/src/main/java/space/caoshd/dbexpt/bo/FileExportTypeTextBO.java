package space.caoshd.dbexpt.bo;

import space.caoshd.common.service.bo.BaseBO;
import space.caoshd.dbexpt.po.enums.FIELD_SEPARATOR;
import space.caoshd.dbexpt.po.enums.LINE_SEPARATOR;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 导出TEXT文本类型BO
 *
 * @author caoshd
 * @since 2023-05-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileExportTypeTextBO extends BaseBO {

    private static final long serialVersionUID = 1L;

    /**
     * 输出文件字符集
     */
    private String charset;

    /**
     * 行分割符
     */
    private String lineSeparator;

    /**
     * 字段分割符
     */
    private char fieldSeparator;

    /**
     * 包围字符串
     */
    private String warpWith;

    /**
     * 是都自动生成头(0-不自动生成/1-自动生成)
     */
    private Integer autoHeader;

    public boolean isAutoHeader() {
        return autoHeader == 1;
    }

    public void setLineSeparator(Integer lineSeparator) {
        this.lineSeparator = LINE_SEPARATOR.byCode(lineSeparator).value();
    }

    public void setFieldSeparator(Integer fieldSeparator) {
        this.fieldSeparator = FIELD_SEPARATOR.byCode(fieldSeparator).value();
    }

    @Override
    public String toString() {
        return "FileExportTypeTextPO{" + "charset='" + charset + '\'' + ", lineSeparator='" + LINE_SEPARATOR.byValue(
            lineSeparator).name() + '\'' + ", fieldSeparator='" + FIELD_SEPARATOR.byValue(fieldSeparator).name() +
               '\'' + ", warpWith='" + warpWith + '\'' + ", autoHeader=" + autoHeader + '}';
    }

}
