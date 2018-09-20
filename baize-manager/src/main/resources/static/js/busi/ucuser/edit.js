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
}

//以下为添加页面内容
$().ready(function () {
//    // 以下为CKEditor编辑器初始化
//    var config = {
//        extraPlugins: 'codesnippet',
//        codeSnippet_theme: 'zenburn',
//        height: 356
//    };
//    var editor = CKEDITOR.replace('content', config);
    
    // 增加校验
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#userForm").validate({
        rules: {
        	nickName: "required",
        	phoneNo: "required",
        	passwd: "required"
        },
        messages: {
        	nickName: icon + "请输入昵称",
        	phoneNo: icon + "请输入手机号",
        	passwd: icon + "请输入密码"
        }
    });

    
    var userId = $.getUrlParam('userId');
    $('#userId').val(userId);

    $.post("/ucuser/detail", {userId : userId}, function(result){
        var data = eval(result);
        var entity = data.data;
        if(!entity){
        	return;
        }
        $('#phoneNo').val(entity.phoneNo);
        $('#email').val(entity.email);
        $('#status').val(entity.status);
        $('#nickName').val(entity.nickName);

    }, 'json');
    

});

// 关闭窗口
function closeEdit(){
    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
    parent.layer.close(index); //再执行关闭
}

// 提交按钮
function save(){
    if(!$('#userForm').valid()) {
        toastr.error("用户", "参数有误");
        return;
    }
     
    //var content = editor.document.getBody().getHtml();
    var data = {};
    data.userId = $('#userId').val();
    data.nickName = $('#nickName').val();
    data.phoneNo = $('#phoneNo').val();
    data.email = $('#email').val();
    data.passwd = $('#passwd').val();
    data.status = $('#status').val();
    //data.content = content;
    $.post("/ucuser/save", data, function(result){
        if(result.code == '10001') {
            toastr.success("提示信息", result.msg);
            closeEdit();
        }else {
            toastr.error("错误信息", result.msg);
        }
    }, 'json');
}

