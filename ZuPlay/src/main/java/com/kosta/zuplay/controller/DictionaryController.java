package com.kosta.zuplay.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DictionaryController {

	@ResponseBody
	@RequestMapping(value = "searchFinancialTerm", produces = "application/json;charset=UTF-8")
	public String getStockList(HttpSession session, String term) throws Exception {
		BufferedReader rd;
		HttpURLConnection conn;
		try {
			StringBuilder urlBuilder = new StringBuilder(
				"http://api.seibro.or.kr/openapi/service/FnTermSvc/getFinancialTermMeaning"); /* URL */
		urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8")
				+ "=pZqmiyxvoN2Gs3%2BDlr4F0vaxjytfFV3%2FIlnklIFqPoyNZvux7gwoAycn7VfKYPbdcZaFzlA2EWcTdIKRBM%2F4jw%3D%3D"); 
		urlBuilder.append("&" + URLEncoder.encode("term", "UTF-8") + "=" + URLEncoder.encode(term, "UTF-8")); /* 용어명 */
		urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "="
				+ URLEncoder.encode("1", "UTF-8")); /* 페이지 번호 */
		urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
				+ URLEncoder.encode("100", "UTF-8")); /* 한 페이지 결과 수 */
		URL url = new URL(urlBuilder.toString());
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		} catch(Exception e) {
			session.setAttribute("errorMsg", e.toString());
			e.printStackTrace();
			throw new Exception();
		}
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		conn.disconnect();
		return sb.toString();
	}
}
