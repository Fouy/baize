var needCheck;

//以下为添加页面内容
$().ready(function () {
    // 初始化switchery
    var elem = document.querySelector('#need');
    needCheck = new Switchery(elem);

    // 增加校验
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#addForm").validate({
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

    var apiId = $.getUrlParam('apiId');
    $('#apiId').val(apiId);

});

// 提交按钮
function saveAdd() {
    if (!$('#addForm').valid()) {
        toastr.error("请检查表单参数", "参数有误");
        return;
    }

    var data = {};
    data.apiId = $('#apiId').val();
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

    $.post("/apiparam/save", data, function (result) {
        if (result.code == '1000') {
            parent.toastr.success("提示信息", result.msg);
            closeWin();
        } else {
            toastr.error("错误信息", result.msg);
        }
    }, 'json');
}

