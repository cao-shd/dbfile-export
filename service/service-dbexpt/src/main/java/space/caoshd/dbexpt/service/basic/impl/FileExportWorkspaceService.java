package space.caoshd.dbexpt.service.basic.impl;

import space.caoshd.common.service.service.impl.BaseService;
import space.caoshd.dbexpt.bo.FileExportWorkspaceBO;
import space.caoshd.dbexpt.mapper.IFileExportWorkspaceMapper;
import space.caoshd.dbexpt.po.FileExportWorkspacePO;
import space.caoshd.dbexpt.service.basic.IFileExportWorkspaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * 工作空间 服务实现类
 *
 * @author caoshd
 * @since 2023-05-02
 */
@Slf4j
@Service
public class FileExportWorkspaceService extends BaseService<FileExportWorkspacePO, FileExportWorkspaceBO>
    implements IFileExportWorkspaceService<FileExportWorkspacePO, FileExportWorkspaceBO> {

    private final IFileExportWorkspaceMapper fileExportWorkspaceMapper;

    /**
     * 构造方法
     *
     * @param fileExportWorkspaceMapper 文件导出工作空间 Mapper
     */
    public FileExportWorkspaceService(
        @Autowired IFileExportWorkspaceMapper fileExportWorkspaceMapper
    ) {
        super.setBaseMapper(fileExportWorkspaceMapper);
        super.setBoClass(FileExportWorkspaceBO.class);
        super.setPoClass(FileExportWorkspacePO.class);
        this.fileExportWorkspaceMapper = fileExportWorkspaceMapper;
    }

    /**
     * 清理工作空间
     *
     * @param fileExportWorkspaceBOS 工作空间实体集合
     */
    @Override
    public void cleanWorkSpace(List<FileExportWorkspaceBO> fileExportWorkspaceBOS) {
        for (FileExportWorkspaceBO fileExportWorkspaceBO : fileExportWorkspaceBOS) {
            cleanWorkSpace(fileExportWorkspaceBO);
        }
    }

    /**
     * 清理工作空间
     *
     * @param fileExportWorkspaceBO 清理工作空间实体
     */
    @Override
    public void cleanWorkSpace(FileExportWorkspaceBO fileExportWorkspaceBO) {
        // 删除工作空间文件
        cleanWorkSpaceFile(fileExportWorkspaceBO);
        // 删除工作空间表记录
        cleanWorkSpaceTable(fileExportWorkspaceBO);
    }

    /**
     * 清理工作空间文件
     *
     * @param fileExportWorkspaceBO 清理工作空间实体
     */
    private void cleanWorkSpaceFile(FileExportWorkspaceBO fileExportWorkspaceBO) {
        Long instanceId = fileExportWorkspaceBO.getInstanceId();
        File fileDir = new File(fileExportWorkspaceBO.getFilePath());
        File file = new File(fileDir, fileExportWorkspaceBO.getFileName());
        if (file.delete()) {
            log.info("实例: {} 删除工作空间文件成功: {}", instanceId, file);
            if (fileDir.delete()) {
                log.info("实例: {} 删除工作空间文件夹成功: {}", instanceId, fileDir);
            } else {
                log.warn("实例: {} 删除工作空间文件夹成功: {}", instanceId, fileDir);
            }
        } else {
            log.warn("实例: {} 删除工作空间文件失败: {}", instanceId, file);
        }
    }

    /**
     * 清理工作空间表记录
     *
     * @param fileExportWorkspaceBO 清理工作空间实体
     */
    private void cleanWorkSpaceTable(FileExportWorkspaceBO fileExportWorkspaceBO) {
        super.deleteById(fileExportWorkspaceBO.getId());
        Long instanceId = fileExportWorkspaceBO.getInstanceId();
        log.info("实例: {} 删除工作空间配置信息: {}", instanceId, fileExportWorkspaceBO);
    }

}
