function getUrlParam(name) {
    // 取得url中?后面的字符
    const query = window.location.search.substring(1);
    // 把参数按&拆分成数组
    const param_arr = query.split("&");
    for (let i = 0; i < param_arr.length; i++) {
        const pair = param_arr[i].split("=");
        if (pair[0] == name) {
            return pair[1];
        }
    }
    return '';
}


// 为侧边栏添加链接点击效果
let type = getUrlParam('tab');
if (type == '' || type == null) {
    type = 'details';
}
$('#tab-' + type).click();

// 获得表单数据
//form表单数据转为JSON格式
function getFormData($form) {
    //serializeArray()把form表单的值序列化成一个数组
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function (n, i) {
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}

//获取url中的参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]);
    return null; //返回参数值
}

/**
 * 提示消息：JQuery信息提示框插件 jquery.toast.js 的使用
 * @param text
 * @param icon
 * @param hideAfter
 */
function showMsg(text, icon, hideAfter) {
    const heading = "提示";
    $.toast({
        text: text,//消息提示框的内容。
        heading: heading,
        icon: icon,//消息提示框的图标样式
        showHideTransition: 'fade',//消息提示框的动画效果。可取值：plain，fade，slide
        allowToastClose: true,//是否显示关闭按钮。(true 显示，false 不显示)
        hideAfter: hideAfter,//一秒后关闭
        stack: 1,
        position: 'top-center',
        textAlign: 'left',
        loader: true,
        loaderBg: '#ffffff'
    });
}
//提示且跳转
function showMsgAndRedirect(text, icon, hideAfter, url) {
    const heading = "提示";
    $.toast({
        text: text,
        heading: heading,
        icon: icon,
        showHideTransition: 'fade',
        allowToastClose: true,
        hideAfter: hideAfter,
        stack: 1,
        position: 'top-center',
        textAlign: 'left',
        loader: true,
        loaderBg: '#ffffff',
        afterHidden: function () {
            window.location.href = url;
        }
    });
}


function showMsgAndReload(text, icon, hideAfter) {
    const heading = "提示";
    $.toast({
        text: text,
        heading: heading,
        icon: icon,
        showHideTransition: 'fade',
        allowToastClose: true,
        hideAfter: hideAfter,
        stack: 1,
        position: 'top-center',
        textAlign: 'left',
        loader: true,
        loaderBg: '#ffffff',
        afterHidden: function () {
            window.location.reload();
        }
    });
}

/**
 * 登录提交
 */
$('#login_submit').click(function () {
    $.ajax({
        type: 'post',
        url: '/login',
        data: $('#login_form').serialize(),
        success: function (data) {
            console.log(data);
            if (data.code == 0) {
                if (data.data.type == 1) {
                    //如果是管理员跳转到后台登录页面
                    showMsgAndRedirect(data.msg, "success", 1000, "/account?tab=details");
                } else {
                    showMsgAndRedirect(data.msg, "success", 1000, "/");
                }
            } else {
                showMsg(data.msg, "error", 1000);
            }
        }
    });
});

/**
 * 注册提交
 */
$('#register_submit').click(function () {
    $.ajax({
        type: 'post',
        url: '/register',
        data: $('#register_form').serialize(),
        success: function (data) {
            console.log(data);
            if (data.code == 0) {
                showMsgAndRedirect(data.msg, "success", 1000, "/login");
            } else {
                showMsg(data.msg, "error", 1000);
            }
        }
    });
});

/**
 * 管理员注册提交
 */
$('#managerregister_submit').click(function () {
    $.ajax({
        type: 'post',
        url: '/managerregister',
        data: $('#managerregister_form').serialize(),
        success: function (data) {
            console.log(data);
            if (data.code == 0) {
                showMsg(data.msg, "success", 1000);
            } else {
                showMsg(data.msg, "error", 1000);
            }
        }
    });
});
/**
 * 加载种类菜单，点击就马上切换
 */
