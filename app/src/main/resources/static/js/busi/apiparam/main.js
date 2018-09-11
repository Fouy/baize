/*!
 * Copyright 2017 xuefeihu
 * Licensed under the Themeforest Standard Licenses
 */

(function() {

    var apiId = $.getUrlParam('apiId');
    $('#apiId').val(apiId);

    initApiInfo();

    // 初始化表格
    $('#baseParamTable').bootstrapTable({
        method: 'get',
        toolbar: '#baseParamToolbar',    //工具按钮用哪个容器
        striped: true,      //是否显示行间隔色
        cache: false,      //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,
        sortable: false,
        sortOrder: "asc",
        pageNumber:1,      //初始化加载第一页，默认第一页
        pageSize: 10,      //每页的记录行数（*）
        pageList: [10, 25, 50, 100],  //可供选择的每页的行数（*）
        url: "/apiparam/list?apiId=" + apiId,
        queryParamsType:'', //默认值为 'limit' ,在默认情况下 传给服务端的参数为：offset,limit,sort
                            // 设置为 ''  在这种情况下传给服务器的参数为：pageSize,pageNumber
        //queryParams: queryParams,//前端调用服务时，会默认传递上边提到的参数，如果需要添加自定义参数，可以自定义一个函数返回请求参数
        sidePagination: "server",   //分页方式：client客户端分页，server服务端分页（*）
        strictSearch: true,
        //showColumns: true,     //是否显示所有的列
        //showRefresh: true,     //是否显示刷新按钮
        minimumCountColumns: 2,    //最少允许的列数
        clickToSelect: true,    //是否启用点击选中行
        searchOnEnterKey: true,
        columns: [{
            field: 'name',
            title: '参数名',
            align: 'center'
        }, {
            field: 'type',
            title: '类型',
            align: 'center'
        }, {
            field: 'position',
            title: '位置',
            align: 'center'
        }, {
            field: 'need',
            title: '是否必须',
            align: 'center'
        }, {
            field: 'info',
            title: '描述说明',
            align: 'center'
        }, {
            field: 'paramId',
            title: '操作',
            align: 'center',
            formatter:function(value, row, index) {
                var a = '<div class="btn-group">';
                a = a +     '<button data-toggle="dropdown" class="btn btn-success btn-outline btn-xs dropdown-toggle">操作 <span class="caret"></span></button>';
                a = a +     '<ul class="dropdown-menu">';
                a = a +         '<li><a href="javascript:void(0)" onclick=editWin('+value+')>编辑</a></li>';
                a = a +         '<li><a href="javascript:void(0)" onclick=delWin('+value+')>删除</a></li>';
                a = a +     '</ul>';
                a = a + '</div>';
                return a;
            }
        }]
    });

    // 初始化表格
    $('#backParamTable').bootstrapTable({
        method: 'get',
        toolbar: '#backParamToolbar',    //工具按钮用哪个容器
        striped: true,      //是否显示行间隔色
        cache: false,      //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,
        sortable: false,
        sortOrder: "asc",
        pageNumber:1,      //初始化加载第一页，默认第一页
        pageSize: 10,      //每页的记录行数（*）
        pageList: [10, 25, 50, 100],  //可供选择的每页的行数（*）
        url: "/apiparammap/list?apiId="+apiId,
        queryParamsType:'', //默认值为 'limit' ,在默认情况下 传给服务端的参数为：offset,limit,sort
                            // 设置为 ''  在这种情况下传给服务器的参数为：pageSize,pageNumber
        //queryParams: queryParams,//前端调用服务时，会默认传递上边提到的参数，如果需要添加自定义参数，可以自定义一个函数返回请求参数
        sidePagination: "server",   //分页方式：client客户端分页，server服务端分页（*）
        strictSearch: true,
        //showColumns: true,     //是否显示所有的列
        //showRefresh: true,     //是否显示刷新按钮
        minimumCountColumns: 2,    //最少允许的列数
        clickToSelect: true,    //是否启用点击选中行
        searchOnEnterKey: true,
        columns: [{
            field: 'name',
            title: '参数名',
            align: 'center'
        }, {
            field: 'type',
            title: '类型',
            align: 'center'
        }, {
            field: 'position',
            title: '位置',
            align: 'center'
        }, {
            field: 'need',
            title: '是否必须',
            align: 'center'
        }, {
            field: 'info',
            title: '描述说明',
            align: 'center'
        }, {
            field: 'paramId',
            title: '操作',
            align: 'center',
            formatter:function(value, row, index) {
                var a = '<div class="btn-group">';
                a = a +     '<button data-toggle="dropdown" class="btn btn-success btn-outline btn-xs dropdown-toggle">操作 <span class="caret"></span></button>';
                a = a +     '<ul class="dropdown-menu">';
                a = a +         '<li><a href="javascript:void(0)" onclick=editWin('+value+')>编辑</a></li>';
                a = a +         '<li><a href="javascript:void(0)" onclick=delWin('+value+')>删除</a></li>';
                a = a +     '</ul>';
                a = a + '</div>';
                return a;
            }
        }]
    });


})();

