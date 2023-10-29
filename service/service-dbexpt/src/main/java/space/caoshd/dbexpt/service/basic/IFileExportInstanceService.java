package space.caoshd.dbexpt.service.basic;

import space.caoshd.common.service.service.IBaseService;

/**
 * 文件导出实例 服务类
 *
 * @author caoshd
 * @since 2023-05-01
 */
public interface IFileExportInstanceService<FileExportInstancePO, FileExportInstanceBO>
    extends IBaseService<FileExportInstancePO, FileExportInstanceBO> {

    /**
     * 更新关联任务状态
     *
     * @param query 查询字段
     * @return 更新影响记录数
     */
    int updateAssociatedTaskStatus(FileExportInstancePO query);

    /**
     * 更新完成文件数据
     *
     * @param query 查询字段
     * @return 更新影响记录数
     */
    int updateAssociatedDoneFileCnt(FileExportInstancePO query);
}
