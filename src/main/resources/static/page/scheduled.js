/**
 * 数据请求
 *
 * @author 付成垒
 *
 * @type {{getData: Common.getData}}
 */

function Properties(size) {
    this.page = 1,
        this.size = size
}

var state;  //获取列表类型

var interval;   //循环控制

var time = 1000;    //页面刷新速度 默认不刷新

var pageSize = 10;  //每页显示数据数量 默认10

var refresh = false;    //页面是否即时刷新  默认不刷新

var securityCode = null;    //数据库安全码

var currentTask = "task";   //当前的任务类型 用来开启任务或者关闭任务 全部关闭 全部开启按钮

var currentTaskTemp = "";   //辅助值 当前的任务类型 用来开启任务或者关闭任务 全部关闭 全部开启按钮

//系统列表总值
var scheduled = {

    listTaskData: function (properties) {   //TASK
        Common.getData("/monitor/getListHttpTask", "json", "post", false,
            {
                id: $('#id').val(),
                state: $('#state').val(),
                name: $('#name').val(),
                currentPage: properties.page,
                pageSize: properties.size,
                security: securityCode
            },
            function (data) {
                if (data.state == -10086) {
                    scheduled.securityCheck();
                    return;
                }
                $('#tbTask').html('');
                scheduled.packData(data.data, 1);
                var content = "<ul id=\"paginationTask\" class=\"pagination\">\n" +
                    "                    </ul>";
                $('#page').html(content);
                $('#paginationTask').jqPaginator({
                    totalPages: data.count,
                    visiblePages: properties.size,
                    currentPage: properties.page,
                    onPageChange: function (num, type) {
                        if (type == 'change') {
                            properties.page = num;
                            $('#paginationTask').jqPaginator('destroy');
                            scheduled.listTaskData(properties);
                        }
                    }
                });
            },
            function (err) {
            }
        )
    },
    listTaskDataStart: function () {    //TASK
        currentTask = "task";
        $('#page').html('');
        $('#data').html("");
        var content =
            "    <table class=\"table table-bordered text-nowrap\">" +
            "        <thead><tr>" +
            "        <th>任务ID</th>\n" +
            "        <th>任务名</th>" +
            "        <th>URL</th>" +
            "        <th>访问类型</th>" +
            "        <th>当前信息</th>" +
            "        <th>创建时间</th>" +
            "        <th>下一次预计执行时间</th>" +
            "        <th>本次任务开始时间</th>" +
            "        <th>最后一次成功时间</th>" +
            "        <th>最后一次失败时间</th>" +
            "        <th>单次任务超时时间</th>" +
            "        <th>间隔执行类型</th>" +
            "        <th>间隔单元</th>" +
            "        <th>任务参数</th>" +
            "        <th>调用耗时</th>" +
            "        <th>操作</th>" +
            "        </tr></thead>" +
            "        <tbody id='tbTask'>" +
            "         </tbody>" +
            "    </table>"
        $('#data').html(content);
        clearInterval(interval);
        var p = new Properties(pageSize);
        scheduled.listTaskData(p);
        interval = setInterval(function () {
            if (refresh)
                scheduled.listTaskData(p);
        }, time);
    },
    getListJob: function (properties) {     //JOB
        Common.getData("/monitor/getListJob", "json", "post", false,
            {
                id: $('#id').val(),
                state: $('#state').val(),
                name: $('#name').val(),
                currentPage: properties.page,
                pageSize: properties.size,
                security: securityCode
            },
            function (data) {
                if (data.state == -10086) {
                    scheduled.securityCheck();
                    return;
                }
                scheduled.packData(data.data, 2);
                $('#paginationJOB').jqPaginator({
                    totalPages: data.count,
                    visiblePages: properties.size,
                    currentPage: properties.page,
                    onPageChange: function (num, type) {
                        if (type == 'change') {
                            properties.page = num;
                            $('#paginationJOB').jqPaginator('destroy');
                            scheduled.getListJob(properties);
                        }
                    }
                });
            },
            function (err) {
            }
        )
    },
    getListJobStart: function () {      //JOB
        currentTask = "job";
        $('#page').html('');
        var content = "<ul id=\"paginationJOB\" class=\"pagination\">\n" +
            "                    </ul>";
        $('#page').html(content);
        var content =
            "    <table class=\"table table-bordered text-nowrap\">" +
            "        <thead><tr>" +
            "        <th>JOBID</th>" +
            "        <th>JOB名</th>" +
            "        <th>URL</th>" +
            "        <th>访问类型</th>" +
            "        <th>当前信息</th>" +
            "        <th>延时</th>" +
            "        <th>创建时间</th>" +
            "        <th>任务预计执行时间</th>" +
            "        <th>任务开始执行时间</th>" +
            "        <th>任务超时时间</th>" +
            "        <th>任务参数</th>" +
            "        <th>调用耗时</th>" +
            "        <th>操作</th>" +
            "        </tr></thead>" +
            "        <tbody id='tbJOB'>"
        "         </tbody>" +
        "    </table>"
        $('#data').html(content);
        clearInterval(interval);
        var p = new Properties(pageSize);
        scheduled.getListJob(p);
        interval = setInterval(function () {
            if (refresh)
                scheduled.getListJob(p);
        }, time);
    },
    getListJobFail: function (properties) {     //已失败的JOB
        Common.getData("/monitor/getListFailHttpJob", "json", "post", false,
            {
                id: $('#id').val(),
                state: $('#state').val(),
                name: $('#name').val(),
                currentPage: properties.page,
                pageSize: properties.size,
                security: securityCode
            },
            function (data) {
                if (data.state == -10086) {
                    scheduled.securityCheck();
                    return;
                }
                $('#tbTask').html('');
                scheduled.packData(data.data, 3);
                $('#paginationFailJOB').jqPaginator({
                    totalPages: data.count,
                    visiblePages: properties.size,
                    currentPage: properties.page,
                    onPageChange: function (num, type) {
                        if (type == 'change') {
                            properties.page = num;
                            $('#paginationFailJOB').jqPaginator('destroy');
                            scheduled.getListJobFail(properties);
                        }
                    }
                });
            },
            function (err) {
            }
        )
    },
    getListJobFailStart: function () {      //已失败的JOB
        currentTask = "";
        $('#page').html('');
        var content = "<ul id=\"paginationFailJOB\" class=\"pagination\">\n" +
            "                    </ul>";
        $('#page').html(content);
        var content =
            "    <table class=\"table table-bordered text-nowrap\">" +
            "        <thead><tr>" +
            "        <th>JOBID</th>" +
            "        <th>JOB名</th>" +
            "        <th>URL</th>" +
            "        <th>访问类型</th>" +
            "        <th>失败原因</th>" +
            "        <th>延时</th>" +
            "        <th>创建时间</th>" +
            "        <th>任务开始时间</th>" +
            "        <th>任务失败时间</th>" +
            "        <th>任务超时时间</th>" +
            "        <th>任务参数</th>" +
            "        <th>调用耗时</th>" +
            "        <th>操作</th>" +
            "        </tr></thead>" +
            "        <tbody id='tbFailJOB'>"
        "         </tbody>" +
        "    </table>"
        $('#data').html(content);
        clearInterval(interval);
        var p = new Properties(pageSize);
        scheduled.getListJobFail(p);
        interval = setInterval(function () {
            if (refresh)
                scheduled.getListJobFail(p);
        }, time);
    },
    getListJobSuccess: function (properties) {      //已成功地JOB
        Common.getData("/monitor/getListSuccessHttpJob", "json", "post", false,
            {
                id: $('#id').val(),
                state: $('#state').val(),
                name: $('#name').val(),
                currentPage: properties.page,
                pageSize: properties.size,
                security: securityCode
            },
            function (data) {
                if (data.state == -10086) {
                    scheduled.securityCheck();
                    return;
                }
                $('#tbTask').html('');
                scheduled.packData(data.data, 4);
                $('#paginationSuccessJOB').jqPaginator({
                    totalPages: data.count,
                    visiblePages: properties.size,
                    currentPage: properties.page,
                    onPageChange: function (num, type) {
                        if (type == 'change') {
                            properties.page = num;
                            $('#paginationSuccessJOB').jqPaginator('destroy');
                            scheduled.getListJobSuccess(properties);
                        }
                    }
                });
            },
            function (err) {
            }
        )
    },
    getListJobSuccessStart: function () {       //已成功地JOB
        currentTask = "";
        $('#page').html('');
        var content = "<ul id=\"paginationSuccessJOB\" class=\"pagination\">\n" +
            "                    </ul>";
        $('#page').html(content);
        var content =
            "    <table class=\"table table-bordered text-nowrap\">" +
            "        <thead><tr>" +
            "        <th>JOBID</th>" +
            "        <th>JOB名</th>" +
            "        <th>URL</th>" +
            "        <th>访问类型</th>" +
            "        <th>失败原因</th>" +
            "        <th>延时</th>" +
            "        <th>创建时间</th>" +
            "        <th>任务开始时间</th>" +
            "        <th>任务完成时间</th>" +
            "        <th>任务超时时间</th>" +
            "        <th>任务参数</th>" +
            "        <th>调用耗时</th>" +
            "        <th>操作</th>" +
            "        </tr></thead>" +
            "        <tbody id='tbSuccessJOB'>"
        "         </tbody>" +
        "    </table>"
        $('#data').html(content);
        clearInterval(interval);
        var p = new Properties(pageSize);
        scheduled.getListJobSuccess(p);
        interval = setInterval(function () {
            if (refresh)
                scheduled.getListJobSuccess(p);
        }, time);
    },
    getHandOutListTask: function (properties) {     //分发任务
        Common.getData("/monitor/getHandOutListTask", "json", "post", false,        //获取数据
            {
                id: $('#id').val(),     //任务id 搜索选项
                state: $('#state').val(),   //任务状态 搜索选项
                name: $('#name').val(),     //任务名称 搜索选项
                currentPage: properties.page,   //下一页
                pageSize: properties.size,      //每页大小
                security: securityCode      //安全码
            },
            function (data) {
                if (data.state == -10086) {     //安全码校验 独有返回码
                    scheduled.securityCheck();  //弹窗输入安全码
                    return;
                }
                $('#tbTask').html('');  //初始化数据显示
                scheduled.packData(data.data, 5);  //组装数据
                $('#paginationHandOut').jqPaginator({   //分页控件显示
                    totalPages: data.count,     //数据总量
                    visiblePages: properties.size,      //每页数据数
                    currentPage: properties.page,   //当前页数
                    onPageChange: function (num, type) {    //点击下一页
                        if (type == 'change') {     //页面切换
                            properties.page = num;  //获取下一页
                            $('#paginationHandOut').jqPaginator('destroy');     //销毁组件
                            scheduled.getHandOutListTask(properties);       //获取下一页数据
                        }
                    }
                });
            },
            function (err) {
            }
        )
    },
    getHandOutListTaskStart: function () {      //分发任务点击按钮切换
        //改变任务类型到分发任务
        currentTask = "handOut";
        //初始化分页
        $('#page').html('');
        var content = "<ul id=\"paginationHandOut\" class=\"pagination\">\n" +
            "                    </ul>";
        $('#page').html(content);
        //初始化Table
        var content =
            "    <table class=\"table table-bordered text-nowrap\">" +
            "        <thead><tr>" +
            "        <th>任务ID</th>" +
            "        <th>任务名</th>" +
            "        <th>任务并发数</th>" +
            "        <th>当前信息</th>" +
            "        <th>单次抓取数据超时时间</th>" +
            "        <th>单次处理数据超时时间</th>" +
            "        <th>间隔执行类型</th>" +
            "        <th>间隔单元</th>" +
            "        <th>创建时间</th>" +
            "        <th>下一次预计执行时间</th>" +
            "        <th>本次任务开始时间</th>" +
            "        <th>最后一次成功时间</th>" +
            "        <th>最后一次失败时间</th>" +
            "        <th>参数模式</th>" +
            "        <th>数据获取地址</th>" +
            "        <th>数据获取方式</th>" +
            "        <th>数据处理地址</th>" +
            "        <th>数据处理方式</th>" +
            "        <th>调用耗时</th>" +
            "        <th>操作</th>" +
            "        </tr></thead>" +
            "        <tbody id='tbHandOut'>"
        "         </tbody>" +
        "    </table>"
        $('#data').html(content);
        //删除刷新
        clearInterval(interval);
        //初始化分页值
        var p = new Properties(pageSize);
        //获取初始数据
        scheduled.getHandOutListTask(p);
        //刷新初始化
        interval = setInterval(function () {
            if (refresh)
                scheduled.getHandOutListTask(p);
        }, time);
    },
    packData: function (data, nState) {     //组装数据
        var content = "";
        if (nState == 1) {      //TASK显示
            $.each(data, function (i, item) {
                var td = "<td bgcolor='olive'>";
                var _td = "";
                var state = "";
                var op = "";
                if (item.nState != 5) {
                    op = "<button onclick='scheduled.closeHttpTask(" + item.lId + ")' class='btn btn-success btn-sm' type='button'>停止</button>&nbsp;&nbsp;<button onclick='scheduled.editHttpTask(" + item.lId + ")' class='btn btn-success btn-sm' type='button'>编辑</button>&nbsp;&nbsp;<button onclick='scheduled.deleteHttpTask(" + item.lId + ")' class='btn btn-success btn-sm' type='button'>删除</button>&nbsp;&nbsp;<button onclick='scheduled.immediateExecutionHttpTask(" + item.lId + ")' class='btn btn-success btn-sm' type='button'>立即执行</button>";
                }
                else {
                    op = "<button onclick='scheduled.startHttpTask(" + item.lId + ")' class='btn btn-success btn-sm' type='button'>启动</button>&nbsp;&nbsp;<button onclick='scheduled.editHttpTask(" + item.lId + ")' class='btn btn-success btn-sm' type='button'>编辑</button>&nbsp;&nbsp;<button onclick='scheduled.deleteHttpTask(" + item.lId + ")' class='btn btn-success btn-sm' type='button'>删除</button>&nbsp;&nbsp;<button onclick='scheduled.immediateExecutionHttpTask(" + item.lId + ")' class='btn btn-success btn-sm' type='button'>立即执行</button>";
                }
                if (item.nState == 0)
                    td = "<td bgcolor='#d3d3d3'>";
                else if (item.nState == 1)
                    td = "<td bgcolor='#20b2aa'>";
                else if (item.nState == 2)
                    td = "<td bgcolor='#87cefa'>";
                else if (item.nState == 3)
                    td = "<td bgcolor='#90ee90'>";
                else if (item.nState == 4)
                    td = "<td bgcolor='#f08080'>";
                else if (item.nState == 5)
                    td = "<td bgcolor='#ff8c00'>";
                var executeType = "";
                if (item.nExecuteType == 1)
                    executeType = "秒";
                else if (item.nExecuteType == 2)
                    executeType = "分";
                else if (item.nExecuteType == 3)
                    executeType = "时";
                else if (item.nExecuteType == 4)
                    executeType = "天";
                else if (item.nExecuteType == 5)
                    executeType = "周";
                else if (item.nExecuteType == 6)
                    executeType = "月";
                else if (item.nExecuteType == 7)
                    executeType = "年";
                content += "<tr>" +
                    td + item.lId + "</td>" +
                    td + item.strName + "</td>" +
                    td + item.strUrl + "</td>" +
                    td + item.strType + "</td>" +
                    td + item.strMessage + "</td>" +
                    td + scheduled.formatTime(item.dtCreateTime) + "</td>" +
                    td + scheduled.formatTime(item.dtExecuteTime) + "</td>" +
                    td + scheduled.formatTime(item.dtStartTime) + "</td>" +
                    td + scheduled.formatTime(item.dtFinishedTime) + "</td>" +
                    td + scheduled.formatTime(item.dtFailTime) + "</td>" +
                    td + item.nOverTime + "ms</td>" +
                    td + executeType + "</td>" +
                    td + item.nExecuteUnit + "</td>" +
                    td + item.strArgs + "</td>" +
                    td + scheduled.formatString(item.strUseTime) + "</td>" +
                    td + op + "</td>" +
                    "</tr>"
            });
            $('#tbTask').html('');
            $('#tbTask').html(content);
        }
        if (nState == 2) {      //JOB显示
            $.each(data, function (i, item) {
                var td = "<td bgcolor='olive'>";
                var _td = "";
                var state = "";
                var op = "<button onclick='scheduled.jobImmediateExecution(" + item.lId + "," + 1 + ")' class='btn btn-success btn-sm' type='button'>立即执行</button>";
                if (item.nState == 0)
                    td = "<td bgcolor='#d3d3d3'>";
                else if (item.nState == 1)
                    td = "<td bgcolor='#20b2aa'>";
                else if (item.nState == 2)
                    td = "<td bgcolor='#87cefa'>";
                else if (item.nState == 3)
                    td = "<td bgcolor='#90ee90'>";
                else if (item.nState == 4)
                    td = "<td bgcolor='#f08080'>";
                else if (item.nState == 5)
                    td = "<td bgcolor='#ff8c00'>";
                content += "<tr>" +
                    td + item.lId + "</td>" +
                    td + item.strName + "</td>" +
                    td + item.strUrl + "</td>" +
                    td + item.strType + "</td>" +
                    td + item.strMessage + "</td>" +
                    td + item.nTime + "ms</td>" +
                    td + scheduled.formatTime(item.dtCreateTime) + "</td>" +
                    td + scheduled.formatTime(item.dtExecuteTime) + "</td>" +
                    td + scheduled.formatTime(item.dtStartTime) + "</td>" +
                    td + item.nOverTime + "ms</td>" +
                    td + item.strArgs + "</td>" +
                    td + scheduled.formatString(item.strUseTime) + "</td>" +
                    td + op + "</td>" +
                    "</tr>"
            });
            $('#tbJOB').html('');
            $('#tbJOB').html(content);
        }
        if (nState == 3) {      //已失败的JOB显示
            $.each(data, function (i, item) {
                var td = "<td bgcolor='olive'>";
                var _td = "";
                var state = "";
                var op = "<button onclick='scheduled.jobImmediateExecution(" + item.lId + "," + 2 + ")' class='btn btn-success btn-sm' type='button'>立即执行</button>";
                if (item.nState == 0)
                    td = "<td bgcolor='#d3d3d3'>";
                else if (item.nState == 1)
                    td = "<td bgcolor='#20b2aa'>";
                else if (item.nState == 2)
                    td = "<td bgcolor='#87cefa'>";
                else if (item.nState == 3)
                    td = "<td bgcolor='#90ee90'>";
                else if (item.nState == 4)
                    td = "<td bgcolor='#f08080'>";
                else if (item.nState == 5)
                    td = "<td bgcolor='#ff8c00'>";
                content += "<tr>" +
                    td + item.lId + "</td>" +
                    td + item.strName + "</td>" +
                    td + item.strUrl + "</td>" +
                    td + item.strType + "</td>" +
                    td + item.strMessage + "</td>" +
                    td + item.nTime + "ms</td>" +
                    td + scheduled.formatTime(item.dtCreateTime) + "</td>" +
                    td + scheduled.formatTime(item.dtStartTime) + "</td>" +
                    td + scheduled.formatTime(item.dtFailTime) + "</td>" +
                    td + item.nOverTime + "ms</td>" +
                    td + item.strArgs + "</td>" +
                    td + scheduled.formatString(item.strUseTime) + "</td>" +
                    td + op + "</td>" +
                    "</tr>"
            });
            $('#tbFailJOB').html('');
            $('#tbFailJOB').html(content);
        }
        if (nState == 4) {      //已成功的JOB显示
            $.each(data, function (i, item) {
                var td = "<td bgcolor='olive'>";
                var _td = "";
                var state = "";
                var op = "<button onclick='scheduled.jobImmediateExecution(" + item.lId + "," + 3 + ")' class='btn btn-success btn-sm' type='button'>立即执行</button>";
                if (item.nState == 0)
                    td = "<td bgcolor='#d3d3d3'>";
                else if (item.nState == 1)
                    td = "<td bgcolor='#20b2aa'>";
                else if (item.nState == 2)
                    td = "<td bgcolor='#87cefa'>";
                else if (item.nState == 3)
                    td = "<td bgcolor='#90ee90'>";
                else if (item.nState == 4)
                    td = "<td bgcolor='#f08080'>";
                else if (item.nState == 5)
                    td = "<td bgcolor='#ff8c00'>";
                content += "<tr>" +
                    td + item.lId + "</td>" +
                    td + item.strName + "</td>" +
                    td + item.strUrl + "</td>" +
                    td + item.strType + "</td>" +
                    td + item.strMessage + "</td>" +
                    td + item.nTime + "ms</td>" +
                    td + scheduled.formatTime(item.dtCreateTime) + "</td>" +
                    td + scheduled.formatTime(item.dtStartTime) + "</td>" +
                    td + scheduled.formatTime(item.dtFinishedTime) + "</td>" +
                    td + item.nOverTime + "ms</td>" +
                    td + item.strArgs + "</td>" +
                    td + scheduled.formatString(item.strUseTime) + "</td>" +
                    td + op + "</td>" +
                    "</tr>"
            });
            $('#tbSuccessJOB').html('');
            $('#tbSuccessJOB').html(content);
        }
        if (nState == 5) {      //分发任务显示
            $.each(data, function (i, item) {
                var td = "<td bgcolor='olive'>";
                var _td = "";
                var state = "";
                var op = "";
                if (item.nState != 5) {
                    op = "<button onclick='scheduled.closeHandleTask(" + item.lId + ")' class='btn btn-success btn-sm' type='button'>停止</button>&nbsp;&nbsp;<button onclick='scheduled.editHandleTask(" + item.lId + ")' class='btn btn-success btn-sm' type='button'>编辑</button>&nbsp;&nbsp;<button onclick='scheduled.deleteHandleTask(" + item.lId + ")' class='btn btn-success btn-sm' type='button'>删除</button>&nbsp;&nbsp;<button onclick='scheduled.handOutTaskImmediateExecution(" + item.lId + ")' class='btn btn-success btn-sm' type='button'>立即执行</button>";
                }
                else {
                    op = "<button onclick='scheduled.startHandleTask(" + item.lId + ")' class='btn btn-success btn-sm' type='button'>启动</button>&nbsp;&nbsp;<button onclick='scheduled.editHandleTask(" + item.lId + ")' class='btn btn-success btn-sm' type='button'>编辑</button>&nbsp;&nbsp;<button onclick='scheduled.deleteHandleTask(" + item.lId + ")' class='btn btn-success btn-sm' type='button'>删除</button>&nbsp;&nbsp;<button onclick='scheduled.handOutTaskImmediateExecution(" + item.lId + ")' class='btn btn-success btn-sm' type='button'>立即执行</button>";
                }
                if (item.nState == 0)
                    td = "<td bgcolor='#d3d3d3'>";
                else if (item.nState == 1)
                    td = "<td bgcolor='#20b2aa'>";
                else if (item.nState == 2)
                    td = "<td bgcolor='#87cefa'>";
                else if (item.nState == 3)
                    td = "<td bgcolor='#90ee90'>";
                else if (item.nState == 4)
                    td = "<td bgcolor='#f08080'>";
                else if (item.nState == 5)
                    td = "<td bgcolor='#ff8c00'>";
                var executeType = "";
                if (item.nExecuteType == 1)
                    executeType = "秒";
                else if (item.nExecuteType == 2)
                    executeType = "分";
                else if (item.nExecuteType == 3)
                    executeType = "时";
                else if (item.nExecuteType == 4)
                    executeType = "天";
                else if (item.nExecuteType == 5)
                    executeType = "周";
                else if (item.nExecuteType == 6)
                    executeType = "月";
                else if (item.nExecuteType == 7)
                    executeType = "年";
                var parameterType = "";
                if (item.nParameterType == 0)
                    parameterType = "JSON"
                else if (item.nParameterType == 1)
                    parameterType = "MAP"
                content += "<tr>" +
                    td + item.lId + "</td>" +
                    td + item.strName + "</td>" +
                    td + item.nConcurrencyNumber + "</td>" +
                    td + item.strMessage + "</td>" +
                    td + item.nOverTime + "ms</td>" +
                    td + item.nHandleTime + "ms</td>" +
                    td + executeType + "</td>" +
                    td + item.nExecuteUnit + "</td>" +
                    td + scheduled.formatTime(item.dtCreateTime) + "</td>" +
                    td + scheduled.formatTime(item.dtExecuteTime) + "</td>" +
                    td + scheduled.formatTime(item.dtStartTime) + "</td>" +
                    td + scheduled.formatTime(item.dtFinishedTime) + "</td>" +
                    td + scheduled.formatTime(item.dtFailTime) + "</td>" +
                    td + parameterType + "</td>" +
                    td + item.strSourceURL + "</td>" +
                    td + item.strSourceType + "</td>" +
                    td + item.strDestinationURL + "</td>" +
                    td + item.strDestinationType + "</td>" +
                    td + scheduled.formatString(item.strUseTime) + "</td>" +
                    td + op + "</td>" +
                    "</tr>"
            });
            $('#tbHandOut').html('');
            $('#tbHandOut').html(content);
        }
    },
    getTaskState: function () {     //执行器状态
        Common.getData("/monitor/getTaskState", "json", "post", false,
            {
                security: securityCode
            },
            function (date) {
                if (date.state == -10086) {
                    scheduled.securityCheck();
                    return;
                }
                $('#_coreNum').text(date.data._coreNum);
                $('#_currentFinishedNum').text(date.data._currentFinishedNum);
                $('#_currentTaskNum').text(date.data._currentTaskNum);
                $('#_maxNum').text(date.data._maxNum);
                $('#_waitNum').text(date.data._waitNum);
                $('#coreNum').text(date.data.coreNum);
                $('#currentFinishedNum').text(date.data.currentFinishedNum);
                $('#currentTaskNum').text(date.data.currentTaskNum);
                $('#maxNum').text(date.data.maxNum);
                $('#waitNum').text(date.data.waitNum);
            },
            function (err) {
            }
        )
    },
    formatTime: function (date) {       //时间格式化
        if (date == null) return "";
        return date.split('.')[0];
    },
    formatString: function (str) {      //字符串非空格式化
        return str == null || str == "" ? "" : str;
    },
    startAll: function () {     //开始所有任务 执行器级别
        Common.getData("/semaphore/semaphoreSwitch", "json", "post", false,
            {
                state: 0,
                security: securityCode
            },
            function (data) {
                if (data.state == -10086) {
                    scheduled.securityCheck();
                    return;
                }
                if (data.data)
                    alert("success");
                else
                    alert('fail');
                scheduled.stateRub("cli");
            },
            function (err) {
                alert('fail')
            }
        )
    },
    stopAll: function () {      //停止所有任务 执行器级别
        Common.getData("/semaphore/semaphoreSwitch", "json", "post", false,
            {
                state: 3,
                security: securityCode
            },
            function (data) {
                if (data.state == -10086) {
                    scheduled.securityCheck();
                    return;
                }
                if (data.data)
                    alert("success");
                else
                    alert('fail');
                scheduled.stateRub("cli");
            },
            function (err) {
                alert('fail')
            }
        )
    },
    start: function () {        //启动一类任务 执行器级别
        Common.getData("/semaphore/semaphoreSwitch", "json", "post", false,
            {
                taskType: currentTask,
                state: 0,
                security: securityCode
            },
            function (data) {
                if (data.state == -10086) {
                    scheduled.securityCheck();
                    return;
                }
                if (data.data)
                    alert("success");
                else
                    alert('fail');
                scheduled.stateRub("cli");
            },
            function (err) {
                alert('fail')
            }
        )
    },
    stateRub: function (cli) {      //启动 停止 全部关闭 全部启动 按钮可用控制
        if (cli == 'run' && currentTaskTemp == currentTask) return;
        if (state == 3 || state == 4) {
            $('#start').addClass("cus");
            $('#stop').addClass("cus");
            return;
        }
        Common.getData("/semaphore/showSemaphore", "json", "post", false,
            {
                taskType: currentTask,
                security: securityCode
            },
            function (data) {
                if (data.state == -10086) {
                    scheduled.securityCheck();
                    return;
                }
                if (data.data == 3) {
                    $('#start').removeClass("cus");
                    $('#stop').addClass("cus");
                }
                else {
                    $('#start').addClass("cus");
                    $('#stop').removeClass("cus");
                }
                currentTaskTemp = currentTask;
            },
            function (err) {
            }
        )
    },
    stop: function () {     //停止任务执行 一类任务
        Common.getData("/semaphore/semaphoreSwitch", "json", "post", false,
            {
                taskType: currentTask,
                state: 3,
                security: securityCode
            },
            function (data) {
                if (data.state == -10086) {
                    scheduled.securityCheck();
                    return;
                }
                if (data.data)
                    alert("success");
                else
                    alert('fail');
                scheduled.stateRub("cli");
            },
            function (err) {
                alert('fail')
            }
        )
    },
    immediateExecutionHttpTask: function (taskId) {     //task立即执行
        Common.getData("/task/taskImmediateExecution", "json", "post", false,
            {
                taskId: taskId,
                security: securityCode
            },
            function (data) {
                if (data.state == -10086) {
                    scheduled.securityCheck();
                    return;
                }
                if (data.data)
                    alert("success");
                else
                    alert('fail');
            },
            function (err) {
                alert('fail')
            }
        )
    },
    jobImmediateExecution: function (taskId, type) {     //task立即执行
        Common.getData("/task/jobImmediateExecution", "json", "post", false,
            {
                type: type,
                taskId: taskId,
                security: securityCode
            },
            function (data) {
                if (data.state == -10086) {
                    scheduled.securityCheck();
                    return;
                }
                if (data.data)
                    alert("success");
                else
                    alert('fail');
            },
            function (err) {
                alert('fail')
            }
        )
    },
    handOutTaskImmediateExecution: function (taskId) {     //task立即执行
        Common.getData("/task/handOutTaskImmediateExecution", "json", "post", false,
            {
                taskId: taskId,
                security: securityCode
            },
            function (data) {
                if (data.state == -10086) {
                    scheduled.securityCheck();
                    return;
                }
                if (data.data)
                    alert("success");
                else
                    alert('fail');
            },
            function (err) {
                alert('fail')
            }
        )
    },
    securityCheck: function () {        //安全码检查
        if ($('#msg_s').length > 0) return;
        window.alert = function (txt) {
            $("body").append('<div id="msg_s"><div id="msg_top_s">输&nbsp;入&nbsp;安&nbsp;全&nbsp;码</div><div id="msg_cont_s"><input id="security" style="width: 400px;margin-bottom: 20px" placeholder="数据库中的安全码 " type="text" /><button id="securityBtn" class="task btn btn-info" type="button">确定</button></div></div>');
            $('#securityBtn').bind({
                click: function () {
                    securityCode = $('#security').val();
                    $("#msg_s").remove();
                    $('#securityBtn').unbind('click');
                    try {
                        if (state == 1)
                            scheduled.listTaskDataStart();
                        if (state == 2)
                            scheduled.getListJobStart();
                        if (state == 3)
                            scheduled.getListJobFailStart();
                        if (state == 4)
                            scheduled.getListJobSuccessStart();
                        if (state == 5)
                            scheduled.getHandOutListTaskStart();
                    }
                    catch (e) {
                    }
                    window.alert = function (txt) {
                        $("body").append('<div id="msg"><div id="msg_top">温&nbsp;馨&nbsp;提&nbsp;示&nbsp;</div><div id="msg_cont">' + txt + '</div></div>');
                        setTimeout(function () {
                            $("#msg").remove();
                        }, 1000);
                    }
                }
            });
        }
        alert("安全码");
    },
    alertReset: function () {       //弹窗初始化
        window.alert = function (txt) {
            $("body").append('<div id="msg"><div id="msg_top">温&nbsp;馨&nbsp;提&nbsp;示&nbsp;</div><div id="msg_cont">' + txt + '</div></div>');
            setTimeout(function () {
                $("#msg").remove();
            }, 1000);
        }
    },
    createHttpTask: function () {       //创建task
        window.alert = function (txt) {
            $("body").append('<div id="msg_t"><div id="msg_top_t">创建任务</div><div id="msg_cont_t">\n' +
                '                <div><label class="control-label" style="">任务名称</label>&nbsp;&nbsp;<input id="taskName" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="">调用地址</label>&nbsp;&nbsp;<input id="taskUrl" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="">调用类型</label>&nbsp;&nbsp;<select id="taskType" style="width: 500px;margin: 15px;" type="text" ><option value="POST">POST</option><option value="GET">GET</option></select></div>\n' +
                '                <div><label class="control-label" style="">执行时间</label>&nbsp;&nbsp;<input id="taskExecuteTime" onclick="scheduled.dateShow();" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="">间隔单元</label>&nbsp;&nbsp;<select id="taskExecuteType" style="width: 500px;margin: 15px;" ><option value="1">秒</option><option value="2">分</option><option value="3">时</option><option value="4">天</option><option value="5">周</option><option value="6">月</option><option value="7">年</option></select></div>\n' +
                '                <div><label class="control-label" style="">间隔单位</label>&nbsp;&nbsp;<input id="taskExecuteUnit" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="">超时时间</label>&nbsp;&nbsp;<input id="taskOverTime" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="">任务参数</label>&nbsp;&nbsp;<input id="taskArgs" style="width: 500px;margin: 15px;" type="text" /></div><div><button id="taskSubmit" class="task btn btn-info" type="button">确定</button>&nbsp;&nbsp;&nbsp;&nbsp;<button id="closeSubmit" class="task btn btn-info" type="button">取消</button></div></div>\n' +
                '        </div>');
            $('#taskSubmit').bind({
                click: function () {
                    //调用服务存储
                    Common.getData("/task/createOrUpdateHttpTask", "json", "post", false,
                        {
                            id: null,
                            name: $('#taskName').val(),
                            url: $('#taskUrl').val(),
                            type: $('#taskType').val(),
                            executeTime: $('#taskExecuteTime').val(),
                            executeType: $('#taskExecuteType').val(),
                            executeUnit: $('#taskExecuteUnit').val(),
                            overTime: $('#taskOverTime').val(),
                            args: $('#taskArgs').val(),
                            security: securityCode
                        },
                        function (data) {
                            $("#msg_t").remove();
                            $('#taskSubmit').unbind('click');
                            scheduled.alertReset();
                            if (data.state == -10086) {
                                scheduled.securityCheck();
                                return;
                            }
                            if (data.data) {
                                scheduled.refreshData();
                                alert("success")
                            }
                            else
                                alert('fail')
                        },
                        function (err) {
                            $("#msg_t").remove();
                            $('#taskSubmit').unbind('click');
                            scheduled.alertReset();
                            alert('fail')
                        }
                    )
                }
            });
            $('#closeSubmit').bind({
                click: function () {
                    $("#msg_t").remove();
                    $('#taskSubmit').unbind('click');
                    $('#closeSubmit').unbind('click');
                    scheduled.alertReset();
                }
            });
        }
        alert("安全码");
    },
    editHttpTask: function (taskId) {       //编辑task
        window.alert = function (txt) {
            $("body").append('<div id="msg_t"><div id="msg_top_t">创建任务</div><div id="msg_cont_t">\n' +
                '                <div><label class="control-label" style="">任务名称</label>&nbsp;&nbsp;<input id="taskName" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="">调用地址</label>&nbsp;&nbsp;<input id="taskUrl" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="">调用类型</label>&nbsp;&nbsp;<select id="taskType" style="width: 500px;margin: 15px;" type="text" ><option value="POST">POST</option><option value="GET">GET</option></select></div>\n' +
                '                <div><label class="control-label" style="">执行时间</label>&nbsp;&nbsp;<input id="taskExecuteTime" onclick="scheduled.dateShow();" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="">间隔单元</label>&nbsp;&nbsp;<select id="taskExecuteType" style="width: 500px;margin: 15px;" ><option value="1">秒</option><option value="2">分</option><option value="3">时</option><option value="4">天</option><option value="5">周</option><option value="6">月</option><option value="7">年</option></select></div>\n' +
                '                <div><label class="control-label" style="">间隔单位</label>&nbsp;&nbsp;<input id="taskExecuteUnit" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="">超时时间</label>&nbsp;&nbsp;<input id="taskOverTime" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="">任务参数</label>&nbsp;&nbsp;<input id="taskArgs" style="width: 500px;margin: 15px;" type="text" /></div><div><button id="taskSubmit" class="task btn btn-info" type="button">确定</button>&nbsp;&nbsp;&nbsp;&nbsp;<button id="closeSubmit" class="task btn btn-info" type="button">取消</button></div></div></div>\n' +
                '        </div>');
            $('#taskSubmit').bind({
                click: function () {
                    //调用服务存储
                    Common.getData("/task/createOrUpdateHttpTask", "json", "post", false,
                        {
                            id: taskId,
                            name: $('#taskName').val(),
                            url: $('#taskUrl').val(),
                            type: $('#taskType').val(),
                            executeTime: $('#taskExecuteTime').val(),
                            executeType: $('#taskExecuteType').val(),
                            executeUnit: $('#taskExecuteUnit').val(),
                            overTime: $('#taskOverTime').val(),
                            args: $('#taskArgs').val(),
                            security: securityCode
                        },
                        function (data) {
                            $("#msg_t").remove();
                            $('#taskSubmit').unbind('click');
                            scheduled.alertReset();
                            if (data.state == -10086) {
                                scheduled.securityCheck();
                                return;
                            }
                            if (data.data) {
                                scheduled.refreshData();
                                alert("success");
                            }
                            else
                                alert('fail');
                        },
                        function (err) {
                            $("#msg_t").remove();
                            $('#taskSubmit').unbind('click');
                            scheduled.alertReset();
                        }
                    )
                }
            });
            $('#closeSubmit').bind({
                click: function () {
                    $("#msg_t").remove();
                    $('#taskSubmit').unbind('click');
                    $('#closeSubmit').unbind('click');
                    scheduled.alertReset();
                }
            });
        }
        alert("安全码");
        //调用服务存储
        Common.getData("/task/searchTaskOne", "json", "post", false,
            {
                taskId: taskId,
                security: securityCode
            },
            function (data) {
                if (data.state == -10086) {
                    scheduled.securityCheck();
                    return;
                }
                if (data.state == 0) {
                    $('#taskName').val(data.data.strName);
                    $('#taskUrl').val(data.data.strUrl);
                    $('#taskType').val(data.data.strType);
                    $('#taskExecuteTime').val(data.data.dtExecuteTime);
                    $('#taskExecuteType').val(data.data.nExecuteType);
                    $('#taskExecuteUnit').val(data.data.nExecuteUnit);
                    $('#taskOverTime').val(data.data.nOverTime);
                    $('#taskArgs').val(data.data.strArgs);
                }
                else
                    alert('fail')
            },
            function (err) {
                $("#msg_t").remove();
                $('#taskSubmit').unbind('click');
                scheduled.alertReset();
                alert('fail')
            }
        )
    },
    startHttpTask: function (taskId) {      //启动task
        Common.getData("/task/startHttpTask", "json", "post", false,
            {
                id: taskId,
                security: securityCode
            },
            function (data) {
                scheduled.alertReset();
                if (data.state == -10086) {
                    scheduled.securityCheck();
                    return;
                }
                if (data.data) {
                    scheduled.refreshData();
                    alert("success");
                }
                else
                    alert('fail')
            },
            function (err) {
                scheduled.alertReset();
                alert('fail')
            }
        )
    },
    closeHttpTask: function (taskId) {      //关闭task
        Common.getData("/task/closeHttpTask", "json", "post", false,
            {
                id: taskId,
                security: securityCode
            },
            function (data) {
                scheduled.alertReset();
                if (data.state == -10086) {
                    scheduled.securityCheck();
                    return;
                }
                if (data.data) {
                    scheduled.refreshData();
                    alert("success");
                }
                else
                    alert('fail');
            },
            function (err) {
                scheduled.alertReset();
                alert('fail')
            }
        )
    },
    deleteHttpTask: function (taskId) {     //删除task
        confirm("确认删除", function () {
            Common.getData("/task/deleteHttpTask", "json", "post", false,
                {
                    id: taskId,
                    security: securityCode
                },
                function (data) {
                    scheduled.alertReset();
                    if (data.state == -10086) {
                        scheduled.securityCheck();
                        return;
                    }
                    if (data.data) {
                        scheduled.refreshData();
                        alert("success");
                    }
                    else
                        alert('fail')
                },
                function (err) {
                    scheduled.alertReset();
                    alert('fail')
                }
            )
        })
    },
    createHandleTask: function () {     //创建分发任务
        window.alert = function (txt) {
            //初始化弹窗
            $("body").append('<div id="msg_h"><div id="msg_top_h">创建分发任务</div><div id="msg_cont_h">\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">任务名称</label>&nbsp;&nbsp;<input id="taskName" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">执行时间</label>&nbsp;&nbsp;<input id="taskExecuteTime" onclick="scheduled.dateShow();" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">任务并发数</label>&nbsp;&nbsp;<input id="taskConcurrencyNumber" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">参数交互方式</label>&nbsp;&nbsp;<select id="taskParameterType" style="width: 500px;margin: 15px;" type="text" ><option value="1">MAP</option><option value="0">JSON</option></select></div>\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">获取地址</label>&nbsp;&nbsp;<input id="taskSourceURL" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">数据源获取方式</label>&nbsp;&nbsp;<select id="taskSourceType" style="width: 500px;margin: 15px;" type="text" ><option value="POST">POST</option><option value="GET">GET</option></select></div>\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">分发地址</label>&nbsp;&nbsp;<input id="taskDestinationURL" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">目的地分发方式</label>&nbsp;&nbsp;<select id="taskDestinationType" style="width: 500px;margin: 15px;" type="text" ><option value="POST">POST</option><option value="GET">GET</option></select></div>\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">间隔单元</label>&nbsp;&nbsp;<select id="taskExecuteType" style="width: 500px;margin: 15px;" ><option value="1">秒</option><option value="2">分</option><option value="3">时</option><option value="4">天</option><option value="5">周</option><option value="6">月</option><option value="7">年</option></select></div>\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">间隔单位</label>&nbsp;&nbsp;<input id="taskExecuteUnit" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">单次抓取超时时间</label>&nbsp;&nbsp;<input id="taskOverTime" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">单次处理超时时间</label>&nbsp;&nbsp;<input id="taskHandleTime" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><button id="taskHandleSubmit" class="task btn btn-info" type="button">确定</button>&nbsp;&nbsp;&nbsp;&nbsp;<button id="closeHandleSubmit" class="task btn btn-info" type="button">取消</button></div></div>\n' +
                '        </div>');
            //绑定确定按钮事件
            $('#taskHandleSubmit').bind({
                click: function () {
                    //调用服务存储
                    Common.getData("/task/createOrUpdateHandleTask", "json", "post", false,
                        {
                            id: null,
                            name: $('#taskName').val(),
                            executeTime: $('#taskExecuteTime').val(),
                            concurrencyNumber: $('#taskConcurrencyNumber').val(),
                            parameterType: $('#taskParameterType').val(),
                            sourceURL: $('#taskSourceURL').val(),
                            sourceType: $('#taskSourceType').val(),
                            destinationURL: $('#taskDestinationURL').val(),
                            destinationType: $('#taskDestinationType').val(),
                            executeType: $('#taskExecuteType').val(),
                            executeUnit: $('#taskExecuteUnit').val(),
                            overTime: $('#taskOverTime').val(),
                            handleTime: $('#taskHandleTime').val(),
                            security: securityCode
                        },
                        function (data) {   //成功
                            $("#msg_h").remove();   //删除弹窗
                            $('#taskHandleSubmit').unbind('click'); //解绑弹窗确定按钮事件
                            scheduled.alertReset(); //初始化弹窗
                            if (data.state == -10086) {     //安全校验码独有
                                scheduled.securityCheck();  //弹窗输入安全码
                                return;
                            }
                            if (data.data) {
                                scheduled.refreshData();    //成功则刷新页面数据
                                alert("success")    //弹窗成功
                            }
                            else
                                alert('fail')
                        },
                        function (err) {
                            $("#msg_h").remove();   //删除弹窗
                            $('#closeHandleSubmit').unbind('click'); //解绑弹窗确定按钮事件
                            scheduled.alertReset();
                            alert('fail')
                        }
                    )
                }
            });
            $('#closeHandleSubmit').bind({      //关闭弹窗
                click: function () {
                    $("#msg_h").remove();   //删除弹窗
                    $('#taskHandleSubmit').unbind('click'); //解绑弹窗确定按钮事件
                    $('#closeHandleSubmit').unbind('click'); //解绑弹窗确定按钮事件
                    scheduled.alertReset(); //弹窗初始化
                }
            });
        }
        alert("安全码");
    },
    editHandleTask: function (taskId) {     //编辑分发任务
        window.alert = function (txt) {
            //初始化弹窗类容
            $("body").append('<div id="msg_h"><div id="msg_top_h">创建分发任务</div><div id="msg_cont_h">\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">任务名称</label>&nbsp;&nbsp;<input id="taskName" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">执行时间</label>&nbsp;&nbsp;<input id="taskExecuteTime" onclick="scheduled.dateShow();" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">任务并发数</label>&nbsp;&nbsp;<input id="taskConcurrencyNumber" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">参数交互方式</label>&nbsp;&nbsp;<select id="taskParameterType" style="width: 500px;margin: 15px;" type="text" ><option value="1">MAP</option><option value="0">JSON</option></select></div>\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">获取地址</label>&nbsp;&nbsp;<input id="taskSourceURL" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">数据源获取方式</label>&nbsp;&nbsp;<select id="taskSourceType" style="width: 500px;margin: 15px;" type="text" ><option value="POST">POST</option><option value="GET">GET</option></select></div>\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">分发地址</label>&nbsp;&nbsp;<input id="taskDestinationURL" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">目的地分发方式</label>&nbsp;&nbsp;<select id="taskDestinationType" style="width: 500px;margin: 15px;" type="text" ><option value="POST">POST</option><option value="GET">GET</option></select></div>\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">间隔单元</label>&nbsp;&nbsp;<select id="taskExecuteType" style="width: 500px;margin: 15px;" ><option value="1">秒</option><option value="2">分</option><option value="3">时</option><option value="4">天</option><option value="5">周</option><option value="6">月</option><option value="7">年</option></select></div>\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">间隔单位</label>&nbsp;&nbsp;<input id="taskExecuteUnit" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">单次抓取超时时间</label>&nbsp;&nbsp;<input id="taskOverTime" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><label class="control-label" style="text-align: left;width: 150px">单次处理超时时间</label>&nbsp;&nbsp;<input id="taskHandleTime" style="width: 500px;margin: 15px;" type="text" /></div>\n' +
                '                <div><button id="taskHandleSubmit" class="task btn btn-info" type="button">确定</button>&nbsp;&nbsp;&nbsp;&nbsp;<button id="closeHandleSubmit" class="task btn btn-info" type="button">取消</button></div></div>\n' +
                '        </div>');
            //绑定确定按钮事件
            $('#taskHandleSubmit').bind({
                click: function () {
                    //调用服务存储
                    Common.getData("/task/createOrUpdateHandleTask", "json", "post", false,
                        {
                            id: taskId,
                            name: $('#taskName').val(),
                            executeTime: $('#taskExecuteTime').val(),
                            concurrencyNumber: $('#taskConcurrencyNumber').val(),
                            parameterType: $('#taskParameterType').val(),
                            sourceURL: $('#taskSourceURL').val(),
                            sourceType: $('#taskSourceType').val(),
                            destinationURL: $('#taskDestinationURL').val(),
                            destinationType: $('#taskDestinationType').val(),
                            executeType: $('#taskExecuteType').val(),
                            executeUnit: $('#taskExecuteUnit').val(),
                            overTime: $('#taskOverTime').val(),
                            handleTime: $('#taskHandleTime').val(),
                            security: securityCode
                        },
                        function (data) {   //成功
                            $("#msg_h").remove();   //删除弹窗
                            $('#taskHandleSubmit').unbind('click'); //解绑确定按钮事件
                            scheduled.alertReset(); //弹窗初始化
                            if (data.state == -10086) { //安全码校验独有返回值
                                scheduled.securityCheck();  //输入安全码
                                return;
                            }
                            if (data.data) {    //成功
                                scheduled.refreshData();    //刷新页面数据
                                alert("success");   //弹出成功
                            }
                            else
                                alert('fail');  //弹窗失败
                        },
                        function (err) {
                            $("#msg_h").remove();   //删除弹窗
                            $('#taskHandleSubmit').unbind('click'); //解绑确定按钮事件
                            scheduled.alertReset(); //初始化弹窗
                        }
                    )
                }
            });
            $('#closeHandleSubmit').bind({  //关闭按钮绑定事件
                click: function () {
                    $("#msg_h").remove();   //删除弹窗
                    $('#taskHandleSubmit').unbind('click'); //解绑确定按钮事件
                    $('#closeHandleSubmit').unbind('click');    //解绑关闭按钮事件
                    scheduled.alertReset(); //初始化弹窗
                }
            });
        }
        //弹出窗口 回写数据
        alert("安全码");
        //调用服务存储 获取回写数据
        Common.getData("/task/searchHandleTaskOne", "json", "post", false,
            {
                taskId: taskId,
                security: securityCode
            },
            function (data) {
                if (data.state == -10086) { //安全码校验吗独有返回值
                    scheduled.securityCheck();
                    return;
                }
                if (data.state == 0) {  //填写回写数据
                    $('#taskName').val(data.data.strName);
                    $('#taskExecuteTime').val(data.data.dtExecuteTime);
                    $('#taskConcurrencyNumber').val(data.data.nConcurrencyNumber);
                    $('#taskParameterType').val(data.data.nParameterType);
                    $('#taskSourceURL').val(data.data.strSourceURL);
                    $('#taskSourceType').val(data.data.strSourceType);
                    $('#taskDestinationURL').val(data.data.strDestinationURL);
                    $('#taskDestinationType').val(data.data.strDestinationType);
                    $('#taskExecuteType').val(data.data.nExecuteType);
                    $('#taskExecuteUnit').val(data.data.nExecuteUnit);
                    $('#taskOverTime').val(data.data.nOverTime);
                    $('#taskHandleTime').val(data.data.nHandleTime);
                }
                else
                    alert('fail')
            },
            function (err) {
                $("#msg_h").remove();   //删除弹窗
                $('#taskHandleSubmit').unbind('click'); //解绑确定按钮事件
                scheduled.alertReset(); //初始化弹窗
                alert('fail')
            }
        )
    },
    startHandleTask: function (taskId) {
        Common.getData("/task/startHandleTask", "json", "post", false,
            {
                id: taskId,
                security: securityCode
            },
            function (data) {
                scheduled.alertReset();
                if (data.state == -10086) {
                    scheduled.securityCheck();
                    return;
                }
                if (data.data) {
                    scheduled.refreshData();
                    alert("success");
                }
                else
                    alert('fail')
            },
            function (err) {
                scheduled.alertReset();
                alert('fail')
            }
        )
    },
    closeHandleTask: function (taskId) {
        Common.getData("/task/closeHandleTask", "json", "post", false,
            {
                id: taskId,
                security: securityCode
            },
            function (data) {
                scheduled.alertReset();
                if (data.state == -10086) {
                    scheduled.securityCheck();
                    return;
                }
                if (data.data) {
                    scheduled.refreshData();
                    alert("success");
                }
                else
                    alert('fail');
            },
            function (err) {
                scheduled.alertReset();
                alert('fail')
            }
        )
    },
    deleteHandleTask: function (taskId) {
        confirm("确认删除", function () {
            Common.getData("/task/deleteHandleTask", "json", "post", false,
                {
                    id: taskId,
                    security: securityCode
                },
                function (data) {
                    scheduled.alertReset();
                    if (data.state == -10086) {
                        scheduled.securityCheck();
                        return;
                    }
                    if (data.data) {
                        scheduled.refreshData();
                        alert("success");
                    }
                    else
                        alert('fail')
                },
                function (err) {
                    scheduled.alertReset();
                    alert('fail')
                }
            )
        });
    },
    dateShow: function () {
        WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', readOnly: true})
    },
    refreshData: function () {
        if (securityCode == null || securityCode == "") {
            scheduled.securityCheck();
        }
        if (state == 1)
            scheduled.listTaskDataStart();
        if (state == 2)
            scheduled.getListJobStart();
        if (state == 3)
            scheduled.getListJobFailStart();
        if (state == 4)
            scheduled.getListJobSuccessStart();
        if (state == 5)
            scheduled.getHandOutListTaskStart();
    }
}

