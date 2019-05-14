<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page language="java" import="com.ax.springmvc.pojo.User" %>
<!DOCTYPE html>
<html>
<head>
<base href="${pageContext.request.contextPath}/" />
<meta charset="utf-8">
<title>Insert title here</title>

<link rel="stylesheet" href="css/bootstrap.css"/>

<style>
    #box{background-color:#fff;position: absolute;top: 20%;right: 10%;width: 350px;
        height: 300px;box-sizing: border-box;border: 1px solid #a0b1c4;z-index: 12;border-radius: 5px;overflow: hidden;}
    #box>p>button{width: 49%;outline: none;margin: 5px 0;}
    #box>form{margin: 0 30px;}
    [for="input4"].col-sm-3{padding-left: 3px;}
    #login{padding-top: 40px;}
    #login .form-group{margin-bottom: 20px;}
    #register{display: none;}
    [type="submit"].btn{width: 120px;}
    
	/* #bgBox{width: 100%;height: 100%;position:absolute;top:0;left:0;background: url('img/background-4.png');z-index:0;} */
</style>
</head>
<body style="background: url('img/login.png');height: 100%">
<div id="box">
    <p class="text-center"><button class="btn btn-primary" doShow="login">登陆</button>
        <button class="btn btn-default" doShow="register">注册</button></p>
    <form class="form-horizontal" id="login" action="javascript:void(0)" method="post">
        <div class="form-group">
            <label for="inputEmail3" class="col-sm-3 control-label">账号:</label>
            <div class="col-sm-9">
                <input type="text" class="form-control" id="inputEmail3" placeholder="账号" required name="phone">
            </div>
        </div>
        <div class="form-group">
            <label for="inputPassword3" class="col-sm-3 control-label">密码:</label>
            <div class="col-sm-9">
                <input type="password" class="form-control" id="inputPassword3" placeholder="密码" required name="pwd">
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-9">
                <button type="submit" class="btn btn-danger">登陆</button>
            </div>
        </div>
    </form>

    <form class="form-horizontal" id="register" action="javascript:void(0)" method="get">
        <div class="form-group">
            <label for="input1" class="col-sm-3 control-label">*账号</label>
            <div class="col-sm-9">
                <input type="text" id="input1" info="账号" class="form-control" placeholder="账号" required pattern="[a-zA-Z0-9]{6,16}" name="phone">
            </div>
        </div>
        <div class="form-group">
            <label for="input2" class="col-sm-3 control-label">*昵称</label>
            <div class="col-sm-9">
                <input type="text" id="input2" info="昵称" class="form-control" placeholder="昵称" required name="nickname">
            </div>
        </div>
        <div class="form-group">
            <label for="input3" class="col-sm-3 control-label">*密码</label>
            <div class="col-sm-9">
                <input type="password" id="input3" info="密码" class="form-control" placeholder="密码" required pattern="\w{3,16}" name="pwd">
            </div>
        </div>
        <div class="form-group">
            <label for="input4" class="col-sm-3 control-label">*确认密码</label>
            <div class="col-sm-9">
                <input type="password" id="input4" info="确认密码" class="form-control" placeholder="请与密码输入一致" required name="repwd">
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-9">
                <button type="submit" class="btn btn-danger">注册并登陆</button>
            </div>
        </div>
    </form>
</div>
<script src="js/jquery-3.2.1.js"></script>
<script src="js/bootstrap.js"></script>
<script>
    $(function(){
		$("#login button[type=submit]").click(function(){
			$.getJSON("login/login",{"phone":$("#login [name=phone]").val(),"pwd":$("#login [name=pwd]").val()},
					function(data){
				alert(data.msg);
				if(data.status==200)
					location.href="home/redirectIndex";
			})
		});
		
		$("#register button[type=submit]").click(function(){
			var check1=feiNullPhone($("#register [name=phone]"),"账号格式不正确【请输入长度6-16位的数字或英组成】");
			var check2=feiNull($("#register [name=nickname]"),"昵称长度不能为空");
			var check3=feiNull($("#register [name=pwd]"),"密码格式不正确【请输入长度3-16位的字符】");
			var check4=feiNullRepwd($("#register [name=repwd]"),"两次密码不一致");
			if(check1 && check2 && check3 && check4){
				var user={
					"phone":$("#register [name=phone]").val(),
					"pwd":$("#register [name=pwd]").val(),
					"nickname":$("#register [name=nickname]").val()
				};
				$.post("login/register",user,function(data){
					alert(data.msg);
					if(data.status==200)
						location.href="home/redirectIndex";
				});
			}
		});
    	
        var $box = $("#box");
        $box.find("p:eq(0)>.btn").click(function(){
            $(this).addClass("btn-primary").removeClass("btn-default").siblings(".btn").addClass("btn-default").removeClass("btn-primary");
            $("#"+$(this).attr("doShow")).show().siblings("form").hide();
        });
    });

    function feiNull($el,msg){
        var el=$el[0];
        if(el.validity.valueMissing==true){
            el.setCustomValidity($el.attr("info")+"不能为空");
        }else if(el.validity.patternMismatch==true){
            el.setCustomValidity(msg);
        }else{
            el.setCustomValidity("");
            return true;
        }
        return false;
    }
    function feiNullPhone($el,msg){
        var el=$el[0];
        var phone=$el.val();
        var result=0;
        $.ajax({
            type : "get",
            url : "login/checkPhone",
            data : "phone="+phone,
            async : false,
            success : function(res){
                result=res;
            }
        });
        if(el.validity.valueMissing==true){
            el.setCustomValidity($el.attr("info")+"不能为空");
        }else if(el.validity.patternMismatch==true){
            el.setCustomValidity(msg);
        }else if(result>0){
            el.setCustomValidity("该账号已存在不可用");
        }else{
            el.setCustomValidity("");
            return true;
        }
        return false;
    }
    function feiNullRepwd($el,msg){
        var el=$el[0],pwd=$("#register [name=pwd]").val();
        if(el.validity.valueMissing==true){
            el.setCustomValidity($el.attr("info")+"不能为空");
        }else if($el.val()!=pwd){
            el.setCustomValidity(msg);
        }else{
            el.setCustomValidity("");
            return true;
        }
        return false;
    }
</script>

</body>
</html>