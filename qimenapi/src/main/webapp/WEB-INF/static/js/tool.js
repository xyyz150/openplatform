$(document).ready(function () {
    $.fn.select2.defaults.set("theme", "bootstrap");
    $.extend($.validator.messages, {
        required: "这是必填字段",
        remote: "请修正此字段",
        email: "请输入有效的电子邮件地址",
        url: "请输入有效的网址",
        date: "请输入有效的日期",
        dateISO: "请输入有效的日期 (YYYY-MM-DD)",
        number: "请输入有效的数字",
        digits: "只能输入数字",
        creditcard: "请输入有效的信用卡号码",
        equalTo: "你的输入不相同",
        extension: "请输入有效的后缀",
        maxlength: $.validator.format("最多可以输入 {0} 个字符"),
        minlength: $.validator.format("最少要输入 {0} 个字符"),
        rangelength: $.validator.format("请输入长度在 {0} 到 {1} 之间的字符串"),
        range: $.validator.format("请输入范围在 {0} 到 {1} 之间的数值"),
        max: $.validator.format("请输入不大于 {0} 的数值"),
        min: $.validator.format("请输入不小于 {0} 的数值")
    });
    var apiList;
    $.ajax({
        type: "get",
        url: '/tool/getApiList',
        async: false,
        dataType: "json",
        contentType: "application/json",
        success: function (data) {
            apiList = data;
        }
    });
    $('#apiName').select2({
        //minimumResultsForSearch: Infinity,
        data: apiList,
        allowClear: true
    });
    $('#apiName').on('select2:select', function (e) {
        $.ajax({
            type: "get",
            url: '/tool/getApiInfo?name=' + e.params.data.id,
            async: false,
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                $('#version').val(data.version);
            }
        });
    })

    /*var validater = $('#myform').validate({
        onfocusout: true,
        onkeyup: true
    });*/

    $('#tijiao').click(function () {
        var isok = $('#myform').valid();
        if (isok == false)return;
        var postdata = {};
        var $apiname = $('#apiName');
        postdata.method = $apiname.select2('data')[0].id;
        postdata.format = $('#format').val();
        postdata.v = $('#version').val();
        postdata.app_key = $('#appkey').val();
        postdata.app_serect = $('#appsercet').val();
        postdata.sessionkey = $('#sessionkey').val();
        postdata.body = $('#body').val();
        $.ajax({
            type: "POST",
            url: "/tool/postTest",
            dataType: "json",
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify(postdata),
            success: function (data) {
                $('#requestbody').val(data.request);
                $('#responsebody').val(data.response);
            }
        });
    });
})
;
