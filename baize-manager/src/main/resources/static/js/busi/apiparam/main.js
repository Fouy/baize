/*!
 * Copyright 2017 xuefeihu
 * Licensed under the Themeforest Standard Licenses
 */

(function() {

    var apiId = $.getUrlParam('apiId');
    $('#apiId').val(apiId);

    initApiInfo();

    // 初始化 基础参数 表格
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
        pageList: [50, 100],  //可供选择的每页的行数（*）
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
            align: 'left'
        }, {
            field: 'type',
            title: '类型',
            align: 'left',
            formatter:function(value, row, index) {
                if (value && value == 'INT') {
                    return '整数';
                } else if (value && value == 'DECIMAL') {
                    return '小数';
                } else if (value && value == 'CHAR') {
                    return '字符';
                } else if (value && value == 'TIME') {
                    return '时间';
                } else if (value && value == 'BOOLEAN') {
                    return '布尔类型';
                }
            }
        }, {
            field: 'position',
            title: '位置',
            align: 'left',
            formatter:function(value, row, index) {
                if (value && value == 'PATH') {
                    return 'PATH参数';
                } else if (value && value == 'GET') {
                    return 'GET参数';
                } else if (value && value == 'POST') {
                    return 'POST参数';
                } else if (value && value == 'HEAD') {
                    return 'HEAD参数';
                } else if (value && value == 'BODY') {
                    return 'body参数';
                }
            }
        }, {
            field: 'need',
            title: '是否必须',
            align: 'left',
            formatter:function(value, row, index) {
                if (value && value == 'YES') {
                    return '是';
                } else if (value && value == 'NO') {
                    return '否';
                }
            }
        }, {
            field: 'defaultValue',
            title: '默认值',
            align: 'left'
        }, {
            field: 'info',
            title: '描述说明',
            align: 'left'
        }, {
            field: 'status',
            title: '状态',
            align: 'left',
            formatter:function(value, row, index) {
                if (value && value == 'BIND') {
                    return '<span class="label label-running">已绑定</span>';
                } else if (value && value == 'UNBIND') {
                    return '<span class="label label-down">未绑定</span>';
                }
            }
        }, {
            field: 'paramId',
            title: '操作',
            align: 'left',
            formatter:function(value, row, index) {
                var a = '<div class="btn-group">';
                a = a +     '<button data-toggle="dropdown" class="btn btn-success btn-outline btn-xs dropdown-toggle">操作 <span class="caret"></span></button>';
                a = a +     '<ul class="dropdown-menu">';
                a = a +         '<li><a href="javascript:void(0)" onclick=editBaseWin('+value+')>编辑</a></li>';
                a = a +         '<li><a href="javascript:void(0)" onclick=delBaseParamWin('+value+')>删除</a></li>';
                a = a +     '</ul>';
                a = a + '</div>';
                return a;
            }
        }]
    });

    // 初始化 后端映射 表格
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
        pageList: [50, 100],  //可供选择的每页的行数（*）
        url: "/apiparammap/list?mapType=MAP&apiId="+apiId,
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
            align: 'left'
        }, {
            field: 'type',
            title: '类型',
            align: 'left',
            formatter:function(value, row, index) {
                if (value && value == 'INT') {
                    return '整数';
                } else if (value && value == 'DECIMAL') {
                    return '小数';
                } else if (value && value == 'CHAR') {
                    return '字符';
                } else if (value && value == 'TIME') {
                    return '时间';
                } else if (value && value == 'BOOLEAN') {
                    return '布尔类型';
                }
            }
        }, {
            field: 'position',
            title: '位置',
            align: 'left',
            formatter:function(value, row, index) {
                if (value && value == 'PATH') {
                    return 'PATH参数';
                } else if (value && value == 'GET') {
                    return 'GET参数';
                } else if (value && value == 'POST') {
                    return 'POST参数';
                } else if (value && value == 'HEAD') {
                    return 'HEAD参数';
                } else if (value && value == 'BODY') {
                    return 'body参数';
                }
            }
        }, {
            field: 'need',
            title: '是否必须',
            align: 'left',
            formatter:function(value, row, index) {
                if (value && value == 'YES') {
                    return '是';
                } else if (value && value == 'NO') {
                    return '否';
                }
            }
        }, {
            field: 'defaultValue',
            title: '默认值',
            align: 'left'
        }, {
            field: 'info',
            title: '描述说明',
            align: 'left'
        }, {
            field: 'mapId',
            title: '操作',
            align: 'left',
            formatter:function(value, row, index) {
                var a = '<div class="btn-group">';
                a = a +     '<button data-toggle="dropdown" class="btn btn-success btn-outline btn-xs dropdown-toggle">操作 <span class="caret"></span></button>';
                a = a +     '<ul class="dropdown-menu">';
                a = a +         '<li><a href="javascript:void(0)" onclick=editBackWin('+value+',"' + $('#apiId').val() + '")>编辑</a></li>';
                a = a +         '<li><a href="javascript:void(0)" onclick=delBackParamWin('+value+')>删除</a></li>';
                a = a +     '</ul>';
                a = a + '</div>';
                return a;
            }
        }]
    });

    // 初始化 后端自定义 表格
    $('#backConstantParamTable').bootstrapTable({
        method: 'get',
        toolbar: '#backConstantParamToolbar',    //工具按钮用哪个容器
        striped: true,      //是否显示行间隔色
        cache: false,      //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,
        sortable: false,
        sortOrder: "asc",
        pageNumber:1,      //初始化加载第一页，默认第一页
        pageSize: 10,      //每页的记录行数（*）
        pageList: [50, 100],  //可供选择的每页的行数（*）
        url: "/apiparammap/list?mapType=CONSTANT&apiId="+apiId,
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
            align: 'left'
        }, {
            field: 'type',
            title: '类型',
            align: 'left',
            formatter:function(value, row, index) {
                if (value && value == 'INT') {
                    return '整数';
                } else if (value && value == 'DECIMAL') {
                    return '小数';
                } else if (value && value == 'CHAR') {
                    return '字符';
                } else if (value && value == 'TIME') {
                    return '时间';
                } else if (value && value == 'BOOLEAN') {
                    return '布尔类型';
                }
            }
        }, {
            field: 'position',
            title: '位置',
            align: 'left',
            formatter:function(value, row, index) {
                if (value && value == 'PATH') {
                    return 'PATH参数';
                } else if (value && value == 'GET') {
                    return 'GET参数';
                } else if (value && value == 'POST') {
                    return 'POST参数';
                } else if (value && value == 'HEAD') {
                    return 'HEAD参数';
                } else if (value && value == 'BODY') {
                    return 'body参数';
                }
            }
        }, {
            field: 'need',
            title: '是否必须',
            align: 'left',
            formatter:function(value, row, index) {
                if (value && value == 'YES') {
                    return '是';
                } else if (value && value == 'NO') {
                    return '否';
                }
            }
        }, {
            field: 'defaultValue',
            title: '默认值',
            align: 'left'
        }, {
            field: 'info',
            title: '描述说明',
            align: 'left'
        }, {
            field: 'mapId',
            title: '操作',
            align: 'left',
            formatter:function(value, row, index) {
                var a = '<div class="btn-group">';
                a = a +     '<button data-toggle="dropdown" class="btn btn-success btn-outline btn-xs dropdown-toggle">操作 <span class="caret"></span></button>';
                a = a +     '<ul class="dropdown-menu">';
                a = a +         '<li><a href="javascript:void(0)" onclick=editBackConstantWin('+value+',"' + $('#apiId').val() + '")>编辑</a></li>';
                a = a +         '<li><a href="javascript:void(0)" onclick=delBackParamWin('+value+')>删除</a></li>';
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
        content: '/apiparam/add?apiId=' + $('#apiId').val(),
        end: function () {
            searchAll();
        }
    });
}

