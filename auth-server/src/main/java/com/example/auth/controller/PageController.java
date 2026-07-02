package com.example.auth.controller;

import com.example.auth.dto.PageDto;
import com.example.auth.dto.PageParam;
import com.example.auth.dto.BaseResponse;
import com.example.auth.dto.ResponseEnum;
import com.example.auth.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth-management/pages")
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;

    @PostMapping
    public ResponseEntity<BaseResponse> createPage(@RequestBody PageParam param) {
        PageDto pageDto = pageService.createPage(param);
        return new ResponseEntity<>(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("Page created successfully")
                .pageDto(pageDto)
                .build(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getAllPages() {
        List<PageDto> pages = pageService.getAllPages();
        return ResponseEntity.ok(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("Pages retrieved successfully")
                .pageDtos(pages)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getPageById(@PathVariable Long id) {
        PageDto pageDto = pageService.getPageById(id);
        return ResponseEntity.ok(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("Page retrieved successfully")
                .pageDto(pageDto)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deletePage(@PathVariable Long id) {
        pageService.deletePage(id);
        return ResponseEntity.ok(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("Page deleted successfully")
                .build());
    }

    @PostMapping("/{pageId}/assign-permissions")
    public ResponseEntity<BaseResponse> assignPermissionsToPage(
            @PathVariable Long pageId,
            @RequestBody Set<Long> permissionIds) {
        PageDto pageDto = pageService.assignPermissionsToPage(pageId, permissionIds);
        return ResponseEntity.ok(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("Permissions assigned to page successfully")
                .pageDto(pageDto)
                .build());
    }
}
