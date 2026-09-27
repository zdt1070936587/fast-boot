package com.fastboot.modules.bpm.api;

/** BPM 流程查询端口（占位，供未来微服务或单体扩展）。 */
public interface ProcessQueryPort {

    boolean exists(String processKey);
}
