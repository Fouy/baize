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
        url: "/evaluate/list",
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
            field: 'spuCode',
            title: '商品编码',
            align: 'center'
        }, {
            field: 'spuName',
            title: '商品名称',
            align: 'center'
        },{
            field: 'orderNo',
            title: '订单编码',
            align: 'center'
        }, {
            field: 'phoneNo',
            title: '手机号',
            align: 'center'
        }, {
            field: 'replyFlagName',
            title: '是否回复',
            align: 'center'
        },{
            field: 'evaluateTypeName',
            title: '评价类型',
            align: 'center'
        }, {
            field: 'shieldFlagName',
            title: '屏蔽状态',
            align: 'center'
        }, {
            field: 'createTime',
            title: '创建时间',
            align: 'center',
            formatter:function(value,row,index){
            	var date = new Date(value);
            	var time = date.format("yyyy-MM-dd HH:mm:ss");
            	return time;
            }
        }, {
            field: 'evaluateId',
            title: '操作',
            align: 'center',
            formatter:function(value, row, index){
                return '<a href="javascript:;" onclick="openLineWin('+value+')">回复</a>'
            } 
        }]
    });

    // 初始化type
   // var type = $("#type");
   // type.empty();
   // $.post("/articletype/list", {}, function(result){
   //     var data = eval(result);
   //     var list = data.data;
   //
   //     var opt = $('<option></option>');
   //     opt.attr('value', '');
   //     opt.append('--全部--');
   //     opt.appendTo(type);
   //     for(var i = 0; i < list.length; i++) {
   //         var opt = $('<option></option>');
   //         opt.attr('value', list[i].type_id);
   //         opt.append(list[i].name);
   //         opt.appendTo(type);
   //     }
   // }, 'json');

})();


// 搜索
function search(){
    var param = {};
    var evaluateType = $('#evaluateType').val();
    if (evaluateType) {
        param.evaluateType = evaluateType;
    }
    var phoneNo = $('#phoneNo').val();
    if(phoneNo) {
        param.phoneNo = phoneNo;
    }
    var spuCode = $('#spuCode').val();
    if (spuCode) {
        param.spuCode = spuCode;
    }
    var shieldFlag = $('#shieldFlag').val();
    if (shieldFlag) {
        param.shieldFlag = shieldFlag;
    }
    $('#exampleTableEvents').bootstrapTable('refresh',{query : param});
}


function openLineWin(value) {
    parent.layer.open({
        type: 2,
        title: '订单详情',
        //skin: 'layui-layer-rim', //加上边框
        area: ['900px', '550px'], //宽高
        content: '/evaluate/reply/main.html?evaluateId='+value,
        end: function () {
            search();
        }
    });
}