// 新增 back map 窗口
function addBackParamWin(){
  //页面层
    parent.layer.open({
        type: 2,
        title: '新增',
        skin: 'layui-layer-rim', //加上边框
        area: ['1000px', '650px'], //宽高
        content: '/apiparammap/add?apiId=' + $('#apiId').val(),
        end: function () {
            searchAll();
        }
    });
}

// 新增 back Constant 窗口
function addBackConstantParamWin(){
  //页面层
    parent.layer.open({
        type: 2,
        title: '新增',
        skin: 'layui-layer-rim', //加上边框
        area: ['1000px', '650px'], //宽高
        content: '/apiparammap/constantadd?apiId=' + $('#apiId').val(),
        end: function () {
            searchAll();
        }
    });
}

// 编辑 base param 窗口
function editBaseWin(paramId){
    parent.layer.open({
        type: 2,
        title: '编辑',
        skin: 'layui-layer-rim', //加上边框
        area: ['1000px', '650px'], //宽高
        content: '/apiparam/edit.html?paramId='+paramId,
        end: function () {
            searchAll();
        }
    });
}

// 编辑 back map 窗口
function editBackWin(mapId, apiId){
    parent.layer.open({
        type: 2,
        title: '编辑',
        skin: 'layui-layer-rim', //加上边框
        area: ['1000px', '650px'], //宽高
        content: '/apiparammap/edit.html?mapId='+mapId+'&apiId='+apiId,
        end: function () {
            searchAll();
        }
    });
}

// 编辑 back constant 窗口
function editBackConstantWin(mapId, apiId){
    parent.layer.open({
        type: 2,
        title: '编辑',
        skin: 'layui-layer-rim', //加上边框
        area: ['1000px', '650px'], //宽高
        content: '/apiparammap/constantedit.html?mapId='+mapId+'&apiId='+apiId,
        end: function () {
            searchAll();
        }
    });
}

/**
 * base param 删除
 * @param paramId
 */
function delBaseParamWin(paramId){
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
                param.paramId = paramId;
                $.post("/apiparam/delete", param, function(result) {
                    if (result.code == "1000") {
                        swal("删除成功！", result.msg, "success");
                    } else {
                        swal("删除失败！", result.msg, "error");
                    }
                    searchAll();
                }, 'json');
            }
        });
}

/**
 * back param 删除
 * @param mapId
 */
function delBackParamWin(mapId){
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
                param.mapId = mapId;
                $.post("/apiparammap/delete", param, function(result) {
                    if (result.code == "1000") {
                        swal("删除成功！", result.msg, "success");
                    } else {
                        swal("删除失败！", result.msg, "error");
                    }
                    searchAll();
                }, 'json');
            }
        });
}

// 刷新全部表格
function searchAll() {
    searchBaseParam();
    searchBackParam();
    searchBackConstantParam();
}

// 搜索 base param
function searchBaseParam(){
    var param = {};
    param.apiId = $('#apiId').val();
    $('#baseParamTable').bootstrapTable('refresh',{query : param});
}

// 搜索 back map
function searchBackParam(){
    var param = {};
    param.apiId = $('#apiId').val();
    $('#backParamTable').bootstrapTable('refresh',{query : param});
}

// 搜索 back constant
function searchBackConstantParam(){
    var param = {};
    param.apiId = $('#apiId').val();
    $('#backConstantParamTable').bootstrapTable('refresh',{query : param});
}

