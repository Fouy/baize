//以下为添加页面内容
$().ready(function () {

    // 增加校验
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#editForm").validate({
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


    var groupId = $.getUrlParam('groupId');
    $('#groupId').val(groupId);

    $.post("/apigroup/detail", {groupId: groupId}, function (result) {
        var data = eval(result);
        var entity = data.data;
        if (!entity) {
            return;
        }
        $('#name').val(entity.name);
        $('#type').val(entity.type);
        $('#serviceId').val(entity.serviceId);
        $('#info').val(entity.info);
    }, 'json');


});

// 提交按钮
function save() {
    if (!$('#editForm').valid()) {
        toastr.error("编辑", "参数有误");
        return;
    }

    var data = {};
    data.groupId = $('#groupId').val();
    data.name = $('#name').val();
    data.type = $('#type').val();
    data.serviceId = $('#serviceId').val();
    data.info = $('#info').val();
    $.post("/apigroup/update", data, function (result) {
        if (result.code == '1000') {
            parent.toastr.success("提示信息", result.msg);
            close();
        } else {
            toastr.error("错误信息", result.msg);
        }
    }, 'json');
}