/**
 * 初始化apiInfo
 */
function initApiInfo() {
    var apiId = $('#apiId').val();
    $.post("/api/detail", {apiId: apiId}, function (result) {
        var data = eval(result);
        var entity = data.data;
        if (!entity) {
            return;
        }

        $('#name').text(entity.name);
        $('#groupName').text(entity.groupName);
        $('#path').text(entity.path);
        $('#methods').text(entity.methods);
        $('#protocol').text(entity.protocol);
        $('#info').text(entity.info);

        if (entity.status == 'ON') {
            $('#status').html('<span class="label label-running">已启用</span>');
        } else if(entity.status == 'OFF') {

            $('#status').html('<span class="label label-down">已停用</span>');
        }
        $('#version').text(entity.version);
        if (entity.cached == 'YES') {
            $('#cached').text('是');
        } else if(entity.cached == 'NO') {
            $('#cached').text('否');
        }
        $('#createTime').text(entity.createTime);
        $('#modifyTime').text(entity.modifyTime);

    }, 'json');


}

// 新增 base param 窗口
function addBaseParamWin(){
  //页面层
    parent.layer.open({
        type: 2,
        title: '新增',
        skin: 'layui-layer-rim', //加上边框
        area: ['1000px', '650px'], //宽高
        content: '/apiparam/add',
        end: function () {
            search();
        }
    });
}

// 新增 back param 窗口
function addBackParamWin(){
  //页面层
    parent.layer.open({
        type: 2,
        title: '新增',
        skin: 'layui-layer-rim', //加上边框
        area: ['1000px', '650px'], //宽高
        content: '/apiparammap/add',
        end: function () {
            searchBaseParam();
        }
    });
}

// 编辑窗口
function editWin(apiId){
    parent.layer.open({
        type: 2,
        title: '编辑',
        skin: 'layui-layer-rim', //加上边框
        area: ['1000px', '650px'], //宽高
        content: '/api/edit.html?apiId='+apiId,
        end: function () {
            searchBackParam();
        }
    });
}

/**
 * 删除
 * @param apiId
 */
function delWin(apiId){
    swal({
            title: "您确定要删除这条记录吗",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "是的，我要删除！",
            cancelButtonText: "让我再考虑一下…",
            closeOnConfirm: false,
            closeOnCancel: true
        },
        function (isConfirm) {
            if (isConfirm){
                var param = {};
                param.apiId = apiId;
                $.post("/api/delete", param, function(result) {
                    if (result.code == "1000") {
                        swal("删除成功！", result.msg, "success");
                    } else {
                        swal("删除失败！", result.msg, "error");
                    }
                    search();
                }, 'json');
            }
        });
}

// 搜索 base param
function searchBaseParam(){
    var param = {};
    param.apiId = $('#apiId').val();
    $('#baseParamTable').bootstrapTable('refresh',{query : param});
}

// 搜索 back param
function searchBackParam(){
    var param = {};
    param.apiId = $('#apiId').val();
    $('#backParamTable').bootstrapTable('refresh',{query : param});
}

