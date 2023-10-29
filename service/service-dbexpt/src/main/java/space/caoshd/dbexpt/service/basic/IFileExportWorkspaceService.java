package space.caoshd.dbexpt.service.basic;

import space.caoshd.common.service.service.IBaseService;

import java.util.List;

/**
 * 工作空间 服务类
 *
 * @author caoshd
 * @since 2023-05-02
 */
public interface IFileExportWorkspaceService<FileExportWorkspacePO, FileExportWorkspaceBO>
    extends IBaseService<FileExportWorkspacePO, FileExportWorkspaceBO> {

    /**
     * 清理工作空间
     *
     * @param fileExportWorkspaceBOS 工作空间实体集合
     */
    void cleanWorkSpace(List<FileExportWorkspaceBO> fileExportWorkspaceBOS);

    /**
     * 清理工作空间
     *
     * @param fileExportWorkspaceBO 清理工作空间实体
     */
    void cleanWorkSpace(FileExportWorkspaceBO fileExportWorkspaceBO);

}
