//以下为添加页面内容
$().ready(function () {
    $('input[type="file"]').prettyFile();
    // 增加校验
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#addForm").validate({
        rules: {
            name: "required",
            type: "required",
            file: "required",
            version: "required"
        },
        messages: {
            name: icon + "请输入API名称",
            file: icon + "请选择文件",
            type: icon + "请选择类型",
            version: icon + "请输入版本号"
        }
    });

});

// 提交按钮
function saveAdd() {
    if (!$('#addForm').valid()) {
        toastr.error("请检查表单参数", "参数有误");
        return;
    }

    var data = new FormData($("#addForm")[0]);

    $.ajax({
        type: 'POST',
        data: data,
        url: '/component/save',
        processData: false,
        contentType: false,
        async: false,
        success: function (result) {
            if (result.code == '1000') {
                parent.toastr.success("提示信息", result.msg);
                closeWin();
            } else {
                toastr.error("错误信息", result.msg);
            }
        }
    });
}

