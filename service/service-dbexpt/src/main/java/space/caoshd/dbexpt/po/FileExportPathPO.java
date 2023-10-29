package space.caoshd.dbexpt.po;

import com.baomidou.mybatisplus.annotation.TableName;
import space.caoshd.common.service.po.BasePO;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 导出路径配置PO
 *
 * @author caoshd
 * @since 2023-04-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_file_export_path")
public class FileExportPathPO extends BasePO {

    private static final long serialVersionUID = 1L;

    /**
     * 路径名称
     */
    private String pathName;

    /**
     * 路径地址
     */
    private String location;
}
