package space.caoshd.dbexpt.po;

import com.baomidou.mybatisplus.annotation.TableName;
import space.caoshd.common.service.po.BasePO;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 文件导出实例PO
 *
 * @author caoshd
 * @since 2023-05-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_file_export_instance")
public class FileExportInstancePO extends BasePO {

    private static final long serialVersionUID = 1L;

    /**
     * 父实例ID
     */
    private Long parentId;

    /**
     * 实例状态
     */
    private Integer taskStatus;

    /**
     * 处理节点
     */
    private String processNode;

    /**
     * 文件总数
     */
    private Integer fileCnt;

    /**
     * 完成文件总数
     */
    private Integer doneFileCnt;

    /**
     * 文件类型(1-文本文件)
     */
    private Integer fileType;

    /**
     * 文件类型ID
     */
    private Long fileTypeId;

    /**
     * 输出路径
     */
    private String filePath;

    /**
     * 输出文件名
     */
    private String fileName;

    /**
     * 输出文件后缀
     */
    private String filePrefix;

    /**
     * 数据库别名
     */
    private String dbAlias;

    /**
     * 前置生成逻辑
     */
    private String preLogic;

    /**
     * 主体生成逻辑
     */
    private String mainLogic;

    /**
     * 后置生成逻辑
     */
    private String postLogic;

    /**
     * 回调处理逻辑
     */
    private String callbackLogic;
}
