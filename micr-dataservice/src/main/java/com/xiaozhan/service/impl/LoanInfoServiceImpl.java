package com.xiaozhan.service.impl;

import com.xiaozhan.common.CommonUtils;
import com.xiaozhan.common.PageUtils;
import com.xiaozhan.licai.mapper.LoanInfoMapper;
import com.xiaozhan.licai.mix.LoanBidInfo;
import com.xiaozhan.licai.model.LoanInfo;
import com.xiaozhan.licai.service.LoanInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = LoanInfoService.class,version = "1.0")
public class LoanInfoServiceImpl implements LoanInfoService {
    @Resource
    private LoanInfoMapper loanInfoMapper;
    /*平均值利益率*/
    @Override
    public BigDecimal queryHistoryAvgRate() {
        return loanInfoMapper.selectAvgRate();
    }
    /*分页查询产品*/
    @Override
    public List<LoanInfo> queryPageByType(Integer productType, Integer pageNo, Integer pageSize) {
        List<LoanInfo> loanInfoList =new ArrayList<>();
        if(CommonUtils.checkProductType(productType)){
            //数据检查
            pageNo= PageUtils.defaultPageNo(pageNo);
            pageSize=PageUtils.defaultPageSize(pageSize);
            //计算offSet
            int offSet=(pageNo-1)*pageSize;
            //pageSize写成pageNo了，第三个参数传的是每页的条数
            loanInfoList= loanInfoMapper.selectPageByType(productType,offSet,pageSize);
        }

        return loanInfoList;
    }
    /*获取分页查询产品总记录数*/
    @Override
    public int queryRecordNumsByType(Integer productType) {
        int nums=0;
        if(CommonUtils.checkProductType(productType)){
            nums=loanInfoMapper.selectCountRecordByType(productType);
        }
        return nums;
    }
    /*使用主键，查询产品的信息*/
    @Override
    public LoanInfo queryByLoanId(Integer loanId) {
        LoanInfo loanInfo=null;
        if(loanId!=null&&loanId.intValue()>0){
            loanInfo=loanInfoMapper.selectByPrimaryKey(loanId);
        }
        return loanInfo;
    }
    /*某产品的最近的3条投资记录*/
    @Override
    public List<LoanBidInfo> queryBidInfoLoanId(Integer loanId) {

        return loanInfoMapper.selectBidInfoByLoanId(loanId);
    }
}
