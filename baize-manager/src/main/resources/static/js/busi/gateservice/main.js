/*!
 * Copyright 2017 xuefeihu
 * Licensed under the Themeforest Standard Licenses
 */

(function() {
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
        url: "/gateservice/list",
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
            title: '服务名',
            align: 'left',
            formatter:function(value, row, index) {
                return '<i class="fa fa-user">&nbsp;</i>' + value;
            }
        }, {
            field: 'serviceCode',
            title: '服务编码',
            align: 'left'
        }, {
            field: 'hosts',
            title: '后端服务',
            align: 'left'
        }, {
            field: 'status',
            title: '状态',
            align: 'left',
            formatter:function(value, row, index) {
                if (value && value == 'ON') {
                    return '<span class="label label-running">已上线</span>';
                } else if (value && value == 'OFF') {
                    return '<span class="label label-down">已下线</span>';
                }
            }
        }, {
            field: 'serviceId',
            title: '操作',
            align: 'left',
            formatter:function(value, row, index) {
                var a = '<div class="btn-group">';
                a = a +     '<button data-toggle="dropdown" class="btn btn-success btn-outline btn-sm dropdown-toggle">更多&nbsp;<span class="caret"></span></button>';
                a = a +     '<ul class="dropdown-menu">';

                if (row.status == 'OFF') {
                    a = a +     '<li><a href="javascript:void(0)" onclick=statusWin('+value+',"ON")><i class="fa fa-arrow-up">&nbsp;</i>上线</a></li>';
                    a = a +     '<li><a href="javascript:void(0)" onclick=editWin('+value+')><i class="fa fa-edit">&nbsp;</i>编辑</a></li>';
                    a = a +     '<li><a href="javascript:void(0)" onclick=delWin('+value+')><i class="fa fa-trash-o">&nbsp;</i>删除</a></li>';
                } else if (row.status == 'ON') {
                    a = a +     '<li><a href="javascript:void(0)" onclick=instanceWin('+value+')><i class="fa fa-eye">&nbsp;</i>查看实例</a></li>';
                    a = a +     '<li><a href="javascript:void(0)" onclick=statusWin('+value+',"OFF")><i class="fa fa-arrow-down">&nbsp;</i>下线</a></li>';
                }

                a = a +     '</ul>';
                a = a + '</div>';
                return a;
            }
        }]
    });

})();

// 新增窗口
function addWin(){
  //页面层
    parent.layer.open({
        type: 2,
        title: '新增',
        skin: 'layui-layer-rim', //加上边框
        area: ['1000px', '650px'], //宽高
        content: '/gateservice/add',
        end: function () {
            search();
        }
    });
}

// 编辑窗口
function editWin(serviceId){
    parent.layer.open({
        type: 2,
        title: '编辑',
        skin: 'layui-layer-rim', //加上边框
        area: ['1000px', '650px'], //宽高
        content: '/gateservice/edit.html?serviceId='+serviceId,
        end: function () {
            search();
        }
    });
}

// 编辑窗口
function instanceWin(serviceId){
    parent.layer.open({
        type: 2,
        title: '服务实例',
        skin: 'layui-layer-rim', //加上边框
        area: ['1000px', '650px'], //宽高
        content: '/gateinstance/main.html?serviceId='+serviceId,
        end: function () {
            search();
        }
    });
}

/**
 * 删除
 * @param serviceId
 */
function delWin(serviceId){
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
                param.serviceId = serviceId;
                $.post("/gateservice/delete", param, function(result) {
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

/**
 * 停启用
 * @param serviceId
 * @param status
 */
function statusWin(serviceId, status){
    var tip = '';
    if (status == 'ON') {
        tip = '上线';
    } else {
        tip = '下线';
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
                param = {};
                param.serviceId = serviceId;
                param.status = status;
                $.post("/gateservice/option", param, function(result) {
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

// 搜索
function search(){
    var param = {};
    param.name = $('#name').val();
    param.status = $('#status').val();
    $('#exampleTableEvents').bootstrapTable('refresh',{query : param});
}

