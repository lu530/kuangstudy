var left = 0;
var initLeft = 0;
$(function(){
    // 初始化图片验证码
    initImageValidate();
    /* 初始化按钮拖动事件 */
    // 鼠标点击事件
 /*   $("#sliderInner").mousedown(function(){
        console.log("鼠标已经按下!");
        // 鼠标移动事件
        $(document).mousemove(function (ev) {
        //document.onmousemove = function(ev) {
            console.log("onmousemove 触发");
            left = ev.clientX;
            //if(left >= 67 && left <= 563){
                console.log("left " + left);
                $("#sliderInner").css("left",(left-67)+"px");
                $("#slideImage").css("left",(left-67)+"px");
          //  }
        });
        // 鼠标松开事件
        document.onmouseup=function(){
            document.onmousemove=null;
            checkImageValidate();
        };
    });*/


    $('#sliderInner').mousedown(function(){
        initLeft = $(this).offset().left;
        $(document).mousemove(function(e){
           /* $("#sliderInner").css("left",e.pageX);
            $("#slideImage").css("left",e.pageX);*/
               // left = e.pageX;
                left = e.clientX - initLeft;
                $('#sliderInner').offset({
                    left:e.pageX
                });
                $("#slideImage").offset({
                    left:e.pageX
                });
        });
        $('#sliderInner').mouseup(function(){
            $(document).off('mousemove');
            checkImageValidate();
        })

    })

});

function initImageValidate(){
    $.ajax({
        async : false,
        type : "POST",
        url : "/createImgValidate",
        dataType: "json",
        data:{
            email:telephone,
            rePassword:rePassword
        },
        success : function(data) {
            if(data.status == 200){
                // 设置图片的src属性
                $("#validateImage").attr("src", "data:image/png;base64,"+data.data.oriCopyImage);
                $("#slideImage").attr("src", "data:image/png;base64,"+data.data.newImage);
            }else{
                var index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);//关闭当前页
                parent.layer.open({
                    icon:2,
                    title: "温馨提示",
                    content: data.info
                });
            }
        },
        error : function() {}
    });
}

function exchange(){
    initImageValidate();
}

// 校验
function checkImageValidate(){
    $.ajax({
        async : true,
        type : "POST",
        url : "/checkImgValidate",
        dataType: "json",
        data:{
            telephone:telephone,
            offsetHorizontal:left
        },
        success : function(data) {
            if(data.status < 400){
                $("#operateResult").html(data.info).css("color","#28a745");
                // 校验通过，调用发送短信的函数
               // parent.getValidateCode(left);
                parent.layer.alert(data.info);
                $("#imageValidateBtn", window.parent.document).addClass("hide");
                var index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);//关闭当前页
            }else{
                parent.layer.alert("校验不通过，请重新校验！");
                $("#operateResult").html(data.info).css("color","#dc3545");
                // 验证未通过，将按钮和拼图恢复至原位置
                $("#sliderInner").animate({"left":"0px"},200);
                $("#slideImage").animate({"left":"0px"},200);
            }
        },
        error : function() {}
    });
}