package com.haoyu.swift.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by haoyu on 2018/8/11.
 */
@Component
public class OS3Config {

    private static String swiftServer;
    private static String domainId;
    private static String projectName;
    private static String gzacUser;
    private static String gzacPwd;
    private static String emlContainer;

    public static String getSwiftServer() {
        return swiftServer;
    }

    @Value("${openstack.swiftServer}")
    public void setSwiftServer(String swiftServer) {
        OS3Config.swiftServer = swiftServer;
    }

    public static String getDomainId() {
        return domainId;
    }

    @Value("${openstack.domainId}")
    public void setDomainId(String domainId) {
        OS3Config.domainId = domainId;
    }

    public static String getProjectName() {
        return projectName;
    }

    @Value("${openstack.projectName}")
    public void setProjectName(String projectName) {
        OS3Config.projectName = projectName;
    }

    public static String getGzacUser() {
        return gzacUser;
    }

    @Value("${openstack.gzacUser}")
    public void setGzacUser(String gzacUser) {
        OS3Config.gzacUser = gzacUser;
    }

    public static String getGzacPwd() {
        return gzacPwd;
    }

    @Value("${openstack.gzacPwd}")
    public void setGzacPwd(String gzacPwd) {
        OS3Config.gzacPwd = gzacPwd;
    }

    public static String getEmlContainer() {
        return emlContainer;
    }

    @Value("${openstack.emlContainer}")
    public void setEmlContainer(String emlContainer) {
        OS3Config.emlContainer = emlContainer;
    }


}
