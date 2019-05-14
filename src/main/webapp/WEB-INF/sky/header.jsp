<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<nav class="navbar navbar-inverse">
	<div class="navbar-header">
		<button style="margin-top: 12px;" type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        	<span class="sr-only">Toggle navigation</span>
        	<span class="icon-bar"></span>
        	<span class="icon-bar"></span>
        	<span class="icon-bar"></span>
    	</button>
    	<a class="navbar-brand" href="javascript:void(0)"><span class="glyphicon glyphicon-fire"></span> <b>阿湘网盘</b></a>
	</div>
	<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
	 	<ul class="nav navbar-nav">
	        <li><a href="home/index">网盘 <span class="sr-only">(current)</span></a></li>
	        <li><a href="home/share">分享</a></li>
	        <li><a href="javascript:void(0)">更多</a></li>
    	</ul>
    	<ul class="nav navbar-nav navbar-right myNav">
	        <!--<li><a href="javascript:void(0)">超级会员 超低价</a></li>-->
	        <li>
	        	<a href="javascript:void(0)" style="padding: 9px 0;">
	        		<img style="width: 40px;border-radius: 50%;" id="user-headImg" src="/ax/${user.headImg}" alt="${user.nickname}" />
	        	</a>
	        </li>
	        <li><a href="javascript:void(0)">${user.phone}</a></li>
	        <li><span></span></li>
	        <li><a href="login/logout" style="color:#3b8cff;">注销</a></li>
	        <li><button id="nav-btn-vip" class="btn btn-default">会员中心</button></li>
		</ul>
	</div>
