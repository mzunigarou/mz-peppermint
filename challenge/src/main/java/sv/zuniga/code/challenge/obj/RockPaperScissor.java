package sv.zuniga.code.challenge.obj;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class RockPaperScissor {
	@Id
	@GeneratedValue
	private Long uid;
	private String sessionId;
	private String firstPlayer;
	private String secondPlayer;
	private String resultingRound;
	private String roundState;

	public RockPaperScissor() {

	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getFirstPlayer() {
		return firstPlayer;
	}

	public void setFirstPlayer(String firstPlayer) {
		this.firstPlayer = firstPlayer;
	}

	public String getSecondPlayer() {
		return secondPlayer;
	}

	public void setSecondPlayer(String secondPlayer) {
		this.secondPlayer = secondPlayer;
	}

	public String getResultingRound() {
		return resultingRound;
	}

	public void setResultingRound(String resultingRound) {
		this.resultingRound = calculateResult(this.firstPlayer, this.secondPlayer);
	}

	public String getRoundState() {
		return roundState;
	}

	public void setRoundState(String roundState) {
		this.roundState = roundState;
	}
	
	private String calculateResult(String firstPlayer, String secondPlayer) {
		String result = "";
		if (firstPlayer.equals(secondPlayer)) result = "3";
		else {
			if (RockPaperScissorConstant.ROCK.equals(firstPlayer) && RockPaperScissorConstant.PAPER.equals(secondPlayer)) result = "2";
			else if (RockPaperScissorConstant.ROCK.equals(firstPlayer) && RockPaperScissorConstant.SCISSOR.equals(secondPlayer)) result = "1";
			else if (RockPaperScissorConstant.PAPER.equals(firstPlayer) && RockPaperScissorConstant.SCISSOR.equals(secondPlayer)) result = "2";
			else if (RockPaperScissorConstant.SCISSOR.equals(firstPlayer) && RockPaperScissorConstant.PAPER.equals(secondPlayer)) result = "1";
			else if (RockPaperScissorConstant.SCISSOR.equals(firstPlayer) && RockPaperScissorConstant.ROCK.equals(secondPlayer)) result = "2";
			else if (RockPaperScissorConstant.PAPER.equals(firstPlayer) && RockPaperScissorConstant.ROCK.equals(secondPlayer)) result = "1";
		}
		return result;
	}

}
