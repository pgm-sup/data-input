//调用OCUpload插件的方法
$("#uploadExcel").upload({
    action:"/table/uploadExcel/", //表单提交的地址
    name:"myFile",
    onComplete:function (data) { //提交表单之后
        if(data == "0"){
            $("#myModal").modal(); //提示框，Excel导入成功
        }else{
            $("#myModal2").modal(); //提示框，Excel导入成失败
        }
    },
    onSelect: function() {//当用户选择了一个文件后触发事件
        //当选择了文件后，关闭自动提交
        this.autoSubmit=false;
        //校验上传的文件名是否满足后缀为.xls或.xlsx
        var regex =/^.*\.(?:xls|xlsx)$/i;
        //this.filename()返回当前选择的文件名称 (ps：我使用这个方法没好使，自己写了一个获取文件的名的方法) $("[name = '"+this.name()+"']").val())
        //alert(this.filename());
        if(regex.test($("[name = '"+this.name()+"']").val())){
            //通过校验
            this.submit();
        }else{
            //未通过
            $("#myModal3").modal(); //错误提示框，文件格式不正确，必须以.xls或.xlsx结尾
        }
    }
});