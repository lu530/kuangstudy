
function initEvent(){
    $(".comment-reply-span").on("click", function(){
        console.log("显示回复输入框");
        $(".comment-reply-input").addClass("hide");//将其他的回复框都隐藏掉
        if($(this).hasClass("reply-open")){
            $(this).parent().parent().next().addClass("hide");
            $(this).text("回复");
            $(this).removeClass("reply-open");
            $(this).addClass("reply-close");
        }else{
            $(this).parent().parent().next().removeClass("hide");
            $(this).text("收起");
            $(this).removeClass("reply-close");
            $(this).addClass("reply-open");
        }
    });


    $("#thumbsUp").on("click", function(){
        var bid = $(this).attr("bid");
        var uid = $(this).attr("uid")
        thumbsUpDown(uid, bid, 1);
    });

    $("#thumbsDown").on("click", function(){
        var bid = $(this).attr("bid");
        var uid = $(this).attr("uid")
        thumbsUpDown(uid, bid, -1);
    });
}


function thumbsUpDown(uid, bid, thunbsFlag){
    if(uid && bid && thunbsFlag){
        $.ajax({
            async : false,
            type : "POST",
            url : "/thumbs/add",
            dataType: "json",
            data:{
                topicId:bid,
                userId:uid,
                thumbsFlag:thunbsFlag,
                topicCategory:2
            },
            success : function(data) {
                if(data.code == 200) {

                    if (thunbsFlag == 1) {
                        var val = $("#thumbsUpNum").text();
                        $("#thumbsUpNum").text(++val);
                    } else {
                        var val = $("#thumbsDownNum").text();
                        $("#thumbsDownNum").text(++val);
                    }
                }else{
                    alert(data.message);
                }
            },
            error : function() {}
        })
    }else{
        alert("登录后才可以进行此操作");
    }


}