</nav>
<div id="spinner"><i class="fa fa-spinner" aria-hidden="true"></i>加载中。。。</div>
<script type="text/javascript">
function createMeg(content){
	var $nav=$("<span>"+content+"</span>");
	$nav.addClass("errmsg");
	var width=40+(content.length*15);
	$nav.css("width",width);
	$("body").append($nav);
	$nav.siblings(".cormsg,.errmsg").remove();
	setTimeout(function(){$nav.fadeOut(300,function(){$nav.remove();});},4500);
}
function createCormsg(content){
	var $nav=$("<span>"+content+"</span>");
	$nav.addClass("cormsg");
	$("body").append($nav);
	$nav.siblings(".cormsg,.errmsg").remove();
	setTimeout(function(){$nav.fadeOut(300,function(){$nav.remove();});},2000);
}
function checkMsg(content,status){
	var $nav=$("<span>"+content+"</span>");
	var time =2000;
	if(status==200){
		$nav.addClass("cormsg");
		time=4500;
	}else{
		$nav.addClass("errmsg");
	}
	var width=40+(content.length*15);
	$nav.css("width",width);
	$("body").append($nav);
	$nav.siblings(".cormsg,.errmsg").remove();
	setTimeout(function(){$nav.fadeOut(300,function(){$nav.remove();});},time);
}
function openSpinner(content){
	content=(content == "undefined" || content.length<1)?"加载中。。。":content;
	$("#spinner").html('<i class="fa fa-spinner"></i>'+content).show();
}
function closeSpinner(){
	$("#spinner").hide().html('<i class="fa fa-spinner"></i>加载中。。。');
}
</script>
<div id="folioModal" class="modal fade" tabindex="11" role="dialog" style="top: 90px;">
    <div class="modal-dialog" role="document" style="width: 500px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">复制到</h5>
            </div>
            <div class="modal-body">
                <div id="folioModalBody">
                    <div data-parentDir="0" class="folioAll childrenDiv">
                        <div class="p active-p" style="border-color: rgb(154, 205, 253);">
                            <i class="fa fa1 fa-minus-square-o" data-folioid="0"></i>
                            <i class="fa fa2 fa-folder-open"></i>
                            <span>全部文件</span>
                            <!--<i class="fa fa-plus-square-o" aria-hidden="true"></i>+
                            <i class="fa fa-minus-square-o" aria-hidden="true"></i>-
                            <i class="fa fa-spinner" aria-hidden="true"></i>0
                            <i class="fa fa-folder"></i>
                            <i class="fa fa-folder-open" aria-hidden="true"></i>-->
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <script>
                    $(function(){
                        var $div =$("#folioModalBody>div:eq(0)");
                        $div.on('click',"div.p:not(.now)",function(){
                            $("div.p").removeClass("active-p").css("border-color","rgba(0,0,0,0)");
                            $(this).addClass("active-p").css("border-color","#9acdfd");
                            
                            var $fa1=$(this).find(".fa1:eq(0)");
                            if($fa1.hasClass("fa-plus-square-o")){
                                $fa1.addClass("fa-minus-square-o").removeClass("fa-plus-square-o fa-spinner");
                                $fa1.parents(".p").siblings(".childrenDiv").show();
                                $fa1.siblings(".fa2").removeClass("fa-folder").addClass("fa-folder-open");
                                //if($fa1.closest(".childrenDiv").find(".childrenDiv").length==0)
                               	if($(this).parent().hasClass("childrenDiv"))
                               		getFolio($fa1.attr("data-folioId"),$fa1.closest(".childrenDiv"));
                            }else if($fa1.hasClass("fa-minus-square-o")){
                                $fa1.addClass("fa-plus-square-o").removeClass("fa-minus-square-o fa-spinner");
                                $fa1.parents(".p").siblings(".childrenDiv").hide();
                                $fa1.siblings(".fa2").removeClass("fa-folder-open").addClass("fa-folder");
                            }
                        }).end().on('click',"div.notp",function(){
                        	//var $tilte=$(this).parents("#folioModal").find(".modal-header>.modal-title").html().trim();
                        	//var str = $tilte=="移动到"?"无发移动,目标文件夹是源文件的子文件夹":"";
                        	createMeg("无发移动,目标文件夹是源文件的子文件夹");
                        });
                        $div.on('click','.fa1',function(){
                        	
                        });
                        $("#creatFolio").click(function(){
                            var $active=$(".active-p");
                            var parentDir=parseInt($active.parent().attr("data-parentDir"))+1;

                            var $children=$('<div class="childrenDiv new" data-parentDir="'+parentDir+'"><div class="p now">'+
                                    '<i class="fa fa1"></i> <i class="fa fa2 fa-folder"></i>'+
                                    ' <input type="text" name="fileName"><span class="text-center spanOption ok">√</span>'+
                                    '<span class="text-center spanOption toHide">×</span>'+
                                    '</div></div>');
                            $children.find(".p").css("padding-left",parentDir*20+"px");
                            $children.find(".toHide").on('click',function(){
                                $children.remove();
                            }).end().find(".ok").on('click',function(){
                                var $fileName = $children.find("input[name=fileName]").val();
                                if ($fileName.trim().length == 0) {//确定创建文件夹
                                    openModal("文件名不能为空", "<span>好的</span>");
                                } else {
                                	var parentDirId = $active.find(".fa1:eq(0)").attr("data-folioid");
                                	
                                    createFolio($fileName,parentDirId,$children);
                                }
                            });
                            $active.parent().append($children).find("[name=fileName]").focus();

                        })
                    });
                    
                    function getFolio(parentDir,$el){
                    	$el.find(".childrenDiv").remove();
                    	var dir = parseInt($el.attr("data-parentDir"))+1;
                    	
                    	var tarClazz="";
                    	if($el.find(".fa1").hasClass("fa-plus-square-o")){
                    		tarClazz="fa-plus-square-o";
                    	}else if($el.find(".fa1").hasClass("fa-minus-square-o")){
                    		tarClazz="fa-minus-square-o";
                    	}
                    	$.ajax({
                    		type:"GET",
                    		url:"rest/folio/getFolio",
                    		data:"parentDir="+parentDir,
                    		beforeSend:function(){
                    			if(parentDir>0){
                    				$el.find(".fa1").addClass("fa-spinner").removeClass("fa-minus-square-o fa-plus-square-o");
                    			}else{
                    				openSpinner("拼命加载中！！！！");
                    			}
                    		},success:function(data){
                    			console.info(data);
                    			$.each(data,function(i,obj){
                    				var clazz = obj.children>0?'fa-plus-square-o':'';
                    				var $children=$('<div class="childrenDiv" data-parentDir="'+dir+'"><div class="p">'+
    	                                    '<i class="fa fa1 '+clazz+'" data-folioId="'+obj.id+'"></i> <i class="fa fa2 fa-folder"></i>'+
    	                                    '<span>'+obj.showName+'</span>'+
    	                                    '</div></div>');
                    				$children.find(".p").css("padding-left",dir*20+"px");
                    				
                    				var $span=$("#folioModal .modal-footer .btn.btn-primary>span"),
                    					typeId=$span.attr("filetype").split(","),
                    					folioAndFileId=$span.attr("currentfileid").split(","),
                    					option=$span.attr("option");
                    				
                    				$.each(typeId,function(i,obk){
                    					//console.info(option+folioAndFileId+"-"+typeId)
	                    				if(obk == obj.type && folioAndFileId[i] == obj.id && option=="move"){
	                    					$children.find("div.p:eq(0)").removeClass("p active").addClass("notp")
												.find(".fa1:eq(0)").removeClass("fa-plus-square-o");
	                    				}
                    				});
                   					$el.append($children);
                    			});
                    		},error:function(xhr){
                    			alert("getFolio错误\n"+xhr.responseTest);
                    		},complete:function(){
                    			if(parentDir>0){
                    				$el.find(".fa1:eq(0)").addClass(tarClazz).removeClass("fa-spinner");
                    			}else{
                    				closeSpinner();
                    			}
                    		}
                    	});
                    }
                    
                    //创建文件夹
                    function createFolio($fileName,parentdir,target){
                    	$.ajax({
                           	url:"rest/folio",
                           	type:"POST",
                           	//contentType:"application/json",
                           	data:"folioName="+$fileName.trim()+"&parentDir="+parentdir,
                           	beforeSend:function(){
                           		openSpinner("正在加载中。。。");
                           	},success:function(data){
                           		checkMsg(data.msg,data.status);
                           		if(data.status==200){
                           			if(target==null){
                                   		getMyFile(parentdir,null,"timeAsc");
                           			}else{
                           				var $active = target.siblings("div.p");
                           				$active.find(".fa1").removeClass("fa-plus-square-o fa-spinner").addClass("fa-minus-square-o")
   	                                   		.end().find(".fa2").removeClass("fa-folder").addClass("fa-folder-open");
                           				getFolio($active.find(".fa1").attr("data-folioId"),$active.parent());
                           			}
                           		}else{
                           			if(target!=null){
                           				target.remove();
                           			}
                           		}
                           	},error:function(xhr){
                           		console.info(this.data);
                           		alert("错误"+xhr.responseText);
                           	},complete:function(){
                           		closeSpinner();
                           	}
                           });
                    }
                    function openFolioModal($el,title){
                    	$("#folioModal").modal("show").find(".modal-footer>.btn.btn-primary").html($el)
                    		.end().find(".modal-header h5").html(title);
                    }
                </script>
                <button type="button" id="creatFolio" class="btn btn-default">新建文件夹</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>

