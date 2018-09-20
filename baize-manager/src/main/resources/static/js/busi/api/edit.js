var cacheCheck;

//以下为添加页面内容
$().ready(function () {
    // 初始化switchery
    var elem = document.querySelector('#cached');
    cacheCheck = new Switchery(elem);

    // 初始化 chosen select
    initChosenSelect();
    // 初始化apigroup select
    initApiGroup();

    // 增加校验
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#editForm").validate({
        rules: {
            name: "required",
            groupId: "required",
            path: "required",
            methods: "required",
            cached: "required",
            protocol: "required",
            version: "required"
        },
        messages: {
            name: icon + "请输入API名称",
            groupId: icon + "请选择分组",
            path: icon + "请输入路径",
            methods: icon + "请选择请求方式",
            cached: icon + "请选择是否缓存",
            protocol: icon + "请选择协议类型",
            version: icon + "请填写版本号"
        }
    });

    var apiId = $.getUrlParam('apiId');
    $('#apiId').val(apiId);

});

/**
 * 初始化API GROUP
 */
function initApiGroup() {
    // 初始化select
    var groupId = $("#groupId");
    groupId.empty();
    var optAll = $('<option></option>');
    optAll.attr('value', '');
    optAll.append('--请选择--');
    optAll.appendTo(groupId);

    $.get("/apigroup/all", {}, function (result) {
        var data = eval(result);
        var list = data.data;
        for (var i = 0; i < list.length; i++) {
            var opt = $('<option></option>');
            opt.attr('value', list[i].groupId);
            opt.append(list[i].name);
            opt.appendTo(groupId);
        }
        initFormData();
    }, 'json');
}

/**
 * 初始化表单数据
 */
function initFormData() {
    var apiId = $('#apiId').val();
    $.post("/api/detail", {apiId: apiId}, function (result) {
        var data = eval(result);
        var entity = data.data;
        if (!entity) {
            return;
        }

        $('#name').val(entity.name);
        $('#groupId').val(entity.groupId);
        $('#path').val(entity.path);
        $('#version').val(entity.version);
        $('#info').val(entity.info);

        // 设置是否缓存
        if ('YES' == entity.cached) {
            cacheCheck.setPosition(true);
        }
        // 设置请求方法
        var methodArray = entity.methods.split(",");
        $('#methods').val(methodArray);
        $("#methods").trigger("chosen:updated");
        // 设置支持协议
        var protocolArray = entity.protocol.split(",");
        $('#protocol').val(protocolArray);
        $("#protocol").trigger("chosen:updated");

    }, 'json');
}


// 提交按钮
function saveEdit() {
    if (!$('#editForm').valid()) {
        toastr.error("编辑", "参数有误");
        return;
    }

    var data = {};
    data.apiId = $('#apiId').val();
    data.name = $('#name').val();
    data.groupId = $('#groupId').val();
    data.path = $('#path').val();
    data.methods = $('#methods').val();
    data.protocol = $('#protocol').val();
    data.version = $('#version').val();
    data.info = $('#info').val();
    // 请求方式格式转换
    if (!data.methods || data.methods.length == 0) {
        toastr.error("请检查表单参数", "请求方式为空");
        return ;
    }
    var tempMethods = '';
    for (var i = 0; i < data.methods.length; i++) {
        if (i == 0) {
            tempMethods = data.methods[i];
        } else {
            tempMethods = tempMethods + "," + data.methods[i];
        }
    }
    data.methods = tempMethods;
    // 是否缓存转换
    if (cacheCheck.isChecked()) {
        data.cached = 'YES';
    } else {
        data.cached = 'NO';
    }

    // 协议格式转换
    if (!data.protocol || data.protocol.length == 0) {
        toastr.error("请检查表单参数", "支持协议为空");
        return ;
    }
    var tempProtocol = '';
    for (var i = 0; i < data.protocol.length; i++) {
        if (i == 0) {
            tempProtocol = data.protocol[i];
        } else {
            tempProtocol = tempProtocol + "," + data.protocol[i];
        }
    }
    data.protocol = tempProtocol;

    $.post("/api/update", data, function (result) {
        if (result.code == '1000') {
            parent.toastr.success("提示信息", result.msg);
            closeWin();
        } else {
            toastr.error("错误信息", result.msg);
        }
    }, 'json');
}

