package space.caoshd.dbexpt.service.advance;

import space.caoshd.dbexpt.bo.FileExportInstanceBO;

/**
 * 文件导出执行 服务类
 *
 * @author caoshd
 * @since 2023-04-29
 */
public interface IFileExportExecuteService {

    void export(FileExportInstanceBO instance);

}
