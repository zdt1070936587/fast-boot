package com.fastboot.common.api;

import java.util.List;

/**
 * 分页结果封装。
 *
 * <p>组件说明：{@code items} 当前页数据列表；{@code total} 总记录数；{@code page} 当前页码；{@code size} 每页条数。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public record PageData<T>(List<T> items, long total, long page, long size) {

    /**
     * 构造分页结果。
     *
     * @param items 当前页数据
     * @param total 总记录数
     * @param page 页码
     * @param size 每页大小
     * @param <T> 元素类型
     * @return 分页对象
     */
    public static <T> PageData<T> of(List<T> items, long total, long page, long size) {
        return new PageData<>(items, total, page, size);
    }
}
