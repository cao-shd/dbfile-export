package space.caoshd.dbexpt.service.advance.impl;

import space.caoshd.dbexpt.bo.FileExportInstanceBO;
import space.caoshd.dbexpt.bo.FileExportWorkspaceBO;
import space.caoshd.dbexpt.po.FileExportWorkspacePO;
import space.caoshd.dbexpt.service.advance.IFileExportExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
public class FileExportExecutorTypeZip extends AbstractFileExportExecutor implements IFileExportExecutor {

    @Override
    public void execute(FileExportInstanceBO instanceBO) {
        // 准备处理
        prepare(instanceBO);

        // 生成文件
        generate(instanceBO);

        // 创建工作空间信息
        insertWorkSpace(instanceBO);

        // 移动文件
        move(instanceBO);

        // 清理工作空间
        cleanWorkSpace(instanceBO.getFileExportWorkspaceBO());
        cleanWorkSpace(instanceBO.getSubFileExportWorkspaceBOS());

        // 回调处理
        callback(instanceBO);
    }

    @Override
    public void generate(FileExportInstanceBO instanceBO) {
        log.info("实例: {} 正在执行压缩.", instanceBO.getId());
        super.generate(instanceBO);
        try (
            FileOutputStream fos = new FileOutputStream(instanceBO.getWorkTempFullPath());
            ZipOutputStream zos = new ZipOutputStream(fos)
        ) {
            for (FileExportWorkspaceBO fileExportWorkspaceBO : instanceBO.getSubFileExportWorkspaceBOS()) {
                String filePath = fileExportWorkspaceBO.getFilePath();
                String fileName = fileExportWorkspaceBO.getFileName();
                try (
                    FileInputStream fis = new FileInputStream(new File(filePath, fileName))
                ) {
                    zos.putNextEntry(new ZipEntry(fileName));
                    StreamUtils.copy(fis, zos);
                    zos.closeEntry();
                }
            }
            log.info("实例: {} 执行压缩成功.", instanceBO.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void prepare(FileExportInstanceBO instanceBO) {
        super.prepare(instanceBO);
        FileExportWorkspacePO query = new FileExportWorkspacePO();
        query.setInstanceParentId(instanceBO.getId());
        List<FileExportWorkspaceBO> fileExportWorkspaceBOS = fileExportWorkspaceService.list(query);
        for (FileExportWorkspaceBO fileExportWorkspaceBO : fileExportWorkspaceBOS) {
            log.info("实例: {} 添加工作空间信息: {}", instanceBO.getId(), fileExportWorkspaceBO);
        }
        instanceBO.setSubFileExportWorkspaceBOS(fileExportWorkspaceBOS);
    }

    public void callback(FileExportInstanceBO instanceBO) {
        if (!StringUtils.hasText(instanceBO.getCallbackLogic())) {
            log.info("实例: {} 正在执行回调.", instanceBO.getId());
            log.info("实例: {} 执行回调成功.", instanceBO.getId());
        }
    }

}
