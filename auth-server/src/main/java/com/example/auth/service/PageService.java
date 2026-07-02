package com.example.auth.service;

import com.example.auth.dto.PageParam;
import com.example.auth.dto.PageDto;

import java.util.List;
import java.util.Set;

public interface PageService {
    PageDto createPage(PageParam param);

    PageDto getPageById(Long id);

    List<PageDto> getAllPages();

    void deletePage(Long id);

    PageDto assignPermissionsToPage(Long pageId, Set<Long> permissionIds);
}
