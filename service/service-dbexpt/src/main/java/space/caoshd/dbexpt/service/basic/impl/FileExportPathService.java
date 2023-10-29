package space.caoshd.dbexpt.service.basic.impl;

import space.caoshd.common.service.service.impl.BaseService;
import space.caoshd.dbexpt.bo.FileExportPathBO;
import space.caoshd.dbexpt.mapper.IFileExportPathMapper;
import space.caoshd.dbexpt.po.FileExportPathPO;
import space.caoshd.dbexpt.service.basic.IFileExportPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文件导出路径 服务实现类
 *
 * @author caoshd
 * @since 2023-04-29
 */
@Service
public class FileExportPathService extends BaseService<FileExportPathPO, FileExportPathBO>
    implements IFileExportPathService<FileExportPathPO, FileExportPathBO> {

    private final IFileExportPathMapper fileExportPathMapper;

    /**
     * 构造方法
     * @param fileExportPathMapper 文件导出路径映射 Mapper
     */
    public FileExportPathService(
        @Autowired IFileExportPathMapper fileExportPathMapper
    ) {
        super.setBaseMapper(fileExportPathMapper);
        super.setBoClass(FileExportPathBO.class);
        super.setPoClass(FileExportPathPO.class);
        this.fileExportPathMapper = fileExportPathMapper;
    }

}
