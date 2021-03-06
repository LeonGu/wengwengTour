package com.tour.frame.utils;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
public class FileUploadUtil {
	/**
	 * bear
	 */
	
	//当上传文件超过限制时设定的临时文件位置，注意是绝对路径
    private String tempPath = null;
    //文件上传目标目录，注意是绝对路径
    private String dstPath = null;
    //新文件名称，不设置时默认为原文件名
    private String newFileName = "testtest";
    //获取的上传请求
    private HttpServletRequest fileuploadReq = null;
    //设置最多只允许在内存中存储的数据,单位:字节，这个参数不要设置太大
    private int sizeThreshold = 4096;
    //设置允许用户上传文件大小,单位:字节
    //共10M
    private long sizeMax = 10485760;
    //图片文件序号
    private int picSeqNo = 1;
    private boolean isSmallPic = false;
    public FileUploadUtil(){
    }
    public FileUploadUtil(String tempPath, String destinationPath){
        this.tempPath  = tempPath;
        this.dstPath = destinationPath;
    }
    public FileUploadUtil(String tempPath, String destinationPath, HttpServletRequest fileuploadRequest){
        this.tempPath   = tempPath;
        this.dstPath = destinationPath;
        this.fileuploadReq = fileuploadRequest;
    }
    /** 文件上载
     * @return true —— success; false —— fail.
     */
    public FileBean Upload(){
        DiskFileItemFactory factory = new DiskFileItemFactory();
        FileBean fb=new FileBean();
        try {
            //如果没有上传目的目录，则创建它
            FileUtil.createDir(dstPath);
            /*if (!FileUtil.makeDirectory(dstPath+"/ddd")) {
                throw new IOException("Create destination Directory Error.");
            }*/
            //如果没有临时目录，则创建它
            FileUtil.createDir(tempPath);
            /*if (!FileUtil.makeDirectory(tempPath+"/ddd")) {
                throw new IOException("Create Temp Directory Error.");
            }*/
            //上传项目只要足够小，就应该保留在内存里。
            //较大的项目应该被写在硬盘的临时文件上。
            //非常大的上传请求应该避免。
            //限制项目在内存中所占的空间，限制最大的上传请求，并且设定临时文件的位置。
            //设置最多只允许在内存中存储的数据,单位:字节
            factory.setSizeThreshold(sizeThreshold);
            // the location for saving data that is larger than getSizeThreshold()
            factory.setRepository(new File(tempPath));
            ServletFileUpload upload = new ServletFileUpload(factory);
            //设置允许用户上传文件大小,单位:字节
            upload.setSizeMax(sizeMax);
           
            List<FileItem> fileItems = upload.parseRequest(fileuploadReq);
            Iterator iter = fileItems.iterator();
            //  正则匹配，过滤路径取文件名
            String regExp = ".+\\\\(.+)$";
            //  过滤掉的文件类型
            String[] errorType = {".exe", ".com", ".cgi", ".asp", ".php", ".jsp"};
            Pattern p = Pattern.compile(regExp);
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                //忽略其他不是文件域的所有表单信息
                if (!item.isFormField()) {
                    String name = item.getName();
                    long size = item.getSize();
                    //有多个文件域时，只上传有文件的
                    if ((name == null || name.equals("")) && size == 0)
                        continue;
                    Matcher m = p.matcher(name);
                    //boolean result = m.find();
                    boolean    result=true;
                    if (result) {
                           String ext = item.getName();
                           fb.setPhotoName(ext);
                        try {
                            //保存上传的文件到指定的目录
                            //在下文中上传文件至数据库时，将对这里改写
                            //没有指定新文件名时以原文件名来命名
                            if (newFileName == null || newFileName.trim().equals(""))
                            {
                                item.write(new File(dstPath +"/"+ m.group(1)));
                            }
                            else
                            {
                                String uploadfilename = "";
                                String generateFileName=fb.generateFileName(ext);
                                if (isSmallPic)
                                {
                                    uploadfilename = dstPath +File.separator+ "_small"+generateFileName;
                                }
                                else
                                {
                                    uploadfilename = dstPath +File.separator+ generateFileName;
                                }
                                fb.setPhotoId(generateFileName);
                                //生成所有未生成的目录
                               // FileUtil.createDir(uploadfilename);
                                //item.write(new File(dstPath +"/"+ newFileName));
                                item.write(new File(uploadfilename));
                            }
                        } catch (Exception e) {
                        	e.printStackTrace();
                             throw new IOException(e.getMessage());
                        }
                    } else {
                        throw new IOException("fail to upload");
                    }
                }
            }
        } catch (IOException e) {
        	e.printStackTrace();
            System.out.println(e);
        } catch (FileUploadException e) {
        	e.printStackTrace();
            System.out.println(e);
        }
        return fb;
    }
    /**从路径中获取单独文件*/
    public String GetFileName(String filepath)
    {
        String returnstr = "*.*";
        int length       = filepath.trim().length();
        filepath = filepath.replace('\\', '/');
        if(length >0)
        {
            int i = filepath.lastIndexOf("/");
            if (i >= 0)
            {
                filepath  = filepath.substring(i + 1);
                returnstr = filepath;
            }
        }
        return returnstr;
    }
    
    /**
     * 设置临时存贮目录
     */
    public void setTmpPath(String tmppath)
    {
        this.tempPath = tmppath;
    }
    /**
     * 设置目标目录
     */
    public void setDstPath(String dstpath) {
        this.dstPath = dstpath;
    }
    /**
     * 设置最大上传文件字节数，不设置时默认10M
     */
    public void setFileMaxSize(long maxsize) {
        this.sizeMax = maxsize;
    }
    /**
     * 设置Http 请求参数，通过这个能数来获取文件信息
     */
    public void setHttpReq(HttpServletRequest httpreq) {
        this.fileuploadReq = httpreq;
    }
    /**
     * 设置Http 请求参数，通过这个能数来获取文件信息
     */
    public void setNewFileName(String filename) {
        this.newFileName = filename;
    }
    /**
     * 设置此上传文件是否是缩略图文件，这个参数主要用于缩略图命名
     */
    public void setIsSmalPic(boolean isSmallPic) {
        this.isSmallPic = isSmallPic;
    }
    /**
     * 设置Http 请求参数，通过这个能数来获取文件信息
     */
    public void setPicSeqNo(int seqNo) {
        this.picSeqNo = seqNo;
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
