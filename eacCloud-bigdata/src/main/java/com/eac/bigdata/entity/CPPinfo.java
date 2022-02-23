package com.eac.bigdata.entity;


import java.io.Serializable;

public class CPPinfo implements Serializable {
    private String id;

    private String accessTypeInfo;

    private Long createTime;

    private Long monthTime;

    private String neighborInfo;

    private String probeId;

    private String probeInfo;

    private String statusInfo;

    private Long timesheet;

    private String trafficInfo;

    private String sgwInfo;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessTypeInfo() {
        return accessTypeInfo;
    }

    public void setAccessTypeInfo(String accessTypeInfo) {
        this.accessTypeInfo = accessTypeInfo;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getMonthTime() {
        return monthTime;
    }

    public void setMonthTime(Long monthTime) {
        this.monthTime = monthTime;
    }

    public String getNeighborInfo() {
        return neighborInfo;
    }

    public void setNeighborInfo(String neighborInfo) {
        this.neighborInfo = neighborInfo;
    }

    public String getProbeId() {
        return probeId;
    }

    public void setProbeId(String probeId) {
        this.probeId = probeId;
    }

    public String getProbeInfo() {
        return probeInfo;
    }

    public void setProbeInfo(String probeInfo) {
        this.probeInfo = probeInfo;
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(String statusInfo) {
        this.statusInfo = statusInfo;
    }

    public Long getTimesheet() {
        return timesheet;
    }

    public void setTimesheet(Long timesheet) {
        this.timesheet = timesheet;
    }

    public String getTrafficInfo() {
        return trafficInfo;
    }

    public void setTrafficInfo(String trafficInfo) {
        this.trafficInfo = trafficInfo;
    }

    public String getSgwInfo() {
        return sgwInfo;
    }

    public void setSgwInfo(String sgwInfo) {
        this.sgwInfo = sgwInfo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", accessTypeInfo=").append(accessTypeInfo);
        sb.append(", createTime=").append(createTime);
        sb.append(", monthTime=").append(monthTime);
        sb.append(", neighborInfo=").append(neighborInfo);
        sb.append(", probeId=").append(probeId);
        sb.append(", probeInfo=").append(probeInfo);
        sb.append(", statusInfo=").append(statusInfo);
        sb.append(", timesheet=").append(timesheet);
        sb.append(", trafficInfo=").append(trafficInfo);
        sb.append(", sgwInfo=").append(sgwInfo);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
