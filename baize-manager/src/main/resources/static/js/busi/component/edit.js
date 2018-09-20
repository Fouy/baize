//以下为添加页面内容
$().ready(function () {
    $('input[type="file"]').prettyFile();
    // 增加校验
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#editForm").validate({
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

    var compId = $.getUrlParam('compId');
    $('#compId').val(compId);

    initFormData();

});

/**
 * 初始化表单数据
 */
function initFormData() {
    var compId = $('#compId').val();
    $.post("/component/detail", {compId: compId}, function (result) {
        var data = eval(result);
        var entity = data.data;
        if (!entity) {
            return;
        }

        $('#name').val(entity.name);
        $('#type').val(entity.type);
        $('#version').val(entity.version);
        $('#info').val(entity.info);
    }, 'json');
}


// 提交按钮
function saveEdit() {
    if (!$('#editForm').valid()) {
        toastr.error("编辑", "参数有误");
        return;
    }

    var data = new FormData($("#editForm")[0]);

    $.ajax({
        type: 'POST',
        data: data,
        url: '/component/update',
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

