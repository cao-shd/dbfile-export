package space.caoshd.dbexpt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import space.caoshd.dbexpt.po.FileExportInstancePO;
import org.apache.ibatis.annotations.Param;

/**
 * 文件导出实例 Mapper 接口
 *
 * @author caoshd
 * @since 2023-05-01
 */
public interface IFileExportInstanceMapper extends BaseMapper<FileExportInstancePO> {
    int updateAssociatedTaskStatus(
        @Param("version") Integer version,
        @Param("processNode") String processNode,
        @Param("parentId") Long parentId
    );

    int updateAssociatedDoneFileCnt(
        @Param("id") Long id,
        @Param("parentId") Long parentId
    );

}
