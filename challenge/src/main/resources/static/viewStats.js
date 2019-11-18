var renderValue = function(value) {
	var textValue = "Unknown";
	if (value == "1") textValue = "Rock";
	else if (value == "2") textValue = "Paper";
	else if (value == "3") textValue = "Scissor";
	return textValue;
};

$(document).ready(function() {
	var theTable = $("#tableResults").DataTable({
		paging: false,
		searching: false,
		info: false,
		ajax: {
			url: "/challenge-api/rockPaperScissors",
			type: "GET",
			dataType: "json",
			success: function(result) {
				var html = "";
				var rounds = 0;
				var firstPlayerWins = 0;
				var secondPlayerWins = 0;
				var draws = 0;
				$.each(result._embedded.rockPaperScissors, function (i, data) {
					html +=
						"<tr>"
							+ "<td>" + renderValue(this.firstPlayer) + "</td>"
							+ "<td>" + renderValue(this.secondPlayer) + "</td>"
							+ "<td>" + renderResultValue(this.resultingRound) + "</td>"
						+ "</tr>";
					rounds = i + 1;
					switch(parseInt(this.resultingRound)) {
						case 1: firstPlayerWins++; break;
						case 2: secondPlayerWins++; break;
						case 3: draws++; break;
					}
				});
				$("#tableTBody").html(html);
				$("#roundsPlayed").text(rounds);
				$("#firstPlayerWins").text(firstPlayerWins);
				$("#secondPlayerWins").text(secondPlayerWins);
				$("#draws").text(draws);
			}
		}
	});
	$("#refreshButton").click(function() {
		theTable.ajax.reload();
	});
});