package org.example.demo.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 实体类基类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseEntity<T extends Model<?>> extends Model<T> {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间（时间戳）
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createAt;

    /**
     * 数据状态：0-正常 1-删除
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer status;

    /**
     * 修改时间（时间戳）
     */
    @TableField(fill = FieldFill.UPDATE)
    private Long updateAt;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    /**
     * 乐观锁版本号
     */
    @Version
    @TableField(fill = FieldFill.UPDATE)
    private Integer version;
}
