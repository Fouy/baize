(function () {

    var lineoption = {
        title: {
            text: ''
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross'
            }
        },
        grid: {
            x: 24,
            x2: 40,
            y: 30,
            y2: 24
        },
        calculable: true,
        xAxis: [
            {
                type: 'category',
                boundaryGap: false,
                data: ['18/09/12', '18/09/13', '18/09/14', '18/09/15', '18/09/16', '18/09/17',
                    '18/09/18', '18/09/19', '18/09/20', '18/09/21', '18/09/22', '18/09/23', '18/09/24',
                    '18/09/25', '18/09/26', '18/09/27', '18/09/28', '18/09/29', '18/09/30',
                    '18/10/01', '18/10/02', '18/10/03', '18/10/04', '18/10/05', '18/10/06',
                    '18/10/07', '18/10/08', '18/10/09', '18/10/10', '18/10/11', '18/10/12']
            }
        ],
        yAxis: [
            {
                type: 'value',
                axisLabel: {
                    formatter: '{value}'
                }
            }
        ],
        series: [
            {
                name: '日总访问量',
                type: 'line',
                data: [11, 11, 15, 13, 12, 13, 11, 11, 20, 13, 12, 13, 9, 11, 17, 13, 12, 13, 11, 11, 15, 13, 12, 13, 11, 11, 17, 13, 12, 13, 10],
                markPoint: {
                    data: [
                        {type: 'max', name: '最大值'},
                        {type: 'min', name: '最小值'}
                    ]
                },
                markLine: {
                    data: [
                        {type: 'average', name: '平均值'}
                    ]
                }
            }
        ]
    };

    var lineoption2 = {
        title: {
            text: ''
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross'
            }
        },
        grid: {
            x: 24,
            x2: 40,
            y: 30,
            y2: 24
        },
        calculable: true,
        xAxis: [
            {
                type: 'category',
                boundaryGap: false,
                data: ['18/09/12', '18/09/13', '18/09/14', '18/09/15', '18/09/16', '18/09/17',
                    '18/09/18', '18/09/19', '18/09/20', '18/09/21', '18/09/22', '18/09/23', '18/09/24',
                    '18/09/25', '18/09/26', '18/09/27', '18/09/28', '18/09/29', '18/09/30',
                    '18/10/01', '18/10/02', '18/10/03', '18/10/04', '18/10/05', '18/10/06',
                    '18/10/07', '18/10/08', '18/10/09', '18/10/10', '18/10/11', '18/10/12']
            }
        ],
        yAxis: [
            {
                type: 'value',
                axisLabel: {
                    formatter: '{value}'
                }
            }
        ],
        series: [
            {
                name: '日总访问量',
                type: 'line',
                data: [11, 11, 15, 13, 12, 13, 11, 11, 20, 13, 12, 13, 9, 11, 17, 13, 12, 13, 11, 11, 15, 13, 12, 13, 11, 11, 17, 13, 12, 13, 10],
                markPoint: {
                    data: [
                        {type: 'max', name: '最大值'},
                        {type: 'min', name: '最小值'}
                    ]
                },
                markLine: {
                    data: [
                        {type: 'average', name: '平均值'}
                    ]
                }
            }
        ]
    };

    var lineInvoke = echarts.init(document.getElementById("line-invoke"));
    lineInvoke.setOption(lineoption);
    $(window).resize(lineInvoke.resize);

    var lineHealth = echarts.init(document.getElementById("line-health"));
    lineHealth.setOption(lineoption2);
    $(window).resize(lineHealth.resize);

})();