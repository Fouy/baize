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
        url: "/goodscategory/list?level=1",
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
            field: 'categoryName',
            title: '类目名称',
            align: 'center'
        }, {
            field: 'logoUrl',
            title: 'LOGO',
            align: 'center',
            formatter:function(value, row, index){
                if (value && value != '') {
                    var a = '<img src='+value+' style="height: 50px;" />';
                    return a;
                }
            }
        }, {
            field: 'level',
            title: '层级',
            align: 'center'
        }, {
            field: 'cateTree',
            title: '树形结构',
            align: 'center'
        }, {
            field: 'order',
            title: '排序',
            align: 'center'
        }, {
            field: 'status',
            title: '状态',
            align: 'center',
            formatter:function(value, row, index){
                if (value && value == 'ON') {
                    return '启用';
                } else if (value && value == 'OFF') {
                    return '<span style="color: red;">停用</span>';
                }
            }
        }, {
            field: 'categoryId',
            title: '操作',
            align: 'center',
            formatter:function(value, row, index){
                //value：当前field的值，即userId
                //row：当前行的数据
                var a = '<div class="btn-group">';
                a = a +     '<button data-toggle="dropdown" class="btn btn-success btn-outline dropdown-toggle">操作 <span class="caret"></span></button>';
                a = a +     '<ul class="dropdown-menu">';
                a = a +         '<li><a href="javascript:void(0)" onclick=editWin('+value+')>编辑</a></li>';

                if (row.status == 'OFF') {
                    a = a +     '<li><a href="javascript:void(0)" onclick=statusWin('+value+',"ON")>启用</a></li>';
                } else if (row.status == 'ON') {
                    a = a +     '<li><a href="javascript:void(0)" onclick=statusWin('+value+',"OFF")>停用</a></li>';
                }

                //a = a +         '<li><a href="javascript:void(0)" onclick=fieldWin('+value+')>属性</a></li>';
                a = a +         '<li><a href="javascript:void(0)" onclick=pictureWin('+value+')>上传图片</a></li>';
                a = a +         '<li><a href="javascript:void(0)" onclick=childWin('+value+')>子类目</a></li>';
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
        content: '/goodscategory/add',
        end: function () {
            search();
        }
    });
}

// 编辑窗口
function editWin(categoryId){
    parent.layer.open({
        type: 2,
        title: '编辑',
        skin: 'layui-layer-rim', //加上边框
        area: ['1000px', '650px'], //宽高
        content: '/goodscategory/edit.html?categoryId='+categoryId,
        end: function () {
            search();
        }
    });
}

// 图片窗口
function pictureWin(categoryId){
    parent.layer.open({
        type: 2,
        title: '图片上传',
        skin: 'layui-layer-rim', //加上边框
        area: ['1000px', '650px'], //宽高
        content: '/goodscategory/picture.html?categoryId='+categoryId,
        end: function () {
            search();
        }
    });
}

// 子类目窗口
function childWin(parentId){
    //页面层
    parent.layer.open({
        type: 2,
        title: '子类目',
        skin: 'layui-layer-rim', //加上边框
        area: ['1000px', '650px'], //宽高
        content: '/goodscategory/child.html?parentId='+parentId,
        end: function () {
            search();
        }
    });
}

// 属性管理窗口
function fieldWin(categoryId){
    //页面层
    parent.layer.open({
        type: 2,
        title: '子类目',
        skin: 'layui-layer-rim', //加上边框
        area: ['1000px', '650px'], //宽高
        content: '/goodscategoryfield/main?categoryId='+categoryId,
        end: function () {
            search();
        }
    });
}

/**
 * 停启用
 * @param categoryId
 * @param status
 */
function statusWin(categoryId, status){
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
                param = {};
                param.categoryId = categoryId;
                param.status = status;
                $.post("/goodscategory/option", param, function(result) {
                    if (result.code == "10001") {
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
    param.categoryName = $('#categoryName').val();
    param.categoryCode = $('#categoryCode').val();
    $('#exampleTableEvents').bootstrapTable('refresh',{query : param});
}

