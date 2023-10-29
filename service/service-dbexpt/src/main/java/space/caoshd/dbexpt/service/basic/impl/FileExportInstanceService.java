package space.caoshd.dbexpt.service.basic.impl;

import space.caoshd.common.service.service.impl.BaseService;
import space.caoshd.dbexpt.bo.FileExportInstanceBO;
import space.caoshd.dbexpt.mapper.IFileExportInstanceMapper;
import space.caoshd.dbexpt.po.FileExportInstancePO;
import space.caoshd.dbexpt.service.basic.IFileExportInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文件导出实例 服务实现类
 *
 * @author caoshd
 * @since 2023-05-01
 */
@Service
public class FileExportInstanceService extends BaseService<FileExportInstancePO, FileExportInstanceBO>
    implements IFileExportInstanceService<FileExportInstancePO, FileExportInstanceBO> {

    private final IFileExportInstanceMapper fileExportInstanceMapper;

    /**
     * 构造方法
     *
     * @param fileExportInstanceMapper 文件导出实例 Mapper
     */
    public FileExportInstanceService(
        @Autowired IFileExportInstanceMapper fileExportInstanceMapper
    ) {
        super.setBaseMapper(fileExportInstanceMapper);
        super.setBoClass(FileExportInstanceBO.class);
        super.setPoClass(FileExportInstancePO.class);
        this.fileExportInstanceMapper = fileExportInstanceMapper;
    }

    /**
     * 更新关联任务状态
     *
     * @param query 查询字段
     * @return 更新影响记录数
     */
    @Override
    public int updateAssociatedTaskStatus(FileExportInstancePO query) {
        return fileExportInstanceMapper.updateAssociatedTaskStatus(
            query.getVersion(),
            query.getProcessNode(),
            query.getParentId()
        );
    }

    /**
     * 更新完成文件数量
     *
     * @param query 查询字段
     * @return 更新影响记录数
     */
    @Override
    public int updateAssociatedDoneFileCnt(FileExportInstancePO query) {
        return fileExportInstanceMapper.updateAssociatedDoneFileCnt(query.getId(), query.getParentId());
    }

}
