package sv.zuniga.code.challenge;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

import sv.zuniga.code.challenge.obj.RockPaperScissorDataRepository;

@Controller
public class RockPaperScissorController {
	@Resource
	private RockPaperScissorDataRepository repository;
	
	@RequestMapping(value = "/")
	public String home() {
		return "home";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/play")
	public String play(HttpServletRequest request, @RequestParam(required = false) String firstPlayerMode, @RequestParam(required = false) String userName) {
		String sessionId = request.getSession().getId();
		String secondPlayerMode = "4".equals(firstPlayerMode) ? "1" : "4";
		request.getSession().setAttribute("sessionId", sessionId);
		request.getSession().setAttribute("userName", userName);
		request.getSession().setAttribute("firstPlayerMode", firstPlayerMode);
		request.getSession().setAttribute("secondPlayerMode", secondPlayerMode);
		return "play";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/restart")
	public void restart(HttpServletRequest request, HttpServletResponse response) throws Exception {
		repository.deleteBySessionId((String) request.getSession().getAttribute("sessionId"));
		HashMap<String, Object> map = new HashMap<>();
		map.put("success", true);
		map.put("message", "Data has been cleared!");
		Gson gson = new Gson();
		response.getOutputStream().write(gson.toJson(map).getBytes());	
	}
	
}
