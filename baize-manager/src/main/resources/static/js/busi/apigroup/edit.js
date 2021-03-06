//以下为添加页面内容
$().ready(function () {

    // 增加校验
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#editForm").validate({
        rules: {
            name: "required",
            type: "required",
            path: "required",
            serviceId: "required",
            info: "required"
        },
        messages: {
            name: icon + "请输入分组名称",
            type: icon + "请选择类型",
            path: icon + "请输入路径",
            serviceId: icon + "请输入服务ID",
            info: icon + "请输入描述说明"
        }
    });

    initGateService();

    var groupId = $.getUrlParam('groupId');
    $('#groupId').val(groupId);

});

/**
 * 初始化网关服务
 */
function initGateService() {
    // 初始化select
    var serviceId = $("#serviceId");
    serviceId.empty();
    var optAll = $('<option></option>');
    optAll.attr('value', '');
    optAll.append('--请选择--');
    optAll.appendTo(serviceId);

    $.post("/gateservice/all", {}, function(result) {
        if (result.code == '1000') {
            var list = result.data;
            for(var i = 0; i < list.length; i++) {
                var opt = $('<option></option>');
                opt.attr('value', list[i].serviceId);
                opt.append(list[i].name);
                opt.appendTo(serviceId);
            }

            $.post("/apigroup/detail", {groupId: $('#groupId').val()}, function (result) {
                if (result.code == '1000') {
                    var entity = result.data;
                    if (!entity) {
                        return;
                    }
                    $('#name').val(entity.name);
                    $('#type').val(entity.type);
                    $('#path').val(entity.path);
                    $('#serviceId').val(entity.serviceId);
                    $('#info').val(entity.info);
                } else {
                    toastr.error("错误信息", result.msg);
                }
            }, 'json');
        } else {
            toastr.error("错误信息", result.msg);
        }
    }, 'json');
}

// 提交按钮
function saveEdit() {
    if (!$('#editForm').valid()) {
        toastr.error("编辑", "参数有误");
        return;
    }

    var data = {};
    data.groupId = $('#groupId').val();
    data.name = $('#name').val();
    data.type = $('#type').val();
    data.path = $('#path').val();
    data.serviceId = $('#serviceId').val();
    data.info = $('#info').val();
    $.post("/apigroup/update", data, function (result) {
        if (result.code == '1000') {
            parent.toastr.success("提示信息", result.msg);
            closeWin();
        } else {
            toastr.error("错误信息", result.msg);
        }
    }, 'json');
}

