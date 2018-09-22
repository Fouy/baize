var needCheck;

//以下为添加页面内容
$().ready(function () {
    var apiId = $.getUrlParam('apiId');
    $('#apiId').val(apiId);

    // 初始化switchery
    var elem = document.querySelector('#need');
    needCheck = new Switchery(elem);

    // 增加校验
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#addForm").validate({
        rules: {
            paramId: "required",
            name: "required",
            position: "required",
            type: "required"
        },
        messages: {
            paramId: icon + "请选择映射参数",
            name: icon + "请输入参数名",
            position: icon + "请选择参数位置",
            type: icon + "请选择类型"
        }
    });

    // 初始化 base param列表
    initBaseParamList();

});

// 初始化 base param列表
function initBaseParamList() {
    // 初始化select
    var paramId = $("#paramId");
    paramId.empty();
    var optAll = $('<option></option>');
    optAll.attr('value', '');
    optAll.append('--请选择--');
    optAll.appendTo(paramId);

    $.get("/apiparam/allunbind", {apiId : $('#apiId').val()}, function (result) {
        var data = eval(result);
        var list = data.data;
        for (var i = 0; i < list.length; i++) {
            var opt = $('<option></option>');
            opt.attr('value', list[i].paramId);
            opt.append(list[i].name);
            opt.appendTo(paramId);
        }
    }, 'json');

    // 设置base param的选择事件
    $('#paramId').change(function(){
        if ($('#paramId').val() && $('#paramId').val() != '') {
            $.post("/apiparam/detail", {paramId : $('#paramId').val()}, function(result) {
                var entity = result.data;
                $('#name').val(entity.name);
                $('#position').val(entity.position);
                $('#type').val(entity.type);
                $('#defaultValue').val(entity.defaultValue);
                $('#info').val(entity.info);
                // 设置是否缓存
                if ('YES' == entity.need) {
                    needCheck.setPosition(true);
                }
            }, 'json');
        }
    })
}

// 提交按钮
function saveAdd() {
    if (!$('#addForm').valid()) {
        toastr.error("请检查表单参数", "参数有误");
        return;
    }

    var data = {};
    data.apiId = $('#apiId').val();
    data.paramId = $('#paramId').val();
    data.name = $('#name').val();
    data.position = $('#position').val();
    data.defaultValue = $('#defaultValue').val();
    data.info = $('#info').val();
    data.mapType = 'MAP';

    $.post("/apiparammap/save", data, function (result) {
        if (result.code == '1000') {
            parent.toastr.success("提示信息", result.msg);
            closeWin();
        } else {
            toastr.error("错误信息", result.msg);
        }
    }, 'json');
}

