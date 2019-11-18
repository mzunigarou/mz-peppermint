var renderValue = function(value) {
	var textValue = "Unknown";
	if (value == "1") textValue = "Rock";
	else if (value == "2") textValue = "Paper";
	else if (value == "3") textValue = "Scissor";
	return textValue;
};

var renderResultValue = function(value) {
	var textValue = "Unknown";
	if (value == "1") textValue = "First Player Wins";
	else if (value == "2") textValue = "Second Player Wins";
	else if (value == "3") textValue = "Draw";
	return textValue;
};