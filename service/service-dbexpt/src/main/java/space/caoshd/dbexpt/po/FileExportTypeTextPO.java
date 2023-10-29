package space.caoshd.dbexpt.po;

import com.baomidou.mybatisplus.annotation.TableName;
import space.caoshd.common.service.po.BasePO;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 导出TEXT文本类型PO
 *
 * @author caoshd
 * @since 2023-05-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_file_export_type_text")
public class FileExportTypeTextPO extends BasePO {

    private static final long serialVersionUID = 1L;

    /**
     * 输出文件字符集
     */
    private String charset;

    /**
     * 行分割符
     */
    private Integer lineSeparator;

    /**
     * 字段分割符
     */
    private Integer fieldSeparator;

    /**
     * 包围字符串
     */
    private String warpWith;

    /**
     * 是都自动生成头(0-不自动生成/1-自动生成)
     */
    private Integer autoHeader;

}
