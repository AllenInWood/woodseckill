function g_showLoading() {
    var idx = layer.msg("handling...", {icon: 16, shade: [0.5, '#f5f5f5'], scrollbar: false, offset: '0px', time: 10000});
    return idx;
}

var g_password_salt = "1a2b3c4d";

// get url attr
function g_getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}

// Date format, use eg: new Date().format("yyyMMddhhmmss")
Date.prototype.format = function (format) {
    var args = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
    };
    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    for(var k in args)
        if(new RegExp("("+ k +")").test(format))
            format = format.replace(RegExp.$1, (RegExp.$1.length==1) ? (args[k]) : (("00"+ args[k]).substr((""+ args[k]).length)));
    return format;
}