<div id="showShareFileInfo" class="modal fade" data-backdrop="static">
	<div class="modal-dialog" style="margin-top: 100px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title">文件信息</h4>
			</div>
			<div class="modal-body" style="padding: 30px 0;text-align: center;">
				<div class="settingInfo clearfix" style="max-height: 174px;overflow: auto;">
					<style>
						#shareFileInfo .fa{font-size: 20px;margin:0 10px;}
						#shareFileInfo .fa-folder{color: #ffd65a;}
						#shareFileInfo .fa-file-audio-o{color: #8284f1;}
						#shareFileInfo .fa-file-audio-o{color: #8284f1;}
						#shareFileInfo .fa-file-image-o{color: #ff7844;}
						#shareFileInfo .fa.fa-file{color: #64c424;}
						#shareFileInfo tr{height: 32px;box-sizing: border-box;}
						#shareFileInfo tr:first-of-type td{border-top: 1px solid #ccc;}
						#shareFileInfo tr td{border-bottom: 1px solid #ccc;}
					</style>
					<table style="width: 80%;margin: 0 10%;text-align: left;" cellspacing="0">
						<thead>
							<tr style="height: 45px;">
								<th width="80%"><span>文件名</span></th>
								<th width="20%"><span>大小</span></th>
							</tr>
						</thead>
						<tbody id="shareFileInfo">
							<script type="text/javascript">
								function showShareFileInfo(fileShareId){
									$("#showShareFileInfo").modal("show");
									var shareFileInfo = $("#shareFileInfo");
									shareFileInfo.empty();
									$.ajax({
										type:"GET",
										url:"rest/fileShare/showShareFileInfo/"+fileShareId,
										beforeSend:function(){
											openSpinner("正在打开分享文件记录中哦。。");
										},complete:function(){
											closeSpinner();
										},error:function(xhr){
											alert("查看分享文件记录错误\n"+xhr.responseText);
										},success:function(data){
											$.each(data,function(i,obj){
												var $tr = $("<tr data-id='"+obj.id+"' data-type='"+obj.type+"' data-parentDir='"+obj.parentDir+"'>"+
														"<td width='80%'><i class='fa "+obj.clazz+"'></i>" +
			                                            "<span>" + obj.showName + "</span></td>" +
			                                            "<td width='20%'>" + obj.size + "</td></tr>");
												shareFileInfo.append($tr);
											});
										}
									});
									
								}
							</script>
						</tbody>
					</table>
				</div>
			</div>
			<div class="modal-footer" style="margin: 10px 15px 10px;">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>