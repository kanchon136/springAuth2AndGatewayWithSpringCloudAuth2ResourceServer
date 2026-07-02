package com.example.auth.impl;

import com.example.auth.dto.PageDto;
import com.example.auth.dto.PageParam;
import com.example.auth.mapper.EntityMapper;
import com.example.auth.repository.PageRepository;
import com.example.auth.repository.PermissionRepository;
import com.example.auth.service.PageService;
import com.example.common.model.Page;
import com.example.common.model.Permission;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;
    private final PermissionRepository permissionRepository;

    @Autowired
    public PageServiceImpl(PageRepository pageRepository, PermissionRepository permissionRepository) {
        this.pageRepository = pageRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Transactional
    public PageDto createPage(PageParam param) {
        Page page = new Page();
        page.setName(param.getName());
        page.setUrlPath(param.getUrlPath());

        Set<Permission> permissions = new HashSet<>();
        if (param.getPermissionIds() != null && !param.getPermissionIds().isEmpty()) {
            permissions = param.getPermissionIds().stream()
                    .map(permId -> permissionRepository.findById(permId)
                            .orElseThrow(() -> new EntityNotFoundException("Permission not found with id: " + permId)))
                    .collect(Collectors.toSet());
        }
        page.setPermissions(permissions);

        Page savedPage = pageRepository.save(page);
        return EntityMapper.toPageDto(savedPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageDto getPageById(Long id) {
        Page page = pageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Page not found with id: " + id));
        return EntityMapper.toPageDto(page);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PageDto> getAllPages() {
        return pageRepository.findAll().stream()
                .map(EntityMapper::toPageDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePage(Long id) {
        if (!pageRepository.existsById(id)) {
            throw new EntityNotFoundException("Page not found with id: " + id);
        }
        pageRepository.deleteById(id);
    }

    @Override
    @Transactional
    public PageDto assignPermissionsToPage(Long pageId, Set<Long> permissionIds) {
        Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new EntityNotFoundException("Page not found with id: " + pageId));
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        page.setPermissions(new HashSet<>(permissions));
        Page savedPage = pageRepository.save(page);
        return EntityMapper.toPageDto(savedPage);
    }
}
