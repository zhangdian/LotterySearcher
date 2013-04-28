package com.bd17kaka.LotterySearcher.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bd17kaka.LotterySearcher.service.SSHCalProbabilityService;

/**
 * @author bd17kaka
 * 计算双色球概率的Controller
 */
@Controller(value = "sshCalProbabilityController")
public class SSHCalProbabilityController extends BaseController {

	
	@Resource(name="sshCalProbabilityServiceSimpleSpan3Impl")
	private SSHCalProbabilityService sshCalProbabilityService;
	
	/**
	 * 计算双色球红球序列的概率
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws WeiboException
	 */
	@RequestMapping("/sshCalRedProbability.do")
	public void sshCalRedProbability(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		String input = StringUtils.trimToEmpty(request.getParameter("input"));
		if (StringUtils.isEmpty(input)) {
			writePlain(request, response, "输入有误");
			return;
		}
		
		double fm = sshCalProbabilityService.calRedProbability(input);
		writePlain(request, response, String.valueOf(fm));
	}
	
	/**
	 * 计算最有可能出现的红球序列
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping("/calRedMostProbability.do")
	public void calRedMostProbability(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		sshCalProbabilityService.calRedMostProbability(10);
		
	}
}
