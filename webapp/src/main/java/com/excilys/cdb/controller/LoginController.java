package main.java.com.excilys.cdb.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 
@Controller
public class LoginController {
 
    @RequestMapping(value = "/")
    public String welcome(Model model) {
        model.addAttribute("name", "Home Page");
        model.addAttribute("description", "unsecured page !");
        return "home";
 
    }
 
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(ModelMap model) {
 
        return "login";
 
    }
 
    @RequestMapping(value="/logout", method = RequestMethod.POST)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){    
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
    }
 
    @RequestMapping(value = "/loginError", method = RequestMethod.GET)
    public String loginError(ModelMap model) {
        model.addAttribute("error", true);
        return "login";
 
    }
}