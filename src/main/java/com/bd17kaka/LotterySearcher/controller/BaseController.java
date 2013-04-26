package com.bd17kaka.LotterySearcher.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;


public class BaseController {

	/**用户帐号在request中的key*/
	public static final String PassportAttKey = "passport";
	
    /**
     * 设置不要缓存数据
     * @param response
     */
    public void setNoCache(HttpServletResponse response){
    	response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
    }
    
    /**写入信息*/
    private void write(String contentType,HttpServletRequest req, HttpServletResponse resp,String msg)throws ServletException, IOException 
    {
    	resp.setContentType(contentType);
		OutputStreamWriter out = new OutputStreamWriter(resp.getOutputStream(),"UTF-8");
		out.write(msg);
		resp.setContentLength(msg.getBytes("UTF-8").length);
		out.flush();
    }
    
	protected void writeHtml(HttpServletRequest req, HttpServletResponse resp,String msg)throws ServletException, IOException 
	{
		write("text/html; charset=UTF-8",req,resp,msg);
	}
	
	protected void writeJavaScript(HttpServletRequest req, HttpServletResponse resp,String msg)throws ServletException, IOException 
	{
		write("application/x-javascript; charset=\"UTF-8\"",req,resp,msg);
	}
	
	protected void writeJson(HttpServletRequest req, HttpServletResponse resp,String msg)throws ServletException, IOException 
	{
		write("text/html; charset=\"UTF-8\"",req,resp,msg);
	}
	
	protected void writeXml(HttpServletRequest req, HttpServletResponse resp,String msg)throws ServletException, IOException 
	{
		write("application/xml; charset=\"UTF-8\"",req,resp,msg);
	}
	
    
    /**
     * 判断字符串是否是数字和字母组成
     * @param str
     * @return
     */
    public boolean isLegalString(String str)
    {
    	return str!=null && str.matches("^[0-9|a-z|A-Z]+$");
    }
    
    /**
     * 判断passport是否合法
     * @param passport
     * @return
     */
    public boolean isLegalPassport(String passport)
    {
    	return passport!=null && passport.matches("^[^'\"`]{4,20}$");
    }
    
    public String getNotNull(String key,HttpServletRequest req) 
    {
		String value = req.getParameter(key);
		return (value == null ? "" : value);
	}
    
    /**
     * 返回登录的通行证
     * @param request
     * @param response
     * @return
     */
    public static String getRequestPassport(HttpServletRequest request,boolean needExist)
    {
    	String passport = (String )request.getAttribute(PassportAttKey);
    	if(StringUtils.isEmpty(passport) && needExist)
    		throw new RuntimeException("not found request attribute "+PassportAttKey);
    	return passport;
    }
    
    /**
     * 设置登陆的通行证
     * @param request
     * @param passport
     */
    public static void setLoginedPassport(HttpServletRequest request,String passport)
    {
    	request.setAttribute(PassportAttKey, passport);
    }
    
	/**
	 * 通过request文件头拿到真实IP地址
	 */
	public static String getRealIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isEmpty(ip)) {
			return request.getRemoteAddr();
		}
		ip = ip.split(", ")[0].trim();
		if ("127.0.0.1".equals(ip) || !isLicitIp(ip)) {
			return request.getRemoteAddr();
		}
		return ip;
	}
	
    public static boolean isLicitIp(String ip)
    {
        if(StringUtils.isEmpty(ip))
            return false;
        String regex = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(ip);
        return m.find();
    }
    
    public int getPageNum(HttpServletRequest request){
    	try{
    		return Integer.parseInt(request.getParameter("pagenum"));
    	}catch(NumberFormatException ne){
    		return 1;
    	}
    }
    
}
