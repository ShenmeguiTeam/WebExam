package com.scsx.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.WebResource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import com.mysql.fabric.xmlrpc.Client;
import com.scsx.domain.User;
import com.scsx.service.UserService;

import cn.dsna.util.images.ValidateCode;
  
@Controller
@MultipartConfig
public class UserController {
	
	@RequestMapping(value = "/getAllUsers.do")
	public void getAllUsers(HttpServletRequest req, HttpServletResponse res) {
		User user = (User) req.getSession().getAttribute("user");
		res.setHeader("Content-type", "text/html;charset=UTF-8");
		res.setCharacterEncoding("UTF-8");  
		try {
			if (!UserService.getUserServiceInstance().confirmAdmin(user)){
				res.getWriter().write("err");
			}
			int page = Integer.parseInt(req.getParameter("page"));
			String usersJson = UserService.getUserServiceInstance().findAllUsers(page);
			System.out.println(usersJson);
			res.getWriter().write(usersJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/codeServlet.do", method = RequestMethod.GET)
	public void codeServlet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		ValidateCode vc = new ValidateCode(110, 25, 4, 9);
		//向session中保存验证码
		request.getSession().setAttribute("scode", vc.getCode());
		vc.write(response.getOutputStream());
	}
	
	@RequestMapping(value = "/upload.do")
    public String upload(@RequestParam("file") MultipartFile file,HttpServletRequest request) {  
        // 判断文件是否为空  
		System.out.println("开始");
        if (!file.isEmpty()) {  
        	System.out.println("in if");
            try {  
                // 文件保存路径  
                String filePath = request.getSession().getServletContext().getRealPath("/") + "images/"  
                        + file.getOriginalFilename();  
                //System.out.println(filePath);
                // 转存文件  
                file.transferTo(new File(filePath));
                System.out.println(file.getOriginalFilename());
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        // 跳转  
        return "test";  
    }  
	
	@Bean
	public MultipartResolver multipartResolver() {
	    return new StandardServletMultipartResolver();
	}
}  
