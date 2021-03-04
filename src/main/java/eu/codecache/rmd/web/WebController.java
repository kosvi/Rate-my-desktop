package eu.codecache.rmd.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}
}
