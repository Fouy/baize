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
            $('#group_title').empty();
            $('#group_title').text(data.data.name + '-组件配置');
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
                        compIdArray.push(list[j].compId + '');
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
        var compId = $(this).attr('data');
        compIdArray.push(compId);
        $.unique(compIdArray.sort());
        // 改变 pannel 主题
        $('#panel_' + compId).attr('class', '');
        $('#panel_' + compId).attr('class', 'panel panel-primary');
    });
    $('.i-checks').on('ifUnchecked', function (event) {
        var compId = $(this).attr('data');
        compIdArray.splice($.inArray(compId, compIdArray), 1);
        // 改变 pannel 主题
        $('#panel_' + compId).attr('class', '');
        $('#panel_' + compId).attr('class', 'panel panel-default');
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
