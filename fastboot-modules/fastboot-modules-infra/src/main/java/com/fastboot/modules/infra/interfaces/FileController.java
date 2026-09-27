package com.fastboot.modules.infra.interfaces;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.fastboot.common.api.R;
import com.fastboot.modules.infra.api.FileRef;
import com.fastboot.modules.infra.application.FileAppService;
import com.fastboot.modules.infra.infrastructure.persistence.entity.SysFileEntity;
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

    @GetMapping
    @SaCheckPermission("sys:file:read")
    public R<java.util.List<SysFileEntity>> list(@RequestParam(required = false) String keyword) {
        return R.ok(fileAppService.list(keyword));
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
