package space.caoshd.dbexpt.po;

import com.baomidou.mybatisplus.annotation.TableName;
import space.caoshd.common.service.po.BasePO;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 导出工作空间配置PO
 *
 * @author caoshd
 * @since 2023-05-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_file_export_workspace")
public class FileExportWorkspacePO extends BasePO {

    private static final long serialVersionUID = 1L;

    /**
     * 实例ID
     */
    private Long instanceId;

    /**
     * 父实例ID
     */
    private Long instanceParentId;

    /**
     * 工作路径
     */
    private String filePath;

    /**
     * 文件名称
     */
    private String fileName;
}
