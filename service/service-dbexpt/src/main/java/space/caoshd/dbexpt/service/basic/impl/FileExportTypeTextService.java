package space.caoshd.dbexpt.service.basic.impl;

import space.caoshd.common.service.service.impl.BaseService;
import space.caoshd.dbexpt.bo.FileExportTypeTextBO;
import space.caoshd.dbexpt.mapper.IFileExportTypeTextMapper;
import space.caoshd.dbexpt.po.FileExportTypeTextPO;
import space.caoshd.dbexpt.service.basic.IFileExportTypeTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文件导出文本类型 服务实现类
 *
 * @author caoshd
 * @since 2023-05-01
 */
@Service
public class FileExportTypeTextService extends BaseService<FileExportTypeTextPO, FileExportTypeTextBO>
    implements IFileExportTypeTextService<FileExportTypeTextPO, FileExportTypeTextBO> {

    private final IFileExportTypeTextMapper fileExportTypeTextMapper;

    /**
     * 构造方法
     *
     * @param fileExportTypeTextMapper 文件导出文本类型 Mapper
     */
    public FileExportTypeTextService(
        @Autowired IFileExportTypeTextMapper fileExportTypeTextMapper
    ) {
        super.setBaseMapper(fileExportTypeTextMapper);
        super.setBoClass(FileExportTypeTextBO.class);
        super.setPoClass(FileExportTypeTextPO.class);
        this.fileExportTypeTextMapper = fileExportTypeTextMapper;
    }

}
