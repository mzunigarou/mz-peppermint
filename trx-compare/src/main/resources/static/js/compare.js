// Column Model to be used with the DataTables jQuery plugin
var columnModel = [
	{ data: "date" },
	{ data: "reference" },
	{ data: "amount" }
];
// jQuery initialization
$(document).ready(function() {
	// Table used to display results for the first file in the unmatched report
	var theTable1 = $("#tableResults1").DataTable({
		paging: false,
		searching: false,
		info: false,
		columns: columnModel
	});
	// Table used to display results for the second file in the unmatched report
	var theTable2 = $("#tableResults2").DataTable({
		paging: false,
		searching: false,
		info: false,
		columns: columnModel
	});
	// Custom action for the Compare button
	$("#compareButton").click(function(event) {
		event.preventDefault();
		var form = $('#formId')[0];
		var data = new FormData(form);
		$.ajax({
			url: "/compare",
			type: "POST",
			enctype: "multipart/form-data",
			contentType: false,
			processData: false,
			data: data
		}).then(function(data) {
			var jsonResponse = JSON.parse(data);
			if (console) console.log(jsonResponse.message);
			if (jsonResponse.success) {
				$("#uhId1").val(jsonResponse.map1.uhId);
				$("#file1Name").text(jsonResponse.map1.filename);
				$("#file1TotalRecords").text(jsonResponse.map1.totalRecords);
				$("#file1MatchingRecords").text(jsonResponse.map1.matchingRecords);
				$("#file1UnmatchedRecords").text(jsonResponse.map1.unmatchedRecords);
				$("#uhId2").val(jsonResponse.map2.uhId);
				$("#file2Name").text(jsonResponse.map2.filename);
				$("#file2TotalRecords").text(jsonResponse.map2.totalRecords);
				$("#file2MatchingRecords").text(jsonResponse.map2.matchingRecords);
				$("#file2UnmatchedRecords").text(jsonResponse.map2.unmatchedRecords);
				$("#fieldset1Id").show();
			}
			else {
				$("#message").text(jsonResponse.message);
				$("#dialog").dialog({
					dialogClass: "no-close",
					buttons: [{
						text: "OK",
						click: function() {
							$(this).dialog("close");
						}
					}]
				});
			}
		});
	});
	// Custom action for the Unmatched Report button
	$("#form2Id").submit(function(event) {
		event.preventDefault();
		var url = $(this).attr("action");
		var type = $(this).attr("method");
		var data = $(this).serialize();
		$.ajax({
			url: url,
			type: type,
			data: data
		}).then(function(data) {
			var jsonResponse = JSON.parse(data);
			if (console) console.log(jsonResponse.message);
			$("#fieldset2Id").show();
			$("#file1NameR").text(jsonResponse.file1);
			$("#file2NameR").text(jsonResponse.file2);
			theTable1.clear().draw();
			theTable2.clear().draw();
			$.each(jsonResponse.details1, function (i, data) {
				theTable1.row.add(data).draw();
			});
			$.each(jsonResponse.details2, function (i, data) {
				theTable2.row.add(data).draw();
			});
			// Even though the records do not match exactly but a potential match is also possible using the WalletReference and TransactionAmount from the CSV file
			theTable1.rows().every(function (rowIdx, tableLoop, rowLoop) {
				var rowIdx1 = rowIdx;
				var ref = this.data()["reference"];
				var amo = this.data()["amount"];
				theTable2.rows().every(function (rowIdx, tableLoop, rowLoop) {
					if (ref === this.data()["reference"] && amo === this.data()["amount"]) {
						theTable1.rows(rowIdx1).nodes().to$().addClass("highlight");
						theTable2.rows(rowIdx).nodes().to$().addClass("highlight");
					}
				});
			});
		});
	});
});