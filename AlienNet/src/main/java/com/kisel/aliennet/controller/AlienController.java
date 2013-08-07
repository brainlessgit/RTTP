package com.kisel.aliennet.controller;

/**
 *
 * @author brainless
 */
import com.kisel.aliennet.model.Alien;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AlienController {

    @Autowired
    com.kisel.client.controller.Controller distAppController;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showFrom(ModelMap model) {
        Alien alien = new Alien();
        model.addAttribute("alien", alien);
        return "registration";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String processForm(@ModelAttribute("alien") Alien alien, ModelMap model) {
        boolean res = distAppController.register(alien);
        if (res) {
            return "Ok";
        }
        return "Not ok";
    }

    @RequestMapping(value = "/login/{name}/{password}", method = RequestMethod.GET)
    @ResponseBody
    public String login(@PathVariable String name, @PathVariable String password, ModelMap model) {
        Alien alien = distAppController.auth(name, password);
        if (alien != null) {
            return "Ok. Your credits: " + alien.getName();
        }
        return "bad :(";
    }

    @RequestMapping(value = "search/{name}", method = RequestMethod.GET)
    public String search(@PathVariable String name, ModelMap model) {
        model.addAttribute("aliens", distAppController.search(name));
        return "../list";
    }
}
