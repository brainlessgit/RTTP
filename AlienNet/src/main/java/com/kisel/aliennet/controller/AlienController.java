package com.kisel.aliennet.controller;

/**
 *
 * @author brainless
 */
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/services")
public class AlienController {

    @RequestMapping(value = "/ahello", method = RequestMethod.GET)
    public String hell(ModelMap model) {
        return "index";

    }

    @RequestMapping(value = "/register/{name}/{password}", method = RequestMethod.GET)
    @ResponseBody
    public String register(@PathVariable String name, @PathVariable String password, ModelMap model) {
        return "Ok";

    }

    @RequestMapping(value = "/login/{name}/{password}", method = RequestMethod.GET)
    @ResponseBody
    public String login(@PathVariable String name, @PathVariable String password, ModelMap model) {
        if ("aaa".equals(name) && "bbb".equals(password)) {
            return "Ok";
        } else {
            return "Bad bad";
        }
    }
}
