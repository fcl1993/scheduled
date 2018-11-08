/**
 * 数据请求
 *
 * @author 付成垒
 *
 * @type {{getData: Common.getData}}
 */
var Common = {

    getData: function (url, dataType, type, cache, data, success, error) {
        jQuery.ajax({
            type: type,
            url: url,
            cache: cache,
            data: data,
            dataType: dataType,
            success: success,
            error: error
        });
    }

}