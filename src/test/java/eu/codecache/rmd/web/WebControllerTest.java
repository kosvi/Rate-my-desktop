package eu.codecache.rmd.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WebControllerTest {

	@Autowired
	private MockMvc mockMvc;

	/*
	 * This test will test that our "index"-page renders and contains the
	 * "register"-link
	 */
	@Test
	public void testRegisterLink() throws Exception {
		this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Register")));
	}

	/*
	 * Also test that register page loads correctly
	 */
	@Test
	public void testRegisterPage() throws Exception {
		this.mockMvc.perform(get("/register")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Password")));
	}
}
