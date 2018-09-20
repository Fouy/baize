//以下为添加页面内容
$().ready(function () {
    // 增加校验
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#addForm").validate({
        rules: {
            engineRoom: "required",
            nodeNo: "required",
            sshAddr: "required",
            sshAccount: "required",
            sshPwd: "required",
            detectIp: "required",
            derectPort: "required"
        },
        messages: {
            engineRoom: icon + "请输入机房名称",
            nodeNo: icon + "请输入节点编码",
            sshAddr: icon + "请输入SSH地址",
            sshAccount: icon + "请输入SSH账号",
            sshPwd: icon + "请输入SSH密码",
            detectIp: icon + "请输入探测IP",
            derectPort: icon + "请输入探测端口"
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
    data.engineRoom = $('#engineRoom').val();
    data.nodeNo = $('#nodeNo').val();
    data.sshAddr = $('#sshAddr').val();
    data.sshAccount = $('#sshAccount').val();
    data.sshPwd = $('#sshPwd').val();
    data.detectIp = $('#detectIp').val();
    data.derectPort = $('#derectPort').val();
    $.post("/gatenode/save", data, function (result) {
        if (result.code == '1000') {
            parent.toastr.success("提示信息", result.msg);
            closeWin();
        } else {
            toastr.error("错误信息", result.msg);
        }
    }, 'json');
}

