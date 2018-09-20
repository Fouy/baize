var needCheck;

//以下为添加页面内容
$().ready(function () {
    var mapId = $.getUrlParam('mapId');
    $('#mapId').val(mapId);
    var apiId = $.getUrlParam('apiId');
    $('#apiId').val(apiId);

    // 初始化switchery
    var elem = document.querySelector('#need');
    needCheck = new Switchery(elem);

    // 增加校验
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#editForm").validate({
        rules: {
            name: "required",
            position: "required",
            type: "required"
        },
        messages: {
            name: icon + "请输入参数名",
            position: icon + "请选择参数位置",
            type: icon + "请选择类型"
        }
    });

    initFormData();

});

/**
 * 初始化表单数据
 */
function initFormData() {
    var mapId = $('#mapId').val();
    $.post("/apiparammap/detail", {mapId: mapId}, function (result) {
        var data = eval(result);
        var entity = data.data;
        if (!entity) {
            return;
        }

        $('#name').val(entity.name);
        $('#position').val(entity.position);
        $('#type').val(entity.type);
        $('#info').val(entity.info);

        // 设置是否必须
        if ('YES' == entity.need) {
            needCheck.setPosition(true);
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
    data.mapId = $('#mapId').val();
    data.name = $('#name').val();
    data.position = $('#position').val();
    data.type = $('#type').val();
    data.info = $('#info').val();
    // 是否缓存转换
    if (needCheck.isChecked()) {
        data.need = 'YES';
    } else {
        data.need = 'NO';
    }

    $.post("/apiparammap/update", data, function (result) {
        if (result.code == '1000') {
            parent.toastr.success("提示信息", result.msg);
            closeWin();
        } else {
            toastr.error("错误信息", result.msg);
        }
    }, 'json');
}

