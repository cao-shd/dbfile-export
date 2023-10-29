package space.caoshd.dbexpt.service.advance.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import space.caoshd.dbexpt.bo.FileExportInstanceBO;
import space.caoshd.dbexpt.bo.FileExportWorkspaceBO;
import space.caoshd.dbexpt.mapper.IFileExportPathMapper;
import space.caoshd.dbexpt.po.FileExportPathPO;
import space.caoshd.dbexpt.po.FileExportWorkspacePO;
import space.caoshd.dbexpt.service.basic.IFileExportWorkspaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public abstract class AbstractFileExportExecutor {

    @Value("${file-export.path.base:./}")
    private String baseDir;
    @Value("${file-export.path.runtime:work/}")
    private String workDir;

    public void setWorkDir(String workDir) {
        this.workDir = workDir;
    }

    @Value("${file-export.path.backup:bak/}")
    private String bakDir;

    public void setBakDir(String bakDir) {
        this.bakDir = bakDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    @Autowired
    protected IFileExportPathMapper fileExportPathMapper;

    public void setFileExportPathMapper(IFileExportPathMapper fileExportPathMapper) {
        this.fileExportPathMapper = fileExportPathMapper;
    }

    @Autowired
    protected IFileExportWorkspaceService<FileExportWorkspacePO, FileExportWorkspaceBO> fileExportWorkspaceService;

    public void setFileExportWorkspaceService(
        IFileExportWorkspaceService<FileExportWorkspacePO, FileExportWorkspaceBO> fileExportWorkspaceService
    ) {
        this.fileExportWorkspaceService = fileExportWorkspaceService;
    }

    protected void prepare(FileExportInstanceBO instanceBO) {
        instanceBO.setTempDir(UUID.randomUUID() + File.separator);
        instanceBO.setWorkDir(baseDir + workDir);
        instanceBO.setBakDir(baseDir + bakDir);
    }

    protected String createDir(Long instanceId, String tempOutputPath) {
        try {
            File file = new File(tempOutputPath);
            String canonicalPath = file.getCanonicalPath() + File.separator;
            if (!file.isDirectory()) {
                boolean mkdir = file.mkdirs();
                if (mkdir) {
                    log.info("实例: {} 创建文件夹: {}", instanceId, canonicalPath);
                }
            }
            return canonicalPath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void generate(FileExportInstanceBO instanceBO) {
        String workTempDirAbsolute = createDir(instanceBO.getId(), instanceBO.getWorkTempDir());
        instanceBO.setWorkTempDirAbsolute(workTempDirAbsolute);
    }

    protected void move(FileExportInstanceBO instanceBO) {
        try {
            // 创建最终生成文件的文件夹
            String filePath = convertFilePath(instanceBO.getFilePath());
            String destDir = createDir(instanceBO.getId(), filePath);
            instanceBO.setDestDir(destDir);

            File temp = new File(instanceBO.getWorkTempFullPath());
            File dest = new File(instanceBO.getDestFullPath());
            log.info("实例: {} 正在移动文件: FROM: [{}], TO: [{}]", instanceBO.getId(), temp, dest);
            FileCopyUtils.copy(temp, dest);
            log.info("实例: {} 移动文件成功.", instanceBO.getId());
        } catch (Exception e) {
            throw new RuntimeException("实例: {} 移动文件发生预想外异常", e);
        }
    }

    protected String convertFilePath(String outputPath) {
        // 直接路径
        if (outputPath.startsWith(File.separator)) {
            return outputPath;
        }
        // 映射路径
        QueryWrapper<FileExportPathPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("path_name", outputPath);
        FileExportPathPO fileExportPathPO = fileExportPathMapper.selectOne(queryWrapper);
        return fileExportPathPO.getLocation();
    }

    protected void insertWorkSpace(FileExportInstanceBO instanceBO) {
        FileExportWorkspaceBO fileExportWorkspaceBO = new FileExportWorkspaceBO();
        fileExportWorkspaceBO.setInstanceId(instanceBO.getId());
        fileExportWorkspaceBO.setInstanceParentId(instanceBO.getParentId());
        fileExportWorkspaceBO.setFilePath(instanceBO.getWorkTempDirAbsolute());
        fileExportWorkspaceBO.setFileName(instanceBO.getFileFullName());
        fileExportWorkspaceService.insert(fileExportWorkspaceBO);

        instanceBO.setFileExportWorkspaceBO(fileExportWorkspaceBO);
        log.info("实例: {} 插入工作空间信息: {}", instanceBO.getId(), fileExportWorkspaceBO);
    }

    protected void cleanWorkSpace(List<FileExportWorkspaceBO> fileExportWorkspaceBOS) {
        fileExportWorkspaceService.cleanWorkSpace(fileExportWorkspaceBOS);
    }

    protected void cleanWorkSpace(FileExportWorkspaceBO fileExportWorkspaceBO) {
        fileExportWorkspaceService.cleanWorkSpace(fileExportWorkspaceBO);
    }

}
