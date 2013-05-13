package com.bd17kaka.LotterySearcher.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bd17kaka.LotterySearcher.constat.SSH.SSHRedAlgorithm;
import com.bd17kaka.LotterySearcher.service.SSHCalProbabilityService;

/**
 * @author bd17kaka
 * 计算双色球概率的Controller
 */
@Controller(value = "sshCalProbabilityController")
public class SSHCalProbabilityController extends BaseController {

	
	@Resource(name="sshCalProbabilityServiceSimpleSpan3V5Impl")
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
			writePlain(request, response, "输入不能为空");
			return;
		}
		
		double fm = sshCalProbabilityService.calRedProbability(input);
		writePlain(request, response, String.valueOf(fm));
	}
	
	/**
	 * 计算最有可能的双色球
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping("/sshCalRedMostProbability.do")
	public void sshCalRedMostProbability(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		// 只有Simple_Span3v5有这个字段
		String distribute = StringUtils.trimToEmpty(request.getParameter("distribute"));
		
		Map<String, Double> rsMap = sshCalProbabilityService.calRedMostProbability(SSHRedAlgorithm.getMaxTotal(), distribute);
		if (rsMap == null || rsMap.size() == 0) {
			writePlain(request, response, "{}");
		}
		
		// 将rsMap瓶装成JSON
		JSONArray array = new JSONArray();
		Set<String> keys = rsMap.keySet();
		for (String key : keys) {
			JSONObject o = new JSONObject();
			o.put(keys, rsMap.get(key));
			array.add(o);
		}
		
		writePlain(request, response, array.toString());
	}
}
