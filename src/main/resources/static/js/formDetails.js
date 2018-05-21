function updataForm(){
	var json = {};
    json['tableName'] = "student";
    json['columns'] = ["id", "name", "sex"];
    $.ajax({
        url: "/table/SubmitExcelData",
        type: "post",
        dataType: "json",
        data:{"data": JSON.stringify(json)},
        success: function (data) {
            if (data&&data.code=="1") {
                window.location.href = "/table/DownloadExcel";
            } else {
                $.dialog.alert("下载失败!");
            }
        },
        error: function (err) {
            $.dialog.alert("下载失败");
        }
    });
}
