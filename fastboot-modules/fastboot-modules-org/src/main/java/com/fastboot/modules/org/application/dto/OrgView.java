package com.fastboot.modules.org.application.dto;

/**
 * 组织树节点展示对象
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public record OrgView(
        /** 组织 ID */ Long id,
        /** 上级组织 ID */ Long parentId,
        /** 组织名称 */ String name,
        /** 物化路径 */ String path) {}