function loadCategoryList() {
    $.ajax({
        type: 'get',
        url: '/category',
        success: function (data) {
            console.log(data);
            if (data.code == 0) {
                let str = '';
                $.each(data.data, function (i, item) {
                    str += '<li><a href="/?cateId=' + item.id + '">' + item.name + '(' + item.count + ')' + '</a></li>'
                });
                $('#categories_ul').html(str);
            } else {
                showMsg(data.msg, "error", 1000);
            }
        }
    });

};
loadCategoryList();

/**
 * 删除用户记录
 */
$('.delete_user').click(function () {
    const id = $(this).attr('data-id');
    const tr = $(this).parents('tr');
    $.ajax({
        type: 'delete',
        url: '/account/user/' + id,
        success: function (data) {
            console.log(data);
            if (data.code == 0) {
                showMsg(data.msg, "success", 1000);
                tr.remove();
            } else {
                showMsg(data.msg, "error", 1000);
            }
        }
    });
});

/**
 * 更新用户状态
 */
$('.update_user_status').click(function () {
    var a = $(this);
    var text = a.text();
    const id = a.attr('data-id');
    $.ajax({
        type: 'post',
        url: '/account/user/status/' + id,
        success: function (data) {
            console.log(data);
            if (data.code == 0) {
                if (text == '禁用') {
                    a.html('启用');
                    a.removeClass('text-danger');
                    a.addClass('text-success');
                } else {
                    a.html('禁用');
                    a.removeClass('text-success');
                    a.addClass('text-danger');
                }
                showMsg(data.msg, "success", 1000);
            } else {
                showMsg(data.msg, "error", 1000);
            }
        }
    });
});

/**
 * 删除竞价记录
 */
$('.delete_bidding').click(function () {
    const id = $(this).attr('data-id');
    const tr = $(this).parents('tr');
    $.ajax({
        type: 'delete',
        url: '/account/bidding/' + id,
        success: function (data) {
            console.log(data);
            if (data.code == 0) {
                showMsg(data.msg, "success", 1000);
                tr.remove();
            } else {
                showMsg(data.msg, "error", 1000);
            }
        }
    });
});

/**
 * 删除商品记录
 */
$('.delete_product').click(function () {
    const id = $(this).attr('data-id');
    const tr = $(this).parents('tr');
    $.ajax({
        type: 'delete',
        url: '/account/product/' + id,
        success: function (data) {
            console.log(data);
            if (data.code == 0) {
                showMsg(data.msg, "success", 1000);
                tr.remove();//移除该条
            } else {
                showMsg(data.msg, "error", 1000);
            }
        }
    });
});

/**
 * 上架商品记录
 */
$('.grounding_product').click(function () {
    const id = $(this).attr('data-id');
    $.ajax({
        type: 'post',
        url: '/account/productGrounding/' + id,
        success: function (data) {
            console.log(data);
            if (data.code == 0) {
                showMsg(data.msg, "success", 1000);
                window.location.reload();
            } else {
                showMsg(data.msg, "error", 1000);
            }
        }
    });
});

/**
 * 下架商品记录
 */
$('.off_product').click(function () {
    const id = $(this).attr('data-id');
    $.ajax({
        type: 'post',
        url: '/account/productOff/' + id,
        success: function (data) {
            console.log(data);
            if (data.code == 0) {
                showMsg(data.msg, "success", 1000);
                window.location.reload();
            } else {
                showMsg(data.msg, "error", 1000);
            }
        }
    });
});
function add0(m){return m<10?'0'+m:m }
function format(firsttime)
{
    var time = new Date(firsttime);
    var y = time.getFullYear();
    var m = time.getMonth()+1;
    var d = time.getDate();
    var h = time.getHours();
    var mm = time.getMinutes();
    var s = time.getSeconds();
    return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
}
/**
 * 新增竞价
 */
