package com.fastboot.modules.infra.application;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.modules.infra.api.JobPort;
import com.fastboot.modules.infra.application.job.JobHandler;
import com.fastboot.modules.infra.infrastructure.mapper.SysJobLogMapper;
import com.fastboot.modules.infra.infrastructure.mapper.SysJobMapper;
import com.fastboot.modules.infra.infrastructure.persistence.entity.SysJobEntity;
import com.fastboot.modules.infra.infrastructure.persistence.entity.SysJobLogEntity;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 定时任务应用服务。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Service
public class JobAppService implements JobPort {

    private final SysJobMapper jobMapper;
    private final SysJobLogMapper jobLogMapper;
    private final Map<String, JobHandler> handlers;

    public JobAppService(SysJobMapper jobMapper, SysJobLogMapper jobLogMapper, List<JobHandler> handlerList) {
        this.jobMapper = jobMapper;
        this.jobLogMapper = jobLogMapper;
        this.handlers =
                handlerList.stream().collect(Collectors.toMap(h -> h.name().toLowerCase(), Function.identity()));
    }

    @Override
    @Transactional
    public void register(Long jobId) {
        SysJobEntity job = requireJob(jobId);
        if (!Boolean.TRUE.equals(job.getEnabled())) {
            return;
        }
        // db-scheduler 注册由 DbSchedulerConfiguration 在启动时同步
    }

    @Override
    @Transactional
    public void trigger(Long jobId) {
        executeJob(requireJob(jobId));
    }

    @Override
    @Transactional
    public void pause(Long jobId) {
        SysJobEntity job = requireJob(jobId);
        job.setEnabled(false);
        jobMapper.updateById(job);
    }

    @Override
    @Transactional
    public void resume(Long jobId) {
        SysJobEntity job = requireJob(jobId);
        job.setEnabled(true);
        jobMapper.updateById(job);
    }

    @Transactional(readOnly = true)
    public List<SysJobEntity> list() {
        return jobMapper.selectList(null);
    }

    @Transactional
    public Long create(SysJobEntity entity) {
        jobMapper.insert(entity);
        return entity.getId();
    }

    @Transactional
    public void update(SysJobEntity entity) {
        if (jobMapper.updateById(entity) == 0) {
            throw new BizException(ErrorCode.CONFLICT, "数据已被修改");
        }
    }

    public void executeJob(SysJobEntity job) {
        long start = System.currentTimeMillis();
        boolean success = true;
        String message = "OK";
        try {
            JobHandler handler = handlers.get(job.getHandler().toLowerCase());
            if (handler == null) {
                throw new BizException(ErrorCode.NOT_FOUND, "任务处理器不存在: " + job.getHandler());
            }
            handler.execute(job.getParams());
        } catch (Exception ex) {
            success = false;
            message = ex.getMessage();
            throw ex;
        } finally {
            SysJobLogEntity log = new SysJobLogEntity();
            log.setJobId(job.getId());
            log.setSuccess(success);
            log.setMessage(message);
            log.setDurationMs(System.currentTimeMillis() - start);
            jobLogMapper.insert(log);
        }
    }

    private SysJobEntity requireJob(Long jobId) {
        SysJobEntity job = jobMapper.selectById(jobId);
        if (job == null) {
            throw new BizException(ErrorCode.NOT_FOUND);
        }
        return job;
    }
}
