package com.fastboot.modules.system.api;

import java.util.List;
import java.util.Optional;

/**
 * 字典查询端口。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public interface DictQueryPort {

    List<DictItem> data(String typeCode);

    record DictItem(String label, String value, int sortNo) {}
}
