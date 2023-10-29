package space.caoshd.dbexpt.bo;

import space.caoshd.common.service.bo.BaseBO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件导出实例BO
 *
 * @author caoshd
 * @since 2023-05-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileExportInstanceBO extends BaseBO {

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
     * 输出文件名(不带扩展名)
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

    /**
     * 临时路径
     */
    private String tempDir;

    /**
     * 工作路径
     */
    private String workDir;

    /**
     * 备份路径
     */
    private String bakDir;

    /**
     * 目标文件路径
     */
    private String destDir;

    /**
     * 工作路径
     */
    private String workTempDirAbsolute;

    /**
     * 导出配置 Text 类型
     */
    private FileExportTypeTextBO fileExportTypeTextBO;

    /**
     * 工作空间信息
     */
    private FileExportWorkspaceBO fileExportWorkspaceBO;

    /**
     * 子文件工作空间信息
     */
    private List<FileExportWorkspaceBO> subFileExportWorkspaceBOS = new ArrayList<>();

    /**
     * 文件全名 (文件名 + 扩展名)
     */
    public String getFileFullName() {
        return fileName + filePrefix;
    }

    /**
     * 获取临时文件夹
     *
     * @return 临时文件夹
     */
    public String getWorkTempDir() {
        return workDir + tempDir;
    }

    /**
     * 获取临时文件全路径
     *
     * @return 临时文件全路径
     */
    public String getWorkTempFullPath() {
        return workTempDirAbsolute + getFileFullName();
    }

    /**
     * 获取目标文件全路径
     *
     * @return 目标文件全路径
     */
    public String getDestFullPath() {
        return destDir + getFileFullName();
    }



}
