package com.viettel.firstspring;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response){
    	ModelAndView modelAndView = new ModelAndView();
    	Cookie[] cookie = request.getCookies();
    	boolean isFirstLogin = true;
    	String email = request.getParameter("email");
		Cookie ck = new Cookie("username", email);
		ck.setPath("/");
		response.addCookie(ck);
    	for(int i = 0; i < cookie.length; i++) {
    		if(cookie[i].getClass().getName() == "username") {
    			isFirstLogin = false;
    			modelAndView.addObject("username", cookie[i]);
    		}
    	}
//    	if(isFirstLogin) {
//    		String email = request.getParameter("email");
//    		Cookie ck = new Cookie("username", email);
//    		response.addCookie(ck);
//    	}
        modelAndView.setViewName("login");
        return modelAndView;
    }


    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult,
    		HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
//            Cookie cookie = new Cookie("username", user.getEmail());
//            cookie.setPath("/login");
//            cookie.setDomain("localhost");
//            cookie.setMaxAge(60*60*24*24);
//            response.addCookie(cookie);
//            cookie.setPath("/?");
//            cookie.setDomain("localhost");
//            cookie.setMaxAge(60*60*24*24);
//            response.addCookie(cookie);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

    @RequestMapping(value="/admin/home", method = RequestMethod.GET)
    public ModelAndView home(HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        String email = user.getEmail();
		Cookie ck = new Cookie("username", email);
		ck.setPath("/login");
		ck.setDomain("localhost");
		ck.setMaxAge(60*24*24);
		response.addCookie(ck);
        modelAndView.addObject("email", user.getEmail());
        Cookie cookie = new Cookie("email", user.getEmail());
        cookie.setPath("/");
        cookie.setDomain("/");
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }

    @RequestMapping(value="admin/information", method = RequestMethod.GET)
    public ModelAndView info() {
    	ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("email",user.getEmail());
        modelAndView.addObject("user", user.getName() + " " + user.getLastName());
        modelAndView.addObject("role", user.getActive());
        modelAndView.setViewName("admin/information");
    	return modelAndView;
    }

}