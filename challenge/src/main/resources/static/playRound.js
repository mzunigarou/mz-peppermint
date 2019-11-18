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
			url: "/challenge-api/rockPaperScissors/search/bySessionId?sessionId=" + $("#sessionId").val(),
			type: "GET",
			dataType: "json",
			success: function(result) {
				var html = "";
				var rounds = 0;
				$.each(result._embedded.rockPaperScissors, function (i, data) {
					html +=
						"<tr>"
							+ "<td>" + renderValue(this.firstPlayer) + "</td>"
							+ "<td>" + renderValue(this.secondPlayer) + "</td>"
							+ "<td>" + this.resultingRound + "</td>"
						+ "</tr>";
					rounds = i + 1;
				});
				$("#tableTBody").html(html);
				$("#roundsPlayed").text("Rounds Played: " + rounds);
			}
		}
	});
	$("#playButton").click(function() {
		var x1 = $('#firstPlayerMode').val() === "4" ? Math.floor(Math.random() * 3) + 1 : parseInt($('#firstPlayerMode').val());
		var x2 = $('#secondPlayerMode').val() === "4" ? Math.floor(Math.random() * 3) + 1 : parseInt($('#secondPlayerMode').val());
		$.ajax({
			url: "/challenge-api/rockPaperScissors",
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify({
				sessionId: $("#sessionId").val(),
				firstPlayer: x1,
				secondPlayer: x2,
				resultingRound: ""
			})
		}).then(function(data) {
			$("#winner").text(data.resultingRound);
			theTable.ajax.reload();
		});
	});
	$("#restartButton").click(function() {
		$.post("/restart", function(data) {
			if (console) console.log(data.message);
			theTable.ajax.reload();
		});
	});
});