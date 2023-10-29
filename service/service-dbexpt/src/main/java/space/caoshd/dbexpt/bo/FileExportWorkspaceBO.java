package space.caoshd.dbexpt.bo;

import space.caoshd.common.service.bo.BaseBO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 导出工作空间配置BO
 *
 * @author caoshd
 * @since 2023-05-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileExportWorkspaceBO extends BaseBO {

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
