<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<base href="${pageContext.request.contextPath}/" />

<meta charset="utf-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css"
	href="font-awesome-4.7.0/css/font-awesome.css" />
<link rel="stylesheet" type="text/css" href="css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="css/sky/header.css" />
<link rel="stylesheet" type="text/css" href="css/sky/index.css" />
<style type="text/css">
th.orderby,.headerTr~tr{cursor: pointer;}
.btn.btn-default.myOption{display: none;padding:3px 12px;color: #3b8cff;border: 1px solid #3b8cff;background-color: rgba(0,0,0,0);margin: 0 0 0 20px;}
</style>
</head>
<body>
	<script type="text/javascript" src="js/jquery-3.2.1.js"></script>

	<jsp:include page="header.jsp"></jsp:include>
	
	<c:choose>
		<c:when test="${skyParentDir == null ||  skyParentDir < 0}">
			<script type="text/javascript">$(function(){getMyFile(0,null,"timeAsc");});</script>
		</c:when>
		<c:otherwise>
			<script type="text/javascript">$(function(){getMyFile(<%=session.getAttribute("skyParentDir")%>,null,"timeAsc");});</script>
		</c:otherwise>
	</c:choose>
	<c:if test="${skyMsg != null}">
		<script type="text/javascript">$(function(){checkMsg("<%=session.getAttribute("skyMsg")%>",200);});</script>
		<%session.removeAttribute("skyMsg");%>
	</c:if>
	
	<div class="clearfix" id="content">
		<nav id="left">
			<dl class="nav-dl">
				<dt class="bHzsaPb dt">
					<span class="glyphicon glyphicon-floppy-disk"></span> 全部文件
				</dt>
				<dd data-type="jpg,bmp,png,gif,tiff,raw,jpeg">图片</dd>
				<dd data-type="txt,text,docx,doc,ppt,pptx">文档</dd>
				<dd data-type="mp4,mpg,avi,rm,mov,asf,wmv,flv,3gp,awv,f4v">视频</dd>
				<!-- <dd>种子</dd> -->
				<dd data-type="mp3,wma,wav,ra,aac,mid,ogg,m4a">音乐</dd>
				<dd data-type="">其他</dd>
				<dt class="dt MyShare">
					<i style="font-size: 18px;" class="fa fa-share-alt"
						aria-hidden="true"></i> 我的分享
				</dt>
				<dt class="dt recycleBin">
					<span class="glyphicon glyphicon-trash"></span> 回收站
				</dt>
			</dl>
			<script type="text/javascript">
            $(function () {
                $(".nav-dl").on('click', 'dt,dd', function () {
                    $(this).addClass("bHzsaPb").siblings().removeClass("bHzsaPb");
                    if($(this).hasClass("MyShare")){
                    	myShare();
                    }else if($(this).hasClass("recycleBin")){
                    	recycleBin();
                    }else if($(this).hasClass("dt")){
                    	getMyFile(0,null,"timeAsc");
                    	$("#myFile .myCreateFile").show();
                    }else{
                    	getMyFile(0,$(this).attr("data-type"),"timeAsc");
                    	$("#myFile .myCreateFile").hide();
                    }
                });
            });
        </script>
        <p style="position: absolute;bottom: 16px;left:20px"><img width="100%" src="img/navFooter.PNG"></p>
		</nav>

		<div id="myFile">
			<nav class="navbar navbar-default">
				<div class="container-fluid">
					<div class="navbar-header"></div>
					<div class="collapse navbar-collapse">
						<ul class="nav navbar-nav">
							<script type="text/javascript">
								$(function(){
									$("#ToFileUpLaod,#ToFileUpLaod1,#folioUpLoad").change(function(){
										openSpinner("文件上传中。。。");
										$(this).parents("form.h5-uploader-form").submit();
									});
								});
							</script>
							<li class="dropdown myDropdown"><a href="javascript:void(0)"
								class="fileUpLoad btn btn-default"
								style="position: relative; background-color: #3b8cff; color: #FFF;overflow: hidden;"
								role="button"> <span class="fa fa-cloud-upload"></span>&nbsp;
									上传<form class="h5-uploader-form" action="upload/springUpload" method="post" enctype="multipart/form-data">
										<input title="点击选择文件" id="ToFileUpLaod" multiple="multiple" 
											type="file" name="html5uploader">
									</form>
							</a>
								<ul class="dropdown-menu">
									<li style="position: relative;overflow: hidden;"><a href="javascript:void(0)">上传文件
											<form class="h5-uploader-form" action="upload/springUpload" method="post" enctype="multipart/form-data">
													<input title="点击选择文件" id="ToFileUpLaod1" multiple="multiple" accept="*/*"
												type="file" name="html5uploader">
											</form>
									</a></li>
									<li style="position: relative;overflow: hidden;"><a
										href="javascript:void(0)">上传文件夹
											<form class="h5-uploader-form" action="upload/folioUpload"
											 method="post" enctype="multipart/form-data">
												<input title="点击选择文件夹" id="folioUpLoad" multiple="multiple"
													webkitdirectory="webkitdirectory" accept="*/*" type="file"
													name="html5uploader">
											</form>
									</a></li>
								</ul> <script type="text/javascript">
                                $(function () {
                                    $("#myFile").find(".myDropdown").hover(function () {	//上传文件和上传文件夹
                                        $(this).find(".dropdown-menu").show();
                                    }, function () {
                                        $(this).find(".dropdown-menu").hide();
                                    }).end().find(".myCreateFile").click(function () {	//新建文件夹
                                        $(".headerTr").show().find("input[name=fileName]").focus();
                                    	$(".emptyTr").hide();
                                    }).end().find(".downloadFile,.btn-group.option>.btn").click(function () {
                                        if ($(".headerTr~tr td [name=ck]:checked").length == 0) {
                                            openModal("请选择需要下载的文件或文件夹", $("<span>确定</span>"));
                                        } else {//选择了文件（tr）
											var $trs = $(".headerTr~tr td [name=ck]:checked").closest("tr");

                                    		var typeId=[],fileId=[],oldParId=[];
											$.each($trs,function(i,obj){
												typeId.push($(obj).attr("data-type"));
												fileId.push($(obj).attr("data-id"));
												oldParId.push($(obj).attr("data-parentdir"));
											});
											if($(this).hasClass("downloadFile")){
												
												var param = "a=1";
												$.each(typeId,function(i,obj){
													param+="&typeId="+obj+"&id="+fileId[i];
													//location.href="upload/downLoad?typeId="+obj+"&id="+fileId[i];
												});
												
										        location.href="download/load?"+param;
												/* $.ajax({
													url:"download/test",
													data:param,
													beforeSend:function(){
														openSpinner("努力打开下载通道中");
													},success:function(data){
														console.info(data);
													},error:function(xhr){
														alert("下载错误"+xhr.responseText);
													},complete:function(){
														closeSpinner();
													}
												}); */

											}else if($(this).hasClass("move")){
												var currentFileId,fileType;
												$.each($trs,function(i,obj){
													currentFileId+=$(obj).attr("data-id");
													fileType+=$(obj).attr("data-type");
													if(i<$trs.length-1){
														currentFileId+=",";
														fileType+=","
													}
												});
												var $el = $("<span currentFileId='"+currentFileId+"' fileType='"+fileType+"' option='move'>确定</span>");
	                                        	$el.on('click',function(){
	                                        		var $active=$(this).parents("#folioModal").find(".active-p .fa1");
	    											moves(typeId,fileId,$active,oldParId);
	                                        	});
	                                        	openFolioModal($el,"移动到");
												
											}else if($(this).hasClass("copy")){
												var currentFileId,fileType;
												$.each($trs,function(i,obj){
													currentFileId+=$(obj).attr("data-id");
													fileType+=$(obj).attr("data-type");
													if(i<$trs.length-1){
														currentFileId+=",";
														fileType+=","
													}
												});
												var $el = $("<span currentFileId='"+currentFileId+"' fileType='"+fileType+"' option='copy'>确定</span>");
	                                        	$el.on('click',function(){
	                                        		var $active=$(this).parents("#folioModal").find(".active-p .fa1");
	                                        		copys(typeId,fileId,$active.attr("data-folioid"),oldParId);
	                                        	});
	                                        	openFolioModal($el,"复制到");
											}else if($(this).hasClass("delete")){
												delFolioAndFile(typeId,fileId,$($trs[0]).attr("data-parentdir"));
											}
                                        }
                                    });
                                });
                            </script></li>
							<li><a href="javascript:void(0)"
								class="btn btn-default downloadFile" role="button"> <span
									class="fa fa-download" aria-hidden="true"></span>&nbsp; 下载
							</a></li>
							<li><a href="javascript:void(0)"
								class="btn btn-default myCreateFile" role="button"> <span
									class="fa fa-file-o"></span>&nbsp; 新建文件夹
							</a></li>
							<li class="btn-group option" role="group" style="display:none;">
						  		<button type="button" class="btn btn-default move">移动到</button>
						  		<button type="button" class="btn btn-default copy">复制到</button>
						  		<button type="button" class="btn btn-default delete"><span class="fa fa-trash"></span>&nbsp; 删除</button>
							</li>
						</ul>
						<!--搜索框-->
						<form class="navbar-form navbar-right">
							<div class="input-group">
								<input id="searchVal" style="border-radius: 17px 0 0 17px; padding: 6px 16px;"
									type="text" class="form-control" placeholder="搜索您的文件">
								<span class="input-group-btn">
									<button id="search" class="btn btn-default" type="button"
										style="border-radius: 0 17px 17px 0; outline: none;">
										<i class="fa fa-search" aria-hidden="true"></i>
									</button>
								</span>
							</div>
						</form>
						<script type="text/javascript">
							$(function(){
								$("#search").click(function(){
									var searchVal = $("#searchVal").val();
									var $table = $(".myTabBox .table");
									$.ajax({
										type:"GET",
										url:"rest/folio/listByLikeFileName/"+searchVal,
										beforesend:function(){
											openSpinner("加载中...");
										},
										success:function(data){
											checkShareFile(2);
											$table.find(".headerTr").siblings().remove();
											
											var $myFileHeader = $("#myFileHeader");
											$myFileHeader.find("div.last").html("已全部加载，共"+data.files.length+"个");
											$.each(data.files, function (i, obj) {
			                    				//var fileName = obj.rname==1?obj.fileName:obj.fileName+"("+obj.rname+")";
			                    				var $tr = $("<tr data-id='"+obj.id+"' data-type='"+obj.type+"' data-parentDir='"+obj.parentDir+"'><td width='60%'><input type='checkbox' data-id='" + obj.id + "' name='ck' value='' />" +
			                                            "<i class='fa "+obj.clazz+"'></i>" +
			                                            "<span>" + obj.showName + "</span>" +
			                                            "<div class='file-option'>" +
			                                            "<i title='创建分享' class='fa fa-share-alt' aria-hidden='true'></i>" +
			                                            "<i title='下载' class='fa fa-download' aria-hidden='true'></i>" +
			                                            "<i title='更多' class='fa fa-ellipsis-h' aria-hidden='true'></i>" +
			                                            "<div class='fileOption'><ul><li>移动到</li>" +
			                                            "<li>复制到</li>" +
			                                            "<li>重命名</li>" +
			                                            "<li>删除</li>" +
			                                            "</ul></div></div></td>" +
			                                            "<td width='16%'>" + obj.size + "</td><td width='24%'>" + obj.fileUpdateTime + "</td></tr>");
			                                    $table.append($tr);
			                                });
			                    			closeSpinner();
			                    		},error:function(e){
			                    			alert("错误"+e.responseText);
			                    		},complete:function(){
			                    		}
									});
								});
							});
						</script>
					</div>
				</div>
			</nav>
			<!--导航路径-->
			<header id="myFileHeader">
				<div class="head bread-h cursor" parentdir="0">返回上一级</div>
				<div class="head share-h">分享文件</div>
				<div class="head recycleBin-h">回收站</div>
				<ol class="breadcrumb">
					<li class="bread-h" parentdir="0"><a href="javascript:void(0)">返回上一级</a></li>
				</ol>
				<div class="last">已全部加载，共6个</div>
			</header>
			<!--表头-->
			<table border="0" cellspacing="0" cellpadding="0"
				style="width: 99%; margin-left: 2px;">
				<tr style="height: 45px;">
					<th width='60%'><input style="width: 14px; height: 14px;"
						type="checkbox" name="ckAll" id="ckAll" /> <span>文件名</span>
						<button class="btn btn-default myOption recycleBin">还原</button>
						<button class="btn btn-default myOption MyShare">取消分享</button>
						</th>
					<th width='16%'><span>大小</span></th>
					<th width='24%' class="orderby time" data-order="timeAsc"><span>修改时间</span></th>
				</tr>
			</table>
			<div class="myTabBox">
				<table class="table table-hover" border="0" cellspacing="0"
					cellpadding="0">
					<tr class="headerTr">
						<td width="60%"><input ckval="0" type='checkbox'
							disabled="disabled" /> 
							<i class='fa fa-folder'></i>

							<div class="FileName-div">
								<input type="text" name="fileName" /> <span
									class="text-center spanOption ok">√</span> <span
									class="text-center spanOption toHide">×</span>
							</div></td>
						<td width="16%">-</td>
						<td width="24%">-</td>
					</tr>
					<script type="text/javascript">
                    $(function () {

                        var $table = $(".myTabBox .table"),
                                $ckAll = $("#ckAll"),	//全选按钮;
                                $nowtr = $(".headerTr");	//创建文件
                        $table.on('click', ".headerTr~tr", function () {
                            var $ck = $(this).find("[name=ck]");
                            if ($ck.attr("ckval") == 1) {
                                $ck.attr("ckval", 0);
                                $ck.prop("checked", false);
                            } else {
                                $ck.attr("ckval", 1);
                                $ck.prop("checked", true);
                            }
                            
                            if ($table.find(".headerTr~tr").length == $table.find(".headerTr~tr input[name=ck]:checked").length) {
                                $ckAll.prop("checked", true);
                            } else {
                                $ckAll.prop("checked", false);
                            }
                            if($table.find(".headerTr~tr input[name=ck]:checked").length>0){
                            	if($(".dt.recycleBin.bHzsaPb").length>0){
                            		$(".btn.btn-default.recycleBin").show();
                            	}else if($(".dt.MyShare.bHzsaPb").length>0){
                            		$(".btn.btn-default.MyShare").show();
                            	}else{
                            		$(".btn-group.option").show();
                            	}
                            }else{
                            	$(".btn-group.option").hide();
                        		$(".btn.btn-default.recycleBin").hide();
                        		$(".btn.btn-default.MyShare").hide();
                            }
                            ///$ck.attr("ckval", 1).parents("tr").siblings("tr:not(.headerTr)").find("[name=ck]").attr("ckval", 0);
                            //$ck.prop("checked", true).parents("tr").siblings("tr:not(.headerTr)").find("[name=ck]").prop("checked", false);
                        }).on('mouseleave', ".headerTr~tr", function () {
                            $(this).find(".fileOption").hide();
                        }).on('dblclick',".headerTr~tr", function () {
                        	if($(this).attr("data-type")==1 && !$(".bHzsaPb").hasClass("recycleBin") && !$(".bHzsaPb").hasClass("MyShare")){
                            	getMyFile($(this).attr("data-id"),null,"timeAsc");
                        	}
                        });
                        $ckAll.click(function () {
                            var isChecked = $(this).prop("checked");
                            if (isChecked == true) {
                                $table.find("input[name=ck]").attr("ckval", 1);
                                if($(".dt.recycleBin.bHzsaPb").length>0){
                            		$(".btn.btn-default.recycleBin").show();
                            	}else if($(".dt.MyShare.bHzsaPb").length>0){
                            		$(".btn.btn-default.MyShare").show();
                            	}else{
                            		$(".btn-group.option").show();
                            		$(".btn.btn-default.recycleBin").hide();
                            		$(".btn.btn-default.MyShare").hide();
                            	}
                            } else {
                                $table.find("input[name=ck]").attr("ckval", 0);
                                closeOption();
                            }
                            $table.find(".headerTr~tr input[name=ck]").prop("checked", isChecked);
                        });

                        $nowtr.find(".toHide").click(function () {//取消创建文件夹
                            $nowtr.hide().find("input[name=fileName]").val("");
                            $(".emptyTr").show();
                        }).end().find(".ok").click(function () {
                            var $fileName = $nowtr.find("input[name=fileName]").val();
                            if ($fileName.trim().length == 0) {//确定创建文件夹
                                openModal("文本框不能为空", "<span>好的</span>");
                            } else {
                                //刷新
                                $nowtr.hide().find("input[name=fileName]").val("");//请空文本宽中的内容
                                var parentdir = $("#myFileHeader .breadcrumb li:last").attr("parentdir");
                                createFolio($fileName,parentdir,null);
                            }
                        });
                        
                        //返回上一级
                        $(".head.bread-h").on('click',function(){
                        	getMyFile($(this).attr("parentDir"),null,"timeAsc");
                        });
                        $("#myFileHeader .breadcrumb").on('click',"li:not(:last)",function(){
                        	getMyFile($(this).attr("parentDir"),null,"timeAsc");
                        });
                        
                        $("tr").on('click',"th.orderby",function(){
                        	if($(this).hasClass("time")){
                        		if($(this).attr("data-order")=="timeAsc"){
                        			$(this).attr("data-order","timeDesc")
                        		}else{
                        			$(this).attr("data-order","timeAsc")
                        		}
                        		var target = null;
                        		if(!$(".bHzsaPb").hasClass("dt")){
                        			target=$(".bHzsaPb").attr("data-type");
                        		}
                        		getMyFile($("#myFileHeader .breadcrumb li:last").attr("parentdir"),target,$(this).attr("data-order"));
                        	}
                        });
                    });
                    
                    function closeOption(){
                        $(".btn-group.option").hide();
                		$(".btn.btn-default.recycleBin").hide();
                		$(".btn.btn-default.MyShare").hide();
                    }
                    /*
						当 fileType 为空时 
							得到指定目录（parentDir）下的文件夹和文件
						否者
							按文件类型进行查询
						order : 排序方式
                    */
                    function getMyFile(parentDir,fileType,order) {
						checkShareFile(2);
                    	var $myFileHeader = $("#myFileHeader"),
                    		$bread=$myFileHeader.find(".breadcrumb");
                    	
                    	//路径导航
	        			$bread.empty().show();
	        			$(".headerTr").hide();//隐藏创建文件夹的节点
	        			
	        			$("#ckAll").prop("checked",false);//全选按钮
	        			closeOption();//文件批量操作
                    	
                        var $table = $(".myTabBox .table");
                        $table.find(".headerTr").siblings().remove();
                        
                	 	$("#myFile>nav").show();
                        
                        var FileUrl=fileType==null?"rest/folio/"+parentDir+"?order="+order:"rest/folio?fileType="+fileType+"&order="+order;
                        $.ajax({
                        	url:FileUrl+"&isNotDel=1",
                        	type:"GET",
                        	//contentType:"application/json",
                        	beforeSend:function(){
                        		openSpinner("拼命加载中！！！");
                        	},success:function(data){
                    			console.info(data);
                    			
	        					/*路径导航 begin*/
                    			var $liAll=$('<li parentDir="0">全部文件</li>');
	        					if(data.catalog.length>0){
	        						$liAll.html('<a href="javascript:void(0)">全部文件</a>');
	        						$(".bread-h").show().attr("parentDir",data.catalog[data.catalog.length-1].parentDir).siblings(".head").hide();;     	
	        					}else{
	        						$liAll.addClass("active");
	        						$(".head").hide();
	        					}
                    			$bread.append($liAll);
                    			
                    			$.each(data.catalog,function(i,obj){
                    				var $li=$('<li parentDir="'+obj.id+'"><a href="javascript:void(0)">'+obj.folioName+'</a></li>');
                    				if(i==data.catalog.length-1){
                    					$li.addClass("active").html(obj.folioName).siblings("li").removeClass("active");
                    				}
                    				$bread.append($li);
                    			});
                    			/*路径导航 end*/
                    			
                    			$myFileHeader.find("div.last").html("已全部加载，共"+data.files.length+"个");
                    			if(data.files.length==0){
                    				$table.append('<tr class="emptyTr text-center"><td colspan="3" style="height:60px;line-height:60px;">'+
                    						'您还没上传过文件哦，点击上传选择文件按钮～'+
                    					'</td><tr>');
                    			}
                    			$.each(data.files, function (i, obj) {
                    				//var fileName = obj.rname==1?obj.fileName:obj.fileName+"("+obj.rname+")";
                    				var $tr = $("<tr data-id='"+obj.id+"' data-type='"+obj.type+"' data-parentDir='"+obj.parentDir+"'><td width='60%'><input type='checkbox' data-id='" + obj.id + "' name='ck' value='' />" +
                                            "<i class='fa "+obj.clazz+"'></i>" +
                                            "<span>" + obj.showName + "</span>" +
                                            "<div class='file-option'>" +
                                            "<i title='创建分享' class='fa fa-share-alt' aria-hidden='true'></i>" +
                                            "<i title='下载' class='fa fa-download' aria-hidden='true'></i>" +
                                            "<i title='更多' class='fa fa-ellipsis-h' aria-hidden='true'></i>" +
                                            "<div class='fileOption'><ul><li>移动到</li>" +
                                            "<li>复制到</li>" +
                                            "<li>重命名</li>" +
                                            "<li>删除</li>" +
                                            "</ul></div></div></td>" +
                                            "<td width='16%'>" + obj.size + "</td><td width='24%'>" + obj.fileUpdateTime + "</td></tr>");
                                    $tr.find(".fileOption>ul").on('click', 'li', function () {
                                        if ($(this).html() == "重命名") {
                                            $tr.find("td:eq(0) .fa").siblings("span,div").hide();
                                            var createFile = $('<div class="FileName-div"><input type="text" name="fileName" value="' + obj.showName + '" /><span class="text-center spanOption ok">√</span><span class="text-center spanOption toHide">×</span></div>');
                                            var input = createFile.find("input");
                                            createFile.find(".toHide").on('click', function () {
                                                createFile.remove();
                                                $tr.find("td:eq(0) .fa").siblings("span,div").show();
                                                return false;
                                            }).end().find(".ok").click(function () {//确定修改
                                                createFile.remove();
                                                $tr.find("td:eq(0) .fa").siblings("span,div").show();
                                                var $th = $(this);
                                                $.ajax({
                                                	type:"put",
                                                	url:"rest/folio/"+obj.type,
                                                	data:"parentDir="+obj.parentDir+"&fileName="+$th.siblings("[name=fileName]").val().trim()+"&id="+obj.id,
                                                	//contentType:"application/json",
                                                	success:function(data){
                                                		checkMsg(data.msg,data.status);
                                                		if(data.status==200)
                                                    		getMyFile(obj.parentDir,null,"timeAsc");
                                                	},
                                                	error:function(xhr){
                                                		alert("修改错误\n"+xhr.responseText);
                                                	}
                                                });
                                                return false;
                                            });
                                            input.on('click', function () {return false;}).on('dblclick',function(){return false;});
                                            $tr.find("td:eq(0)").append(createFile);
                                        }else if($(this).html() == "删除"){
                                        	var typeId=[obj.type],fileId=[obj.id];
                                        	delFolioAndFile(typeId,fileId,obj.parentDir);
                                        }else if($(this).html() == "复制到"){
                                        	var $el = $("<span currentFileId='"+obj.id+"' fileType='"+obj.type+"' option='copy'>确定</span>");
                                        	$el.on('click',function(){
                                        		var $active=$(this).parents("#folioModal").find(".active-p .fa1");
    											
                                        		var typeId=[obj.type],
                                        			fileId=[obj.id],
                                        			oldParId=[obj.parentDir];
    											copys(typeId,fileId,$active.attr("data-folioid"),oldParId);
                                        	});
                                        	openFolioModal($el,"复制到");
                                        }else if($(this).html() == "移动到"){
                                        	var $el = $("<span currentFileId='"+obj.id+"' fileType='"+obj.type+"' option='move'>确定</span>");
                                        	$el.on('click',function(){
                                        		var $active=$(this).parents("#folioModal").find(".active-p .fa1");
    											
    											var typeId=[obj.type],
	                                    			fileId=[obj.id],
	                                    			oldParId=[obj.parentDir];
    											moves(typeId,fileId,$active,oldParId);
                                        	});
                                        	openFolioModal($el,"移动到");
                                        }
                                        return false;
                                    }).end().find("td:eq(0)").on('click', '.fa', function () {
                                        var $th = $(this);
                                        if ($th.hasClass("fa-share-alt")) {//分享
                                            alert("分享");
                                        } else if ($th.hasClass("fa-download")) {//下载
                                        	if(obj.type==2){
                                            	location.href="upload/downLoad?typeId="+obj.type+"&id="+obj.id;
                                        	}else{
                                        		createMeg("占时还不提供下载文件夹哦");
                                        	}
                                        } else if ($th.hasClass("fa-ellipsis-h")) {//更多
                                            $th.siblings(".fileOption").show();
                                        }
                                        return false;
                                    }).end().find("td:eq(0)").on('dblclick', '.fa', function () {
                                        return false;
                                    }).end().find(".fileOption>ul").on('dblclick', 'li', function () {
                                        return false;
                                    });
                                    $table.append($tr);
                                });
                    			closeSpinner();
                    		},error:function(e){
                    			alert("错误"+e.responseText);
                    		},complete:function(){
                    		}
                        });//ajax
                    }
                    
                    //检查文件原来的父文件夹下--为了 copys函数和moves函数
                    function checkInfo(oldParId,parentDir){
                    	for(var i =0 ; i< oldParId.length;i++){
                    		if(oldParId[i]==parentDir){
                    			return false;
                    		}
                    	}
                    	return true;
                    }
                  	//批量复制
                    function copys(typeId,id,parentDir,oldParId){
                    	/* if(!checkInfo(oldParId,parentDir)){
                    		createMeg("不能将文件复制到自身或其子目录下");
                    		return false; 
						}else{*/
							var param = "parentDir="+parentDir;
							$.each(typeId,function(i,obj){
								param+="&typeId="+obj+"&id="+id[i];
							});
                			openSpinner("文件拼命复制中。。。");
							$.ajax({
	                    		type:"POST",
	                    		url:"rest/folio/copys",
	                    		data:param,
	                    		beforeSend:function(){
	                    			openSpinner("文件拼命复制中。。。");
	                    		},success:function(data){
	                    			checkMsg(data.msg,data.status);
	                        		if(data.status==200){
	                        			getMyFile($("#myFileHeader .breadcrumb li:last").attr("parentdir"),null,"timeAsc");
	                        		}
	                    		},error:function(xhr){
	                    			alert("文件复制错误\n"+xhr.responseText);
	                    		},complete:function(){
	                    			closeSpinner();
	                    		}
	                    	});
						//}
                    }
                    //批量移动
                    function moves(typeId,id,$active,oldParId){
                    	var parentDir=$active.attr("data-folioid")
                    	if(!checkInfo(oldParId,parentDir)){
                    		createMeg("不能将文件移动到自身或其子目录下");
                    		return false;
                		}else if($active.length>0){
                			var param = "parentDir="+parentDir;
							$.each(typeId,function(i,obj){
								param+="&typeId="+obj+"&id="+id[i];
							});
                			$.ajax({
	                    		type:"PUT",
	                    		url:"rest/folio/moves",
	                    		data:param,
	                    		beforeSend:function(){
	                    			openSpinner("文件拼命移动中。。。");
	                    		},success:function(data){
	                    			checkMsg(data.msg,data.status);
	                        		if(data.status==200){
	                        			getMyFile($("#myFileHeader .breadcrumb li:last").attr("parentdir"),null,"timeAsc");
	                        		}
	                    		},error:function(xhr){
	                    			alert("文件移动错误\n"+xhr.responseText);
	                    		},complete:function(){
	                    			closeSpinner();
	                    		}
	                    	});
                    	}else{
                    		createMeg("请选择文件夹");
                    		return false;
                		}
                    }
                    //批量删除
                    function delFolioAndFile(typeId,fileId,parentDir){
                    	var param="a=1";
                    	$.each(typeId,function(i,obj){
                    		param+="&id="+fileId[i]+"&typeId="+obj;
                    	});
                    	$.ajax({
                    		type:"PUT",
                    		url:"rest/folio",
                    		data:param,
                    		success:function(data){
                    			checkMsg(data.msg,data.status);
                        		if(data.status==200)
                            		getMyFile(parentDir,null,"timeAsc");
                    		},
                    		error:function(xhr){
                    			console.info(this.data)
                    			alert("删除错误\n"+xhr.responseText);
                    		}
                    	});
                    }
                </script>
				</table>
			</div>
		</div>
	</div>
	
	
	<div id="myModal" class="modal fade" tabindex="10" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">温馨提示</h4>
				</div>
				<div class="modal-body">
					<p style="margin: 14px 0;"></p>
				</div>
				<div class="modal-footer">
					<button type="button"
						style="padding-left: 30px; padding-right: 30px;"
						class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button"
						style="padding-left: 30px; padding-right: 30px;"
						class="btn btn-primary" data-dismiss="modal">确定</button>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
    $(function () {
        $("#myModal").on("show.bs.modal", function (e) {

        }).on("shown.bs.modal", function (e) {

        }).on("hide.bs.modal", function (e) {

        }).on("hidden.bs.modal", function (e) {
            //内容清空
            $(this).removeData("bs.modal");
        }).on("loaded.bs.modal", function (e) {

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
    function openModal(content, $el) {
        var $myModal = $("#myModal");
        $myModal.modal("show").find(".modal-body>p").html(content).end()
                .find(".modal-footer button:eq(1)").html("").addClass("getBtn").css("padding", "0").append($el);

    }
</script>

	<style>
		.myTabBox .table tr td:first-of-type>.fa-th-large{cursor:pointer;color: #43a0ff;background-color: #ffc644;
			padding: 2px 4px 0 4px;border-radius: 4px;}
	</style>
	<script type="text/javascript">
	$(function(){
		$(".btn.btn-default.myOption.recycleBin").click(function(){
			var $trs = $(".headerTr~tr td [name=ck]:checked").closest("tr");
			if($trs.length==0){
				openModal("请选择需要还原的文件或文件夹", $("<span>确定</span>"));
			}
			var typeId=[],fileId=[];
			$.each($trs,function(i,obj){
				typeId.push($(obj).attr("data-type"));
				fileId.push($(obj).attr("data-id"));
			});
			restoreFolioOrFile(typeId,fileId);
		});
	});
	
	function myShare(){
		checkShareFile(1);
		var $myFileHeader = $("#myFileHeader"),
			$bread=$myFileHeader.find(".breadcrumb");
			$table = $(".myTabBox .table");
		$bread.find("*").hide();
		$myFileHeader.find(".share-h").show().siblings("head").hide();
		$table.find(".headerTr").siblings().remove();
		$.ajax({
			type:"GET",
			url:"rest/fileShare/fileShareDetail",
			beforeSend:function(){
				openSpinner("拼命加载中！！")
			},success:function(data){
				console.info(data);
				$myFileHeader.find("div.last").html("已全部加载，共"+data.length+"个");
				if(data.length==0){
					$table.append('<tr class="emptyTr text-center"><td colspan="4" style="height:60px;line-height:60px;">'+
							'您没有有效的分享文件噢～</td><tr>');
				}
				$.each(data, function (i, obj) {
					var showName,clazz;
					if(obj.folioInfoVos.length>1){
						showName=obj.folioInfoVos[0].showName+"等多个文件";
						clazz="fa-th-large";
					}else{
						showName=obj.folioInfoVos[0].showName;
						clazz=obj.folioInfoVos[0].clazz;
					}
					var $tr = $("<tr data-id='"+obj.folioInfoVos[0].id+"' data-type='"+obj.folioInfoVos[0].type+"'><td width='48%'><input type='checkbox' data-id='" + obj.id + "' name='ck' value='' />" +
	                        "<i class='fa "+clazz+"'></i>" +
	                        "<span>" + showName + "</span>" +
	                        "<div class='file-option'>" +
	                        "<i title='取消分享' class='fa fa-undo' aria-hidden='true'></i>" +
	                        "</div></td>" +
	                        "<td width='16%'>" + obj.downloadNum + "</td><td width='20%'>" + obj.sharingTime + "</td>"+
	                        "<td width='20%'>" + obj.outTime + "</td></tr>");
					if(obj.folioInfoVos.length>1){
						$tr.on("dblclick",function(){
							showShareFileInfo(obj.id);
						});
					}
	                $tr.find("td:eq(0)").on('click', '.fa', function () {
                    	var id=[obj.id];
                    	var $el=$("<span>确定</span>");
                    	$el.on('click',function(){
                    		$.ajax({
	                        	type:"DELETE",
	                        	url:"rest/fileShare/"+obj.id,
								beforeSend:function(){
									openSpinner("删除分享链接中。。");
								},complete:function(){
									closeSpinner();
								},error:function(xhr){
									alert("删除分享错误\n"+xhr.responseText);
								},success:function(data){
									checkMsg(data.msg,data.status);
									if(data.status==200)
										myShare();
								}
	                        });
                    	});
                    	openModal("取消分享后，该条分享记录将被删除，好友将无法再访问此分享链接。你确认要取消分享吗？！请谨慎此操作", $el);
	                    return false;
	                }).end().find("td:eq(0)").on('dblclick', '.fa', function () {
	                    return false;
	                }).end().find(".fileOption>ul").on('dblclick', 'li', function () {
	                    return false;
	                });
	                $table.append($tr);
	            });
			},complete:function(){
				closeSpinner();
			}
		});
	}
	
	function checkShareFile(num){
		if(num==1){
			$("#myFile>table:eq(0)").find("th:eq(0)").attr("width","48%")
				.end().find("th:eq(1)").html("<span>下载次数</span>").attr("width","16%")
				.end().find("th:eq(2)").html("<span>分享时间</span>").attr("width","20%").removeClass("orderby")
				.after("<th width='20%' class='lastTh'><span>失效时间</span></th>");
		}else{
			$("#myFile>table:eq(0) .lastTh").remove();
			$("#myFile>table:eq(0)").find("th:eq(0)").attr("width","60%")
			.end().find("th:eq(1)").html("<span>大小</span>").attr("width","16%")
			.end().find("th:eq(2)").html("<span>修改时间</span>").attr("width","24%").addClass("orderby");
		}
		
	}
	
	function recycleBin(){
		checkShareFile(2);
		var $myFileHeader = $("#myFileHeader"),
    		$bread=$myFileHeader.find(".breadcrumb");
			$table = $(".myTabBox .table");
		$myFileHeader.find(".head.bread-h.cursor").hide();
		$bread.find("*").hide();
		$myFileHeader.find(".recycleBin-h").show().siblings("head").hide();
	 	$table.find(".headerTr").siblings().remove();

	 	$("#myFile>nav").css("display","none");
		$.ajax({
			type:"GET",
			url:"rest/folio/-1",
			data:"isNotDel=2",
			beforeSend:function(){
				openSpinner("拼命加载中！！")
			},success:function(data){
				$myFileHeader.find("div.last").html("已全部加载，共"+data.files.length+"个");
				if(data.files.length==0){
					$table.append('<tr class="emptyTr text-center"><td colspan="3" style="height:60px;line-height:60px;">'+
							'您的回收站为空噢～</td><tr>');
				}
				$.each(data.files, function (i, obj) {
					var $tr = $("<tr data-id='"+obj.id+"' data-type='"+obj.type+"'><td width='60%'><input type='checkbox' data-id='" + obj.id + "' name='ck' value='' />" +
	                        "<i class='fa "+obj.clazz+"'></i>" +
	                        "<span>" + obj.showName + "</span>" +
	                        "<div class='file-option'>" +
	                        "<i title='还原' class='fa fa-undo' aria-hidden='true'></i>" +
	                        "<i title='彻底删除' class='fa fa-trash-o' aria-hidden='true'></i>" +
	                        "</div></td>" +
	                        "<td width='16%'>" + obj.size + "</td><td width='24%'>" + obj.fileUpdateTime + "</td></tr>");
	                $tr.find("td:eq(0)").on('click', '.fa', function () {
	                    var $th = $(this);
	                    if ($th.hasClass("fa-undo")) {//还原
	                    	var typeId=[obj.type],id=[obj.id];
	                    	restoreFolioOrFile(typeId,id);
	                    } else if ($th.hasClass("fa-trash-o")) {//彻底删除
	                    	var $el=$("<span>确定删除</span>");
	                    	$el.on('click',function(){
	                    		$.ajax({
		                        	type:"DELETE",
		                        	url:"rest/folio/"+obj.id+"/"+obj.type,
									beforeSend:function(){
										openSpinner("正在抛弃文件中。。");
									},complete:function(){
										closeSpinner();
									},error:function(xhr){
										alert("彻底删除错误\n"+xhr.responseText);
									},success:function(data){
										checkMsg(data.msg,data.status);
										if(data.status==200)
											recycleBin();
									}
		                        });
	                    	});
	                    	openModal("彻底删除的文件或文件将无法复原哦！请谨慎此操作", $el);
	                    }
	                    return false;
	                }).end().find("td:eq(0)").on('dblclick', '.fa', function () {
	                    return false;
	                }).end().find(".fileOption>ul").on('dblclick', 'li', function () {
	                    return false;
	                });
	                $table.append($tr);
	            });
			},complete:function(){
				closeSpinner();
			}
		});
	}
	
	//还原文件或者文件夹
	function restoreFolioOrFile(typeId,id){
		
		var param="a=1";
		$.each(typeId,function(i,obj){
	    	param += "&typeId="+obj+"&id="+id[i];
		});
		$.ajax({
			type:"PUT",
			url:"rest/folio/restore",
			data:param,
			beforeSend:function(){
				openSpinner("文件飞速还原中，请稍等。。。");
			},success:function(data){
				checkMsg(data.msg,data.status);
				if(data.status==200)
					recycleBin();
			},error:function(xhr){
				alert("还原错误\n"+xhr.responseText);
			},complete:function(){
				closeSpinner();
			}
		});
	}
</script>
	<script type="text/javascript" src="js/bootstrap.js"></script>
	<script type="text/javascript">
    $(function () {
        $("#bs-example-navbar-collapse-1>ul:eq(0)>li:eq(0)").addClass("active").siblings("li").removeClass("active");
    });
</script>
</body>
</html>