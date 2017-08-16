package com.tongwii.ico.service.impl;

import com.tongwii.ico.dao.ProjectMapper;
import com.tongwii.ico.model.Project;
import com.tongwii.ico.model.TokenDetail;
import com.tongwii.ico.model.User;
import com.tongwii.ico.service.ProjectService;
import com.tongwii.ico.core.AbstractService;
import com.tongwii.ico.service.TokenDetailService;
import com.tongwii.ico.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by Zeral on 2017-08-02.
 */
@Service
@Transactional
public class ProjectServiceImpl extends AbstractService<Project> implements ProjectService {
    @Resource
    private ProjectMapper projectMapper;
    @Autowired
    private TokenDetailService tokenDetailService;
    @Autowired
    private UserService userService;

    @Override
    public void updateOutputTokenMoney(Integer id, TokenDetail tokenDetail) {
        tokenDetailService.save(tokenDetail);
        Project project = findById(id);
        project.setOutputTokenMoneyDetailId(tokenDetail.getId());
        update(project);
    }

    @Override
    public void updateCreateUser(Integer id, User user) {
        userService.save(user);
        Project project = findById(id);
        project.setCreateUserId(user.getId());
        update(project);

    }

    @Override
    public List<Project> findOfficalProject() {
        Project project = new Project();
        project.setState(-1);
        List<Project> projectList = findAll();
        List<Project> projects = projectMapper.select(project);
        for(int i=0; i<projectList.size(); i++){
            for(int j=0; j<projects.size(); j++){
                if(projectList.get(i).getId() == projects.get(j).getId()){
                    projectList.remove(i);
                }
            }
        }
        return projectList;
    }
}
