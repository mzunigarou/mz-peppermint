package sv.zuniga.code.challenge;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.gson.Gson;

import sv.zuniga.code.challenge.obj.RockPaperScissor;
import sv.zuniga.code.challenge.obj.RockPaperScissorConstant;

@SpringBootTest
@AutoConfigureMockMvc
class ChallengeApplicationTests {
	private static final Logger logger = LoggerFactory.getLogger(ChallengeApplicationTests.class);
	private static final String API_URL = "/challenge-api/rockPaperScissors";

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void contextLoads() {
	}

	@Test
	public void apiCalls() throws Exception {
		String sessionId = "ABC123";
		Gson gson = new Gson();
		RockPaperScissor rockPaperScissor = null;
		String jsonEntity = "";
		String itemLink = "";
		MvcResult result = null;

		// TODO: HTTP POST: Save First Item
		rockPaperScissor = new RockPaperScissor();
		rockPaperScissor.setSessionId(sessionId);
		rockPaperScissor.setFirstPlayer(RockPaperScissorConstant.ROCK);
		rockPaperScissor.setSecondPlayer(RockPaperScissorConstant.PAPER);
		rockPaperScissor.setResultingRound("");
		rockPaperScissor.setRoundState("1");
		
		jsonEntity = gson.toJson(rockPaperScissor);
		
		result = mockMvc.perform(MockMvcRequestBuilders.post(API_URL)
			.contentType(MediaType.APPLICATION_JSON)
			.content(jsonEntity)
		).andDo(print()).andExpect(status().isCreated()).andReturn();
		
		itemLink = result.getResponse().getHeader("Location");
		logger.info("Item Link 1: [" + itemLink + "]");
		assertNotNull(itemLink);
		
		// TODO: HTTP POST: Save Second Item
		
		rockPaperScissor = new RockPaperScissor();
		rockPaperScissor.setSessionId("DEF456");
		rockPaperScissor.setFirstPlayer(RockPaperScissorConstant.SCISSOR);
		rockPaperScissor.setSecondPlayer(RockPaperScissorConstant.ROCK);
		rockPaperScissor.setResultingRound("");
		rockPaperScissor.setRoundState("0");
		
		jsonEntity = gson.toJson(rockPaperScissor);
		
		result = mockMvc.perform(MockMvcRequestBuilders.post(API_URL)
			.contentType(MediaType.APPLICATION_JSON)
			.content(jsonEntity)
		).andDo(print()).andExpect(status().isCreated()).andReturn();
		
		itemLink = result.getResponse().getHeader("Location");
		logger.info("Item Link 2: [" + itemLink + "]");
		assertNotNull(itemLink);
		
		// TODO: HTTP GET: Find Second Item by ID
		result = mockMvc.perform(MockMvcRequestBuilders.get(itemLink)
			.accept(MediaType.APPLICATION_JSON)
		).andDo(print()).andExpect(status().isOk()).andReturn();
		
		String jsonResponse = result.getResponse().getContentAsString();
		logger.info("Json Response Item 2: [" + jsonResponse + "]");
		assertNotNull(jsonResponse);
		
		// TODO: HTTP GET: Find Items by SessionId and RoundState (Filtered)
		result = mockMvc.perform(MockMvcRequestBuilders.get(API_URL + "/search/bySessionIdAndRoundState?sessionId=" + sessionId + "&roundState=1")
			.accept(MediaType.APPLICATION_JSON)
		).andDo(print()).andExpect(status().isOk()).andReturn();
		
		jsonResponse = result.getResponse().getContentAsString();
		logger.info("Json Response By SessionId and RoundState: [" + jsonResponse + "]");
		assertNotNull(jsonResponse);
		
		// TODO: HTTP GET: Find All Items
		result = mockMvc.perform(MockMvcRequestBuilders.get(API_URL)
			.accept(MediaType.APPLICATION_JSON)
		).andDo(print()).andExpect(status().isOk()).andReturn();
		
		jsonResponse = result.getResponse().getContentAsString();
		logger.info("Json Response Get All: [" + jsonResponse + "]");
		assertNotNull(jsonResponse);
	}
	
}
