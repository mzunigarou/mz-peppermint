package sv.zuniga.code.challenge;

import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

import sv.zuniga.code.challenge.obj.RockPaperScissor;
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/stats")
	public String stats() {
		return "stats";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/restart")
	public void restart(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Iterable<RockPaperScissor> list = repository.findBySessionIdAndRoundState((String) request.getSession().getAttribute("sessionId"), "1");
		for (Iterator<RockPaperScissor> iterator = list.iterator(); iterator.hasNext();) {
			RockPaperScissor object = (RockPaperScissor) iterator.next();
			object.setRoundState("0");
			repository.save(object);
		}
		HashMap<String, Object> map = new HashMap<>();
		map.put("success", true);
		map.put("message", "Data has been cleared!");
		Gson gson = new Gson();
		response.getOutputStream().write(gson.toJson(map).getBytes());	
	}
	
}
