/*!
 * Copyright 2017 xuefeihu
 * Licensed under the Themeforest Standard Licenses
 */

(function() {
    var categoryId = $.getUrlParam('categoryId');
    $('#categoryId').val(categoryId);
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
        url: "/goodscategoryfield/list?categoryId="+categoryId,
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
            field: 'categoryFieldId',
            title: '属性ID',
            align: 'center'
        }, {
            field: 'fieldName',
            title: '属性名称',
            align: 'center'
        }, {
            field: 'fieldCode',
            title: '属性编码',
            align: 'center'
        }, {
            field: 'order',
            title: '排序',
            align: 'center'
        }, {
            field: 'required',
            title: '是否必填',
            align: 'center',
            formatter:function(value, row, index) {
                var realValue = '';
                if (value == 'YES') {
                    realValue = '必填';
                } else if (value == 'NO') {
                    realValue = '非必填';
                } else if (value == 'PICTURE') {
                    realValue = '必填图片';
                }
                return realValue;
            }
        }, {
            field: 'categoryFieldId',
            title: '操作',
            align: 'center',
            formatter:function(value, row, index){
                //value：当前field的值，即userId
                //row：当前行的数据
                var a = '<button class="btn btn-info btn-sm" type="button" onclick=editWin('+value+')><i class="fa fa-edit"></i> 编辑</button>';
                return a;
            }
        }]
    });

})();

// 新增窗口
function addWin(){
    var categoryId = $('#categoryId').val();
    //页面层
    parent.layer.open({
        type: 2,
        title: '新增',
        skin: 'layui-layer-rim', //加上边框
        area: ['1000px', '650px'], //宽高
        content: '/goodscategoryfield/add?categoryId='+categoryId,
        end: function () {
            search();
        }
    });
}

// 编辑窗口
function editWin(categoryFieldId){
    parent.layer.open({
        type: 2,
        title: '编辑',
        skin: 'layui-layer-rim', //加上边框
        area: ['1000px', '650px'], //宽高
        content: '/goodscategoryfield/edit.html?categoryFieldId='+categoryFieldId,
        end: function () {
            search();
        }
    });
}

// 搜索
function search(){
    var param = {};
    $('#exampleTableEvents').bootstrapTable('refresh',{query : param});
}

