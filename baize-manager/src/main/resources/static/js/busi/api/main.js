/*!
 * Copyright 2017 xuefeihu
 * Licensed under the Themeforest Standard Licenses
 */

(function() {

    initApiGroup();

    // 初始化表格
    $('#exampleTableEvents').bootstrapTable({
        method: 'get',
        toolbar: '#exampleTableEventsToolbar',    //工具按钮用哪个容器
        striped: true,      //是否显示行间隔色
        cache: false,      //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,
        sortable: false,
        sortOrder: "asc",
        pageNumber:1,      //初始化加载第一页，默认第一页
        pageSize: 10,      //每页的记录行数（*）
        pageList: [10, 25, 50, 100],  //可供选择的每页的行数（*）
        url: "/api/list",
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
            title: 'API名称',
            align: 'left',
            formatter:function(value, row, index) {
                return '<i class="fa fa-cube">&nbsp;</i>' + value;
            }
        }, {
            field: 'groupName',
            title: '分组',
            align: 'left'
        }, {
            field: 'path',
            title: '路径',
            align: 'left'
        }, {
            field: 'methods',
            title: '请求方式',
            align: 'left'
        }, {
            field: 'version',
            title: '版本',
            align: 'left'
        }, {
            field: 'cached',
            title: '是否缓存',
            align: 'left',
            formatter:function(value, row, index) {
                if (value && value == 'YES') {
                    return '是';
                } else if (value && value == 'NO') {
                    return '否';
                }
            }
        }, {
            field: 'protocol',
            title: '支持协议',
            align: 'left'
        }, {
            field: 'status',
            title: '状态',
            align: 'left',
            formatter:function(value, row, index) {
                if (value && value == 'ON') {
                    return '<span class="label label-running">已启用</span>';
                } else if (value && value == 'OFF') {
                    return '<span class="label label-down">已停用</span>';
                }
            }
        }, {
            field: 'apiId',
            title: '操作',
            align: 'left',
            formatter:function(value, row, index) {
                //value：当前field的值，即userId
                //row：当前行的数据
                var a = '<div class="btn-group">';
                a = a +     '<button data-toggle="dropdown" class="btn btn-success btn-outline btn-sm dropdown-toggle">更多&nbsp;<span class="caret"></span></button>';
                a = a +     '<ul class="dropdown-menu">';

                if (row.status == 'ON') {
                    a = a +     '<li><a href="javascript:void(0)" onclick=statusWin('+value+',"OFF")><i class="fa fa-toggle-off">&nbsp;</i>停用</a></li>';
                } else if (row.status == 'OFF') {
                    a = a +     '<li><a href="javascript:void(0)" onclick=apiParamWin('+value+')><i class="fa fa-dot-circle-o">&nbsp;</i>参数配置</a></li>';
                    a = a +     '<li><a href="javascript:void(0)" onclick=compWin('+value+')><i class="fa fa-gears">&nbsp;</i>组件配置</a></li>';
                    a = a +     '<li><a href="javascript:void(0)" onclick=editWin('+value+')><i class="fa fa-edit">&nbsp;</i>编辑</a></li>';
                    a = a +     '<li><a href="javascript:void(0)" onclick=delWin('+value+')><i class="fa fa-trash-o">&nbsp;</i>删除</a></li>';
                    a = a +     '<li><a href="javascript:void(0)" onclick=statusWin('+value+',"ON")><i class="fa fa-toggle-on">&nbsp;</i>启用</a></li>';
                }

                a = a +     '</ul>';
                a = a + '</div>';
                return a;
            }
        }]
    });

})();

/**
 * 初始化API GROUP
 */
function initApiGroup() {
    // 初始化select
    var groupId = $("#groupId");
    groupId.empty();
    var optAll = $('<option></option>');
    optAll.attr('value', '');
    optAll.append('全部分组');
    optAll.appendTo(groupId);

    $.get("/apigroup/all", {}, function(result) {
        var data = eval(result);
        var list = data.data;
        for(var i = 0; i < list.length; i++) {
            var opt = $('<option></option>');
            opt.attr('value', list[i].groupId);
            opt.append(list[i].name);
            opt.appendTo(groupId);
        }
        // 设置group默认值
        var groupId0 = $.getUrlParam('groupId');
        if (groupId0 && groupId0 != '') {
            $('#groupId').val(groupId0);
            search();
        }
        // 设置组件ID
        var compId = $.getUrlParam('compId');
        if (compId && compId != '') {
            $('#compId').val(compId);
            $('#api_title').empty();
            $('#api_title').text('组件绑定的API');
            search();
        }

    }, 'json');
}

// 新增窗口
function addWin(){
  //页面层
    parent.layer.open({
        type: 2,
        title: '新增',
        skin: 'layui-layer-rim', //加上边框
        area: ['1000px', '650px'], //宽高
        content: '/api/add',
        end: function () {
            search();
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
            search();
        }
    });
}

// 参数配置窗口
function apiParamWin(apiId){
    var dataUrl = '/apiparam/main',
        dataIndex = '/apiparam/main',
        menuName = 'API参数配置',
        iframeUrl = dataUrl + '?apiId=' + apiId;
    parent.customItem(dataUrl, dataIndex, menuName, iframeUrl);
}

// 组件配置窗口
function compWin(apiId){
    var dataUrl = '/api/compconfig',
        dataIndex = '/api/compconfig',
        menuName = 'API组件配置',
        iframeUrl = dataUrl + '?apiId=' + apiId;
    parent.customItem(dataUrl, dataIndex, menuName, iframeUrl);
}

/**
 * 停启用
 * @param apiId
 * @param status
 */
function statusWin(apiId, status){
    var tip = '';
    if (status == 'ON') {
        tip = '启用';
    } else {
        tip = '停用';
    }

    swal({
            title: "您确定要"+tip+"这条记录吗",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "是的，我要"+tip+"！",
            cancelButtonText: "让我再考虑一下…",
            closeOnConfirm: false,
            closeOnCancel: true
        },
        function (isConfirm) {
            if (isConfirm){
                var param = {};
                param.apiId = apiId;
                param.status = status;
                $.post("/api/option", param, function(result) {
                    if (result.code == "1000") {
                        swal(tip+"成功！", result.msg, "success");
                    } else {
                        swal(tip+"失败！", result.msg, "error");
                    }
                    search();
                }, 'json');
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

// 搜索
function search(){
    var param = {};
    param.compId = $('#compId').val();
    param.name = $('#name').val();
    param.path = $('#path').val();
    param.groupId = $('#groupId').val();
    param.status = $('#status').val();
    $('#exampleTableEvents').bootstrapTable('refresh',{query : param});
}

