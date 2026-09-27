import os

BASE = r"E:\code\fast-boot\fastboot-modules\fastboot-modules-infra\src\main\java\com\fastboot\modules\infra\interfaces"
os.makedirs(BASE, exist_ok=True)

HEADER = """package com.fastboot.modules.infra.interfaces;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.fastboot.common.api.R;
"""

controllers = {
    "FileController.java": HEADER
    + """import com.fastboot.modules.infra.api.FileRef;
import com.fastboot.modules.infra.application.FileAppService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.time.Duration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/infra/files")
@Tag(name = "infra-file")
public class FileController {

    private final FileAppService fileAppService;

    public FileController(FileAppService fileAppService) {
        this.fileAppService = fileAppService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SaCheckPermission("sys:file:write")
    public R<FileRef> upload(@RequestParam("file") MultipartFile file) throws Exception {
        try (InputStream in = file.getInputStream()) {
            return R.ok(
                    fileAppService.upload(
                            file.getOriginalFilename(),
                            file.getContentType(),
                            in,
                            file.getSize()));
        }
    }

    @GetMapping("/{path}/download")
    @SaCheckPermission("sys:file:read")
    public void download(@PathVariable String path, HttpServletResponse response) throws Exception {
        try (InputStream in = fileAppService.download(path)) {
            in.transferTo(response.getOutputStream());
        }
    }

    @DeleteMapping("/{path}")
    @SaCheckPermission("sys:file:write")
    public R<Void> delete(@PathVariable String path) {
        fileAppService.delete(path);
        return R.ok();
    }

    @GetMapping("/{path}/presign")
    @SaCheckPermission("sys:file:read")
    public R<String> presign(@PathVariable String path, @RequestParam(defaultValue = "3600") long seconds) {
        return R.ok(fileAppService.presign(path, Duration.ofSeconds(seconds)));
    }
}
""",
    "JobController.java": HEADER
    + """import com.fastboot.modules.infra.application.JobAppService;
import com.fastboot.modules.infra.infrastructure.persistence.entity.SysJobEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/infra/jobs")
@Tag(name = "infra-job")
public class JobController {

    private final JobAppService jobAppService;

    public JobController(JobAppService jobAppService) {
        this.jobAppService = jobAppService;
    }

    @GetMapping
    @SaCheckPermission("sys:job:read")
    public R<List<SysJobEntity>> list() {
        return R.ok(jobAppService.list());
    }

    @PostMapping
    @SaCheckPermission("sys:job:write")
    public R<Long> create(@RequestBody SysJobEntity body) {
        return R.ok(jobAppService.create(body));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("sys:job:write")
    public R<Void> update(@PathVariable Long id, @RequestBody SysJobEntity body) {
        body.setId(id);
        jobAppService.update(body);
        return R.ok();
    }

    @PostMapping("/{id}/trigger")
    @SaCheckPermission("sys:job:write")
    public R<Void> trigger(@PathVariable Long id) {
        jobAppService.trigger(id);
        return R.ok();
    }

    @PostMapping("/{id}/pause")
    @SaCheckPermission("sys:job:write")
    public R<Void> pause(@PathVariable Long id) {
        jobAppService.pause(id);
        return R.ok();
    }

    @PostMapping("/{id}/resume")
    @SaCheckPermission("sys:job:write")
    public R<Void> resume(@PathVariable Long id) {
        jobAppService.resume(id);
        return R.ok();
    }
}
""",
    "NotifyController.java": HEADER
    + """import com.fastboot.modules.infra.application.NotifyAppService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/infra/notify")
@Tag(name = "infra-notify")
public class NotifyController {

    private final NotifyAppService notifyAppService;

    public NotifyController(NotifyAppService notifyAppService) {
        this.notifyAppService = notifyAppService;
    }

    @PostMapping
    @SaCheckPermission("sys:notify:write")
    public R<Void> send(@RequestBody NotifyRequest request) {
        notifyAppService.send(request.channel(), request.templateCode(), request.receiver(), request.params());
        return R.ok();
    }

    public record NotifyRequest(
            String channel, String templateCode, String receiver, Map<String, Object> params) {}
}
""",
    "InboxController.java": HEADER
    + """import com.fastboot.modules.infra.application.InboxAppService;
import com.fastboot.modules.infra.infrastructure.persistence.entity.SysInboxEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/infra/inbox")
@Tag(name = "infra-inbox")
public class InboxController {

    private final InboxAppService inboxAppService;

    public InboxController(InboxAppService inboxAppService) {
        this.inboxAppService = inboxAppService;
    }

    @GetMapping
    @SaCheckPermission("sys:inbox:read")
    public R<List<SysInboxEntity>> list() {
        return R.ok(inboxAppService.listForUser(StpUtil.getLoginIdAsLong()));
    }

    @PostMapping("/{id}/read")
    @SaCheckPermission("sys:inbox:write")
    public R<Void> markRead(@PathVariable Long id) {
        inboxAppService.markRead(id);
        return R.ok();
    }
}
""",
    "DataChangeLogController.java": HEADER
    + """import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fastboot.modules.infra.application.DataChangeLogQueryService;
import com.fastboot.modules.infra.infrastructure.persistence.entity.SysDataChangeLogEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/infra/data-changes")
@Tag(name = "infra-data-change")
public class DataChangeLogController {

    private final DataChangeLogQueryService dataChangeLogQueryService;

    public DataChangeLogController(DataChangeLogQueryService dataChangeLogQueryService) {
        this.dataChangeLogQueryService = dataChangeLogQueryService;
    }

    @GetMapping
    @SaCheckPermission("sys:audit:read")
    public R<Page<SysDataChangeLogEntity>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return R.ok(dataChangeLogQueryService.page(page, size));
    }
}
""",
}

for name, content in controllers.items():
    with open(os.path.join(BASE, name), "w", encoding="utf-8") as f:
        f.write(content)
print("infra controllers ok")
