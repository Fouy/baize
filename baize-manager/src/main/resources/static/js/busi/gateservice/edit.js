//以下为添加页面内容
$().ready(function () {

    // 增加校验
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#editForm").validate({
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


    var serviceId = $.getUrlParam('serviceId');
    $('#serviceId').val(serviceId);

    $.post("/gateservice/detail", {serviceId: serviceId}, function (result) {
        var data = eval(result);
        var entity = data.data;
        if (!entity) {
            return;
        }
        $('#name').val(entity.name);
        $('#serviceCode').val(entity.serviceCode);
        $('#hosts').val(entity.hosts);
    }, 'json');


});

// 提交按钮
function saveEdit() {
    if (!$('#editForm').valid()) {
        toastr.error("编辑", "参数有误");
        return;
    }

    var data = {};
    data.serviceId = $('#serviceId').val();
    data.name = $('#name').val();
    data.serviceCode = $('#serviceCode').val();
    data.hosts = $('#hosts').val();
    $.post("/gateservice/update", data, function (result) {
        if (result.code == '1000') {
            parent.toastr.success("提示信息", result.msg);
            closeWin();
        } else {
            toastr.error("错误信息", result.msg);
        }
    }, 'json');
}