$('.add_bidding').click(function () {
    const id = $(this).attr('data-id');
    let price = $(this).prev('.price').val();
    if (price.toString().trim().length <= 0) {
        showMsg("请输入正确竞价", "error", 1000);
        return;
    }
    $.ajax({
        type: 'post',
        url: '/bidding',
        data: {
            'productId': id,
            'price': price
        },
        success: function (data) {
            console.log(data);
            if (data.code == 0) {
                var str = "";
                for(var i= 0;i<data.data.length;i++){
                    var onum = i+1;
                    str += "<tr><td>" + onum +
                        "</td><td>" + data.data[i].user.username +
                        "</td><td>" +'¥'+data.data[i].price +'.00'+
                        "</td><td>" + format(data.data[i].createTime) + "</td></tr>";
                }
                var pr ='<p class="current_price" title="最高价">最高价：<font style="font-size: 1.5em; color: red;">¥'+data.data[0].price +'.00'+'</font></p>';
                $('.current_price').html(pr);
                $("#biddingtable").html(str);
                showMsg(data.msg, "success", 2000);
            } else {
                showMsg(data.msg, "error", 1000);
            }
        }
    });
});
/**
*充值按钮 充值金额检查
 */
$('#paybutton').click(function(){
    let price = $('#balance').val();
        if (price.toString().trim().length <= 0) {
            showMsg("请输入正确充值金额", "error", 1000);
            return;
        }
    else{
        $('#image').show();
        $('#paybutton').hide();
        $('#pay_submit').show();
    }
});
/**
 * 充值成功
 */
$('#pay_submit').click(function () {
    $.ajax({
        type: 'post',
        url: '/account/priceupdate',
        contentType: "application/json; charset=utf-8",
        dataType: 'json',//json 返回值类型
        data: JSON.stringify(getFormData($('#pay_form'))),
        success: function (data) {
            $('#balance').val("");
            var str = '<p id = "currentprice">'+data.data+'.00'+'</p>'
            $('#currentprice').html(str);
            if (data.code == 0) {
                showMsgAndReload(data.msg, "success", 10);
            } else {
                showMsg(data.msg, "error", 1000);
            }
        }
    });
});

// 保存用户信息
$('#save_account_submit').click(function () {
    $.ajax({
        type: 'post',
        url: '/account/details',
        contentType: "application/json; charset=utf-8",
        dataType: 'json',//json 返回值类型
        data: JSON.stringify(getFormData($('#user_account_form'))),
        success: function (data) {
            console.log(data);
            if (data.code == 0) {
                showMsgAndReload(data.msg, "success", 1000);
            } else {
                showMsg(data.msg, "error", 1000);
            }
        }
    });
});
//密码修改
$('#save_password_submit').click(function () {
    $.ajax({
        type: 'post',
        url: '/account/password',
        data: $('#user_password_form').serialize(),
        success: function (data) {
            console.log(data);
            if (data.code == 0) {
                showMsg(data.msg, "success", 1000);
            } else {
                showMsg(data.msg, "error", 1000);
            }
        }
    });
});

/**
 * 上传图片
 */
$('body').on('change', '#file', function () {
    var formData = new FormData();
    var files = $($(this))[0].files[0];
    //添加数据
    formData.append("file", files);
    $.ajax({
        url: '/file/upload',
        type: 'POST',
        data: formData,
        //上传文件的时候，不需要把其转换为字符串,所以false
        processData: false,
        //把contentType 改成 false，会改掉之前默认的数据格式，上传文件时不会报错。
        contentType: false,
        dataType: 'json',
        success: function (res) {
            console.log(res);
            $('#imgUrl').val(res.data);
            // alert('上传成功');
        }
        , error: function (res) {
            // alert('错误');
        }
    });
})

/**
 * 发布商品提交
 */
$('#product_submit').click(function () {
    $.ajax({
        type: 'post',
        url: '/account/product',
        data: $('#product_form').serialize(),
        success: function (data) {
            console.log(data);
            if (data.code == 0) {
                $('#id').val(data.data);
                showMsg(data.msg, "success", 1000);
                window.location.href = "/";
            } else {
                showMsg(data.msg, "error", 1000);
            }
        }
    });
});

