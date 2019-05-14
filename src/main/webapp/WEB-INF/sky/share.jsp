<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page language="java" import="com.ax.springmvc.pojo.User" %>
<!DOCTYPE html>
<html>
<head>
<base href="${pageContext.request.contextPath}/"/>
<meta charset="utf-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="font-awesome-4.7.0/css/font-awesome.css"/>
<link rel="stylesheet" type="text/css" href="css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="css/sky/header.css"/>
<link rel="stylesheet" type="text/css" href="css/sky/share.css"/>

<style type="text/css">
#deleteFriend .modal-footer>.btn.btn-primary>span {width: 100%;height: 100%;display: inline-block;padding: 0 12px;}
#deleteFriend .modal-footer>.btn.btn-primary {cursor: pointer;padding: 0;height: 34px;text-align: center;line-height: 34px;
	background-color: #3b8cff;border: 1px solid #3b8cff;}
#confirmShare{float: right;width: 70px;height: 32px;line-height: 32px;}
#shareFilesModal .btn.btn-info.btn-bg-blue{padding: 0;width: 82px;height: 34px;line-height: 34px;}
#confirmShare>span,#shareFilesModal .btn.btn-info.btn-bg-blue>span{width: 100%;height: 100%;display: inline-block;}
	
.cursor{cursor: pointer;}
#myFileHeader>div.head{display: none;float: left;padding: 0 10px;}
#myFileHeader .head.bread-h.cursor{color: rgb(51, 122, 183);border-right: 1px solid;margin: 12px 8px 12px 0;}
.myTabBox .table tr td:first-of-type>.fa {font-size: 20px;margin:0 10px;}
.myTabBox .table tr td:first-of-type>.fa.fa-folder {color: #ffd65a;}
.myTabBox .table .headerTr~tr td:first-of-type>.fa-file-audio-o {color: #8284f1;}
.myTabBox .table .headerTr~tr td:first-of-type>.fa-file-video-o {color: #8284f1;}
.myTabBox .table .headerTr~tr td:first-of-type>.fa-file-image-o {color: #ff7844;}
.myTabBox .table .headerTr~tr td:first-of-type>.fa-file {color: #64c424;}
</style>
</head>
<body style="background: url('img/background-4.png') no-repeat 0 0;">
<script type="text/javascript" src="js/jquery-3.2.1.js"></script>

<jsp:include page="header.jsp" ></jsp:include>
	
<div class="container">
	<nav id="left">
		<p class="text-center left-header"  style="padding: 5px 0 0;">
			<button class="btn add-ft-color" target="ol">会话</button>
			<button class="btn" target="ul">好友</button>
		</p>
		<ol class="userList"></ol>
		<ul class="userList"></ul>
		<script type="text/javascript">
			$(function(){
				getSessionInfo();
				$("ul.userList").on('click','li',function(){
					$(this).addClass("userList-li").siblings("li").removeClass("userList-li");
				});
				$("ol.userList").on('click','li',function(){
					$(this).addClass("userList-li").siblings("li").removeClass("userList-li");
				});
				$(".left-header>button").click(function(){
					$(this).addClass("add-ft-color").siblings().removeClass("add-ft-color");
					$($(this).attr("target")+".userList").show().siblings(".userList").hide();
					if($(this).attr("target")=="ol"){
						getSessionInfo();
					}else{
						getUser();
					}
				});
				
				//查找好友
				$("#findFriend").click(function(){
					var $search = $("#myModal .search-result");
					$.getJSON("rest/friend/"+$("#friendPhone").val(),function(data){
						$search.find("p").show();
						if(data.status==200){
							var findUser=$search.find(".search-result-area.clearfix");
							findUser.show().prev("div").hide();
							findUser.find("a.search-avatar>img").attr("src","/ax/"+data.user.headImg);
							findUser.find("p.search-bd-account").attr("title",data.user.phone)
								.find("span.search-bd-name").html(data.user.phone+" ("+data.user.nickname+")");
						}else{
							$search.find(".search-result-area").show().find("p.search-no-result").html(data.msg).end()
								.next(".clearfix").hide();
						}
					});
				});
				
				//添加好友
				$("#addFriends").click(function(){
					$.ajax({
						type:"post",
						url:"rest/friend",
						data:"phone="+$("#friendPhone").val(),
						beforeSend:function(){
							openSpinner("火速添加中。。。");
						},success:function(data){
							checkMsg(data.msg,data.status);
							if(data.status==200){
								$("#friendPhone").val("");
								$("#myModal").modal("hide");
								if($(".btn.add-ft-color").html().trim()=="好友")
									getUser();
							}
						},error:function(xhr){
							alert("添加好友错误\n"+xhr.responseText);
						},complete:function(){
							closeSpinner();
						}
					});
				});
				
			});
			
			//用户的好友
			function getUser(){
				var $ul=$("ul.userList");
				$ul.empty();
				$.getJSON("rest/friend",function(data){
					if(data.length==0){
						$ul.append('<li class="text-center" style="font-size:16px;margin-top:3px;">您还没有好友吗？<br/>点击下方添加好友咯。</li>');
					}
					$.each(data,function(i,obj){
						var $li =$('<li><img src="/ax/'+obj.headImg+'"/>'+obj.phone+' ('+obj.nickname+')</li>');
						$li.on('click',function(){
							friendInfo(obj);
						});
						$ul.append($li);
					});
				});
			}
			
			//会话记录
			function getSessionInfo(){
				var $ol=$("ol.userList");
				$ol.empty();
				$.getJSON("rest/fileShare",function(data){
					if(data.length==0){
						$ol.append('<li class="text-center" style="font-size:16px;margin-top:3px;">您还没有会话记录吗？<br/>给好友分享的文件呗。</li>');
					}
					$.each(data,function(i,obj){
						var $li =$('<li data-fileShareId="'+obj.id+'" data-userId="'+obj.userId+'">'
							+'<img src="/ax/'+obj.headImg+'"/>'
							+ obj.phone+ '<span>'+ changTime(obj.sharingTime)+'</span></li>');
						$li.on('click',function(){
							sessionRecord(obj.beUserId);
						});
						$ol.append($li);
					});
				});
			}
			
			function changTime(str){
				var d = new Date(str);
				var t = new Date();
				var dh=d.getHours()<10?"0"+d.getHours():d.getHours(),
					dm=d.getMinutes()<10?"0"+d.getMinutes():d.getMinutes();
				if(d.getFullYear()==t.getFullYear() && d.getMonth()==t.getMonth() && d.getDate()==t.getDate()){
					str="今天 "+dh+":"+dm;
				}else if(d.getFullYear()==t.getFullYear()){
					var month = (d.getMonth()+1)<10?"0"+(d.getMonth()+1):(d.getMonth()+1),
						dd=d.getDate()<10?"0"+d.getDate():d.getDate();
					var astr=month+"-"+dd;
					if((d.getDate()+1)==t.getDate()){
						astr="昨天";
					}else if((d.getDate()+2)==t.getDate()){
						astr="前天"; 
					}
					str=astr+" "+dh+":"+dm;
				}
				return str
			}

			function sessionRecord(beUserId){
				var $sess = $("#fileSession");
				$sess.find("section").empty();
				$sess.find(".btn.btn-info.btn-bg-blue.shareFile-u").attr("data-friendid",beUserId);
				$.ajax({
					type:"GET",
					url:"rest/fileShare/"+beUserId,
					beforeSend:function(){
						openSpinner("拼命加载中信息中。。。");
					},success:function(data){
						$.each(data,function(i,obj){
							var friendOroneseif=obj.userId==<%=((User) session.getAttribute("user")).getId()%>?"oneseif":"friend";
							var fileName = obj.folioInfoVos[0].fileName,
								fileSize = obj.folioInfoVos[0].size=="-"?"":obj.folioInfoVos[0].size,
								clazz = obj.folioInfoVos[0].clazz;
							
							if(obj.folioInfoVos.length>1){
								fileName += "等多个文件";
								fileSize = "";
								clazz = "fa-th-large";
							}
							var $div=$('<div class="'+friendOroneseif+'">\
									<div><div class="fileToTime">'+obj.sharingTime+'</div></div>\
								<div class="sessionContent">\
									<div><img src="/ax/'+obj.headImg+'" class="img-thumbnail headImg">\
										<div class="arrow"></div>\
										<div class="file">\
											<div  class="fileLog">\
												<i class="fa '+clazz+'"></i></div>\
											<div class="fileName">\
												<span >'+fileName+'</span>\
												<span class="fileSize">'+fileSize+'</span>\
											</div>\
											<div class="fileToOption">\
												<a href="javascript:void(0)" title="保存到网盘"><i class="fa fa-sign-in"></i></a>&nbsp;\
												<a href="javascript:void(0)" title="下载"><i class="fa fa-download"></i></a>\
											</div>\
										</div></div></div>\
								</div>');
							$div.find(".fa.fa-sign-in").on('click',function(){
								if(obj.isNotEffective==2){
									createMeg("该分享链接已被删除，无法保存");
								}else{
									if(!checkDate(obj.outTime)){
										createMeg("该分享链接已过期，无法查看");
									}else{
										var $el = $("<span currentFileId='"+obj.id+"' fileType='"+obj.type+"' option='copy'>确定</span>");
		                            	$el.on('click',function(){
		                            		var $active=$(this).parents("#folioModal").find(".active-p .fa1");
											
		                            		$.ajax({
												type:"POST",
												url:"rest/fileShare/"+$active.attr("data-folioid")+"/"+obj.id,
												beforeSend:function(){
													openSpinner("文件保存中。。");
												},complete:function(){
													closeSpinner();
												},error:function(xhr){
													alert("文件保存错误\n"+xhr.responseText);
												},success:function(data){
													checkMsg(data.msg,data.status);
													if(data.status==200){
														
													}
												}
		                            		});
		                            	});
		                            	openFolioModal($el,"保存到");
									}
								}
							}).end().find(".fa.fa-th-large").on('click',function(){
								if(obj.isNotEffective==2){
									createMeg("该分享链接已被删除，无法查看");
								}else{
									if(!checkDate(obj.outTime))
										createMeg("该分享链接的分享时间已过期，无法查看");
									else
										showShareFileInfo(obj.id);
								}
							});
							$("#fileSession section:eq(0)").scrollTop($("#fileSession section:eq(0)").height());
							$sess.find("section").append($div);
						});
						setTimeout("changeHeight()",40);
					},error:function(xhr){
						alert("查看分享记录错误\n"+xhr.responeText);
					},complete:function(){
						closeSpinner();
					}
				});
				$sess.show().siblings("div").hide();
			}
			function checkDate(str){
				var out = new Date(str);
				var d = new Date();
				console.info(out);
				console.info(d);
				/* if(d.getFullYear()>out.getFullYear())
					return false;
				if(d.getMonth()>out.getMonth()&&d.getFullYear()>out.getFullYear())
					return false;
				if(d.getDate()>out.getDate())
					return false;
				if(d.getHours()>out.getHours())
					return false;
				if(d.getMinutes()>out.getMinutes())
					return false;
				if(d.getSeconds()>out.getSeconds())
					return false; */
				if(d>out){
					return false;
				}
				return true;
			}
			
			function changeHeight(){
				var $el = $("#fileSession section:eq(0)");
				var beforHeight = $el.scrollTop();
				$el.scrollTop($el.scrollTop()+20);
				var afterHeight = $el.scrollTop();
				if(beforHeight < afterHeight){
					setTimeout("changeHeight()",5);
				}
			}
			
			function friendInfo(friend){
				var $sec=$("#section");
				$sec.show().siblings("div").hide();
				$sec.find("img").attr("src","/ax/"+friend.headImg);
				$sec.find("header>h2").html(friend.phone);
				$sec.find("section>.text:eq(0)").html('<span>昵称：</span>'+friend.nickname);
				$sec.find("section>.text:eq(1)").html('<span>百度账号：</span>'+friend.phone);
				$sec.find("[data-openfiles=target]").attr("data-friendId",friend.id);
				
				var $el = $("<span>确定</span>")
				$el.on('click',function(){
					$.ajax({
						type:"DELETE",
						url:"rest/friend/"+friend.id,
						beforeSend:function(){
							openSpinner("正在解除好友关系中。。。");
						},success:function(data){
							checkMsg(data.msg,data.status);
							if(data.status==200){
								$("#shareInit").show().siblings("div").hide();
								$("#myModal").modal("hide");
								if($(".btn.add-ft-color").html().trim()=="好友")
									getUser();
							}
						},error:function(xhr){
							alert("删除好友出错\n"+xhr.responseText);
						},complete:function(){
							closeSpinner();
						}
					});
				});
				$("#deleteFriend .btn.del-firend").append($el);
			}
		</script>
		<p style="position: absolute;width: 100%;bottom: 0;padding: 5px 0;" class="text-center">
			<button style="width: 33%;" class="btn btn-default" data-toggle="modal" data-target="#shareFilesModal">分享文件</button>
			<button style="width: 33%;" class="btn btn-default" data-toggle="modal" data-target="#myModal">添加好友</button>
			<button style="width: 30%;" class="btn btn-default" data-toggle="modal" data-target="#settingUp">设置</button>
			<script type="text/javascript">
				$(function(){
					$("#myModal").on("show.bs.modal", function (e) {
						$(this).find(".search-result>*").hide();
			        }).on("shown.bs.modal", function (e) {
						$(this).find("#friendPhone").focus();
			        }).on("hide.bs.modal", function (e) {
						$(this).find(".search-result>*").hide();
			        }).on("hidden.bs.modal", function (e) {
			            //内容清空
			            $(this).removeData("bs.modal");
			        }).on("loaded.bs.modal", function (e) {

			        });
					
					$("#shareFilesModal").on("show.bs.modal", function (e) {
						getUser2();
			        }).on("shown.bs.modal", function (e) {
						
			        }).on("hidden.bs.modal", function (e) {
			            //内容清空
			            $(this).removeData("bs.modal");
			            $(this).find(".btn.btn-info.btn-bg-blue").html("确定").addClass("now");
						$("#shareDay").val(30)
			        });
					
					$("#settingUp").on("show.bs.modal", function (e) {
						$("#uploadImg").attr("src","/ax/${user.headImg}");
						$("#user_nickname").val("${user.nickname}");
			        });
					
				 	$("#folioModal").on("show.bs.modal", function (e) {
			        	getFolio(0,$("#folioModalBody .folioAll"));
						$("div.p").removeClass("active-p").css("border-color","rgba(0,0,0,0)");
						$(".folioAll.childrenDiv").find("div,p:eq(0)").addClass("active-p").css("border-color","rgb(154, 205, 253)");
			        }).on("shown.bs.modal", function (e) {

			        }).on("hide.bs.modal", function (e) {
			        	
			        }).on("hidden.bs.modal", function (e) {
			            //内容清空
			            $(this).removeData("bs.modal");
			        }).on("loaded.bs.modal", function (e) {

			        });
				});
			</script>
		</p>
	</nav>
	<!--初始化-->
	<div id="shareInit">
		<p class="text-center" style="margin: 15% 0 5%">
			<img src="img/bg_share.png"/>
		</p>
		<p style="padding: 0 25%;">
			<button class="btn btn-info btn-block btn-bg-blue" data-openFiles="target">给好友分享文件</button>
		</p>
	</div>
	
	<!--会话-->
	<div id="fileSession">
		<header>
			<p class="text-center">
				<span>啊湘</span>
			</p>
		</header>
		<section></section>
		<footer class="text-center">
			<p>
				<button class="btn btn-info btn-bg-blue shareFile-u" data-openFiles="target">分享文件</button>
			</p>
		</footer>
	</div>
	
	<!--好友信息-->
	<div id="section" class="text-center">
		<header style="border-bottom: 1px dashed #ccc;">
			<p><img src="img/16238_100.png" alt="" /></p>
			<h2>wo***de你</h2>
		</header>
		<section>
			<p class="text"><span>昵称：</span>--</p>
			<p class="text"><span>百度账号：</span>wo***de你</p>
			<p><button class="btn btn-primary btn-group-justified btn-bg-blue shareFile-u" data-openFiles="target">分享文件</button></p>
			<p><button class="btn btn-default btn-group-justified" data-toggle="modal" data-target="#deleteFriend">删除好友</button></p>
			<!-- <p><button class="btn btn-default btn-group-justified">加入黑名单</button></p> -->
		</section>
	</div>
</div>

<div id="shareFiles">
	<div>
		<header>
			<p style="margin: 0;" class="fujian-txt">
				<span>文件列表</span>
				<b class="closeFiles" style="float: right;margin-right: 30px;font-size: 22px;cursor: pointer;">×</b>
				<script type="text/javascript">
					$(function(){
						var $nav = $("#shareFiles");
						$(".closeFiles").click(function(){
							$nav.find("div").slideUp(370,function(){$nav.hide();});
						});
						$("[data-openFiles=target]").click(function(){
							var $th = $(this);
							$nav.find(".table input[type=checkbox]").prop("checked",false);
							$nav.show(0,function(){
								getMyFile(0,"timeAsc");
							}).find("div").slideDown(400);

							var $el = $("<span>确定</span>");
							if($th.hasClass("shareFile-u")){
								$el.on('click',function(){
									var $trs = $(this).parents("#shareFiles").find(".myTabBox .table [name=ck]:checked").closest("tr");
									var user = [$th.attr("data-friendId")];
									$nav.find("div").slideUp(370,function(){$nav.hide();});
									shareFiles($trs,user);
								});
								$("#confirmShare").html("").append($el);
							}else{
								$el.on('click',function(){
									var $trs = $(this).parents("#shareFiles").find(".myTabBox .table [name=ck]:checked").closest("tr");
									if($trs.length==0){
										createMeg("请选择分享的文件");
									}else{
										var user = [];
										$nav.find("div").slideUp(370,function(){$nav.hide();});
										$("#shareFilesModal").modal("show");
										var $span=$("<span>确定</span>");
										$span.on('click',function(){
											var $users=$span.parents("#shareFilesModal").find("ul.selectedUser>li .fa.fa-dot-circle-o").parents("li");
											if($users.length==0){
												createMeg("请选择分享的用户");
												return false;
											}else{
												$.each($users,function(){
													user.push($(this).attr("data-userId"));
												});
												shareFiles($trs,user);
											}
										});
										$("#shareFilesModal").find(".btn.btn-info.btn-bg-blue.now").html("").removeClass("now").append($span);
									}
								});
								$("#confirmShare").html("").append($el);
							}
							
						});
					});
					//分享文件
					function shareFiles($trs,user){
						var param="shareDay="+$("#shareDay").val();
						$.each(user,function(){
							param+="&userId="+this;
						});
						$.each($trs,function(){
							param+="&id="+$(this).attr("data-id")+"&typeId="+$(this).attr("data-type");
						});
						$.ajax({
							type:"POST",
							url:"rest/fileShare",
							data:param,
							beforeSend:function(){
								openSpinner("努力分享文件中。。。");
							},success:function(data){
								checkMsg(data.msg,data.status);
								if(data.status==200){
									if($(".btn.add-ft-color").html().trim()=="会话")
										getSessionInfo();
									if($("#fileSession").is(":visible")){
										sessionRecord($(".btn-info.btn-bg-blue.shareFile-u").attr("data-friendid"));
									}
								}
							},error:function(xhr){
								alert("分享错误\n"+xhr.responseText);
							},complete:function(){
								closeSpinner();
							}
						});
					}
				</script>
			</p>
		</header>
		<section style="padding: 0 3px;">
			<div style="position: relative;" class="clearfix">
				<header id="myFileHeader">
					<div class="head bread-h cursor" parentdir="0">返回上一级</div>
					<ol class="breadcrumb"></ol>
				</header>
				<div class="input-group" style="width: 200px; position: absolute; top:5px;right: 10px;">
			    	<span class="input-group-btn">
			        	<button class="btn btn-default" type="button" style="outline: none;">
			    			<i class="fa fa-search" aria-hidden="true"></i>
			    		</button>
			    	</span>
			    	<input type="text" class="form-control" placeholder="搜索我的网盘文件">
			    </div>
			</div>
			<%@ include file="fileInfo.jsp" %>
		</section>
		<footer>
			<p>
				<script type="text/javascript">
					$(function(){
						$("#ToFileUpLaod").change(function(){
							var formData = new FormData($("#fileUploadAjax")[0]);
							$.ajax({
								type:"POST",
								url:"upload/springUploadAjax",
								data:formData,
								cache: false,
				                processData: false,
				                contentType: false,
								beforeSend:function(){
									openSpinner("文件上传中。。。");
								},success:function(data){
									checkMsg(data.msg,data.status);
									if(data.status==200){
										getMyFile($("#myFileHeader .breadcrumb li:last").attr("parentdir"),"timeAsc");
									}
								},error:function(xhr){
									alert("上传文件错误\n"+xhr.responseTest);
								},complete:function(){
									closeSpinner();
								}
							});
						});
						$("#ToFolioUpLoad").change(function(){
							var formData = new FormData($("#folioUploadAjax")[0]);
							$.ajax({
								type:"POST",
								url:"upload/folioUploadAjax",
								data:formData,
								cache: false,
				                processData: false,
				                contentType: false,
								beforeSend:function(){
									openSpinner("文件夹上传中。。。");
								},success:function(data){
									checkMsg(data.msg,data.status);
									if(data.status==200){
										getMyFile($("#myFileHeader .breadcrumb li:last").attr("parentdir"),"timeAsc");
									}
								},error:function(xhr){
									alert("上传文件夹错误\n"+xhr.responseTest);
								},complete:function(){
									closeSpinner();
								}
							});
						});
					});
				</script>
				<button class="btn btn-default btn-ft-color" style="position: relative;overflow: hidden;">上传文件
					<form class="h5-uploader-form" id="fileUploadAjax" action="javascript:void(0)" method="post" enctype="multipart/form-data">
							<input title="点击选择文件" id="ToFileUpLaod" multiple="multiple" accept="*/*"
						type="file" name="html5uploader">
					</form>
				</button>
				<button class="btn btn-default btn-ft-color" style="position: relative;overflow: hidden;">上传文件夹
					<form class="h5-uploader-form" id="folioUploadAjax" action="javascript:void(0)" method="post" enctype="multipart/form-data">
						<input title="点击选择文件夹" id="ToFolioUpLoad" multiple="multiple"
							webkitdirectory="webkitdirectory" accept="*/*" type="file" name="html5uploader">
					</form>
				</button>
				<button class="btn btn-info btn-bg-blue" style="padding: 0;" id="confirmShare">确定</button>
				<button class="btn btn-default closeFiles" style="float: right;margin-right: 10px;">取消</button>
			</p>
		</footer>
	</div>
</div>

<!--模态框-->
<!-- 添加好友 -->
<div id="myModal" class="modal fade" data-backdrop="static">
	<div class="modal-dialog" style="margin-top: 100px;">
		<div class="modal-content">
	    	<div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel">添加好友</h4>
	      	</div>
	      	<div class="modal-body" style="padding: 30px 0;text-align: center;">
         			<input type="text" class="form-control" style="width: 72%;display: inline-block;" required placeholder="手机/邮箱/用户名" id="friendPhone">&nbsp;
				<buton class="btn btn-primary" style="padding: 6px 20px;position: relative;top: -1px;" id="findFriend">搜索</buton>
				<div class="search-result">
					<p class="search-result-text">搜索结果：</p>
					<div class="search-result-area">
						<p class="search-no-result">没有找到你搜索的用户，检查下输入的帐号吧</p>
					</div>
					<div class="search-result-area clearfix">
						<a class="search-avatar" href="javascript:void(0);">
							<img src="img/16238_100.png">
						</a>
						<div class="search-info no-nick-name">
							<p class="search-name-add"></p>
							<p class="search-bd-account" title="w">阿湘帐号：<span class="search-bd-name">w</span></p>
						</div>
						<div class="search-btn-area">
							<button class="btn btn-primary" id="addFriends">加为好友</button>
						</div>
					</div>
				</div>
	      	</div>
	      <div class="modal-footer">
	      		<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	      </div>
	    </div>
	</div>
</div>
<!-- 修改当前用户信息 -->
<div id="settingUp" class="modal fade" data-backdrop="static">
	<div class="modal-dialog my" style="margin-top: 100px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title">设置</h4>
			</div>
			<div class="modal-body" style="padding: 30px 0;text-align: center;">
				<form class="h5-uploader-form settingInfo clearfix" id="uploadForm" enctype="multipart/form-data" action="javascript:void(0);">
					<nav>
						<img src="/ax/${user.headImg}" id="imgContent"/>
						<input title="修改头像" type="file" name="uploadImg" id="uploadImg">
					</nav>
					<div>
						<p><b><input type="text" id="user_nickname" value="${user.nickname}"/></b> <a href="javascript:void(0)" id="updateUserInfo" class="btn btn-primary">确定</a></p>
						<p><b>阿湘账号：</b><span>${user.phone}</span></p>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
			<script type="text/javascript">
				$(function () {
			        $("#updateUserInfo").click(function () {
			            var formData = new FormData($("#uploadForm")[0]);
			            console.info(formData);
			            $.ajax({
			                type: 'POST',
			                url: "upload/updateUserInfo/"+$("#user_nickname").val(),
			                data: formData,
			                cache: false,
			                processData: false,
			                contentType: false,
			           		success:function (data) {
			           			checkMsg(data.msg,data.status);
			           			if(data.status==200){
			           				$("#settingUp").modal('hide');
			           				$("#user-headImg").attr({
			           					"src":"/ax/"+data.user.headImg,
			           					"alt":data.user.nickname
			           				});
			           			}
			           		},error:function () {
			                	alert("上传失败");
			           		}
			        	});//ajax
				    });
			    });
				$("#uploadImg").change(function() {
					var file = document.getElementById("uploadImg").files[0];
					if(file != null) {
						//console.info(file.name);
						var reader = new FileReader(); //创建文件的对象
						//为文件读取成功设置事件
						reader.onload = function(e) {
							var imgFile = e.target.result;
							$("#imgContent").attr("src", imgFile);
						}
						reader.readAsDataURL(file);
					}
				});
			</script>
		</div>
	</div>
</div>
<!-- 删除好友 -->
<div id="deleteFriend" class="modal fade" data-backdrop="static">
	<div class="modal-dialog my" style="margin-top: 100px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title">删除好友</h4>
			</div>
			<div class="modal-body" style="padding: 30px 0;text-align: center;">
				<div class="settingInfo clearfix">
					您确定要删除您的好友吗？
				</div>
			</div>
			<div class="modal-footer" style="margin: 10px 15px 10px;">
				<button type="button" class="btn btn-primary del-firend" data-dismiss="modal"></button>
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
			</div>
		</div>
	</div>
</div>

<!-- 发文件时选择多个好友 -->
<div id="shareFilesModal" class="modal fade" data-backdrop="static">
	<div class="modal-dialog" style="margin-top: 100px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title">分享文件</h4>
			</div>
			<div class="modal-body" style="padding: 10px 0;text-align: center;">
				<ul class="selectedUser"></ul>
				<script>
					$(function(){
						$("ul.selectedUser").on('click','li',function(){
							var fa=$(this).find("i.fa");
							fa.hasClass("fa-circle-o")?fa.removeClass("fa-circle-o").addClass("fa-dot-circle-o")
									:fa.removeClass("fa-dot-circle-o").addClass("fa-circle-o");
						});
						var $btn =	$("#shareFilesModal .btn.btn-info.btn-bg-blue");
						$btn.on("click",function(){
							if($btn.hasClass("now")){
								var user=[];
								var $users=$(this).parents("#shareFilesModal").find("ul.selectedUser>li .fa.fa-dot-circle-o").parents("li");
								if($users.length==0){
									createMeg("请选择分享的用户");
									return false;
								}else{
									$.each($users,function(){user.push($(this).attr("data-userId"));});
									$("#shareFilesModal").modal("hide");
									$("#shareFiles").show(0,function(){
										getMyFile(0,"timeAsc");
									}).find("div").slideDown(400);
									var $el=$("<span>确定</span>");
									$el.on('click',function(){
										var $trs = $(this).parents("#shareFiles").find(".myTabBox .table [name=ck]:checked").closest("tr");
										if($trs.length==0){
											createMeg("请选择分享的文件");
										}else{
											$("#shareFiles").find("div").slideUp(370,function(){$("#shareFiles").hide();});
											shareFiles($trs,user);
										}
									});
									$("#confirmShare").html("").append($el);
								}
							}
						});
					});
					//用户的好友2
					function getUser2(){
						var $ul=$("#shareFilesModal ul.selectedUser");
						$ul.empty();
						$.getJSON("rest/friend",function(data){
							if(data<=0){
								$("#shareFilesModal").modal('hide');
								createMeg("由于您还没有好友，现在帮您打开添加好友功能！");
								setTimeout(function(){$("#myModal").modal('show');},300);
							}
							$.each(data,function(i,obj){
								var $li =$('<li data-userId="'+obj.id+'"><img src="/ax/'+obj.headImg+'"/> '+obj.phone+'<i class="fa fa-circle-o"></i></li>');
								$ul.append($li);
							});
						});
					}
				</script>
			</div>
			<div class="modal-footer">
				<div style="float:left;line-height: 34px;">
					有效期：<select id="shareDay">
						<option value="30" selected>30</option>
						<option value="15">15</option>
						<option value="7">7</option>
						<option value="1">1</option>
					</select>
				</div>
				<button type="button" class="btn btn-info btn-bg-blue now" data-dismiss="modal">确定</button>
			</div>
		</div>
	</div>
</div>




<script type="text/javascript" src="js/bootstrap.js" ></script>
<script type="text/javascript">
	$(function(){
		$("#bs-example-navbar-collapse-1>ul:eq(0)>li:eq(1)").addClass("active").siblings("li").removeClass("active");
	});
	//获得一个元素（$el）指定的样式（attr）
	function getStyle($el, attr) {
		var obj = $el[0];
        if (obj.currentStyle) {
            return obj.currentStyle[attr];
        } else {
            return getComputedStyle(obj, false)[attr];
        }
    }
</script>
</body>
</html>