$(document).ready(function () {

    //安全码检查
    if (securityCode == null || securityCode == "") {
        scheduled.securityCheck();
    }

    //默认弹窗
    window.alert = function (txt) {
        //$("body").append('<div id="msg"><div id="msg_top">温馨提示<span class="msg_close">×</span></div><div id="msg_cont">' + txt + '</div><div class="msg_close" id="msg_clear">确定</div></div>');
        $("body").append('<div id="msg"><div id="msg_top">温&nbsp;馨&nbsp;提&nbsp;示&nbsp;</div><div id="msg_cont">' + txt + '</div></div>');
        /*$(".msg_close").click(function () {
            $("#msg").remove();
        });*/
        setTimeout(function () {
            $("#msg").remove();
        }, 1000);
    }

    //默认弹窗
    window.confirm = function (txt, fun) {
        $("body").append('<div id="msg_c"><div id="msg_top_c">温&nbsp;馨&nbsp;提&nbsp;示&nbsp;</div><div id="msg_cont_c">' + txt + '</div><button id="msg_sure_c" class="task btn btn-info" type="button">确定</button>&nbsp;&nbsp;&nbsp;&nbsp;<button id="msg_clear_c" class="task btn btn-info" type="button">取消</button></div>');
        $("#msg_clear_c").click(function () {
            $("#msg_c").remove();
        });
        $("#msg_sure_c").click(function () {
            $("#msg_c").remove();
            fun();
        });
    }

    //获取任务列表按钮
    $('#listTaskData').bind({
        click: function () {
            if (securityCode == null || securityCode == "") {
                scheduled.securityCheck();
            }
            state = 1;
            scheduled.listTaskDataStart();
        }
    });

    //获取JOB列表按钮
    $('#getListJob').bind({
        click: function () {
            if (securityCode == null || securityCode == "") {
                scheduled.securityCheck();
            }
            state = 2;
            scheduled.getListJobStart();
        }
    });

    //获取失败的JOB列表按钮
    $('#getListJobFail').bind({
        click: function () {
            if (securityCode == null || securityCode == "") {
                scheduled.securityCheck();
            }
            state = 3;
            scheduled.getListJobFailStart();
        }
    });

    //获取成功的JOB列表按钮
    $('#getListJobSuccess').bind({
        click: function () {
            if (securityCode == null || securityCode == "") {
                scheduled.securityCheck();
            }
            state = 4;
            scheduled.getListJobSuccessStart();
        }
    });

    //获取分发任务列表按钮
    $('#getHandOutListTask').bind({
        click: function () {
            if (securityCode == null || securityCode == "") {
                scheduled.securityCheck();
            }
            state = 5;
            scheduled.getHandOutListTaskStart();
        }
    });

    //启动按钮
    $('#start').bind({
        click: function () {
            if (securityCode == null || securityCode == "") {
                scheduled.securityCheck();
            }
            scheduled.start();
        }
    });

    //停止按钮
    $('#stop').bind({
        click: function () {
            if (securityCode == null || securityCode == "") {
                scheduled.securityCheck();
            }
            scheduled.stop();
        }
    });

    //全部启动按钮
    $('#startAll').bind({
        click: function () {
            if (securityCode == null || securityCode == "") {
                scheduled.securityCheck();
            }
            scheduled.startAll();
        }
    });

    //全部停止按钮
    $('#stopAll').bind({
        click: function () {
            if (securityCode == null || securityCode == "") {
                scheduled.securityCheck();
            }
            scheduled.stopAll();
        }
    });

    //查询按钮
    $('#search').bind({
        click: function () {
            if (securityCode == null || securityCode == "") {
                scheduled.securityCheck();
            }
            if (state == 1)
                scheduled.listTaskDataStart();
            if (state == 2)
                scheduled.getListJobStart();
            if (state == 3)
                scheduled.getListJobFailStart();
            if (state == 4)
                scheduled.getListJobSuccessStart();
            if (state == 5)
                scheduled.getHandOutListTaskStart();
        }
    });

    //刷新页面按钮
    $('#refresh').bind({
        click: function () {
            $(this).attr('disabled', true);
            if (refresh) refresh = false
            else refresh = true
            if (refresh) {
                var temp = $('#time').val();
                var re = /^[1-9]+[0-9]*]*$/;
                if (!re.test(temp) || temp == null || temp == "") {
                    if (refresh) refresh = false
                    else refresh = true
                    $(this).attr('disabled', false);
                    alert('fail');
                    return;
                }
                time = parseInt(temp);
                if (time < 1)
                    time = 1;
                time = time * 1000;
            }
            try {
                if (state == 1)
                    scheduled.listTaskDataStart();
                if (state == 2)
                    scheduled.getListJobStart();
                if (state == 3)
                    scheduled.getListJobFailStart();
                if (state == 4)
                    scheduled.getListJobSuccessStart();
                if (state == 5)
                    scheduled.getHandOutListTaskStart();
            }
            catch (e) {
            }
            $(this).attr('disabled', false);
            $(this).text(refresh ? "关闭刷新" : '开启刷新');
            alert('success');
        }
    });

    //刷新每页数据量按钮
    $('#refreshPageSize').bind({
        click: function () {
            $(this).attr('disabled', true);
            var temp = $('#pageSize').val();
            var re = /^[1-9]+[0-9]*]*$/;
            if (!re.test(temp) || temp == null || temp == "") {
                $(this).attr('disabled', false);
                alert('fail');
                return;
            }
            pageSize = parseInt('10', temp);
            if (pageSize < 1)
                pageSize = 1;
            try {
                if (state == 1)
                    scheduled.listTaskDataStart();
                if (state == 2)
                    scheduled.getListJobStart();
                if (state == 3)
                    scheduled.getListJobFailStart();
                if (state == 4)
                    scheduled.getListJobSuccessStart();
                if (state == 5)
                    scheduled.getHandOutListTaskStart();
            }
            catch (e) {
            }
            $(this).attr('disabled', false);
            alert('success');
        }
    });

    //穿件Task按钮
    $('#taskCreate').bind({
        click: function () {
            if (securityCode == null || securityCode == "") {
                scheduled.securityCheck();
            }
            scheduled.createHttpTask();
        }
    });

    //创建分发任务按钮
    $('#handleCreate').bind({
        click: function () {
            if (securityCode == null || securityCode == "") {
                scheduled.securityCheck();
            }
            scheduled.createHandleTask();
        }
    });

    //刷新任务状态
    setInterval(function () {
        if (securityCode == null || securityCode == "") {
            return;
        }
        scheduled.getTaskState();
    }, 1000);

    //按钮可用性按钮点击刷新
    setInterval(function () {
        if (securityCode == null || securityCode == "") {
            return;
        }
        scheduled.stateRub("run");
    }, 100);

    //初始化TASK   页面打开默认为Task列表
    state = 1;
    scheduled.listTaskDataStart();
});