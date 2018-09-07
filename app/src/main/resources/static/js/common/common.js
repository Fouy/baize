//以下为修改jQuery Validation插件兼容Bootstrap的方法，没有直接写在插件中是为了便于插件升级
$.validator.setDefaults({
    highlight: function (element) {
        $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
    },
    success: function (element) {
        element.closest('.form-group').removeClass('has-error').addClass('has-success');
    },
    errorElement: "span",
    errorPlacement: function (error, element) {
        if (element.is(":radio") || element.is(":checkbox")) {
            error.appendTo(element.parent().parent().parent());
        } else {
            error.appendTo(element.parent());
        }
    },
    errorClass: "help-block m-b-none",
    validClass: "help-block m-b-none"
});

// 以下为Toastr初始化
toastr.options = {
    "closeButton": true,
    "debug": false,
    "progressBar": true,
    "positionClass": "toast-top-center",
    "onclick": null,
    "showDuration": "400",
    "hideDuration": "1000",
    "timeOut": "7000",
    "extendedTimeOut": "1000",
    "showEasing": "swing",
    "hideEasing": "linear",
    "showMethod": "fadeIn",
    "hideMethod": "fadeOut"
};

// 获取uri参数
$.getUrlParam = function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var location = decodeURI(window.location.search);
    //console.log(location);
    var r = location.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return '';
};

//关闭窗口
function closeWin() {
    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
    parent.layer.close(index); //再执行关闭
}

/**
 * ajax封装
 * url 发送请求的地址
 * data 发送到服务器的数据
 * successfn 成功回调函数
 */
jQuery.bzpost = function (url, data, successfn) {
    var token = "";
    var userInfo = localStorage.getItem("userInfo");
    if (userInfo) {
        userInfo = JSON.parse(userInfo);
        token = userInfo.token;
    }

    $.ajax({
        type: "post",
        data: data,
        url: url,
        beforeSend: function(request) {
            request.setRequestHeader("X-Authorization", 'Bearer ' + token);
        },
        success: function (d) {
            successfn(d);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            if (XMLHttpRequest.status == 403) {
                location.href = '/login.html';
            } else {
                toastr.error("错误信息", XMLHttpRequest.responseText);
            }
        }
    });
};

/**
 * ajax封装
 * url 发送请求的地址
 * data 发送到服务器的数据
 * successfn 成功回调函数
 */
jQuery.bzget = function (url, data, successfn) {
    var token = "";
    var userInfo = localStorage.getItem("userInfo");
    if (userInfo) {
        userInfo = JSON.parse(userInfo);
        token = userInfo.token;
    }

    $.ajax({
        data: data,
        url: url,
        beforeSend: function(request) {
            request.setRequestHeader("X-Authorization", 'Bearer ' + token);
        },
        success: function (d) {
            successfn(d);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            if (XMLHttpRequest.status == 403) {
                location.href = '/login.html';
            } else {
                toastr.error("错误信息", XMLHttpRequest.responseText);
            }
        }
    });
};