/*!
 * Copyright 2017 xuefeihu
 * Licensed under the Themeforest Standard Licenses
 */

(function() {
    var evaluateId = $.getUrlParam("evaluateId");
    $.get("/evaluate/reply/detail",{evaluateId:evaluateId},function (data) {
        if (data.code == "10001") {
            var html = template('templateId', data.data);
            document.getElementById("contentTemplate").innerHTML = html;
        }

    },"json");

    $("#saveBtn").on("click", function () {
        var content = $("#replyContent").val();
        var evaluateReplyId = $("#evaluateReplyId").val();
        $.post("/evaluate/reply/add",{evaluateId:evaluateId,content:content,evaluateReplyId:evaluateReplyId},function (data) {
            console.log(data);
            if(data.code == "10001") {
                swal({
                        title: "提示",
                        text: "操作成功",
                        type: "success",
                        showCancelButton: false,
                        confirmButtonColor: "#DD6B55",
                        confirmButtonText: "确定",
                        closeOnConfirm: false,
                    },
                    function (isConfirm) {
                        if (isConfirm){
                            parent.layer.close(parent.layer.index);
                        }
                    });
            }
        },'json');

    });

    $("#cancleBtn").on("click", function () {
        $("#replyContent").val('');
        parent.layer.close(parent.layer.index);
    })

})();
