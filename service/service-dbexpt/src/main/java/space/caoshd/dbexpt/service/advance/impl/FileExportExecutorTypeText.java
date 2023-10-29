package space.caoshd.dbexpt.service.advance.impl;

import space.caoshd.common.dynamic_ds.annotation.DataSourceSwitch;
import space.caoshd.common.dynamic_ds.datasource.DataSourceHolder;
import space.caoshd.dbexpt.bo.FileExportInstanceBO;
import space.caoshd.dbexpt.bo.FileExportTypeTextBO;
import space.caoshd.dbexpt.handler.CSVRowCallbackHandler;
import space.caoshd.dbexpt.po.FileExportInstancePO;
import space.caoshd.dbexpt.po.FileExportTypeTextPO;
import space.caoshd.dbexpt.service.advance.IFileExportExecutor;
import space.caoshd.dbexpt.service.basic.IFileExportInstanceService;
import space.caoshd.dbexpt.service.basic.IFileExportTypeTextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.FileWriter;
import java.nio.charset.Charset;

@Slf4j
@Service
public class FileExportExecutorTypeText extends AbstractFileExportExecutor implements IFileExportExecutor {

    @Autowired
    private IFileExportTypeTextService<FileExportTypeTextPO, FileExportTypeTextBO> fileExportTypeTextService;

    public void setFileExportTypeTextService(
        IFileExportTypeTextService<FileExportTypeTextPO, FileExportTypeTextBO> fileExportTypeTextService
    ) {
        this.fileExportTypeTextService = fileExportTypeTextService;
    }

    @Autowired
    private IFileExportInstanceService<FileExportInstancePO, FileExportInstanceBO> fileExportInstanceService;

    public void setFileExportInstanceService(
        IFileExportInstanceService<FileExportInstancePO, FileExportInstanceBO> fileExportInstanceService
    ) {
        this.fileExportInstanceService = fileExportInstanceService;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

        // 删除工作空间文件
        cleanWorkSpace(instanceBO);

        // 更新完成文件数量
        updateDoneFileCnt(instanceBO);

        // 回调处理
        callback(instanceBO);
    }

    public void prepare(FileExportInstanceBO instanceBO) {
        super.prepare(instanceBO);
        FileExportTypeTextBO fileExportTypeTextBO = fileExportTypeTextService.findById(instanceBO.getFileTypeId());
        instanceBO.setFileExportTypeTextBO(fileExportTypeTextBO);
        log.info("实例: {} 配置文件生成参数: {}", instanceBO.getId(), fileExportTypeTextBO);
    }

    @Override
    @DataSourceSwitch
    public void generate(FileExportInstanceBO instanceBO) {
        log.info("实例: {} 正在生成文件.", instanceBO.getId());
        super.generate(instanceBO);

        try (
            FileWriter writer = new FileWriter(
                instanceBO.getWorkTempFullPath(),
               Charset.forName(instanceBO.getFileExportTypeTextBO().getCharset())
            )
        ) {
            DataSourceHolder.setDataSource(instanceBO.getDbAlias());

            if (StringUtils.hasText(instanceBO.getPreLogic())) {
                writer.write(instanceBO.getPreLogic());
                writer.write(instanceBO.getFileExportTypeTextBO().getLineSeparator());
            }

            if (StringUtils.hasText(instanceBO.getMainLogic())) {
                FileExportTypeTextBO fileExportTypeCsvBO = instanceBO.getFileExportTypeTextBO();
                jdbcTemplate.query(instanceBO.getMainLogic(), new CSVRowCallbackHandler(writer, fileExportTypeCsvBO));
            }

            writer.flush();
            log.info("实例: {} 生成文件成功.", instanceBO.getId());
        } catch (Exception e) {
            throw new RuntimeException("实例: {} 生成文件发生预想外异常.", e);
        } finally {
            DataSourceHolder.clearDataSource();
        }
    }


    @Override
    protected void move(FileExportInstanceBO instanceBO) {
        if (checkMove(instanceBO)) {
            super.move(instanceBO);
        }
    }

    private boolean checkMove(FileExportInstanceBO instanceBO) {
        return StringUtils.hasText(instanceBO.getFilePath());
    }

    private void cleanWorkSpace(FileExportInstanceBO instanceBO) {
        if (checkClean(instanceBO)) {
            // 删除工作空间文件
            cleanWorkSpace(instanceBO.getFileExportWorkspaceBO());
        } else {
            log.info("实例: {} 等待处理文件. {}", instanceBO.getId(), instanceBO.getWorkTempFullPath());
        }
    }

    private boolean checkClean(FileExportInstanceBO instanceBO) {
        return instanceBO.getParentId() == null;
    }

    private void updateDoneFileCnt(FileExportInstanceBO instanceBO) {
        try {
            log.info("实例: {} 正在更新完成文件数.", instanceBO.getId());

            FileExportInstancePO query = new FileExportInstancePO();
            query.setId(instanceBO.getId());
            query.setParentId(instanceBO.getParentId());
            int updateCnt = fileExportInstanceService.updateAssociatedDoneFileCnt(query);

            if (updateCnt > 0) {
                instanceBO.setDoneFileCnt(1);
                log.info("实例: {} 更新完成文件数量 {} 个.", instanceBO.getId(), updateCnt);
            } else {
                log.error("实例: {} 更新完成文件数量失败.", instanceBO.getId());
            }

            log.info("实例: {} 更新完成文件数量成功.", instanceBO.getId());
        } catch (Exception e) {
            throw new RuntimeException("实例: {} 移动文件发生预想外异常", e);
        }
    }

    public void callback(FileExportInstanceBO instanceBO) {
        if (!StringUtils.hasText(instanceBO.getCallbackLogic())) {
            log.info("实例: {} 正在执行回调.", instanceBO.getId());
            log.info("实例: {} 执行回调成功.", instanceBO.getId());
        }
    }

}
