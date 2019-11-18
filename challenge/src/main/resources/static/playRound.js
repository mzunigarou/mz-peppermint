$(document).ready(function() {
	var theTable = $("#tableResults").DataTable({
		paging: false,
		searching: false,
		info: false,
		ajax: {
			url: "/challenge-api/rockPaperScissors/search/bySessionIdAndRoundState?sessionId=" + $("#sessionId").val() + "&roundState=1",
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
							+ "<td>" + renderResultValue(this.resultingRound) + "</td>"
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
				resultingRound: "",
				roundState: "1"
			})
		}).then(function(data) {
			$("#winner").text(renderResultValue(data.resultingRound));
			theTable.ajax.reload();
		});
	});
	$("#restartButton").click(function() {
		$.post("/restart", function(data) {
			$("#winner").text("");
			if (console) console.log(JSON.parse(data).message);
			theTable.ajax.reload();
		});
	});
});