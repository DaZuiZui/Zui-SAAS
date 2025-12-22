package org.example.demo.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 产品表（通用）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product")
public class Product extends BaseEntity<Product> {

    /**
     * 产品包代码
     */
    private String packcode;

    /**
     * 应用ID
     */
    private String appid;

    /**
     * 产品层级（数值越大层级越高，用于判断升降级）
     */
    private Integer tier;

    /**
     * 等级（同一tier内的等级，1-5等）
     */
    private Integer level;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 产品描述
     */
    private String description;
}

