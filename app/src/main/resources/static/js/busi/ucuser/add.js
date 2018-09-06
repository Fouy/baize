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

// 以下为CKEditor编辑器初始化
var config = {
    extraPlugins: 'codesnippet',
    codeSnippet_theme: 'zenburn',
    height: 356
};
var editor = CKEDITOR.replace('content', config);

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
    // 增加校验
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#addForm").validate({
        rules: {
            title: "required",
            source: "required",
            content: "required",
            token: "required"
        },
        messages: {
            title: icon + "请输入文章标题",
            source: icon + "请输入来源",
            content: icon + "请输入内容",
            token: icon + "请输入口令"
        }
    });

    // 初始化select        
    var type = $("#type");
    type.empty();
    $.post("/articletype/list", {}, function(result){
        var data = eval(result);
        var list = data.data;
        for(var i = 0; i < list.length; i++) {
            var opt = $('<option></option>');
            opt.attr('value', list[i].type_id);
            opt.append(list[i].name);
            opt.appendTo(type);
        }
    }, 'json');

    var tag = $("#tag");
    tag.empty();
    $.post("/tag/list", {}, function(result){
        var data = eval(result);
        var list = data.data;
        for(var i = 0; i < list.length; i++) {
            var opt = $('<option></option>');
            opt.attr('value', list[i].tag_id);
            opt.append(list[i].name);
            opt.appendTo(tag);
        }
    }, 'json');

});

// 关闭窗口
function closeAdd(){
    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
    parent.layer.close(index); //再执行关闭
}

// 提交按钮
function saveAdd(){
    if(!$('#addForm').valid()) {
        toastr.error("请检查表单参数", "参数有误");
        return;
    }
    
    var content = editor.document.getBody().getHtml();
    var data = {};
    data.token = $('#token').val();
    data.hot = $('#hot').val();
    data.type = $('#type').val();
    data.tag = $('#tag').val();
    data.title = $('#title').val();
    data.source = $('#source').val();
    data.content = content;
    $.post("/article/save", data, function(result){
        if(result.success) {
            parent.toastr.success("提示信息", result.msg);
            closeAdd();
        }else {
            toastr.error("错误信息", result.msg);
        }
    }, 'json');
}