/**
 * 管理员修改用户信息
 */
$('#user_submit').click(function () {
    $.ajax({
        type: 'post',
        url: '/account/user/adminUpdateUser',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(getFormData($('#user_form'))),
        success: function (data) {
            console.log(data);
            if (data.code == 0) {
                showMsg(data.msg, "success", 1000);
                window.location.href = "/account?tab=details";
                //showMsgAndRedirect(data.msg, "success", 1000, "/account?tab=details");
            } else {
                showMsg(data.msg, "error", 1000);
            }
        }
    });
});

/**
 * 管理员修改订单收货地址信息
 */
$('#order_submit').click(function () {
    $.ajax({
        type: 'post',
        url: '/account/order/orderAddressUpdate',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(getFormData($('#order_form'))),
        success: function (data) {
            console.log(data);

            if (data.code == 0) {
                showMsgAndRedirect(data.msg, "success", 1000, "/account?tab=details");
            } else {
                showMsg(data.msg, "error", 1000);
            }
        }
    });
});


/**
* dataTable分页
*/
const datatable_lang = {
    "sProcessing": "处理中...",
    "sLengthMenu": "显示 _MENU_ 项结果",
    "sZeroRecords": "没有匹配结果",
    "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
    "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
    "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
    "sInfoPostFix": "",
    "sSearch": "搜索:",
    "sUrl": "",
    "sEmptyTable": "表中数据为空",
    "sLoadingRecords": "载入中...",
    "sInfoThousands": ",",
    "oPaginate": {
        "sFirst": "首页",
        "sPrevious": "上页",
        "sNext": "下页",
        "sLast": "末页"
    },
    "oAria": {
        "sSortAscending": ": 以升序排列此列",
        "sSortDescending": ": 以降序排列此列"
    }
};

/**
 * 商品列表
 */
$(function () {
    $('#products-table').DataTable({
        language: datatable_lang,
        'pageLength':5,
        "lengthMenu": [　                           //显示几条数据设置
            [5, 10, 20, -1],
            ['5 ', '10 ', '20 ', '全部']
        ],
        'paging': true,
        'lengthChange': true,
        'searching': true,
        'ordering': false,
        'info': true,
        'autoWidth': false
    })
});


/**
 * 竞价列表分页
 */
$(function () {
    $('#biddings-table').DataTable({
        language: datatable_lang,
        'pageLength':5,
        "lengthMenu": [　                           //显示几条数据设置
            [5, 10, 20, -1],
            ['5 ', '10 ', '20 ', '全部']
        ],
        'paging': true,
        'lengthChange': true,
        'searching': true,
        'ordering': false,
        'info': true,
        'autoWidth': false
    })
});

/**
 * 用户列表分页
 */
$(function () {
    $('#users-table').DataTable({
        language: datatable_lang,
        'pageLength':5,
        "lengthMenu": [　                           //显示几条数据设置
            [5, 10, 20, -1],
            ['5 ', '10 ', '20 ', '全部']
        ],
        'paging': true,
        'lengthChange': true,
        'searching': true,
        'ordering': false,
        'info': true,
        'autoWidth': false
    })
});


/**
 * 订单列表
 */
$(function () {
    $('#orders-table').DataTable({
        language: datatable_lang,
        'pageLength':5,
        "lengthMenu": [　                           //显示几条数据设置
            [5, 10, 20, -1],
            ['5 ', '10 ', '20 ', '全部']
        ],
        'paging': true,
        'lengthChange': true,
        'searching': true,
        'ordering': false,
        'info': true,
        'autoWidth': false,
        "Destroy" : true
    })
});

$('#productEndTime').fdatepicker({
    format: 'yyyy-mm-dd hh:ii:ss',
    pickTime: true
});

