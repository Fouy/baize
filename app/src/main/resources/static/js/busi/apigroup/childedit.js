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
    
    // 增加校验
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#editForm").validate({
        rules: {
            categoryName: "required",
            //categoryCode: "required",
            order: "required"
        },
        messages: {
            categoryName: icon + "请输入类目名称",
            //categoryCode: icon + "请输入类目编码",
            order: icon + "请输入排序"
        }
    });

    
    var categoryId = $.getUrlParam('categoryId');
    $('#categoryId').val(categoryId);

    $.post("/goodscategory/detail", {categoryId : categoryId}, function(result){
        var data = eval(result);
        var entity = data.data;
        if(!entity){
        	return;
        }
        $('#categoryName').val(entity.categoryName);
        //$('#categoryCode').val(entity.categoryCode);
        $('#order').val(entity.order);
    }, 'json');
    

});

// 关闭窗口
function closeEdit(){
    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
    parent.layer.close(index); //再执行关闭
}

// 提交按钮
function save(){
    if(!$('#editForm').valid()) {
        toastr.error("类目编辑", "参数有误");
        return;
    }
     
    var data = {};
    data.categoryId = $('#categoryId').val();
    data.categoryName = $('#categoryName').val();
    //data.categoryCode = $('#categoryCode').val();
    data.order = $('#order').val();
    $.post("/goodscategory/update", data, function(result){
        if(result.code == '10001') {
            toastr.success("提示信息", result.msg);
            closeEdit();
        }else {
            toastr.error("错误信息", result.msg);
        }
    }, 'json');
}

