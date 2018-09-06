//以下为添加页面内容
$().ready(function () {
    // 增加校验
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#addForm").validate({
        rules: {
            name: "required",
            type: "required",
            serviceId: "required",
            info: "required"
        },
        messages: {
            name: icon + "请输入分组名称",
            type: icon + "请选择类型",
            serviceId: icon + "请输入服务ID",
            info: icon + "请输入描述说明"
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
    data.type = $('#type').val();
    data.serviceId = $('#serviceId').val();
    data.info = $('#info').val();
    $.post("/apigroup/save", data, function (result) {
        if (result.code == '1000') {
            parent.toastr.success("提示信息", result.msg);
            close();
        } else {
            toastr.error("错误信息", result.msg);
        }
    }, 'json');
}

