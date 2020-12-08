
function imageValidate(rePassword){
    var mailCount =  $('[name="email"]').val();
   // var mailCount = $("[]").val();
    if(!mailCount){
        layer.alert("请填写邮箱号");
        return;
    }
    var urlPath = window.document.location.href;
    var docPath = window.document.location.pathname;
    var index = urlPath.indexOf(docPath);
    var serverPath = urlPath.substring(0, index);
    var index1 = layer.open({
        type: 2,
        area: ['750px', '550px'],
        fixed: false, //不固定
        maxmin: true,
        content: serverPath + '/imgValidate?email=' + mailCount + '&rePassword=' + rePassword
    });
}