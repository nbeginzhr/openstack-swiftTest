package com.haoyu.swift.manage;

import com.haoyu.swift.utils.StringUtils;
import com.haoyu.swift.configuration.OS3Config;
import org.apache.commons.codec.binary.Base64;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.common.Payloads;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.model.storage.object.SwiftObject;
import org.openstack4j.model.storage.object.options.ObjectListOptions;
import org.openstack4j.model.storage.object.options.ObjectPutOptions;
import org.openstack4j.openstack.OSFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by haoyu on 2018/8/11.
 */
@Component("swiftBiz")
public class SwiftBiz {

    private volatile static Token token;
    private final static Logger logger = LoggerFactory.getLogger(SwiftBiz.class);
    private final String SEPERATE = "/";

    public static OSClient.OSClientV3 clientV3() {
        return OSFactory.builderV3()
                .endpoint(OS3Config.getSwiftServer())
                .credentials(OS3Config.getGzacUser(), OS3Config.getGzacPwd(),
                        Identifier.byId(OS3Config.getDomainId()))
                .scopeToProject(Identifier.byName(OS3Config.getProjectName()), Identifier.byId(OS3Config.getDomainId()))
                .authenticate();
    }

    public static void initToken() {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> init OSClient.OSClientV3 token <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        logger.info(">>>>>>>>>>>>>> swift server : " + OS3Config.getSwiftServer());
        token = OSFactory.builderV3()
                .endpoint(OS3Config.getSwiftServer())
                .credentials(OS3Config.getGzacUser(), OS3Config.getGzacPwd(),
                        Identifier.byId(OS3Config.getDomainId()))
                .scopeToProject(Identifier.byName(OS3Config.getProjectName()), Identifier.byId(OS3Config.getDomainId()))
                .authenticate().getToken();
    }

    private OSClient.OSClientV3 getClient() {
        if (null == token) {
            synchronized (SwiftBiz.class) {
                if (null == token) {
                    initToken();
                }
            }
        }
        return OSFactory.clientFromToken(token);
    }


    /**
     * 保存对象到swift
     *
     * @param container   容器名：同一account下唯一
     * @param fileName    文件名： 同一account container下唯一
     * @param inputStream 待保存文件流
     * @param metadata    文件元数据
     * @return 保存文件返回的 MD5 校验和
     */
    public String save(String container, String fileName, InputStream inputStream, Map<String, String> metadata) {
        checkReqParams(container, fileName);
        if (null == metadata) metadata = new HashMap<>();

        String etag = null;
        try {
            etag = getClient().objectStorage().objects().put(container, fileName,
                    Payloads.create(inputStream), ObjectPutOptions.create().metadata(metadata));
        } catch (Exception e) {
            logger.error("保存文件失败:{} ", e);
            logger.warn(">>>>>>>>>>>>>>> Init swift client token !");
            token = null;
            etag = getClient().objectStorage().objects().put(container, fileName,
                    Payloads.create(inputStream), ObjectPutOptions.create().metadata(metadata));
        }
        return etag;
    }

    /**
     * 删除对象
     *
     * @param container
     * @param fileName
     * @return
     */
    public ActionResponse deleteObject(String container, String fileName) {
        checkReqParams(container, fileName);
        ActionResponse response = null;
        try {
            response = getClient().objectStorage().objects().delete(container, fileName);
        } catch (Exception e) {
            logger.error("删除文件失败:{} ", e);
            logger.warn(">>>>>>>>>>>>>>> Init swift client token !");
            token = null;
            response = getClient().objectStorage().objects().delete(container, fileName);
        }
        return response;
    }

    /**
     * 查找object
     *
     * @param container 容器名
     * @param fileName  文件名
     * @return
     */
    public SwiftObject findObject(String container, String fileName) {
        checkReqParams(container, fileName);
        SwiftObject object = null;
        try {
            object = getClient().objectStorage().objects().get(container, fileName);
        } catch (Exception e) {
            logger.error("查找文件失败:{} ", e);
            logger.warn(">>>>>>>>>>>>>>> Init swift client token !");
            token = null;
            object = getClient().objectStorage().objects().get(container, fileName);
        }
        return object;
    }

    /**
     * 检查容器名 和 文件名格式
     *
     * @param containerName
     * @param fileName
     */
    private void checkReqParams(String containerName, String fileName) {
        if (StringUtils.isEmpty(containerName) || StringUtils.isEmpty(fileName)) {
            logger.error("Check object param , containerName:{} , fileName: {}", containerName, fileName);
            throw new IllegalArgumentException("The operation require containerName and fileName !");
        }
        if (containerName.contains("/")) {
            logger.error("Check object param , containerName:{} , fileName: {}", containerName, fileName);
            throw new IllegalArgumentException("The containerName is illegal , can not contain Character '/' ");
        }
    }

    public InputStream readFile(String container, String fileName) {
        SwiftObject object = findObject(container, fileName);
        if (null == object) throw new IllegalArgumentException("文件资源不存在！");
        return object.download().getInputStream();
    }

    public byte[] readFileToByte(String container, String fileName) throws IOException {
        SwiftObject object = findObject(container, fileName);
        if (null == object)
            throw new IllegalArgumentException("文件资源不存在！");

        return FileCopyUtils.copyToByteArray(object.download().getInputStream());
    }

    public String encodeChineseDownloadFileName(HttpServletRequest request, String pFileName) throws UnsupportedEncodingException {
        if (StringUtils.isEmpty(pFileName)) return "";
        String filename;
        String agent = request.getHeader("USER-AGENT");
        if (null != agent) {
            if (-1 != agent.indexOf("Firefox")) {//Firefox
                filename = "=?UTF-8?B?" + (new String(Base64.encodeBase64(pFileName.getBytes("UTF-8")))) + "?=";
            } else if (-1 != agent.indexOf("Chrome")) {//Chrome
                filename = new String(pFileName.getBytes(), "ISO8859-1");
            } else {//IE7+
                filename = URLEncoder.encode(pFileName, "UTF-8");
                filename = org.springframework.util.StringUtils.replace(filename, "+", "%20");//替换空格
            }
        } else {
            filename = pFileName;
        }
        return filename;
    }

    public String getPath(HttpServletRequest request, String subUrl) throws UnsupportedEncodingException {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        path = URLDecoder.decode(path, "UTF-8");
        path = path.substring(contextPath.length() + subUrl.length());
        return path;
    }

    public List<? extends SwiftObject> listAll(String comtainerName, String marker) {
        List<? extends SwiftObject> objects;
        if (StringUtils.isEmpty(marker)) {
            objects = getClient().objectStorage().objects().list(comtainerName);
        } else {
            objects = getClient().objectStorage().objects().list(comtainerName,
                    ObjectListOptions.create().marker(marker)
            );
        }
        logger.info("SWIFT LIST ALL {} , marker: {} , swiftObject size: {}", comtainerName, marker, objects.size());
        return objects;
    }

    public Map<String, String> getMetadata(String container, String fileName) {
        checkReqParams(container, fileName);
        return getClient().objectStorage().objects().getMetadata(container, fileName);
    }


}
