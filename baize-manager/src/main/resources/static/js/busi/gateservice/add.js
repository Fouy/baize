//以下为添加页面内容
$().ready(function () {
    // 增加校验
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#addForm").validate({
        rules: {
            name: "required",
            serviceCode: "required",
            hosts: "required"
        },
        messages: {
            name: icon + "请输入服务名称",
            serviceCode: icon + "请输入服务编码",
            hosts: icon + "请输入HOSTS"
        }
    });

});

// 提交按钮
function saveAdd() {
    if (!$('#addForm').valid()) {
        toastr.error("请检查表单参数", "参数有误");
        return;
    }

    var data = {};
    data.name = $('#name').val();
    data.serviceCode = $('#serviceCode').val();
    data.hosts = $('#hosts').val();
    $.post("/gateservice/save", data, function (result) {
        if (result.code == '1000') {
            parent.toastr.success("提示信息", result.msg);
            closeWin();
        } else {
            toastr.error("错误信息", result.msg);
        }
    }, 'json');
}

