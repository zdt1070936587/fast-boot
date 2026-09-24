package com.fastboot.modules.authz.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.fastboot.modules.authz.infrastructure.mapper.AuthzRelationMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class AuthzAppServiceTest {

    @Mock private AuthzRelationMapper relationMapper;
    @Mock private ApplicationEventPublisher events;
    @InjectMocks private AuthzAppService service;

    @Test
    void visibleOrgIdsReturnsEmptyForAllScope() {
        when(relationMapper.listScopeTypes(1L)).thenReturn(List.of("ALL"));
        assertTrue(service.visibleOrgIds(1L, 9L).isEmpty());
    }

    @Test
    void visibleOrgIdsReturnsEmptyWhenNoScopeConfigured() {
        when(relationMapper.listScopeTypes(1L)).thenReturn(List.of());
        assertTrue(service.visibleOrgIds(1L, 9L).isEmpty());
    }

    @Test
    void visibleOrgIdsIncludesSelfOrg() {
        when(relationMapper.listScopeTypes(1L)).thenReturn(List.of("SELF_ORG"));
        assertEquals(List.of(9L), service.visibleOrgIds(1L, 9L));
    }

    @Test
    void visibleOrgIdsMergesCustomOrgs() {
        when(relationMapper.listScopeTypes(1L)).thenReturn(List.of("SELF_ORG", "CUSTOM"));
        when(relationMapper.listCustomOrgIds(1L)).thenReturn(List.of(7L, 8L));
        assertEquals(List.of(7L, 8L, 9L), service.visibleOrgIds(1L, 9L).stream().sorted().toList());
    }
}
