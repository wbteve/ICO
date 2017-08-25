package com.tongwii.ico.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tongwii.ico.core.Result;
import com.tongwii.ico.exception.StorageFileNotFoundException;
import com.tongwii.ico.model.ProjectWallet;
import com.tongwii.ico.model.TokenMoney;
import com.tongwii.ico.model.UserProjectInvestRecord;
import com.tongwii.ico.model.UserWallet;
import com.tongwii.ico.service.*;
import com.tongwii.ico.service.impl.UserWalletServiceImpl;
import com.tongwii.ico.util.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.List;

/**
 *用户项目投资记录
 * @date 2017-08-25
 */
@RestController
@RequestMapping("/userProjectInvestRecord")
public class UserProjectInvestRecordController {
    @Autowired
    private UserProjectInvestRecordService userProjectInvestRecordService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private TokenMoneyService tokenMoneyService;
    @Autowired
    private ProjectWalletService projectWalletService;
    @Autowired
    private UserWalletService userWalletService;

    /***
     * 保存用户项目投资记录
     * @param userProjectInvestRecord
     * @return
     */
    @PostMapping
    @ResponseBody
    public Result add(@RequestBody UserProjectInvestRecord userProjectInvestRecord) {
        userProjectInvestRecordService.save(userProjectInvestRecord);
        return Result.successResult();
    }
    /***
     * 根据当前用户获取用户项目投资记录
     * @param page
     * @param size
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public Result findUserProjectInvestRecordByUserId(@RequestParam(required = true,defaultValue = "0") Integer page,
                                                      @RequestParam(required = true,defaultValue = "1") Integer size){
        PageHelper.startPage(page, size);
        Integer userId = ContextUtils.getUserId();
        List<UserProjectInvestRecord> userProjectInvestRecordList = userProjectInvestRecordService.findByUserId(userId);
        if(!CollectionUtils.isEmpty(userProjectInvestRecordList)){
            for(int i=0;i<userProjectInvestRecordList.size();i++){
                userProjectInvestRecordList.get(i);
                // 根据projectId获取projictInfo
                userProjectInvestRecordList.get(i).setProject(projectService.findById(userProjectInvestRecordList.get(i).getProjectId()));
                // 查询币种信息
                TokenMoney tokenMoney = tokenMoneyService.findById(userProjectInvestRecordList.get(i).getTokenId());
                userProjectInvestRecordList.get(i).setTokenMoney(tokenMoney);
                // 根据tokenMoneyId与projectId查询projectWallet
                ProjectWallet projectWallet = projectWalletService.findWalletByCionId(tokenMoney.getId(),userProjectInvestRecordList.get(i).getProjectId());
                userProjectInvestRecordList.get(i).setProjectWallet(projectWallet);
                // 根据tokenMoneyId与userId查询userWallet
                UserWallet userWallet = userWalletService.findWalletByCionId(tokenMoney.getId(),userProjectInvestRecordList.get(i).getUserId());
                userProjectInvestRecordList.get(i).setUserWallet(userWallet);
            }
            PageInfo pageInfo = new PageInfo(userProjectInvestRecordList);
            return Result.successResult(pageInfo);
        }else {
            return Result.failResult("暂无用户交易记录!");
        }
    }
    /***
     * 根据项目获取用户项目投资记录
     * @param page
     * @param size
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/project/{projectId}")
    public Result findUserProjectInvestRecordByProjectId(@PathVariable("projectId")Integer projectId,@RequestParam(required = true,defaultValue = "0") Integer page,
                                                      @RequestParam(required = true,defaultValue = "1") Integer size){
        PageHelper.startPage(page, size);
        List<UserProjectInvestRecord> userProjectInvestRecordList = userProjectInvestRecordService.findByProjectId(projectId);
        if(!CollectionUtils.isEmpty(userProjectInvestRecordList)){
            for(int i=0;i<userProjectInvestRecordList.size();i++){
                userProjectInvestRecordList.get(i);
                // 根据projectId获取projictInfo
                userProjectInvestRecordList.get(i).setProject(projectService.findById(userProjectInvestRecordList.get(i).getProjectId()));
                // 查询币种信息
                TokenMoney tokenMoney = tokenMoneyService.findById(userProjectInvestRecordList.get(i).getTokenId());
                userProjectInvestRecordList.get(i).setTokenMoney(tokenMoney);
                // 根据tokenMoneyId与projectId查询projectWallet
                ProjectWallet projectWallet = projectWalletService.findWalletByCionId(tokenMoney.getId(),userProjectInvestRecordList.get(i).getProjectId());
                userProjectInvestRecordList.get(i).setProjectWallet(projectWallet);
                // 根据tokenMoneyId与userId查询userWallet
                UserWallet userWallet = userWalletService.findWalletByCionId(tokenMoney.getId(),userProjectInvestRecordList.get(i).getUserId());
                userProjectInvestRecordList.get(i).setUserWallet(userWallet);
            }
            PageInfo pageInfo = new PageInfo(userProjectInvestRecordList);
            return Result.successResult(pageInfo);
        }else {
            return Result.failResult("暂无用户交易记录!");
        }
    }

}