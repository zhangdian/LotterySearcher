package com.bd17kaka.LotterySearcher.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bd17kaka.LotterySearcher.service.SSHResultService;

/**
 * @author bd17kaka
 * 双色球历史结果Controller
 */
@Controller(value = "sshResultController")
public class SSHResultController extends BaseController {

	
	@Resource(name="sshResultService")
	private SSHResultService sshResultService;
	
	/**
	 * 获取指定分布的出现次数，V3
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws WeiboException
	 */
	@RequestMapping("/countByDistributeV3.do")
	public void countByDistributeV3(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		String distribute = StringUtils.trimToEmpty(request.getParameter("distribute"));
		if (StringUtils.isEmpty(distribute)) {
			writePlain(request, response, "分布不能不能为空");
			return;
		}
		
		long n = sshResultService.countByDistributeV3(distribute);
		writePlain(request, response, String.valueOf(n));
	}

}
