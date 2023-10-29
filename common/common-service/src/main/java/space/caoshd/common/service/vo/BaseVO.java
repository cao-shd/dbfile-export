package space.caoshd.common.service.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseVO implements Serializable {
    private Long id;
    private Integer version;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private String updateBy;
}