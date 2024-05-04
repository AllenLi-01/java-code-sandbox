package com.xinn.bojcodesandbox.model;

import lombok.Data;

/**
 * 判题信息
 *
 * @author allen
 * @date 2024/03/23
 */
@Data
public class JudgeInfo {
    /**
     * 程序执行信息
     */
    private String message;
    /**
     * 时间 ms
     */
    private Long time;
    /**
     * 内存 kb
     */
    private Long memory;

}
