package com.tongwii.ico.service.impl;

import com.tongwii.ico.dao.ProjectUserRelationMapper;
import com.tongwii.ico.model.ProjectUserRelation;
import com.tongwii.ico.service.ProjectUserRelationService;
import com.tongwii.ico.core.AbstractService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by Zeral on 2017-08-02.
 */
@Service
@Transactional
public class ProjectUserRelationServiceImpl extends AbstractService<ProjectUserRelation> implements ProjectUserRelationService {
    @Resource
    private ProjectUserRelationMapper projectUserRelationMapper;

    @Override
    public List<ProjectUserRelation> findByUserId(Integer userId) {
        // 首先先获取项目Id
        ProjectUserRelation projectUserRelation = new ProjectUserRelation();
        projectUserRelation.setUserId(userId);
        List<ProjectUserRelation> projectUserRelations = projectUserRelationMapper.select(projectUserRelation);
        if(CollectionUtils.isEmpty(projectUserRelations)){
            return null;
        }
        return projectUserRelations;
    }

    @Override
    public boolean userIsLockedProject(Integer userId, Integer projectId) {
        ProjectUserRelation projectUserRelation = new ProjectUserRelation();
        projectUserRelation.setProjectId(projectId);
        projectUserRelation.setUserId(userId);
        List<ProjectUserRelation> projectUserRelations = projectUserRelationMapper.select(projectUserRelation);
        if(CollectionUtils.isNotEmpty(projectUserRelations)){
            return true;
        }else{
            return false;
        }
    }
}
