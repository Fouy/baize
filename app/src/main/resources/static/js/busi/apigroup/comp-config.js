/*!
 * Copyright 2017 xuefeihu
 * Licensed under the Themeforest Standard Licenses
 */

(function () {
    var groupId = $.getUrlParam('groupId');
    $('#groupId').val(groupId);

    // 初始化页面
    initComponent(groupId);

})();

/**
 * 初始化组件
 * @param groupId
 */
var compIdArray = [];
function initComponent(groupId) {
    var typeList = ['AUTH', 'ALLOW', 'TRAFFIC', 'CACHE', 'ROUTE', 'LOG', 'PROTO_CONVERT', 'OTHER'];

    $.get("/apigroup/complist?groupId=" + groupId, {}, function (result) {
        var data = eval(result);
        if (data.code == '1000') {
            // 填充组件
            for (var i = 0; i < typeList.length; i++) {
                var tabRowId = '#tab-' + typeList[i] + ' > div.panel-body > div.row';
                var tabRow = $(tabRowId);
                tabRow.empty();

                var map = data.data.componentMap;
                var list = map[typeList[i]];
                for (var j = 0; j < list.length; j++) {
                    $("#componentTemplete").tmpl(list[j]).appendTo(tabRow);
                    if (list[j].checked) {
                        compIdArray.push(list[j].compId);
                    }
                }
            }
        }
        initICheck();
    }, 'json');
}

// 初始化iCheck
function initICheck() {
    $('.i-checks').iCheck({
        checkboxClass: 'icheckbox_square-green',
        radioClass: 'iradio_square-green',

    });
    $('.i-checks').on('ifChecked', function (event) {
        //alert(event.type + ' callback');
        compIdArray.push($(this).attr('data'));
        //alert($(this).attr('data'));
        $.unique(compIdArray.sort());
    });
    $('.i-checks').on('ifUnchecked', function (event) {
        //alert(event.type + ' callback');
        //alert($(this).attr('data'));

        compIdArray.splice($.inArray($(this).attr('data'), compIdArray), 1);

    });
}

/**
 * 整体保存
 */
function saveAll() {
    var data = {};
    data.groupId = $('#groupId').val();
    var temp = '';
    for (var i = 0; i < compIdArray.length; i++) {
        if (i == 0) {
            temp = temp + compIdArray[i];
        } else {
            temp = temp + ',' + compIdArray[i];
        }
    }
    data.compIds = temp;

    $.post("/apigroup/savecomp", data, function (result) {
        if (result.code == '1000') {
            toastr.success("提示信息", result.msg);
        } else {
            toastr.error("错误信息", result.msg);
        }
    }, 'json');
}
