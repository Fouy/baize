/*!
 * Copyright 2017 xuefeihu
 * Licensed under the Themeforest Standard Licenses
 */

(function() {
    // 增加校验
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#loginForm").validate({
        rules: {
            username: "required",
            password: "required"
        },
        messages: {
            username: icon + "请输入登录名",
            password: icon + "请输入密码"
        }
    });

})();

/**
 * 登录
 */
function login () {
    if(!$('#loginForm').valid()) {
        toastr.error("请检查表单参数", "参数有误");
        return;
    }

    var data = {};
    data.username = $('#username').val();
    data.password = $('#password').val();
    $.bzpost("/api/auth/login", data, function(result) {
        if(result.code == '1000') {
            parent.toastr.success("提示信息", result.msg);
            localStorage.setItem('userInfo', JSON.stringify(result.data));

            console.log(result.data);

            window.top.location = '/index.html';
        } else {
            toastr.error("错误信息", result.msg);
        }
    }, 'json');

}