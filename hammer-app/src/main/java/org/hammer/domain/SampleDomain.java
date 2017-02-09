package org.hammer.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
 

/**
 * @author shenx
 * @date 2017年2月3日
 * @see
 * @version
 */


public class SampleDomain implements Serializable {
 
    private static final long serialVersionUID = 1L;
    private Integer orderId;

    private Integer tradeId;

    private Integer orderType;

    private Date opdate;

    private Integer userId;

    private Integer orderStatus;

    private Integer ofcStatus;

    private String ofcOrderid;

    private Integer logisticsStatus;

    private Integer cancelOrderid;

    private BigDecimal fee;

    private Integer point;

    private Integer payStatus;

    private Integer delStatus;

    private Integer channelId;

    private String store;

    private Boolean ispresell;

    private String orderCode;

    private Integer sourceId;

    private Integer entranceId;    

    private Integer appStatus ;
    
    private Integer cmsStatus ;
    
    private Date cancelDate;
    
    private Integer shippingMethod ;

    private Integer regionId;
    

    public Integer getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(Integer shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public Date getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
    }

    public Integer getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(Integer appStatus) {
        this.appStatus = appStatus;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public Integer getCmsStatus() {
        return cmsStatus;
    }

    public void setCmsStatus(Integer cmsStatus) {
        this.cmsStatus = cmsStatus;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getTradeId() {
        return tradeId;
    }

    public void setTradeId(Integer tradeId) {
        this.tradeId = tradeId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Date getOpdate() {
        return opdate;
    }

    public void setOpdate(Date opdate) {
        this.opdate = opdate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getOfcStatus() {
        return ofcStatus;
    }

    public void setOfcStatus(Integer ofcStatus) {
        this.ofcStatus = ofcStatus;
    }

    public String getOfcOrderid() {
        return ofcOrderid;
    }

    public void setOfcOrderid(String ofcOrderid) {
        this.ofcOrderid = ofcOrderid;
    }

    public Integer getLogisticsStatus() {
        return logisticsStatus;
    }

    public void setLogisticsStatus(Integer logisticsStatus) {
        this.logisticsStatus = logisticsStatus;
    }

    public Integer getCancelOrderid() {
        return cancelOrderid;
    }

    public void setCancelOrderid(Integer cancelOrderid) {
        this.cancelOrderid = cancelOrderid;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Integer getPoint() {
        return point;
    }


    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public Boolean getIspresell() {
        return ispresell;
    }

    public void setIspresell(Boolean ispresell) {
        this.ispresell = ispresell;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getEntranceId() {
        return entranceId;
    }

    public void setEntranceId(Integer entranceId) {
        this.entranceId = entranceId;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }
}
