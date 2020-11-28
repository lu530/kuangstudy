$(function () {
    initEvent();
});



function initEvent(){
    $("[name='uploadBtn']").on("click", function(){
        var url = $("#userIcon").attr("src");
        var uid = $("[name='uid']").attr("value");
        $.ajax({
            async : false,
            type : "POST",
            url : "/user/update/avatar",
            dataType: "json",
            data:{
                url:url,
                userId:uid
            },
            success : function(data) {
                if(data.code == 200) {
                    $("#userAvatar").attr("src",url);
                    layer.alert("重新登录可以全局图标才会更新");
                }else{
                    alert(data.message);
                }
            },
            error : function() {}
        })
    })
}